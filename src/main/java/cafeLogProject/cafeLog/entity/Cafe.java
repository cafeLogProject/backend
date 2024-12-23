package cafeLogProject.cafeLog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "cafe_db")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_id")
    private Long id;

    private String cafename;

    private String location;

    private float avgStar = 0;

    private boolean isClosedDown = false;


}
