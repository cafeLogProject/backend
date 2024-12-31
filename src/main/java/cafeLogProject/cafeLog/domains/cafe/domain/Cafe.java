package cafeLogProject.cafeLog.domains.cafe.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Table(name = "cafe_db")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cafe_id")
    private Long id;

    private String cafeName;
    private List<Long> locationXY;
    private boolean isClosedDown = false;

    private String locationStr;
    private String etcLink;

    @Builder
    public Cafe(String cafeName, String locationStr, List<Long> locationXY, boolean isClosedDown, String etcLink) {
        this.cafeName = cafeName;
        this.locationStr = locationStr;
        this.locationXY = locationXY;
        this.isClosedDown = isClosedDown;
        this.etcLink = etcLink;
    }
}
