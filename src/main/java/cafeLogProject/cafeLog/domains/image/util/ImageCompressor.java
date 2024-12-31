package cafeLogProject.cafeLog.domains.image.util;

import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.image.ImageSaveException;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;

import java.io.File;

public class ImageCompressor {

    //이미지 손실 압축 (압축률 높음, 복원 불가)
    public static File convertToWebp(String path, String originalFileName, File originalFile) {
        try {
            File convertedFile =  ImmutableImage.loader()  // 라이브러리 객체 생성
                    .fromFile(originalFile)
                    .output(WebpWriter.DEFAULT, new File( path + originalFileName + ".webp")); // 손실 압축 설정, fileName.webp로 파일 생성
            originalFile.delete();
            return convertedFile;
        } catch (Exception e) {
            throw new ImageSaveException("이미지 압축에 실패했습니다.", ErrorCode.IMAGE_SAVE_ERROR);
        }
    }

    //이미지 무손실 압축 (압축률 낮음, 복원 가능)
    public static File convertToWebpWithLossless(String path, String originalFileName, File originalFile) {
        try {
            File convertedFile = ImmutableImage.loader()  // 라이브러리 객체 생성
                    .fromFile(originalFile)
                    .output(WebpWriter.DEFAULT.withLossless(), new File(path + originalFileName + ".webp")); // 무손실 압축 설정, fileName.webp로 파일 생성
            originalFile.delete();
            return convertedFile;
        } catch (Exception e) {
            throw new ImageSaveException("이미지 압축에 실패했습니다.", ErrorCode.IMAGE_SAVE_ERROR);
        }
    }
}
