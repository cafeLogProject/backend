package cafeLogProject.cafeLog.domains.review.domain;

import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.common.domain.BaseEntity;
import cafeLogProject.cafeLog.domains.image.domain.ReviewImage;
import cafeLogProject.cafeLog.domains.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "review_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String content;

    private int rating;

    private LocalDate visitDate;

//    @ElementCollection
//    @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
//    @Column(name = "image_url")
    @OneToMany(mappedBy = "review")
    private List<ReviewImage> images = new ArrayList<>();

    @ElementCollection(targetClass = Tag.class)
    @CollectionTable(name = "review_tags", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "tag")
    private List<Integer> tagIds = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void addImageIfNotIncluded(ReviewImage image) {
        if (images.contains(image)) return;
        images.add(image);
    }

    public void removeImage(ReviewImage image) {
        if (images.contains(image)) {
            images.remove(image);
        }
    }

    public List<String> getImageIds(){
        List<String> imageIds = new ArrayList<>();
        for (ReviewImage image : images) {
            imageIds.add(image.getId().toString());
        }
        return imageIds;
    }

    @Builder
    public Review(Long id, String content, int rating, LocalDate visitDate, List<ReviewImage> images, List<Integer> tagIds, Cafe cafe, User user){
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.images = images;
        this.tagIds = tagIds;
        this.cafe = cafe;
        this.user = user;
    }



}
