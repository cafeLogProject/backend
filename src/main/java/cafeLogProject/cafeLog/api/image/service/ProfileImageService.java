package cafeLogProject.cafeLog.api.image.service;


import cafeLogProject.cafeLog.api.image.dto.RegistProfileImageResponse;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.user.UserNotFoundException;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProfileImageService {
    private final UserRepository userRepository;

    // 프로필 이미지 저장/덮어쓰기
    @Transactional
    public RegistProfileImageResponse updateProfileImage(String username, MultipartFile multipartFile) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->{
            throw new UserNotFoundException(username, ErrorCode.USER_NOT_FOUND_ERROR);
        });
        String newImageId = user.getId().toString();
        ImageUtil.saveImage(ImageUtil.PROFILE_IMAGE_PATH, newImageId, multipartFile);
        if (!user.isImageExist()) user.updateImageExist(true);

        return RegistProfileImageResponse.builder()
                .imageId(newImageId)
                .build();
    }

    public Resource loadProfileImage(String imageId) {
        Resource imageFile = ImageUtil.loadCompressedImage(ImageUtil.PROFILE_IMAGE_PATH, imageId);
        return imageFile;
    }

    @Transactional
    public void deleteProfileImage(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->{
            throw new UserNotFoundException(username, ErrorCode.USER_NOT_FOUND_ERROR);
        });
        ImageUtil.deleteCompressedImage(ImageUtil.PROFILE_IMAGE_PATH, user.getId().toString());
        user.updateImageExist(false);
    }

}
