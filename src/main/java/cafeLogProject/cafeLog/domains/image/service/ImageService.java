package cafeLogProject.cafeLog.domains.image.service;

import cafeLogProject.cafeLog.domains.image.dto.ImageDto;
import cafeLogProject.cafeLog.domains.image.dto.ImageResponseDto;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional(readOnly = true)
public interface ImageService {
    @Transactional
    String addReviewImage(MultipartFile multipartFile);

    @Transactional
    String addProfileImage(MultipartFile multipartFile);

    ImageResponseDto loadReviewImage(String imageId);
    ImageResponseDto loadProfileImage(String imageId);

}
