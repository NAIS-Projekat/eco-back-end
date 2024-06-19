package com.eco.environet.users.services;

import com.eco.environet.users.dto.*;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    AuthenticationResponse registerUser(RegisterRequest request);
    void registerOrganizationMember(RegisterRequest request);
    boolean checkOldPasswordMatch(AuthenticationRequest request);
    AuthenticationResponse changePassword(AuthenticationRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    void verifyOrganizationMember(VerifyMemberRequest request);
}
