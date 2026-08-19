package com.eazybytes.jobportal.user.service;


import com.eazybytes.jobportal.dto.UserDto;

import java.util.Optional;

public interface UserService {


    UserDto assignCompanyToEmployer(Long userId, Long companyId);

    Optional<UserDto> searchUserByEmail(String email);

    UserDto elevateToEmployer(Long userId);
}
