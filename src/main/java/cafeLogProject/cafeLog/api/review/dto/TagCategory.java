package cafeLogProject.cafeLog.api.review.dto;

import cafeLogProject.cafeLog.domains.review.domain.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static cafeLogProject.cafeLog.domains.review.domain.Tag.isTagValid;


@Data
@NoArgsConstructor
public class TagCategory {
    private List<TagDto> menu = new ArrayList<>();
    private List<TagDto> interior = new ArrayList<>();

    public void isValid() {
        //유효한 태그인지 검사
        for (TagDto m : this.menu) {
            isTagValid(m);
        }
        for (TagDto i : this.interior) {
            isTagValid(i);
        }
        this.menu = menu;
        this.interior = interior;
    }

    // 리뷰 엔티티의 tagIds를 TagCategory로 변환시 사용
    public TagCategory(List<Integer> tagIds) {
        for (Integer tagId : tagIds) {
            Tag tag = Tag.findByNum(tagId);
            TagDto tagDto = new TagDto(tag.getNum(), tag.getDescription());
            if (tagId < 100) {
                if (!menu.contains(tagDto)) menu.add(tagDto);
            } else {
                if (!interior.contains(tagDto)) interior.add(tagDto);
            }
        }
    }

    public List<Integer> findAllIds() {
        List<Integer> ids = new ArrayList<>();
        if (!menu.isEmpty()) {
            menu.forEach(tag -> ids.add(tag.getId()));
        }
        if(!interior.isEmpty()) {
            interior.forEach(tag -> ids.add(tag.getId()));
        }
        return ids;
    }
}
