package cafeLogProject.cafeLog.domains.review.domain;

import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.UnexpectedServerException;
import lombok.Getter;

import static cafeLogProject.cafeLog.common.exception.ErrorCode.UNEXPECTED_ERROR;

@Getter
public enum Tag {

    MENU_SELL_BEAN(1,"원두를 판매해요"),
    MENU_SELL_DESSERT(2,"디저트를 판매해요"),
    MENU_GOOD_COFFEE(3,"커피가 맛있어요"),
    MENU_HAND_DRIP(4,"핸드드립 커피가 있어요"),
    MENU_SELF_ROASTING(5,"매장에서 직접 로스팅해요"),

    ETC_GOOD_WORK(101,"작업하기 좋아요"),
    ETC_GOOD_STUDY(102,"공부하기 좋아요"),
    ETC_GOOD_MOOD(103,"분위기가 좋아요"),
    ETC_GOOD_DATING(104,"데이트하기 좋아요"),
    ETC_GOOD_TALKING(105,"대화하기 좋아요"),
    ETC_WITH_PAT(106,"반려견과 가기 좋아요"),
    ETC_WITH_CHILD(107,"아이와 가기 좋아요"),
    ETC_HAVE_ROOM(108,"방이 있어요"),
    ETC_HAVE_OUTSIDE(109,"야외석이 있어요"),
    ETC_COMFORTABLE_SEAT(110,"좌석이 편해요"),
    ETC_CLEAN_TOILET(111,"화장실이 깨끗해요"),
    ETC_SPACIOUS_STORE(112,"매장이 넓어요"),
    ETC_STAFF_KIND(113,"직원이 친절해요");

    private final String description;
    private final int num;

    Tag(int num, String description) {
        this.num = num;
        this.description = description;
    }

    public static Tag findByNum(int num) {
        for (Tag tag : Tag.values()) {
            if (tag.getNum() == num) {
                return tag;
            }
        }
        throw new UnexpectedServerException("해당 번호의 태그를 찾을 수 없습니다.", UNEXPECTED_ERROR);
    }

}
