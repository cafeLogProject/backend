package cafeLogProject.cafeLog.domains.image.service;

import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.domains.image.exception.ImageLoadException;
import cafeLogProject.cafeLog.domains.image.util.ImageCompressor;
import cafeLogProject.cafeLog.domains.image.util.ImageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Value("${path.base}")
    private String basePath;
    private String testImageRelativePath = "/src/main/resources/static/imgs/test/";
    private String reviewImageRelativePath = "/src/main/resources/static/imgs/review/";
    private String profileImageRelativePath = "/src/main/resources/static/imgs/profile/";

    @Override
    public String addReviewImage(MultipartFile multipartFile) {
        String path = basePath+reviewImageRelativePath;
        File imageFile = addImage(path, multipartFile);
        String imageFileId = ImageHandler.findImageIdByFile(imageFile);
        File compressedFile = ImageCompressor.convertToWebpWithLossless(path, imageFileId, imageFile);
        String imageId = ImageHandler.findImageIdByFile(compressedFile);
        return imageId;
    }

    @Override
    public String addProfileImage(MultipartFile multipartFile) {
        String path = basePath+profileImageRelativePath;
        File imageFile = addImage(path, multipartFile);
        String imageFileId = ImageHandler.findImageIdByFile(imageFile);
        File compressedFile = ImageCompressor.convertToWebpWithLossless(path, imageFileId, imageFile);
        String imageId = ImageHandler.findImageIdByFile(compressedFile);
        return imageId;
    }

    @Override
    public Resource loadReviewImage(String imageId) {
        String path = basePath+reviewImageRelativePath;
        Resource imageFile = loadImage(path, imageId);
        return imageFile;
    }


    @Override
    public Resource loadProfileImage(String imageId) {
        String path = basePath+profileImageRelativePath;
        Resource imageFile = loadImage(path, imageId);
        return imageFile;
    }

    @Override
    public void deleteReviewImage(String imageId) {
        String path = basePath+reviewImageRelativePath;
        ImageHandler.delete(path, imageId);
    }

    @Override
    public void deleteProfileImage(String imageId) {
        String path = basePath+profileImageRelativePath;
        ImageHandler.delete(path, imageId);
    }

    @Transactional
    private File addImage(String path, MultipartFile file) {
        ImageHandler.isImageFile(file);

        return ImageHandler.save(path, file);
    }

    private Resource loadImage(String path, String imageId) {
        Resource imageFile = ImageHandler.load(path, imageId);
        return imageFile;
    }






}
