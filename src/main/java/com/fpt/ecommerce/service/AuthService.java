package com.fpt.ecommerce.service;

import com.fpt.ecommerce.dto.request.LoginRequest;
import com.fpt.ecommerce.dto.request.LogoutRequest;
import com.fpt.ecommerce.dto.request.RefreshTokenRequest;
import com.fpt.ecommerce.dto.request.RegisterRequest;
import com.fpt.ecommerce.dto.response.AuthResponse;
import com.fpt.ecommerce.dto.response.MemberResponse;
import com.fpt.ecommerce.entity.Member;
import com.fpt.ecommerce.entity.RefreshToken;
import com.fpt.ecommerce.entity.Role;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.mapper.MemberMapper;
import com.fpt.ecommerce.repository.MemberRepository;
import com.fpt.ecommerce.repository.RefreshTokenRepository;
import com.fpt.ecommerce.repository.RoleRepository;
import com.fpt.ecommerce.security.jwt.JwtUtils;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    RefreshTokenRepository  refreshTokenRepository;

    @Transactional
    public MemberResponse register(RegisterRequest request) {
        log.info("Register user: {}", request.getUsername());
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Member member = memberMapper.toMember(request);
        System.out.println("DOB in Entity: " + member.getDob());
        member.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        member.setRoles(new HashSet<>(Set.of(userRole)));
        
        Member savedMember = memberRepository.save(member);

        return memberMapper.toMemberResponse(savedMember);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Login user: {}", request.getUsername());

        //Authentication
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        //get user infor
        var member = memberRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));

        //Create access token
        String accessToken = jwtUtils.generateJwtToken(member.getUsername());

        //Create refresh token
        String refreshToken = createRefreshToken(member);

        MemberResponse memberResponse = memberMapper.toMemberResponse(member);

        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .authenticated(true)
                .member(memberResponse)
                .build();
    }


    // Create token function
    public String createRefreshToken(Member member) {
        RefreshToken refreshToken = refreshTokenRepository.findByMemberId(member.getId())
                .orElse(new RefreshToken());

        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plus(30, ChronoUnit.DAYS));
        refreshToken.setMember(member);

        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    // Logic Refresh Token
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        return refreshTokenRepository.findByToken(refreshTokenRequest.getRefreshToken())
                .map(this::verifyExpiration)
                .map(RefreshToken::getMember)
                .map(member -> {
                    String newToken = jwtUtils.generateJwtToken(member.getUsername());
                    return AuthResponse.builder()
                            .token(newToken)
                            .refreshToken(refreshTokenRequest.getRefreshToken())
                            .authenticated(true)
                            .build();
                })
                .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
    }

    //Logic logout
    @Transactional
    public void logout(LogoutRequest logoutRequest) {
        refreshTokenRepository.findByToken(logoutRequest.getRefreshToken())
                .ifPresent(refreshTokenRepository::delete);
    }


    //Check expiration
    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw  new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return token;
    }
}

