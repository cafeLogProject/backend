package cafeLogProject.cafeLog.service.Impl;

import cafeLogProject.cafeLog.dto.RegistReviewRequest;
import cafeLogProject.cafeLog.entity.Cafe;
import cafeLogProject.cafeLog.entity.Review;
import cafeLogProject.cafeLog.entity.User;
import cafeLogProject.cafeLog.repository.CafeRepository;
import cafeLogProject.cafeLog.repository.ReviewRepository;
import cafeLogProject.cafeLog.repository.UserRepository;
import cafeLogProject.cafeLog.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final ReviewRepository reviewRepository;
    @Override
    public Review addReview(long userId, RegistReviewRequest registReviewRequest) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) return null;
        User user = userOptional.get();

        Optional<Cafe> cafeOptional = cafeRepository.findById(registReviewRequest.getCafeId());
        if (cafeOptional.isEmpty()) return null;
        Cafe cafe = cafeOptional.get();

        return reviewRepository.save(registReviewRequest.toEntity(user, cafe));
    }

    @Override
    public Review findReviewById(long reviewId) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewId);
        if (reviewOptional.isEmpty()) return null;
        return reviewOptional.get();
    }
}
