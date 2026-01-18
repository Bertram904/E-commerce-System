package com.fpt.ecommerce.service;

import com.fpt.ecommerce.entity.Member;
import com.fpt.ecommerce.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found with username: " + username));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + member.getRole());

        return new User(
                member.getUsername(),
                member.getPassword(),
                Collections.singletonList(authority)
        );
    }
}
