package cafeLogProject.cafeLog.domains.image.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public interface ImageService {
    @Transactional
    String addReviewImage(MultipartFile multipartFile);

    @Transactional
    String addProfileImage(MultipartFile multipartFile);

    Resource loadReviewImage(String imageId);
    Resource loadProfileImage(String imageId);

    @Transactional
    void deleteReviewImage(String imageId);
    @Transactional
    void deleteProfileImage(String imageId);

}
