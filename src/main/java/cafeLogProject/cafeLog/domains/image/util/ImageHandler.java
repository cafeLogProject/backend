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
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

// 이미지 파일 서버 로컬 스토리지에 저장/불러오기
@Slf4j
public class ImageHandler {

    public static File save(String basePath, String imageId, MultipartFile image) {
        File newFile;
        try{
            if (image == null) throw new ImageInvalidException(ErrorCode.IMAGE_INVALID_ERROR);
            String fullPathName = basePath + imageId;
            newFile = new File(fullPathName);
            image.transferTo(newFile);   //파일 저장
        } catch (Exception e) {
            throw new ImageSaveException(ErrorCode.IMAGE_SAVE_ERROR);
        }
        if (!hasLastModified(newFile)) {
            delete(basePath, imageId);
            throw new ImageSaveException("읽기 권한이 없거나, 알 수 없는 오류로 인해 Last-Modified값이 0입니다", ErrorCode.IMAGE_SAVE_ERROR);
        }
        return newFile;
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

    // 이미지의 Last-Modified 값 리턴
    public static ZonedDateTime getLastModifiedDate(Resource resource) {
        try {
            File file = resource.getFile();
            Long lastModifiedNum = file.lastModified();
            if (lastModifiedNum == 0) throw new ImageLoadException("알 수 없는 원인으로 인해 lastModified값이 0입니다.", ErrorCode.IMAGE_LOAD_ERROR);
            ZonedDateTime lastModifiedDate = Instant.ofEpochMilli(lastModifiedNum).atZone(ZoneId.of("UTC"))
                    .truncatedTo(ChronoUnit.SECONDS);
            return lastModifiedDate;
        } catch (Exception e) {
            // 다른 저장소 (예: S3)에서 로드된 경우 (URL 리소스 처리하는 경우) 별도 처리 필요
            throw new ImageInvalidException(ErrorCode.IMAGE_INVALID_ERROR);
        }
    }

    // 이미지 저장시 Last-Modified 생성되었는지 확인
    public static boolean hasLastModified(File file) {
        try {
            Long lastModifiedNum = file.lastModified();
            if (lastModifiedNum == 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            // 다른 저장소 (예: S3)에서 로드된 경우 (URL 리소스 처리하는 경우) 별도 처리 필요
            return false;
        }
    }

}
