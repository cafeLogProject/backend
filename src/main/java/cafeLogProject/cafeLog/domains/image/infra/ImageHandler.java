package cafeLogProject.cafeLog.domains.image.infra;

import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.domains.image.exception.ImageLoadException;
import cafeLogProject.cafeLog.domains.image.exception.ImageNotFoundException;
import cafeLogProject.cafeLog.domains.image.exception.ImageSaveException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

// 이미지 파일 서버 로컬 스토리지에 저장/불러오기

@Component
public class ImageHandler {
    public String save(String basePath, MultipartFile image) {
        try{
            String imageId = UUID.randomUUID().toString();  // 파일 이름을 위한 UUID 생성
            String fullPathName = basePath + imageId;
            image.transferTo(new File(fullPathName));   //파일 저장
            return imageId;
        } catch (Exception e) {
            throw new ImageSaveException(ErrorCode.IMAGE_SAVE_ERROR);
        }
    }

    public Resource load(String basePath, String imageId) {
        try {
            Resource resource = new UrlResource("file:" + basePath + imageId);
            if (!resource.exists() || !resource.isReadable()) {
                throw new ImageNotFoundException(imageId, ErrorCode.IMAGE_NOT_FOUND_ERROR);
            }
            return resource;
        } catch (Exception e) {
            throw new ImageLoadException(ErrorCode.IMAGE_LOAD_ERROR);
        }
    }
}
