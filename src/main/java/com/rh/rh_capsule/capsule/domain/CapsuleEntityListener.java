package com.rh.rh_capsule.capsule.domain;

import com.rh.rh_capsule.config.ApplicationContextProvider;
import com.rh.rh_capsule.utils.S3Service;
import jakarta.persistence.PreRemove;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CapsuleEntityListener {
    private S3Service s3Service;
    public CapsuleEntityListener() {
    }

    @PreRemove
    public void preRemove(Capsule capsule) {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        if (context != null) {
            this.s3Service = context.getBean(S3Service.class);
            if (capsule.getImageUrl() != null) {
                s3Service.deleteFileFromS3(capsule.getImageUrl());
            }
            if (capsule.getAudioUrl() != null) {
                s3Service.deleteFileFromS3(capsule.getAudioUrl());
            }
        }
    }
}
