package cafeLogProject.cafeLog.test.service;

import cafeLogProject.cafeLog.common.exception.cafe.CafeNotFoundException;
import cafeLogProject.cafeLog.common.exception.user.UserNotFoundException;
import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.domains.cafe.repository.CafeRepository;
import cafeLogProject.cafeLog.domains.user.domain.User;
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

        User user = userRepository.findByUsername(userName).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_ERROR));
        Cafe cafe = cafeRepository.findById(reviewSaveReq.getCafeId()).orElseThrow(() -> new CafeNotFoundException(CAFE_NOT_FOUND_ERROR));

        ReviewEntity reviewEntity = ReviewEntity.builder()
                .user(user)
                .cafe(cafe)
                .content(reviewSaveReq.getContent())
                .rating(reviewSaveReq.getRating())
                .visitDate(reviewSaveReq.getVisitDate())
                .build();

        ReviewEntity savedReviewEntity = reviewEntityRepository.save(reviewEntity);

        List<TagEntity> tagEntityList = new ArrayList<>();

        for (Integer tagId : reviewSaveReq.getTagIds()) {

            tagEntityList.add(TagEntity.builder()
                    .tagId(tagId)
                    .reviewEntity(savedReviewEntity)
                    .build());
        }

        tagEntityRepository.saveAll(tagEntityList);

        ReviewSaveRes reviewSaveRes = new ReviewSaveRes();
        reviewSaveRes.setUserId(user.getId());
        reviewSaveRes.setCafeId(cafe.getId());
        reviewSaveRes.setReviewId(savedReviewEntity.getId());
        reviewSaveRes.setContent(savedReviewEntity.getContent());
        reviewSaveRes.setRating(savedReviewEntity.getRating());
        reviewSaveRes.setVisitDate(savedReviewEntity.getVisitDate());
        reviewSaveRes.setTagIds(reviewSaveReq.getTagIds());

        return reviewSaveRes;
    }

    public List<ReviewFindRes> findAllReview() {

        return reviewEntityRepository.findAllReview();
    }

    public List<ReviewFindRes> findAllByTagIds(List<Integer> tags) {

        return reviewEntityRepository.findAllByTagIds(tags);
    }
}
