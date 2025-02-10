package cafeLogProject.cafeLog.api.image.service;

import cafeLogProject.cafeLog.domains.image.util.ImageCompressor;
import cafeLogProject.cafeLog.domains.image.util.ImageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


// 이미지 파일 저장 관련
@Service
@Slf4j
public class ImageUtil {
    public static String BASE_PATH;

    public static String DRAFT_REVIEW_IMAGE_PATH = "/src/main/resources/static/imgs/draftReview/";
    public static String PROFILE_IMAGE_PATH = "/src/main/resources/static/imgs/profile/";
    public static String REVIEW_IMAGE_PATH = "/src/main/resources/static/imgs/review/";

    @Value("${path.base}")
    public void setBasePath(String basePath){
        this.BASE_PATH = basePath;
    }

    // 이미지 파일 저장 & 압축
    public static void saveImage(String path, String imageId, MultipartFile file) {
        ImageHandler.isImageFile(file);
        ImageHandler.isDamagedImageFile(file);
        File savedImageFile = ImageHandler.save(BASE_PATH+path, imageId, file);
        ImageCompressor.convertToWebpWithLossless(BASE_PATH+path, imageId, savedImageFile);  //이미지 압축
    }

    // 압축 이미지 파일 로드
    public static Resource loadCompressedImage(String path, String imageId) {
        Resource imageFile = ImageHandler.load(BASE_PATH+path, imageId+".webp");
        return imageFile;
    }

    // 압축 이미지 파일 삭제
    public static void deleteCompressedImage(String path, String imageId) {
        ImageHandler.delete(BASE_PATH+path, imageId + ".webp");
    }
    
    // 이미지 파일 경로 변경
    public static void renameCompressedImage(String oldPath, String newPath){
        ImageCompressor.renameWebpFileTo(BASE_PATH+oldPath, BASE_PATH+newPath);
    }

}
