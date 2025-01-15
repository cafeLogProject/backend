package cafeLogProject.cafeLog.test.service;

import cafeLogProject.cafeLog.common.exception.cafe.CafeNotFoundException;
import cafeLogProject.cafeLog.common.exception.user.UserNotFoundException;
import cafeLogProject.cafeLog.domains.cafe.repository.CafeRepository;
import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import cafeLogProject.cafeLog.test.domain.ReviewEntity;
import cafeLogProject.cafeLog.test.domain.TagEntity;
import cafeLogProject.cafeLog.test.dto.ReviewFindRes;
import cafeLogProject.cafeLog.test.dto.ReviewSaveReq;
import cafeLogProject.cafeLog.test.dto.ReviewSaveRes;
import cafeLogProject.cafeLog.test.repository.ReviewEntityRepository;
import cafeLogProject.cafeLog.test.repository.TagEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static cafeLogProject.cafeLog.common.exception.ErrorCode.CAFE_NOT_FOUND_ERROR;
import static cafeLogProject.cafeLog.common.exception.ErrorCode.USER_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReviewEntityService {

    private final ReviewEntityRepository reviewEntityRepository;
    private final TagEntityRepository tagEntityRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;

    @Transactional
    public ReviewSaveRes saveReview(String userName, ReviewSaveReq reviewSaveReq) {

        ReviewEntity savedReviewEntity = saveReviewEntity(userName, reviewSaveReq);
        saveTags(reviewSaveReq, savedReviewEntity);

        return ReviewSaveRes.builder()
                .userId(savedReviewEntity.getUser().getId())
                .cafeId(savedReviewEntity.getCafe().getId())
                .reviewId(savedReviewEntity.getId())
                .content(savedReviewEntity.getContent())
                .rating(savedReviewEntity.getRating())
                .visitDate(savedReviewEntity.getVisitDate())
                .tagIds(reviewSaveReq.getTagIds())
                .build();

    }

    public List<ReviewFindRes> findAllReview() {

        return reviewEntityRepository.findAllReview();
    }

    public List<ReviewFindRes> findAllByTagIds(List<Integer> tags) {

        return reviewEntityRepository.findAllByTagIds(tags);
    }

    private ReviewEntity saveReviewEntity(String userName, ReviewSaveReq reviewSaveReq) {

        ReviewEntity reviewEntity = ReviewEntity.builder()
                .user(userRepository.findByUsername(userName).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR)))
                .cafe(cafeRepository.findById(reviewSaveReq.getCafeId()).orElseThrow(() -> new CafeNotFoundException(CAFE_NOT_FOUND_ERROR)))
                .content(reviewSaveReq.getContent())
                .rating(reviewSaveReq.getRating())
                .visitDate(reviewSaveReq.getVisitDate())
                .build();

        return reviewEntityRepository.save(reviewEntity);
    }

    private void saveTags(ReviewSaveReq reviewSaveReq, ReviewEntity savedReviewEntity) {

        List<TagEntity> tagEntityList = new ArrayList<>();

        for (Integer tagId : reviewSaveReq.getTagIds()) {

            tagEntityList.add(TagEntity.builder()
                    .tagId(tagId)
                    .reviewEntity(savedReviewEntity)
                    .build());
        }

        tagEntityRepository.saveAll(tagEntityList);
    }
}
