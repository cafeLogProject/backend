package cafeLogProject.cafeLog.domains.user.util;

import cafeLogProject.cafeLog.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class NicknameGenerator {
    private final UserRepository userRepository;

    // 형용사 70개
    private static final String[] adjectives = {
            "진한", "깊은", "부드러운", "깔끔한", "따뜻한", "고급진", "풍부한", "스모키한", "강렬한", "신선한",
            "산뜻한", "독특한", "정갈한", "묵직한", "향기로운", "매혹적인", "차가운", "완벽한", "클래식한", "세련된",
            "화사한", "차분한", "강한", "달콤한", "은은한", "개성있는", "진득한", "고소한", "흔치않은", "조화로운",
            "독보적인", "우아한", "농밀한", "신비로운", "생생한", "미묘한", "쌉쌀한", "청량한", "자극적인", "고풍스러운",
            "화끈한", "균형잡힌", "상쾌한", "감미로운", "기분좋은", "고요한", "섬세한", "깨끗한", "짙은", "순수한",
            "톡쏘는", "감칠맛나는", "상큼한", "농후한", "시원한", "풍미깊은", "촉촉한", "달큰한", "여운있는", "싱그러운",
            "씁쓸한", "매끄러운", "따사로운", "말끔한", "풍성한", "크리미한", "녹진한", "따스한", "발랄한", "달달한",
    };

    // 카페 음료 80개
    private static final String[] menues = {
            "에스프레소", "도피오", "아메리카노", "마키아토", "바닐라라떼", "플랫화이트", "카푸치노", "코르타도", "리스트레토", "롱블랙",
            "모카", "아포가토", "콜드브루", "아인슈패너", "비엔나", "루왁", "프라푸치노", "연유라떼", "초코쉐이크", "쿠키쉐이크",
            "핫초코", "아이스초코", "타로밀크티", "로얄밀크티", "녹차라떼", "말차라떼", "딸기스무디", "망고스무디", "유자차", "레몬차",
            "대추차", "카모마일", "페퍼민트", "블랙티", "복숭아아이스티", "자몽에이드", "블루레몬에이드", "오렌지주스", "키위주스", "토마토주스",
            "얼그레이밀크티", "홍차밀크티", "제스민밀크티", "초코밀크티", "흑당밀크티", "말차밀크티", "코코넛밀크티", "딸기밀크티", "호지차밀크티", "레몬에이드",
            "라임에이드", "딸기에이드", "라즈베리에이드", "블루베리에이드", "크랜베리에이드", "복숭아에이드", "망고에이드", "파인애플에이드", "패션후르츠에이드", "코코넛에이드",
            "키위에이드", "청포도에이드", "유자에이드", "자두에이드", "민트초코라떼", "카페라떼", "카라멜라떼", "모카라떼", "얼그레이라떼", "흑당라떼",
            "딸기라떼", "블루베리라떼", "타로라떼", "피스타치오라떼", "쿠키앤크림라떼", "아몬드라떼", "시나몬티", "바나나스무디", "초코스무디", "민트초코스무디",
    };

    // 랜덤 닉네임 생성 함수
    // 중복 미허용, 5천만 닉네임 생성 가능
    public String generateNickname() {
        Random random;
        String newNickname;
        do {
            random = new Random();
            String adjective = adjectives[random.nextInt(adjectives.length)];
            String menu = menues[random.nextInt(menues.length)];
            int number = random.nextInt(1000, 9999);
            newNickname = adjective + "_" + menu + "_" + number;
        } while (userRepository.existsByNickname(newNickname));
        return newNickname;
    }

}
