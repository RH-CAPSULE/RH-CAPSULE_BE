package com.rh.rh_capsule.auth.service;

import com.rh.rh_capsule.auth.controller.dto.UserDetailDTO;
import com.rh.rh_capsule.auth.domain.User;
import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDetailDTO getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            throw  new AuthException(AuthErrorCode.USER_NOT_FOUND);
        }
        return new UserDetailDTO(user.get().getUserEmail(), user.get().getUsername());
    }

}
