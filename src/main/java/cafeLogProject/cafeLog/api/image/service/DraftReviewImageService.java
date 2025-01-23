package cafeLogProject.cafeLog.api.image.service;


import cafeLogProject.cafeLog.api.image.dto.RegistDraftReviewImageResponse;
import cafeLogProject.cafeLog.common.auth.exception.UserNotAuthenticatedException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.draftReview.DraftReviewNotFoundException;
import cafeLogProject.cafeLog.domains.draftReview.domain.DraftReview;
import cafeLogProject.cafeLog.domains.draftReview.repository.DraftReviewRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DraftReviewImageService {
    private final DraftReviewRepository draftReviewRepository;

    @Transactional
    public RegistDraftReviewImageResponse addDraftReviewImage(String username, Long draftReviewId, MultipartFile multipartFile) {
        DraftReview draftReview = validateIdentity(username, draftReviewId);
        UUID newImageId = UUID.randomUUID();
        ImageUtil.saveImage(ImageUtil.DRAFT_REVIEW_IMAGE_PATH, newImageId.toString(), multipartFile);
        draftReview.addImage(newImageId);
        return new RegistDraftReviewImageResponse(newImageId.toString());
    }

    public Resource loadDraftReviewImage(String username, Long draftReviewId, String imageId) {
        validateIdentity(username, draftReviewId);
        Resource imageFile = ImageUtil.loadCompressedImage(ImageUtil.DRAFT_REVIEW_IMAGE_PATH, imageId);
        return imageFile;
    }

    @Transactional
    public void deleteDraftReviewImage(String username, Long draftReviewId, String imageId) {
        DraftReview draftReview = validateIdentity(username, draftReviewId);
        ImageUtil.deleteCompressedImage(ImageUtil.DRAFT_REVIEW_IMAGE_PATH, imageId);
        draftReview.deleteImage(imageId);
    }

    // 본인의 리뷰인지 검사 & DraftReview 리턴
    private DraftReview validateIdentity(String username, Long draftReviewId){
        DraftReview draftReview = draftReviewRepository.findById(draftReviewId).orElseThrow(() -> {
            throw new DraftReviewNotFoundException(draftReviewId.toString(), ErrorCode.DRAFT_REVIEW_NOT_FOUND_ERROR);
        });
        // 해당 리뷰가 본인의 리뷰가 맞는지 확인
        if (!draftReview.getUser().getUsername().equals(username)) {
            throw new UserNotAuthenticatedException(ErrorCode.USER_NOT_AUTH_ERROR);
        }
        return draftReview;
    }
}
