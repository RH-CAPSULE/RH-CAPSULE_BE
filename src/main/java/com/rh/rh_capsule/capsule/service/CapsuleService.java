package com.rh.rh_capsule.capsule.service;

import com.rh.rh_capsule.auth.domain.User;
import com.rh.rh_capsule.auth.jwt.JwtProvider;
import com.rh.rh_capsule.auth.repository.UserRepository;
import com.rh.rh_capsule.capsule.domain.Capsule;
import com.rh.rh_capsule.capsule.domain.CapsuleBox;
import com.rh.rh_capsule.capsule.dto.*;
import com.rh.rh_capsule.capsule.exception.CapsuleErrorCode;
import com.rh.rh_capsule.capsule.exception.CapsuleException;
import com.rh.rh_capsule.capsule.repository.CapsuleBoxRepository;
import com.rh.rh_capsule.capsule.repository.CapsuleRepository;
import com.rh.rh_capsule.capsule.service.dto.UserType;
import com.rh.rh_capsule.utils.FileType;
import com.rh.rh_capsule.utils.S3Service;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CapsuleService {
    private final CapsuleBoxRepository capsuleBoxRepository;
    private final CapsuleRepository capsuleRepository;
    private final UserRepository userRepository;
    private final EntityManager em;
    private final S3Service s3Service;
    private final JwtProvider jwtProvider;
    public void createCapsuleBox(CapsuleBoxCreateDTO capsuleBoxCreateDTO, Long userId) {
        if(hasActiveCapsuleBox(userId)){
            throw new CapsuleException(CapsuleErrorCode.ACTIVE_CAPSULE_BOX_ALREADY_EXISTS);
        }

        User userReference = em.getReference(User.class, userId);
        CapsuleBox newCapsuleBox = CapsuleBox.builder()
                .user(userReference)
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

    public void createCapsuleProcess(CapsuleCreateDTO capsuleCreateDTO, MultipartFile image, MultipartFile audio, UserType accountType, Long userId) {
        CapsuleBox capsuleBox = capsuleBoxRepository.findById(capsuleCreateDTO.capsuleBoxId())
                .orElseThrow(() -> new CapsuleException(CapsuleErrorCode.CAPSULE_BOX_NOT_FOUND));


        if (!capsuleBox.getClosedAt().isAfter(LocalDateTime.now())) {
            throw new CapsuleException(CapsuleErrorCode.NOT_ACTIVE_CAPSULE_BOX);
        }

        Long capsuleBoxUserId = capsuleBox.getUser().getId();
        if (accountType.equals(UserType.GUEST)) {
            createCapsule(capsuleBox, capsuleCreateDTO, image, audio, capsuleBoxUserId, false);
        } else if (accountType.equals(UserType.USER)) {
            if (userId != capsuleBoxUserId) {
                throw new CapsuleException(CapsuleErrorCode.INVALID_CAPSULE_BOX_USER);
            }

            List<Capsule> capsules = capsuleBox.getCapsules();

            Optional<Capsule> capsule = capsules.stream().filter(capsule1 -> capsule1.getIsMine()).findFirst();

            if(capsule.isPresent()){
                throw new CapsuleException(CapsuleErrorCode.EXISTING_OWN_CAPSULE);
            }
            createCapsule(capsuleBox, capsuleCreateDTO, image, audio, capsuleBoxUserId, true);
        }

    }

    private void createCapsule(CapsuleBox capsuleBox, CapsuleCreateDTO capsuleCreateDTO, MultipartFile image, MultipartFile audio, Long userId, Boolean isMine) {
        String imageUrl = uploadImage(capsuleCreateDTO, image, userId);
        String audioUrl = uploadAudio(capsuleCreateDTO, audio, userId);

        Capsule newCapsule = Capsule.builder()
                .capsuleBox(capsuleBox)
                .color(capsuleCreateDTO.color())
                .title(capsuleCreateDTO.title())
                .content(capsuleCreateDTO.content())
                .writer(capsuleCreateDTO.writer())
                .imageUrl(imageUrl)
                .audioUrl(audioUrl)
                .createdAt(LocalDateTime.now())
                .isMine(isMine)
                .build();
        try{
            capsuleRepository.save(newCapsule);
        }catch (Exception e){
            throw new CapsuleException(CapsuleErrorCode.CAPSULE_CREATE_FAILED);
        }
    }

    private String uploadImage(CapsuleCreateDTO capsuleCreateDTO, MultipartFile image, Long userId) {
        if (image == null) {
            return null;
        }
        try {
            return s3Service.uploadFileToS3(image, userId.toString(), capsuleCreateDTO.capsuleBoxId().toString(), FileType.IMAGE);
        } catch (Exception e) {
            throw new CapsuleException(CapsuleErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    private String uploadAudio(CapsuleCreateDTO capsuleCreateDTO, MultipartFile audio, Long userId) {
        if (audio == null) {
            return null;
        }
        try {
            return s3Service.uploadFileToS3(audio, userId.toString(), capsuleCreateDTO.capsuleBoxId().toString(), FileType.AUDIO);
        } catch (Exception e) {
            throw new CapsuleException(CapsuleErrorCode.AUDIO_UPLOAD_FAILED);
        }
    }

    public ActiveCapsuleBoxDto getCapsuleBoxList(Long userId) {
        CapsuleBox activeCapsuleBox = findActiveCapsuleBox(userId);

        List<Capsule> capsules = activeCapsuleBox.getCapsules();
        List<String> capsuleColors = capsules.stream().map(capsule -> capsule.getColor()).toList();

        return new ActiveCapsuleBoxDto(
                activeCapsuleBox.getId(),
                activeCapsuleBox.getTheme(),
                activeCapsuleBox.getOpenedAt(),
                activeCapsuleBox.getClosedAt(),
                capsuleColors
        );
    }

    public Boolean hasActiveCapsuleBox(Long userId){
        List<CapsuleBox> capsuleBoxes = capsuleBoxRepository.findByUserId(userId);

        if(capsuleBoxes.isEmpty()){
            return false;
        }

        Optional<CapsuleBox> capsuleBox = capsuleBoxes.stream().filter(box -> box.getOpenedAt().isAfter(LocalDateTime.now())).findFirst();

        return capsuleBox.isPresent();
    }

    public CapsuleBox findActiveCapsuleBox(Long userId){
        List<CapsuleBox> capsuleBoxes = capsuleBoxRepository.findByUserId(userId);

        if(capsuleBoxes.isEmpty()){
            throw new CapsuleException(CapsuleErrorCode.CAPSULE_BOX_NOT_FOUND);
        }

        Optional<CapsuleBox> capsuleBox = capsuleBoxes.stream().filter(box -> box.getOpenedAt().isBefore(LocalDateTime.now())).findFirst();

        if(!capsuleBox.isPresent()){
            throw new CapsuleException(CapsuleErrorCode.ACTIVE_CAPSULE_BOX_NOT_FOUND);
        }

        return capsuleBox.get();
    }

    public List<HistoryCapsuleBoxes> getHistoryCapsuleBoxes(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<CapsuleBox> capsuleBoxPage = capsuleBoxRepository.findByUserIdAndOpenedAtBefore(userId, LocalDateTime.now(), pageable);

        return capsuleBoxPage.stream().map(capsuleBox -> new HistoryCapsuleBoxes(
                capsuleBox.getId(),
                capsuleBox.getTheme(),
                capsuleBox.getOpenedAt(),
                capsuleBox.getClosedAt(),
                capsuleBox.getCreatedAt()
        )).toList();
    }

    public void deleteCapsuleBox(Long capsuleBoxId) {
        Optional<CapsuleBox> capsuleBox = capsuleBoxRepository.findById(capsuleBoxId);

        if(!capsuleBox.isPresent()){
            throw new CapsuleException(CapsuleErrorCode.CAPSULE_BOX_NOT_FOUND);
        }

        try{
            capsuleBoxRepository.delete(capsuleBox.get());
        }catch (Exception e){
            throw new CapsuleException(CapsuleErrorCode.CAPSULE_BOX_DELETE_FAILED);
        }
    }

    public List<CapsuleListDTO> getCapsuleList(Long capsuleBoxId, Long userId, int page, int size) {
        CapsuleBox capsuleBox = capsuleBoxRepository.findById(capsuleBoxId)
                .orElseThrow(() -> new CapsuleException(CapsuleErrorCode.CAPSULE_BOX_NOT_FOUND));

        if (!capsuleBox.getOpenedAt().isBefore(LocalDateTime.now())) {
            throw new CapsuleException(CapsuleErrorCode.NOT_OPENED_CAPSULE_BOX);
        }

        if(capsuleBox.getUser().getId() != userId){
            throw new CapsuleException(CapsuleErrorCode.INVALID_CAPSULE_BOX_USER);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Capsule> capsulePage = capsuleRepository.findByCapsuleBoxId(capsuleBoxId, pageable);

        return capsulePage.stream().map(capsule -> new CapsuleListDTO(
                capsule.getId(),
                capsule.getColor(),
                capsule.getTitle(),
                capsule.getIsMine(),
                capsule.getCreatedAt()
        )).toList();
    }

    public CapsuleDTO getCapsule(Long capsuleId) {
        Optional<Capsule> capsule = capsuleRepository.findById(capsuleId);

        if(!capsule.isPresent()){
            throw new CapsuleException(CapsuleErrorCode.CAPSULE_NOT_FOUND);
        }

        return new CapsuleDTO(
                capsule.get().getId(),
                capsule.get().getColor(),
                capsule.get().getTitle(),
                capsule.get().getContent(),
                capsule.get().getWriter(),
                capsule.get().getImageUrl(),
                capsule.get().getAudioUrl(),
                capsule.get().getIsMine(),
                capsule.get().getCreatedAt()
        );
    }
}
