package com.enefit.service;

import com.enefit.dto.AuthResponseDto;
import com.enefit.dto.LoginRequestDto;
import com.enefit.entity.Customer;
import com.enefit.repository.CustomerRepository;
import com.enefit.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        Customer customer = customerRepository.findByUsername(request.getUsername())
                .orElseThrow();
        String token = jwtService.generateToken(new UserDetails() {
            @Override
            public String getUsername() {
                return customer.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }

            @Override
            public java.util.Collection<? extends org.springframework.security.core.GrantedAuthority> getAuthorities() {
                return java.util.Collections.emptyList();
            }

            @Override
            public String getPassword() {
                return customer.getPassword();
            }
        });
        return AuthResponseDto.builder()
                .token(token)
                .username(customer.getUsername())
                .role("ROLE_USER")
                .customerId(customer.getCustomerId())
                .build();
    }
} 