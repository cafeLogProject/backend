package cafeLogProject.cafeLog.api.draftReview.service;

import cafeLogProject.cafeLog.api.draftReview.dto.RegistDraftReviewRequest;
import cafeLogProject.cafeLog.api.draftReview.dto.ShowDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.dto.ShowUserDraftReviewResponse;
import cafeLogProject.cafeLog.api.draftReview.dto.UpdateDraftReviewRequest;
import cafeLogProject.cafeLog.api.image.service.ImageUtil;
import cafeLogProject.cafeLog.api.review.dto.ShowReviewResponse;
import cafeLogProject.cafeLog.api.review.dto.UpdateReviewRequest;
import cafeLogProject.cafeLog.common.auth.exception.UserNotAuthenticatedException;
import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.cafe.CafeNotFoundException;
import cafeLogProject.cafeLog.common.exception.draftReview.DraftReviewNotFoundException;
import cafeLogProject.cafeLog.common.exception.review.ReviewNotFoundException;
import cafeLogProject.cafeLog.common.exception.user.UserNotFoundException;
import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.cafe.repository.CafeRepository;
import cafeLogProject.cafeLog.domains.draftReview.domain.DraftReview;
import cafeLogProject.cafeLog.domains.draftReview.repository.DraftReviewRepository;
import cafeLogProject.cafeLog.domains.image.domain.ReviewImage;
import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.review.domain.Tag;
import cafeLogProject.cafeLog.domains.user.domain.User;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DraftReviewService {
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final DraftReviewRepository draftReviewRepository;

    public ShowDraftReviewResponse findDraftReview(String username, Long draftReviewId){
        DraftReview draftReview = validateIdentity(username, draftReviewId);
        ShowDraftReviewResponse res = new ShowDraftReviewResponse(draftReview);
//        ShowDraftReviewResponse res = draftReviewRepository.findShowDraftReviewResponseById(draftReviewId).orElseThrow(() -> {
//            throw new DraftReviewNotFoundException(draftReviewId.toString(), ErrorCode.DRAFT_REVIEW_NOT_FOUND_ERROR);
//        });
        return res;
    }

    public List<ShowUserDraftReviewResponse> findAllReviewsByUser(String username){
        User user = userRepository.findByUsername(username).orElseThrow(() ->{
            throw new UserNotFoundException(username, ErrorCode.USER_NOT_FOUND_ERROR);
        });
        return draftReviewRepository.findAllIdsByUser(user);
    }

    public List<ShowUserDraftReviewResponse> findAllReviewsByUserAndCafe(String username, Long cafeId){
        User user = userRepository.findByUsername(username).orElseThrow(() ->{
            throw new UserNotFoundException(username, ErrorCode.USER_NOT_FOUND_ERROR);
        });
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> {
            throw new CafeNotFoundException(Long.toString(cafeId), ErrorCode.CAFE_NOT_FOUND_ERROR);
        });
        return draftReviewRepository.findAllIdsByUserAndCafe(user, cafe);
    }

    @Transactional
    public ShowDraftReviewResponse addDraftReview(String username, RegistDraftReviewRequest registDraftReviewRequest) {
        User user = userRepository.findByUsername(username).orElseThrow(() ->{
            throw new UserNotFoundException(username, ErrorCode.USER_NOT_FOUND_ERROR);
        });
        Long cafeId = registDraftReviewRequest.getCafeId();
        Cafe cafe = cafeRepository.findById(cafeId).orElseThrow(() -> {
            throw new CafeNotFoundException(Long.toString(cafeId), ErrorCode.CAFE_NOT_FOUND_ERROR);
        });

        DraftReview draftReview = draftReviewRepository.save(registDraftReviewRequest.toEntity(user, cafe));
        return new ShowDraftReviewResponse(draftReview);
    }

    @Transactional
    public ShowDraftReviewResponse updateDraftReview(String username, long draftReviewId, UpdateDraftReviewRequest updateDraftReviewRequest) {
        DraftReview oldDraftReview = validateIdentity(username, draftReviewId);
        DraftReview updatedReview = draftReviewRepository.save(updateDraftReviewRequest.toEntity(oldDraftReview));
        return new ShowDraftReviewResponse(updatedReview);
    }

    @Transactional
    public void deleteDraftReviewAndImage(String username, Long draftReviewId) {
        DraftReview draftReview = validateIdentity(username, draftReviewId);
        deleteAllImageInDraftReview(draftReview);
        draftReviewRepository.deleteById(draftReviewId);
    }


    private void deleteAllImageInDraftReview(DraftReview draftReview) {
        List<String> imageIds = draftReview.getImageIdsStr();
        for (String imageId : imageIds) {
            ImageUtil.deleteCompressedImage(ImageUtil.DRAFT_REVIEW_IMAGE_PATH, imageId);
        }
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
