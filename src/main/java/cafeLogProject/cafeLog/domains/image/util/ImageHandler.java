package cafeLogProject.cafeLog.domains.image.util;

import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.image.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

// 이미지 파일 서버 로컬 스토리지에 저장/불러오기
@Slf4j
public class ImageHandler {

    public static File save(String basePath, String imageId, MultipartFile image) {
        try{
            if (image == null) throw new ImageInvalidException(ErrorCode.IMAGE_INVALID_ERROR);
            String fullPathName = basePath + imageId;
            File newFile = new File(fullPathName);
            image.transferTo(newFile);   //파일 저장
            return newFile;
        } catch (Exception e) {
            throw new ImageSaveException(ErrorCode.IMAGE_SAVE_ERROR);
        }
    }

    public static Resource load(String basePath, String imageId) {
        Resource resource;
        try {
            resource = new UrlResource("file:" + basePath + imageId);
        } catch (Exception e) {
            throw new ImageLoadException(ErrorCode.IMAGE_LOAD_ERROR);
        }
        if (!resource.exists() || !resource.isReadable()) {
            throw new ImageNotFoundException(imageId, ErrorCode.IMAGE_NOT_FOUND_ERROR);
        }
        return resource;
    }

    public static void delete(String basePath, String imageId) {
        File file = new File(basePath + imageId);
        if (!file.exists()){
            log.error("image Not Found Exception ["+basePath+imageId+"] : 존재하지 않는 이미지 삭제 시도");
            throw new ImageNotFoundException(ErrorCode.IMAGE_NOT_FOUND_ERROR);
//          return;     //존재하지 않은 경우(이미 삭제한 경우) 건너뛰기
        }
        try {
            file.delete();
        } catch (Exception e) {
            log.error(e.toString());
            log.error("Image Delete Exception ["+basePath+imageId+"] : 이미지 삭제 실패. 차후 조치 필요");
            throw new ImageDeleteException(ErrorCode.IMAGE_DELETE_ERROR);
        }
    }

    public static String findImageIdByFile(File file) {
        String fileName = file.getName();
        if (fileName.equals("")) throw new ImageLoadException(fileName, ErrorCode.IMAGE_LOAD_ERROR);
        return fileName;
    }

    // MIME 타입 검사
    public static void isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equals("image/jpg") || contentType.equals("image/jpeg"))) {
            throw new ImageInvalidException(ErrorCode.IMAGE_INVALID_ERROR);
        }
    }

    // 손상된 이미지 여부 검사
    public static void isDamagedImageFile(MultipartFile file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null) {
                throw new ImageDamagedException(ErrorCode.IMAGE_DAMAGED_ERROR);
            }
        } catch (Exception e) {
            throw new ImageDamagedException(ErrorCode.IMAGE_DAMAGED_ERROR);
        }
    }

}
