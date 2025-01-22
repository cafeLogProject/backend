package cafeLogProject.cafeLog.domains.image.util;

import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.UnexpectedServerException;
import cafeLogProject.cafeLog.common.exception.image.ImageSaveException;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageCompressor {

    //이미지 손실 압축 (압축률 높음, 복원 불가)
    // 주의) 파일명이 파일명.webp로 변경됨
    public static File convertToWebp(String path, String imageId, File originalFile) {
        try {
            File convertedFile =  ImmutableImage.loader()  // 라이브러리 객체 생성
                    .fromFile(originalFile)
                    .output(WebpWriter.DEFAULT, new File(path+imageId+".webp")); // 손실 압축 설정
            originalFile.delete();
            return convertedFile;
        } catch (Exception e) {
            throw new ImageSaveException("이미지 압축에 실패했습니다.", ErrorCode.IMAGE_SAVE_ERROR);
        }
    }

    // 이미지 무손실 압축 (압축률 낮음, 복원 가능)
    // 주의) 파일명이 파일명.webp로 변경됨
    public static File convertToWebpWithLossless(String path, String imageId, File originalFile) {
        try {
            File convertedFile = ImmutableImage.loader()  // 라이브러리 객체 생성
                    .fromFile(originalFile)
                    .output(WebpWriter.DEFAULT.withLossless(), new File(path+imageId+".webp")); // 무손실 압축 설정
            originalFile.delete();
            return convertedFile;
        } catch (Exception e) {
            throw new ImageSaveException("이미지 압축에 실패했습니다.", ErrorCode.IMAGE_SAVE_ERROR);
        }
    }

    public static void renameWebpFileTo(String oldPath, String newPath) {
        try {
            Path oldFile = Paths.get(oldPath+".webp");
            Path newFile = Paths.get(newPath+".webp");
            Files.move(oldFile, newFile);
        } catch (Exception e) {
            throw new UnexpectedServerException("이미지 이름 변경에 실패했습니다.", ErrorCode.UNEXPECTED_ERROR);
        }
    }
}
