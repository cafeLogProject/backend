package cafeLogProject.cafeLog.domains.review.domain;

import cafeLogProject.cafeLog.common.exception.ErrorCode;
import cafeLogProject.cafeLog.common.exception.review.TagNotFoundException;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Slf4j
@Getter
public class Tags {
    Boolean MENU_SELL_BEAN = false;
    Boolean MENU_SELL_DESSERT = false;
    Boolean MENU_GOOD_COFFEE = false;
    Boolean MENU_HAND_DRIP = false;
    Boolean MENU_SELF_ROASTING = false;

    Boolean ETC_GOOD_WORK = false;
    Boolean ETC_GOOD_STUDY = false;
    Boolean ETC_GOOD_MOOD = false;
    Boolean ETC_GOOD_DATING = false;
    Boolean ETC_GOOD_TALKING = false;
    Boolean ETC_WITH_PAT = false;
    Boolean ETC_WITH_CHILD = false;
    Boolean ETC_HAVE_ROOM = false;
    Boolean ETC_HAVE_OUTSIDE = false;
    Boolean ETC_COMFORTABLE_SEAT = false;
    Boolean ETC_CLEAN_TOILET = false;
    Boolean ETC_SPACIOUS_STORE = false;
    Boolean ETC_STAFF_KIND = false;

    public Tags() {
        MENU_SELL_BEAN = false;
        MENU_SELL_DESSERT = false;
        MENU_GOOD_COFFEE = false;
        MENU_HAND_DRIP = false;
        MENU_SELF_ROASTING = false;

        ETC_GOOD_WORK = false;
        ETC_GOOD_STUDY = false;
        ETC_GOOD_MOOD = false;
        ETC_GOOD_DATING = false;
        ETC_GOOD_TALKING = false;
        ETC_WITH_PAT = false;
        ETC_WITH_CHILD = false;
        ETC_HAVE_ROOM = false;
        ETC_HAVE_OUTSIDE= false;
        ETC_COMFORTABLE_SEAT = false;
        ETC_CLEAN_TOILET = false;
        ETC_SPACIOUS_STORE = false;
        ETC_STAFF_KIND = false;
    }

    public List<Integer> getAllTagIds() {
        List<Integer> tagIds = new ArrayList<>();
        if (MENU_SELL_BEAN) tagIds.add(1);
        if (MENU_SELL_DESSERT) tagIds.add(2);
        if (MENU_GOOD_COFFEE) tagIds.add(3);
        if (MENU_HAND_DRIP) tagIds.add(4);
        if (MENU_SELF_ROASTING) tagIds.add(5);
        if (ETC_GOOD_WORK) tagIds.add(101);
        if (ETC_GOOD_STUDY) tagIds.add(102);
        if (ETC_GOOD_MOOD) tagIds.add(103);
        if (ETC_GOOD_DATING) tagIds.add(104);
        if (ETC_GOOD_TALKING) tagIds.add(105);
        if (ETC_WITH_PAT) tagIds.add(106);
        if (ETC_WITH_CHILD) tagIds.add(107);
        if (ETC_HAVE_ROOM) tagIds.add(108);
        if (ETC_HAVE_OUTSIDE) tagIds.add(109);
        if (ETC_COMFORTABLE_SEAT) tagIds.add(110);
        if (ETC_CLEAN_TOILET) tagIds.add(111);
        if (ETC_SPACIOUS_STORE) tagIds.add(112);
        if (ETC_STAFF_KIND) tagIds.add(113);

        return tagIds;
    }

    public boolean isTagTrue (Integer tagId) {
        switch (tagId) {
            case 1:
                return MENU_SELL_BEAN;
            case 2:
                return MENU_SELL_DESSERT;
            case 3:
                return MENU_GOOD_COFFEE;
            case 4:
                return MENU_HAND_DRIP;
            case 5:
                return MENU_SELF_ROASTING;
            case 101:
                return ETC_GOOD_WORK;
            case 102:
                return ETC_GOOD_STUDY;
            case 103:
                return ETC_GOOD_MOOD;
            case 104:
                return ETC_GOOD_DATING;
            case 105:
                return ETC_GOOD_TALKING;
            case 106:
                return ETC_WITH_PAT;
            case 107:
                return ETC_WITH_CHILD;
            case 108:
                return ETC_HAVE_ROOM;
            case 109:
                return ETC_HAVE_OUTSIDE;
            case 110:
                return ETC_COMFORTABLE_SEAT;
            case 111:
                return ETC_CLEAN_TOILET;
            case 112:
                return ETC_SPACIOUS_STORE;
            case 113:
                return ETC_STAFF_KIND;
            default:
                throw new TagNotFoundException(ErrorCode.TAG_NOT_FOUND_ERROR);
        }

    }

    public Tags (List<Integer> tagIds) {
        for (Integer tagId : tagIds) {
            switch (tagId) {
                case 1:
                    MENU_SELL_BEAN = true;
                    break;
                case 2:
                    MENU_SELL_DESSERT = true;
                    break;
                case 3:
                    MENU_GOOD_COFFEE = true;
                    break;
                case 4:
                    MENU_HAND_DRIP = true;
                    break;
                case 5:
                    MENU_SELF_ROASTING = true;
                    break;
                case 101:
                    ETC_GOOD_WORK = true;
                    break;
                case 102:
                    ETC_GOOD_STUDY = true;
                    break;
                case 103:
                    ETC_GOOD_MOOD = true;
                    break;
                case 104:
                    ETC_GOOD_DATING = true;
                    break;
                case 105:
                    ETC_GOOD_TALKING = true;
                    break;
                case 106:
                    ETC_WITH_PAT = true;
                    break;
                case 107:
                    ETC_WITH_CHILD = true;
                    break;
                case 108:
                    ETC_HAVE_ROOM = true;
                    break;
                case 109:
                    ETC_HAVE_OUTSIDE = true;
                    break;
                case 110:
                    ETC_COMFORTABLE_SEAT = true;
                    break;
                case 111:
                    ETC_CLEAN_TOILET = true;
                    break;
                case 112:
                    ETC_SPACIOUS_STORE = true;
                    break;
                case 113:
                    ETC_STAFF_KIND = true;
                    break;
                default:
                    throw new TagNotFoundException(ErrorCode.TAG_NOT_FOUND_ERROR);
            }
        }
    }
}
