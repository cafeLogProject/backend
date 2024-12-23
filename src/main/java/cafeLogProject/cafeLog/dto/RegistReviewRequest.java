package cafeLogProject.cafeLog.dto;


import cafeLogProject.cafeLog.entity.Cafe;
import cafeLogProject.cafeLog.entity.Review;
import cafeLogProject.cafeLog.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//		  "visitDate" : "2024.12.19",
//                  "rating" : 4,
//                  "images" : ["사진링크", "사진링크"],
//                  "cafeName" : "카페명",
//                  "cafeId" : "카페Id",
//                  "content" : "내용",
//                  "tags" : [1, 3, 101]

@Data
@NoArgsConstructor
public class RegistReviewRequest {
    private String content;
    private int rating;
    private LocalDate visitDate;
    private List<String> images = new ArrayList<>();
    private TagCategory tags;
    private Long cafeId;        //필수
    private String cafeName;

    @Builder
    public RegistReviewRequest(String content, int rating, LocalDate visitDate, List<String> images, TagCategory tags, Long cafeId, String cafeName) {
        this.content = content;
        this.rating = rating;
        this.visitDate = visitDate;
        this.images = images;
        this.tags = tags;
        this.cafeId = cafeId;
        this.cafeName = cafeName;
    }

    public Review toEntity(User user, Cafe cafe) {
        LocalDateTime createdAt = LocalDateTime.now();
        return Review.builder()
                .content(content)
                .rating(rating)
                .visitDate(visitDate)
                .images(images)
                .tags(tags.getAllIds())
                .cafe(cafe)
                .user(user)
                .createdAt(createdAt)
                .build();
    }
}




//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "review_id")
//    private Long id;
//
//    private String content;
//
//    private int rating;
//
//    private LocalDate visitDate;
//
//    @ElementCollection
//    @CollectionTable(name = "review_images", joinColumns = @JoinColumn(name = "review_id"))
//    @Column(name = "image_url")
//    private List<String> images = new ArrayList<>();
//
//    @ElementCollection(targetClass = Tag.class)
//    @CollectionTable(name = "review_tags", joinColumns = @JoinColumn(name = "review_id"))
//    @Column(name = "tag")
//    private List<Integer> tags = new ArrayList<>();
//
//    @ManyToOne
//    @JoinColumn(name = "cafe_id", nullable = false)
//    private Cafe cafe;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
