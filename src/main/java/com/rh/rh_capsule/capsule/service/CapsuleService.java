package com.rh.rh_capsule.capsule.service;

import com.rh.rh_capsule.auth.domain.User;
import com.rh.rh_capsule.auth.exception.AuthErrorCode;
import com.rh.rh_capsule.auth.exception.AuthException;
import com.rh.rh_capsule.auth.jwt.JwtProvider;
import com.rh.rh_capsule.auth.repository.UserRepository;
import com.rh.rh_capsule.capsule.domain.Capsule;
import com.rh.rh_capsule.capsule.domain.CapsuleBox;
import com.rh.rh_capsule.capsule.dto.*;
import com.rh.rh_capsule.capsule.exception.CapsuleErrorCode;
import com.rh.rh_capsule.capsule.exception.CapsuleException;
import com.rh.rh_capsule.capsule.repository.CapsuleBoxRepository;
import com.rh.rh_capsule.capsule.repository.CapsuleRepository;
import com.rh.rh_capsule.utils.S3Uploader;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CapsuleService {
    private final CapsuleBoxRepository capsuleBoxRepository;
    private final CapsuleRepository capsuleRepository;
    private final UserRepository userRepository;
    private final EntityManager em;
    private final S3Uploader s3Uploader;
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

    public void createCapsule(CapsuleCreateDTO capsuleCreateDTO, MultipartFile image, MultipartFile audio, String accessToken) {
        Optional<CapsuleBox> capsuleBox = capsuleBoxRepository.findById(capsuleCreateDTO.capsuleBoxId());

        if(!capsuleBox.isPresent()){
            throw new CapsuleException(CapsuleErrorCode.CAPSULE_BOX_NOT_FOUND);
        }

        if(!capsuleBox.get().getClosedAt().isAfter(LocalDateTime.now())){
            throw new CapsuleException(CapsuleErrorCode.NOT_ACTIVE_CAPSULE_BOX);
        }

        String imageUrl = null;
        String audioUrl = null;
        if(image != null){
            try {
                imageUrl = s3Uploader.uploadFileToS3(image, capsuleCreateDTO.userId().toString(), capsuleCreateDTO.capsuleBoxId().toString(), "image");
            }catch (Exception e){
                throw new CapsuleException(CapsuleErrorCode.IMAGE_UPLOAD_FAILED);
            }
        }
        if(audio != null){
            try {
                audioUrl = s3Uploader.uploadFileToS3(audio, capsuleCreateDTO.userId().toString(), capsuleCreateDTO.capsuleBoxId().toString(), "audio");
            }catch (Exception e){
                throw new CapsuleException(CapsuleErrorCode.AUDIO_UPLOAD_FAILED);
            }

        }

        Boolean isMine = false;

        if(accessToken != null){
            Long userId = jwtProvider.extractId(accessToken);
            if(userId.equals(capsuleCreateDTO.userId())){
                isMine = true;
            }else {
                throw new AuthException(AuthErrorCode.UNAUTHORIZED);
            }

            List<Capsule> capsules = capsuleBox.get().getCapsules();
            Optional<Capsule> mineCapsule = capsules.stream().filter(capsule -> capsule.getIsMine()).findFirst();
            if(mineCapsule.isPresent()){
                throw new CapsuleException(CapsuleErrorCode.MINE_CAPSULE_ALREADY_EXISTS);
            }
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
                .isMine(isMine)
                .build();
        try{
            capsuleRepository.save(newCapsule);
        }catch (Exception e){
            throw new CapsuleException(CapsuleErrorCode.CAPSULE_CREATE_FAILED);
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

        Optional<CapsuleBox> capsuleBox = capsuleBoxes.stream().filter(box -> box.getOpenedAt().isAfter(LocalDateTime.now())).findFirst();

        if(!capsuleBox.isPresent()){
            throw new CapsuleException(CapsuleErrorCode.ACTIVE_CAPSULE_BOX_NOT_FOUND);
        }

        return capsuleBox.get();
    }

    public List<HistoryCapsuleBoxes> getHistoryCapsuleBoxes(Long userId, PaginationDTO paginationDTO) {
        List<CapsuleBox> capsuleBoxes = capsuleBoxRepository.findByUserId(userId);

        List<CapsuleBox> historyCapsuleBoxes = capsuleBoxes.stream().filter(box -> box.getOpenedAt().isAfter(LocalDateTime.now())).toList();

        if(historyCapsuleBoxes.size() == 1){
            return List.of(new HistoryCapsuleBoxes(
                    historyCapsuleBoxes.get(0).getId(),
                    historyCapsuleBoxes.get(0).getTheme(),
                    historyCapsuleBoxes.get(0).getOpenedAt(),
                    historyCapsuleBoxes.get(0).getClosedAt(),
                    historyCapsuleBoxes.get(0).getCreatedAt()
            ));
        }
        historyCapsuleBoxes.sort((box1, box2) -> box2.getCreatedAt().compareTo(box1.getCreatedAt()));


        Long start = paginationDTO.page() * paginationDTO.size();
        Long end = Math.min(start + paginationDTO.size(), historyCapsuleBoxes.size());
        List<CapsuleBox> pagedHistoryCapsuleBoxes = historyCapsuleBoxes.subList(start.intValue(), end.intValue());

        return pagedHistoryCapsuleBoxes.stream().map(box -> new HistoryCapsuleBoxes(
                box.getId(),
                box.getTheme(),
                box.getOpenedAt(),
                box.getClosedAt(),
                box.getCreatedAt()
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

    public List<CapsuleListDTO> getCapsuleList(Long capsuleBoxId, PaginationDTO paginationDTO) {
        Optional<CapsuleBox> capsuleBox = capsuleBoxRepository.findById(capsuleBoxId);

        if(!capsuleBox.isPresent()){
            throw new CapsuleException(CapsuleErrorCode.CAPSULE_BOX_NOT_FOUND);
        }

        List<Capsule> capsules = capsuleBox.get().getCapsules();
        if(capsules.size() == 1){
            return List.of(new CapsuleListDTO(
                    capsules.get(0).getId(),
                    capsules.get(0).getColor(),
                    capsules.get(0).getWriter(),
                    capsules.get(0).getIsMine(),
                    capsules.get(0).getCreatedAt()
            ));
        }

        capsules.sort((capsule1, capsule2) -> capsule2.getCreatedAt().compareTo(capsule1.getCreatedAt()));

        Long start = paginationDTO.page() * paginationDTO.size();
        Long end = Math.min(start + paginationDTO.size(), capsules.size());

        List<Capsule> pagedCapsules = capsules.subList(start.intValue(), end.intValue());

        return pagedCapsules.stream().map(capsule -> new CapsuleListDTO(
                capsule.getId(),
                capsule.getColor(),
                capsule.getWriter(),
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
