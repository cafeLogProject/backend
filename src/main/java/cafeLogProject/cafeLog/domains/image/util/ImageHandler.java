package cafeLogProject.cafeLog.domains.image.util;

import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.domains.image.exception.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

// 이미지 파일 서버 로컬 스토리지에 저장/불러오기

public class ImageHandler {

    //이미지 파일 형식이 맞는지 확인
    public static void isImageFile(MultipartFile file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                throw new ImageInvalidException(ErrorCode.IMAGE_INVALID_ERROR);
            }
        } catch (Exception e) {
            throw new ImageInvalidException(ErrorCode.IMAGE_INVALID_ERROR);
        }
    }

    public static File save(String basePath, MultipartFile image) {
        try{
            String imageId = UUID.randomUUID().toString();  // 파일 이름을 위한 UUID 생성
            String fullPathName = basePath + imageId;
            File newFile = new File(fullPathName);
            image.transferTo(newFile);   //파일 저장
            return newFile;
        } catch (Exception e) {
            throw new ImageSaveException(ErrorCode.IMAGE_SAVE_ERROR);
        }
    }

    public static Resource load(String basePath, String imageId) {
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

    public static void delete(String basePath, String imageId) {
        try {
            File file = new File(basePath + imageId);
            if (!file.exists()){
                return;     //존재하지 않은 경우(이미 삭제한 경우) 건너뛰기
//                throw new ImageNotFoundException(ErrorCode.IMAGE_NOT_FOUND_ERROR);
            }
            file.delete();
        } catch (Exception e) {
            throw new ImageDeleteException(ErrorCode.IMAGE_DELETE_ERROR);
        }
    }

    public static String findImageIdByFile(File file) {
        String fileName = file.getName();
        if (fileName.equals("")) throw new ImageLoadException(ErrorCode.IMAGE_LOAD_ERROR);
        return fileName;
    }


}
