package com.fpt.ecommerce.service;

import com.fpt.ecommerce.dto.request.LoginRequest;
import com.fpt.ecommerce.dto.request.RegisterRequest;
import com.fpt.ecommerce.dto.response.AuthResponse;
import com.fpt.ecommerce.dto.response.MemberResponse;
import com.fpt.ecommerce.entity.Member;
import com.fpt.ecommerce.entity.Role;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.mapper.MemberMapper;
import com.fpt.ecommerce.repository.MemberRepository;
import com.fpt.ecommerce.repository.RoleRepository;
import com.fpt.ecommerce.security.jwt.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

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

    public AuthResponse login(LoginRequest request) {
        log.info("Login user: {}", request.getUsername());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Member member = memberRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        String token = jwtUtils.generateJwtToken(member.getUsername());

        MemberResponse memberResponse = memberMapper.toMemberResponse(member);

        return AuthResponse.builder()
                .token(token)
                .authenticated(true)
                .member(memberResponse)
                .build();
    }
}

