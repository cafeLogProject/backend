package cafeLogProject.cafeLog.service.Impl;

import cafeLogProject.cafeLog.dto.ImageDto;
import cafeLogProject.cafeLog.dto.image.ImageResponseDto;
import cafeLogProject.cafeLog.entity.Review;
import cafeLogProject.cafeLog.repository.ReviewRepository;
import cafeLogProject.cafeLog.service.ImageHandler;
import cafeLogProject.cafeLog.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
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
    ImageHandler imageHandler = new ImageHandler();

    @Override
    public String addImage(ImageDto imageDto) {
        try {
            System.out.println(basePath+testImageRelativePath);
            String imageId = imageHandler.save(basePath+testImageRelativePath, imageDto.getFile());
            return imageId;
        } catch (IOException e) {
            System.out.println("addImage error");
            return "addImage error";
        }
    }

    @Override
    public void addReviewImages(List<ImageDto> imageDtoList, Review review) {
        for (ImageDto imageDto : imageDtoList) {
            String fileId = addImage(imageDto);
            review.addImageId(fileId);
        }
        reviewRepository.save(review);
    }

    @Override
    public ImageResponseDto loadImage(String imageId) {
        try {
            return ImageResponseDto.builder()
                    .imageFile(imageHandler.load(basePath+testImageRelativePath, imageId))
                    .imageId(imageId)
                    .build();
        } catch (Exception e) {
            System.out.println("loadeImage error");
            return null;
        }
    }

}
