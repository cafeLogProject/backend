package cafeLogProject.cafeLog.domains.review.repository;

import cafeLogProject.cafeLog.domains.review.domain.Review;
import cafeLogProject.cafeLog.domains.review.repository.Impl.ReviewQueryDslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewQueryDslRepository {
    Review save(Review review);
    Optional<Review> findById(Long reviewId);
//    @Query(value = "SELECT r FROM Review r " +
//            "WHERE r.createdAt < :createdAt " +  // createdAt보다 과거인 리뷰
//            "AND r.rating <= :currentRating " +  // 현재 별점보다 작거나 같은 리뷰
//            "AND JSON_CONTAINS(" +
//            "  CAST(CONCAT('[' , REPLACE(r.tagIds, ',', '","'), ']') AS JSON), " +
//            "  CAST(CONCAT('[' , REPLACE(:tagIds, ',', '","'), ']') AS JSON) " +
//            ") = 1 " + // tagIds 조건 추가
//            "GROUP BY r.id " +                  // 리뷰 ID로 그룹화
//            "ORDER BY r.rating DESC, r.createdAt DESC", nativeQuery = true)
//    List<Review> findReviewsBeforeDateAndRatingAndTags(@Param("createdAt") LocalDateTime createdAt,
//                                                       @Param("currentRating") int currentRating,
//                                                       @Param("tagIds") String tagIds);
    
    // MYSQL에서만 사용 가능
    @Query(value = "SELECT r FROM Review r " +
            "WHERE r.createdAt < :createdAt " +  // createdAt보다 과거인 리뷰
            "AND r.rating <= :currentRating " +  // 현재 별점보다 작거나 같은 리뷰
            "AND JSON_CONTAINS(" +
            "  JSON_ARRAY(r.tagIds), " +        
            "  JSON_ARRAY(:tagIds) " +          
            ") = 1 " +                          // tagIds 모두 포함여부 확인
            "GROUP BY r.id " +                  // 리뷰 ID로 그룹화
            "ORDER BY r.rating DESC, r.createdAt DESC", nativeQuery = true)
    List<Review> findReviewsBeforeDateAndRatingAndTags(@Param("createdAt") LocalDateTime createdAt,
                                                       @Param("currentRating") int currentRating,
                                                       @Param("tagIds") String tagIds);

    //위의 함수가 업그레이드된 버전임
    // 특정 태그가 포함되고 특정 시간 이전인 데이터 조회, 별점 높은 순 & 최근순으로 정렬
    // 오류 존재
    List<Review> findByTagsAndDateTimeOrderByRatingAndDateTime(List<Integer> tagIds, LocalDateTime createdAt, int currentRating);
    
    // 특정 태그가 포함되고 특정 시간 이전인 데이터 조회
    // 오류 존재
    List<Review> findByTagsAndDateTimeOrderByDateTime(List<Integer> tagIds, LocalDateTime createdAt);

    // 특정 태그 포함 x, 특정 시간 이전인 데이터 조회, 별점 높은 순 & 최근순으로 정렬
    List<Review> findBeforeDateTimeOrderByRatingAndDateTime(LocalDateTime createdAt, int currentRating);

    // 특정 태그 포함 x, 특정 시간 이전의 데이터 조회
    List<Review> findBeforeDateTime(@Param("dateTime") LocalDateTime createdAt);

    // 특정 태그가 포함된 데이터 조회
    // 오류 존재
    List<Review> findByTagsContaining(@Param("tags") List<Integer> tagIds);
}
