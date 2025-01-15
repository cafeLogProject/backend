package cafeLogProject.cafeLog.test.repository;

import cafeLogProject.cafeLog.test.dto.ReviewFindRes;

import java.util.List;

public interface ReviewEntityRepositoryCustom {

    List<ReviewFindRes> findAllReview();

    List<ReviewFindRes> findAllByTagIds(List<Integer> selectedTagIds);
}
