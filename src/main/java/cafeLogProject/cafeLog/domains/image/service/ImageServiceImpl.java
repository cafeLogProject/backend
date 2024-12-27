package cafeLogProject.cafeLog.domains.image.service;

import cafeLogProject.cafeLog.domains.image.dto.ImageDto;
import cafeLogProject.cafeLog.domains.image.dto.ImageResponseDto;
import cafeLogProject.cafeLog.domains.image.infra.ImageHandler;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Value("${path.base}")
    private String basePath;
    private String testImageRelativePath = "/src/main/resources/static/imgs/test/";
    private String reviewImageRelativePath = "/src/main/resources/static/imgs/review/";
    private String profileImageRelativePath = "/src/main/resources/static/imgs/profile/";

    private final ReviewRepository reviewRepository;
    private final ImageHandler imageHandler;

    @Override
    public String addReviewImage(MultipartFile multipartFile) {
        String path = basePath+reviewImageRelativePath;
        String imageId = addImage(path, multipartFile);
        return imageId;
    }

    @Override
    public String addProfileImage(MultipartFile multipartFile) {
        String path = basePath+profileImageRelativePath;
        String imageId = addImage(path, multipartFile);
        return imageId;
    }

    @Override
    public ImageResponseDto loadReviewImage(String imageId) {
        String path = basePath+reviewImageRelativePath;
        System.out.println(path);
        Resource imageFile = loadImage(path, imageId);
        return ImageResponseDto.builder()
                .imageFile(imageFile)
                .imageId(imageId)
                .build();
    }


    @Override
    public ImageResponseDto loadProfileImage(String imageId) {
        String path = basePath+profileImageRelativePath;
        Resource imageFile = loadImage(path, imageId);
        return ImageResponseDto.builder()
                .imageFile(imageFile)
                .imageId(imageId)
                .build();
    }





    @Transactional
    private String addImage(String path, MultipartFile multipartFile) {
        String imageId = imageHandler.save(path, multipartFile);
        return imageId;
    }

    private Resource loadImage(String path, String imageId) {
        Resource imageFile = imageHandler.load(path, imageId);
        return imageFile;
    }


}
