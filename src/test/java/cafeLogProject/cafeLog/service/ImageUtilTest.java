package cafeLogProject.cafeLog.service;

import cafeLogProject.cafeLog.api.image.service.ImageUtil;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.image.ImageDamagedException;
import cafeLogProject.cafeLog.common.exception.image.ImageInvalidException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

//@TestPropertySource(locations = "classpath:application-test.yml")
//@ActiveProfiles("test")
public class ImageUtilTest {
    String requestImagePath;
    String savedFolderPath;
    String damagedImagePath;
    String bigImagePath;
    ImageUtil imageUtil = new ImageUtil();

    @BeforeEach
    public void before() throws Exception{
        // ImageUtil 클래스의 환경변수 임의로 설정
        ReflectionTestUtils.setField(imageUtil, "BASE_PATH", "C:\\spring_study\\backend");   // 로컬에서 테스트시 사용, value에 env의 PROJECT_PATH값 입력해야 함
//        ReflectionTestUtils.setField(imageUtil, "BASE_PATH", "/app");     //깃헙 액션에서 테스트시 사용

        // 환경변수 등록되었는지 확인
        assertTrue(ImageUtil.BASE_PATH != null);
        // 해당 경로에 프로젝트 폴더 존재하는지 확인
        File projectFolder = new File(ImageUtil.BASE_PATH);
        assertTrue(projectFolder.exists());

        // 상대 경로 정의
        requestImagePath =  "/src/test/resources/static/imgs/test/unsaved/sample.jpg";
        damagedImagePath =  "/src/test/resources/static/imgs/test/unsaved/damagedSample.jpg";
        bigImagePath =  "/src/test/resources/static/imgs/test/unsaved/bigVolumeSample.jpg";
        savedFolderPath = "/src/test/resources/static/imgs/test/saved/";

        // 이미지 저장할 테스트 디렉토리 생성
        Path directoryPath = Paths.get(ImageUtil.BASE_PATH+savedFolderPath);
        Files.createDirectory(directoryPath);

        // 테스트할 이미지 존재하는지 확인
        Resource imgResource = new UrlResource("file:"+ImageUtil.BASE_PATH+requestImagePath);
        assertTrue(imgResource.exists() && imgResource.isReadable());
        Resource damagedImgResource = new UrlResource("file:"+ImageUtil.BASE_PATH+damagedImagePath);
        assertTrue(damagedImgResource.exists() && damagedImgResource.isReadable());
        Resource bigImageResource = new UrlResource("file:"+ImageUtil.BASE_PATH+bigImagePath);
        assertTrue(bigImageResource.exists() && bigImageResource.isReadable());
    }

    @AfterEach
    public void after(){
        // 이미지 저장한 테스트 디렉토리 삭제 (저장한 이미지 모두 삭제)
        File savedFolder = new File(ImageUtil.BASE_PATH+savedFolderPath);
        if(savedFolder.isDirectory()){
            File[] images = savedFolder.listFiles();
            // 하위 파일 삭제
            for(File image : images){
                image.delete();
            }
            savedFolder.delete();
        }
    }



    @Test
    @DisplayName("이미지 파일 저장 성공")
    public void saveImageSuccess() throws Exception {
        // Given
        MockMultipartFile image = new MockMultipartFile(
                "test", // 파일의 파라미터 이름
                "sample.jpg", // 실제 파일 이름
                "image/jpeg", // 파일의 확장자 타입
                new FileInputStream(new File(ImageUtil.BASE_PATH+requestImagePath)) // 실제 파일
        );
        UUID newImageId = UUID.randomUUID();

        // When
        ImageUtil.saveImage(savedFolderPath, newImageId.toString(), image);

        // Then
        Resource resource = new UrlResource("file:"+ImageUtil.BASE_PATH+savedFolderPath+newImageId.toString()+".webp");
        assertTrue(resource.exists() && resource.isReadable());

    }

    @Test
    @DisplayName("이미지가 누락됐거나 MIME 타입이 image/jpeg가 아닌 경우 예외 발생")
    public void notMatchedMIMEtypeException() throws Exception{
        // Given
        MockMultipartFile image = new MockMultipartFile(
                "test", // 파일의 파라미터 이름
                "sample.jpg", // 실제 파일 이름
                "????", // 파일의 확장자 타입
                new FileInputStream(new File(ImageUtil.BASE_PATH+requestImagePath)) // 실제 파일
        );
        UUID newImageId = UUID.randomUUID();

        // When
        ImageInvalidException e = assertThrows(ImageInvalidException.class, ()->ImageUtil.saveImage(savedFolderPath, newImageId.toString(), image));

        // Then
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.IMAGE_INVALID_ERROR);
    }

    @Test
    @DisplayName("손상된 이미지인 경우 예외 발생")
    public void damagedImageException() throws Exception {
        // Given
        MockMultipartFile image = new MockMultipartFile(
                "test", // 파일의 파라미터 이름
                "sample.jpg", // 실제 파일 이름
                "image/jpeg", // 파일의 확장자 타입
                new FileInputStream(new File(ImageUtil.BASE_PATH+damagedImagePath)) // 실제 파일
        );
        UUID newImageId = UUID.randomUUID();

        // When
        ImageDamagedException e = assertThrows(ImageDamagedException.class, ()->ImageUtil.saveImage(savedFolderPath, newImageId.toString(), image));

        // Then
        assertThat(e.getErrorCode()).isEqualTo(ErrorCode.IMAGE_DAMAGED_ERROR);
    }

//    // 해당 예외 발생 여부는 컨트롤러단에서 테스트해야함
//    @Test
//    @DisplayName("이미지 용량 초과인 경우 예외 발생")
//    public void bigImageException() throws Exception{
//        // Given
//        MockMultipartFile image = new MockMultipartFile(
//                "test", // 파일의 파라미터 이름
//                "sample.jpg", // 실제 파일 이름
//                "image/jpeg", // 파일의 확장자 타입
//                new FileInputStream(new File(ImageUtil.BASE_PATH+bigImagePath)) // 실제 파일
//        );
//        UUID newImageId = UUID.randomUUID();
//
//        // When
//        MaxUploadSizeExceededException e = assertThrows(MaxUploadSizeExceededException.class, ()->ImageUtil.saveImage(savedFolderPath, newImageId.toString(), image));
//
//        // Then
//        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.PAYLOAD_TOO_LARGE);
//    }
}
