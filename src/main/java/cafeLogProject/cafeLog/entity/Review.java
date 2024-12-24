package cafeLogProject.cafeLog.entity;

import cafeLogProject.cafeLog.entity.enums.Tag;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "review_tb")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Review extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    private String content;

//    @NotBlank
    private int rating;

//    @NotBlank
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
//    @NotBlank
    private Cafe cafe;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void addImageId(String id) {
        if (images.contains(id)) return;
        images.add(id);
    }

}
