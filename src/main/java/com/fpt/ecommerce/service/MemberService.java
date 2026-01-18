package com.fpt.ecommerce.service;

import com.fpt.ecommerce.dto.response.MemberResponse;
import com.fpt.ecommerce.entity.Member;
import com.fpt.ecommerce.exception.AppException;
import com.fpt.ecommerce.exception.ErrorCode;
import com.fpt.ecommerce.mapper.MemberMapper;
import com.fpt.ecommerce.repository.MemberRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MemberService {

    MemberRepository memberRepository;
    MemberMapper memberMapper;

    public List<MemberResponse> getUsers() {
        log.info("Getting all users");
        return memberRepository.findAll().stream()
                .map(memberMapper::toMemberResponse)
                .toList();
    }

    public MemberResponse getUser(Long id) {
        log.info("Fetching user with id: {}", id);
        return memberMapper.toMemberResponse(
                memberRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED))
        );
    }

    public MemberResponse getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        log.info("Fetching my info: {}", username);

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return memberMapper.toMemberResponse(member);
    }
}
