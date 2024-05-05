package com.rh.rh_capsule.capsule.service;

import com.rh.rh_capsule.auth.domain.User;
import com.rh.rh_capsule.auth.repository.UserRepository;
import com.rh.rh_capsule.capsule.domain.Capsule;
import com.rh.rh_capsule.capsule.domain.CapsuleBox;
import com.rh.rh_capsule.capsule.dto.*;
import com.rh.rh_capsule.capsule.exception.CapsuleErrorCode;
import com.rh.rh_capsule.capsule.exception.CapsuleException;
import com.rh.rh_capsule.capsule.repository.CapsuleBoxRepository;
import com.rh.rh_capsule.capsule.repository.CapsuleRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public void createCapsuleBox(CapsuleBoxCreateDTO capsuleBoxCreateDTO, Long userId) {
//        Optional<User> user = userRepository.findById(userId);
//        if (!user.isPresent()) {
//            throw new CapsuleException(CapsuleErrorCode.USER_NOT_FOUND);
//        }

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

    public void createCapsule(CapsuleCreateDTO capsuleCreateDTO, String imageUrl, String audioUrl) {
        Optional<CapsuleBox> capsuleBox = capsuleBoxRepository.findById(capsuleCreateDTO.capsuleBoxId());

        if(!capsuleBox.isPresent()){
            throw new CapsuleException(CapsuleErrorCode.CAPSULE_BOX_NOT_FOUND);
        }

        if(!capsuleBox.get().getClosedAt().isAfter(LocalDateTime.now())){
            throw new CapsuleException(CapsuleErrorCode.NOT_ACTIVE_CAPSULE_BOX);
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
}
