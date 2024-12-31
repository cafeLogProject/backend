package cafeLogProject.cafeLog.domains.review.domain;

import cafeLogProject.cafeLog.domains.cafe.domain.Cafe;
import cafeLogProject.cafeLog.common.domain.BaseEntity;
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

    @ElementCollection
    @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "image_url")
    private List<String> images = new ArrayList<>();

    @ElementCollection(targetClass = Tag.class)
    @CollectionTable(name = "review_tags", joinColumns = @JoinColumn(name = "review_id"))
    @Column(name = "tag")
    private List<Integer> tags = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void addImageId(String id) {
        if (images.contains(id)) return;
        images.add(id);
    }

    @Builder
    public Review(String content, int rating, LocalDate visitDate, List<String> images, List<Integer> tags, Cafe cafe, User user){
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.images = images;
        this.tags = tags;
        this.cafe = cafe;
        this.user = user;
    }

}
