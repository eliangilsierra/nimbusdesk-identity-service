package com.nimbusdesk.identity.service.impl;

import com.nimbusdesk.identity.exception.EmailAlreadyRegisteredException;
import com.nimbusdesk.identity.exception.InvalidCredentialsException;
import com.nimbusdesk.identity.persistence.dto.AuthResponse;
import com.nimbusdesk.identity.persistence.dto.LoginRequest;
import com.nimbusdesk.identity.persistence.dto.RegisterRequest;
import com.nimbusdesk.identity.persistence.entity.UserEntity;
import com.nimbusdesk.identity.persistence.mapper.UserMapper;
import com.nimbusdesk.identity.persistence.repository.UserRepository;
import com.nimbusdesk.identity.security.JwtService;
import com.nimbusdesk.identity.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final String DEFAULT_ROLE = "EMPLOYEE";

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyRegisteredException(request.email());
        }

        UserEntity user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(DEFAULT_ROLE);

        UserEntity saved = userRepository.save(user);
        String token = jwtService.generateToken(saved);

        return userMapper.toAuthResponse(saved, token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user);
        return userMapper.toAuthResponse(user, token);
    }
}
