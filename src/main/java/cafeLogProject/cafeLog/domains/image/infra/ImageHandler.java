package cafeLogProject.cafeLog.domains.image.infra;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

// 이미지 파일 서버 로컬 스토리지에 저장/불러오기
public class ImageHandler {
    public String save(String basePath, MultipartFile image) throws IOException {
        String imageId = UUID.randomUUID().toString();  // 파일 이름을 위한 UUID 생성
        String fullPathName = basePath + imageId;
        image.transferTo(new File(fullPathName));   //파일 저장
        return imageId;
    }

    public Resource load(String basePath, String imageId) throws IOException {
        Resource resource = new UrlResource("file:" + basePath + imageId);
        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("파일을 읽을 수 없거나 존재하지 않습니다: " + imageId);
        }
        return resource;
    }
}
