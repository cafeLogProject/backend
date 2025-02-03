package cafeLogProject.cafeLog.domains.draftReview.domain;


import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.image.ImageNotFoundException;
import cafeLogProject.cafeLog.common.exception.review.TagInvalidException;
import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.common.domain.BaseEntity;
import cafeLogProject.cafeLog.domains.review.domain.Tag;
import cafeLogProject.cafeLog.domains.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "draft_review_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DraftReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "draft_review_id")
    private Long id;

    private String content;

    private Integer rating;                     // null 가능하게 하기 위해 Integer로 설정

    private LocalDate visitDate;

    @ElementCollection
    @CollectionTable(name = "draft_review_tags", joinColumns = @JoinColumn(name = "draft_review_id"))
    private List<Integer> tagIds = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "draft_review_images", joinColumns = @JoinColumn(name = "draft_review_id"))
    private List<UUID> imageIds = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public DraftReview(Long id, String content, Integer rating, LocalDate visitDate, List<UUID> imageIds, List<Integer> tagIds, Cafe cafe, User user){
        this.id = id;
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.imageIds = (imageIds == null ? new ArrayList<>() : imageIds);
        if (tagIds == null){
            this.tagIds = new ArrayList<>();
        } else {
            tagIds.forEach(tagId -> isValidTagId(tagId));
            this.tagIds = tagIds;
        }
        this.cafe = cafe;
        this.user = user;
    }

    public void addImage(UUID imageId){
        if (this.imageIds.contains(imageId)) return;
        this.imageIds.add(imageId);
    }

    public void deleteImage(String imageId) {
        UUID imageUuid;
        try {
            imageUuid = UUID.fromString(imageId);
        } catch (Exception e) {
            // UUID 형식이 아닌 경우
            throw new ImageNotFoundException(ErrorCode.IMAGE_NOT_FOUND_ERROR);
        }
        
        if (this.imageIds.contains(imageUuid)) {
            this.imageIds.remove(imageUuid);
        }
    }

    public List<String> getImageIdsStr() {
        List<String> imageIdsStr = new ArrayList<>();
        for (UUID imageId : imageIds) {
            imageIdsStr.add(imageId.toString());
        }
        return imageIdsStr;
    }

    private static final List<Integer> VALID_TAG_IDS = Arrays.asList(
            1, 2, 3, 4, 5, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113
    );

    public void isValidTagId(Integer tagId) {
        if (!VALID_TAG_IDS.contains(tagId)) {
            throw new TagInvalidException(ErrorCode.TAG_INVALID_ERROR);
        }
    }

}

