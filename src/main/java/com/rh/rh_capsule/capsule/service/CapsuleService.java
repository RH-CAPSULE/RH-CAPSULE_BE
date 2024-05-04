package com.rh.rh_capsule.capsule.service;

import com.rh.rh_capsule.auth.domain.User;
import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import com.rh.rh_capsule.auth.repository.UserRepository;
import com.rh.rh_capsule.capsule.domain.Capsule;
import com.rh.rh_capsule.capsule.domain.CapsuleBox;
import com.rh.rh_capsule.capsule.dto.CapsuleBoxCreateDTO;
import com.rh.rh_capsule.capsule.dto.CapsuleCreateDTO;
import com.rh.rh_capsule.capsule.exception.CapsuleErrorCode;
import com.rh.rh_capsule.capsule.exception.CapsuleException;
import com.rh.rh_capsule.capsule.repository.CapsuleBoxRepository;
import com.rh.rh_capsule.capsule.repository.CapsuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CapsuleService {
    private final CapsuleBoxRepository capsuleBoxRepository;
    private final CapsuleRepository capsuleRepository;
    private final UserRepository userRepository;
    public void createCapsuleBox(CapsuleBoxCreateDTO capsuleBoxCreateDTO, Long userId) {
        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) {
            throw new CapsuleException(CapsuleErrorCode.USER_NOT_FOUND);
        }
        CapsuleBox newCapsuleBox = CapsuleBox.builder()
                .user(user.get())
                .theme(capsuleBoxCreateDTO.theme())
                .openedAt(capsuleBoxCreateDTO.openedAt())
                .closedAt(capsuleBoxCreateDTO.closedAt())
                .createdAt(LocalDateTime.now())
                .build();
        try{
            capsuleBoxRepository.save(newCapsuleBox);
        }catch (Exception e){
            throw new CapsuleException(CapsuleErrorCode.CAPSULE_BOX_CREATE_FAILED);
        }
    }

    public void createCapsule(CapsuleCreateDTO capsuleCreateDTO, String imageUrl, String audioUrl) {
        Optional<CapsuleBox> capsuleBox = capsuleBoxRepository.findById(capsuleCreateDTO.capsuleBoxId());
        System.out.println("capsuleBox = " + capsuleBox.get());
        if(!capsuleBox.isPresent()){
            throw new CapsuleException(CapsuleErrorCode.CAPSULE_BOX_NOT_FOUND);
        }

        Capsule newCapsule = Capsule.builder()
                .capsuleBox(capsuleBox.get())
                .color(capsuleCreateDTO.color())
                .title(capsuleCreateDTO.title())
                .content(capsuleCreateDTO.content())
                .writer(capsuleCreateDTO.writer())
                .imageUrl(imageUrl)
                .audioUrl(audioUrl)
                .createdAt(LocalDateTime.now())
                .isMine(capsuleCreateDTO.isMine())
                .build();
        try{
            capsuleRepository.save(newCapsule);
        }catch (Exception e){
            throw new CapsuleException(CapsuleErrorCode.CAPSULE_CREATE_FAILED);
        }
    }
}
