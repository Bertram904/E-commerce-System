package com.fpt.ecommerce.service;

import com.fpt.ecommerce.dto.request.LoginRequest;
import com.fpt.ecommerce.dto.request.LogoutRequest;
import com.fpt.ecommerce.dto.request.RefreshTokenRequest;
import com.fpt.ecommerce.dto.request.RegisterRequest;
import com.fpt.ecommerce.dto.response.AuthResponse;
import com.fpt.ecommerce.dto.response.MemberResponse;
import com.fpt.ecommerce.entity.InvalidatedToken;
import com.fpt.ecommerce.entity.Member;
import com.fpt.ecommerce.entity.RefreshToken;
import com.fpt.ecommerce.entity.Role;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.mapper.MemberMapper;
import com.fpt.ecommerce.repository.InvalidatedTokenRepository;
import com.fpt.ecommerce.repository.MemberRepository;
import com.fpt.ecommerce.repository.RefreshTokenRepository;
import com.fpt.ecommerce.repository.RoleRepository;
import com.fpt.ecommerce.security.jwt.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {

    MemberRepository memberRepository;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;
    AuthenticationManager authenticationManager;
    MemberMapper memberMapper;
    RoleRepository roleRepository;
    RefreshTokenRepository refreshTokenRepository;
    UserDetailsService userDetailsService;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @Transactional
    public MemberResponse register(RegisterRequest request) {
        log.info("Register request for username: {}", request.getUsername());

        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        Member member = memberMapper.toMember(request);
        member.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        member.setRoles(new HashSet<>(Set.of(userRole)));

        Member savedMember = memberRepository.save(member);

        log.info("User registered successfully: {}", savedMember.getId());
        return memberMapper.toMemberResponse(savedMember);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Login request for user: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        var member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        //Create Scop
        String scope = buildScope(member);

        String accessToken = jwtUtils.generateToken(userDetails, scope);
        String refreshToken = createRefreshToken(member);

        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .member(memberMapper.toMemberResponse(member))
                .build();
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        return refreshTokenRepository.findByToken(request.getRefreshToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getMember)
                .map(member -> {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(member.getUsername());

                    //Nap lai scope
                    String scope = buildScope(member);

                    String newAccessToken = jwtUtils.generateToken(userDetails, scope);

                    return AuthResponse.builder()
                            .token(newAccessToken)
                            .refreshToken(request.getRefreshToken())
                            .authenticated(true)
                            .build();
                })
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
    }

    @Transactional
    public void logout(HttpServletRequest request, LogoutRequest logoutRequest) {
        //1.Lay access token tu header
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            String accessToken = authHeader.substring(7);
            try {
                String jti = jwtUtils.extractJwtId(accessToken);
                Date expiration = jwtUtils.extractClaim(accessToken, Claims::getExpiration);

                // save to blackList
                InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                        .id(jti)
                        .expiryTime(expiration)
                        .build();

                invalidatedTokenRepository.save(invalidatedToken);
                log.info("AccessToken ID {} has been blacklisted", jti);
            } catch (Exception e) {
                log.warn("Token invalid or expired when logging out: {}" + e.getMessage());
            }
        }

        //2. Delete RefreshToken trong DB
        refreshTokenRepository.findByToken(logoutRequest.getRefreshToken())
                .ifPresent(refreshToken -> {
                    refreshTokenRepository.delete(refreshToken);
                    log.info("Refresh token has been deleted: {}", refreshToken);
                });

        //3. Xoa context
        SecurityContextHolder.clearContext();
    }

    private String createRefreshToken(Member member) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(member.getId())
                .orElse(new RefreshToken());

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));
        refreshToken.setMember(member);

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return token;
    }

    private String buildScope(Member member) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(member.getRoles())) {
            member.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());

                //add permission
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> {
                        stringJoiner.add(permission.getName());
                    });
                }
            });
        }
        return stringJoiner.toString();
    }
}