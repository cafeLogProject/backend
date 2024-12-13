package cafeLogProject.cafeLog.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "content_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "content_id")
    private Long id;

    private String content;

    @Enumerated(EnumType.STRING)
    private Rating coffeeTaste;

    @Enumerated(EnumType.STRING)
    private Rating mood;

    @Enumerated(EnumType.STRING)
    private Rating price;

    @ManyToOne
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
