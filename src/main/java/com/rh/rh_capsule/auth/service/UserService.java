package com.rh.rh_capsule.auth.service;

import com.rh.rh_capsule.auth.controller.dto.UserDetailDTO;
import com.rh.rh_capsule.auth.controller.dto.UserUpdate;
import com.rh.rh_capsule.auth.domain.User;
import com.rh.rh_capsule.auth.domain.UserAuthority;
import com.rh.rh_capsule.auth.domain.UserStatus;
import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.repository.UserRepository;
import com.rh.rh_capsule.capsule.repository.CapsuleBoxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CapsuleBoxRepository capsuleBoxRepository;

    public UserDetailDTO getUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            throw new AuthException(AuthErrorCode.USER_NOT_FOUND);
        }
        return new UserDetailDTO(user.get().getUserEmail(), user.get().getUserName());
    }

    @Transactional
    public void resign(Long userId) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));

        capsuleBoxRepository.deleteByUserId(userId);

        String anonymizedEmail = "deleted" + user.getId() + "@example.com";
        user.setStatus(UserStatus.DELETED);
        user.setUserName("Deleted User");
        user.setUserEmail(anonymizedEmail);
        user.setPassword("Deleted User");
        user.setAuthority(UserAuthority.DELETED_USER);
    }

    @Transactional
    public void updateUser(Long userId, UserUpdate userUpdate) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));

        user.setUserName(userUpdate.userName());
    }
}
