package com.eco.environet.users.services.impl;

import com.eco.environet.users.dto.*;
import com.eco.environet.users.exception.CredentialsTakenException;
import com.eco.environet.users.model.*;
import com.eco.environet.users.repository.OrganizationMemberRepository;
import com.eco.environet.users.repository.UserRepository;
import com.eco.environet.users.security.auth.JwtService;
import com.eco.environet.users.services.AuthenticationService;
import com.eco.environet.util.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    @Value("${baseFrontUrl}")
    private String baseFrontUrl;
    private final UserRepository repository;
    private final OrganizationMemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final EmailSender emailSender;

    public AuthenticationResponse registerUser(RegisterRequest request) {
        var userExistsByEmail = repository.findByEmail(request.getEmail()).isPresent();
        var userExistsByUsername = repository.findByUsername(request.getUsername()).isPresent();
        Role role = Role.values()[request.getRole()];

        if (role != Role.REGISTERED_USER) {
            throw new IllegalArgumentException("Role is not registered user");
        }
        if (userExistsByEmail) {
            throw new CredentialsTakenException("Email already used.");
        }
        if (userExistsByUsername) {
            throw new CredentialsTakenException("Username already taken.");
        }

        RegisteredUser user = RegisteredUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .role(role)
                .enabled(true)
                .lastPasswordResetDate(null)
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender() == null ? null : Gender.values()[request.getGender()])
                .active(true)
                .points(0)
                .registrationDate(new Timestamp(System.currentTimeMillis()))
                .build();

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void registerOrganizationMember(RegisterRequest request) {
        validateMember(request);

        var user = User.builder()
                .username(request.getEmail())
                .password("")
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.values()[request.getRole()])
                .enabled(false)
                .lastPasswordResetDate(null)
                .active(true)
                .build();
        OrganizationMember member = new OrganizationMember(user, 18, 8, 20);

        memberRepository.save(member);
        User newUser = new User(member.getId(), member.getName(), member.getSurname(), member.getEmail(), member.getUsername(), member.getPassword(), member.getPhoneNumber(), member.getDateOfBirth(), member.getGender(), member.getLastPasswordResetDate(), member.getRole(), member.isEnabled(), member.isActive());
        sendConfirmationEmail(newUser);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByUsername(request.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public void verifyOrganizationMember(VerifyMemberRequest request) {
        String email = validateToken(request);

        User user = repository.findByUsername(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(user.isEnabled()){
            throw new IllegalStateException("Account is already verified");
        }

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);

        repository.save(user);
    }

    private String validateToken(VerifyMemberRequest request) {
        String token = request.getToken();
        String email = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new BadCredentialsException("Invalid or expired token");
        }
        return email;
    }

    private void validateMember(RegisterRequest request) {
        Role role = Role.values()[request.getRole()];
        if (!role.isOrganizationMember()) {
            throw new IllegalArgumentException("Role is not an organization member");
        }

        var userExistsByEmail = repository.findByEmail(request.getEmail()).isPresent();
        if (userExistsByEmail) {
            throw new CredentialsTakenException("Email already used.");
        }
    }

    private void sendConfirmationEmail(User user) {
        var jwtToken = jwtService.generateToken(user);
        String confirmationLink = baseFrontUrl + "/confirm-email?token=" + jwtToken;

        String emailBody = "Hello " + user.getName() + ",\n\n";
        emailBody += "Thank you for joining our organization. Please click the link below to complete your registration:\n\n";
        emailBody += confirmationLink + "\n\n";
        emailBody += "This link is valid for 24 hours. If the link expires, please contact us for assistance.\n";
        emailBody += "If you did not request this registration, please ignore this email.\n\n";
        emailBody += "Best regards,\nYour EnviroNet Team";

        emailSender.sendEmail(user.getEmail(), "Finalize Registration", emailBody);
    }

    @Override
    public boolean checkOldPasswordMatch(AuthenticationRequest request) {
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }

    @Override
    public AuthenticationResponse changePassword(AuthenticationRequest request) {
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
