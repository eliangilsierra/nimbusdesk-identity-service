package com.nimbusdesk.identity.service;

import com.nimbusdesk.identity.persistence.dto.AuthResponse;
import com.nimbusdesk.identity.persistence.dto.LoginRequest;
import com.nimbusdesk.identity.persistence.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
