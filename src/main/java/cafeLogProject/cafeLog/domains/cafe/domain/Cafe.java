package cafeLogProject.cafeLog.domains.cafe.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    private String cafeName;
    private List<Long> locationXY;
    private boolean isClosedDown = false;

    private String locationStr;
    private String etcLink;
}
