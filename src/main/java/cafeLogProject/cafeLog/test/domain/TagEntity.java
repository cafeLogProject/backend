package cafeLogProject.cafeLog.test.domain;

import cafeLogProject.cafeLog.test.exception.TagInvalidException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

import static cafeLogProject.cafeLog.common.exception.ErrorCode.TAG_INVALID_ERROR;

@Entity
@Getter
@Table(name = "tag_entity_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_entity_id")
    private Long id;

    @Column(name = "tag_id")
    private Integer tagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_entity_id", nullable = false)
    private ReviewEntity reviewEntity;

    @Builder
    public TagEntity(Integer tagId, ReviewEntity reviewEntity) {

        this.tagId = setTagId(tagId);
        this.reviewEntity = reviewEntity;
    }

    private static final List<Integer> VALID_TAG_IDS = Arrays.asList(
            1, 2, 3, 4, 5, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113
    );

    public Integer setTagId(Integer tagId) {
        if (!VALID_TAG_IDS.contains(tagId)) {
            throw new TagInvalidException(TAG_INVALID_ERROR);
        }

        return tagId;
    }
}
