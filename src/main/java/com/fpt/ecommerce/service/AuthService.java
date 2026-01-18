package com.fpt.ecommerce.service;

import com.fpt.ecommerce.dto.request.LoginRequest;
import com.fpt.ecommerce.dto.request.RegisterRequest;
import com.fpt.ecommerce.dto.response.AuthResponse;
import com.fpt.ecommerce.entity.Member;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.mapper.MemberMapper;
import com.fpt.ecommerce.repository.MemberRepository;
import com.fpt.ecommerce.security.jwt.JwtUtils;
import com.fpt.ecommerce.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final MemberMapper memberMapper;

    public Member register(RegisterRequest request) {
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        Member member = memberMapper.toMember(request);
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setRole(Role.USER);

        return memberRepository.save(member);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        Member member = memberRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtUtils.generateJwtToken(member.getUsername());

        return AuthResponse.builder()
                .token(token)
                .username(member.getUsername())
                .role(member.getRole().name())
                .build();
    }
}

