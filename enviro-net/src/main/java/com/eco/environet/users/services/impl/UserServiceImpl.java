package com.eco.environet.users.services.impl;

import com.eco.environet.users.dto.UserDto;
import com.eco.environet.users.dto.UserInfoDto;
import com.eco.environet.users.exception.CredentialsTakenException;
import com.eco.environet.users.model.Role;
import com.eco.environet.users.model.User;
import com.eco.environet.users.repository.UserRepository;
import com.eco.environet.users.repository.UserSpecifications;
import com.eco.environet.users.security.auth.JwtService;
import com.eco.environet.users.services.UserService;
import com.eco.environet.util.EmailSender;
import com.eco.environet.util.EnumMapper;
import com.eco.environet.util.Mapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    @Value("${baseFrontUrl}")
    private String baseFrontUrl;
    private final JwtService jwtService;
    private final EmailSender emailSender;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Override
    public UserInfoDto findUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + id));
        return Mapper.map(user, UserInfoDto.class, "password");
    }

    @Override
    public UserInfoDto updateUser(UserInfoDto userInfoDto) {
        User user = userRepository.findById(userInfoDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userInfoDto.getId()));

        // Update the user information with data from userInfoDto
        user.setName(userInfoDto.getName());
        user.setSurname(userInfoDto.getSurname());
        user.setDateOfBirth(userInfoDto.getDateOfBirth());
        user.setGender(userInfoDto.getGender());
        user.setPhoneNumber(userInfoDto.getPhoneNumber());
        if (!userInfoDto.getEmail().equals(user.getEmail())){
            updateEmail(user, userInfoDto.getEmail());
        }

        userRepository.save(user);
        return  Mapper.map(user, UserInfoDto.class, "password");
    }

    private void updateEmail(User user, String newEmail){
        // check if email is already in database
        var existing = userRepository.findByEmail(newEmail).isPresent();
        if (!existing){
            // send confirmation mail
            sendConfirmationEmail(user, newEmail);
        } else {
            throw new CredentialsTakenException("Email already used.");
        }
    }
    private void sendConfirmationEmail(User user, String newEmail) {
        var confirmationToken = jwtService.generateToken(user);
        String confirmationLink = baseFrontUrl + "/my-profile?token=" + confirmationToken + "&email=" + newEmail;

        String emailBody = "Hello " + user.getName() + ",\n\n";
        emailBody += "This is an automatically generated message for email update. ";
        emailBody += "Please click the link below to complete your email update:\n\n";
        emailBody += confirmationLink + "\n\n";
        emailBody += "This link is valid for 24 hours. If the link expires, please contact us for assistance.\n";
        emailBody += "If you did not request this change, please ignore this email.\n\n";
        emailBody += "Best regards,\nYour EnviroNet Team";

        emailSender.sendEmail(newEmail, "Finalize Email Update", emailBody);
    }

    @Override
    public UserInfoDto updateUserEmail(String currentUsername, String email, String token) {
        boolean isTokenValid = validateToken(currentUsername, token);

        if (isTokenValid) {
            String username = jwtService.extractUsername(token);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
            user.setEmail(email);
            userRepository.save(user);
            return Mapper.map(user, UserInfoDto.class, "password");
        } else {
            throw new BadCredentialsException("Invalid or expired token");
        }
    }
    private boolean validateToken(String currentUsername, String token) {
        String username = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(currentUsername);
        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new BadCredentialsException("Invalid or expired token");
        }
        return username.equals(currentUsername);
    }

    @Override
    public Page<UserDto> findAllOrganizationMembers(String name, String surname, String email, Pageable pageable) {
        List<Role> organizationRoles = Role.getAllOrganizationRoles();

        Specification<User> spec = Specification.where(StringUtils.isBlank(name) ? null : UserSpecifications.nameLike(name))
                .and(StringUtils.isBlank(surname) ? null : UserSpecifications.surnameLike(surname))
                .and(StringUtils.isBlank(email) ? null : UserSpecifications.emailLike(email))
                .and(UserSpecifications.rolesIn(organizationRoles));

        Page<User> members = userRepository.findAll(spec,  pageable);
        Page<UserDto> memberDtos = Mapper.mapPage(members, UserDto.class, "password");
        for (UserDto dto : memberDtos) {
            dto.setRole(EnumMapper.convertToTitleCase(dto.getRole()));
        }

        return memberDtos;
    }

    @Override
    public List<UserDto> findAllUsersByRoles(List<String> roles) {
        List<Role> userRoles = getRoles(roles);
        List<User> users = userRepository.findAllUsersByRoles(userRoles);
        return Mapper.mapList(users, UserDto.class);
    }
    private List<Role> getRoles(List<String> roles) {
        List<Role> userRoles = new ArrayList<>();
        for(String role : roles){
            Role userRole;
            try {
                userRole = Role.valueOf(role);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role provided: " + role);
            }
            userRoles.add(userRole);
        }
        return userRoles;
    }

    @Override
    public void removeOrganizationMember(Long memberId) {
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + memberId));
        user.deactivate();
        userRepository.save(user);
    }
}
