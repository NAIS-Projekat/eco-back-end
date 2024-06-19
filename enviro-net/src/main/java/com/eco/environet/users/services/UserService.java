package com.eco.environet.users.services;

import com.eco.environet.users.dto.UserDto;
import com.eco.environet.users.dto.UserInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserInfoDto findUser(Long id);
    UserInfoDto updateUser(UserInfoDto userInfoDto);
    UserInfoDto updateUserEmail(String currentUsername, String email, String token);
    Page<UserDto> findAllOrganizationMembers(String name, String surname, String email, Pageable pageable);
    List<UserDto> findAllUsersByRoles(List<String> roles);
    void removeOrganizationMember(Long memberId);
}