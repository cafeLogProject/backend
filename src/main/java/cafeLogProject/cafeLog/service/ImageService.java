package cafeLogProject.cafeLog.service;

import cafeLogProject.cafeLog.dto.ImageDto;
import cafeLogProject.cafeLog.dto.image.ImageResponseDto;
import cafeLogProject.cafeLog.entity.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public interface ImageService {
    // 리뷰 이미지 저장
    @Transactional
    void addReviewImages(List<ImageDto> imageDtoList, Review review);

    
    // 이미지 파일 저장, UUID 리턴
    @Transactional
    String addImage(ImageDto imageDto);
//    @Transactional
//    boolean addImages(List<ImageDto> imageDtoList) throws IOException

    ImageResponseDto loadImage(String ImageId);

}
