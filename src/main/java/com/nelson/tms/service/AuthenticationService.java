package com.nelson.tms.service;

import com.nelson.tms.dto.*;

public interface AuthenticationService {
    JwtAuthResponse login(LoginDto loginDto);
}
