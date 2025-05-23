package dev.HTR.services;

import dev.HTR.DTOs.RegisterRequest;

public interface AuthService {

    void loginUser(RegisterRequest request);
}
