package cafeLogProject.cafeLog.domains.review.dto;

import cafeLogProject.cafeLog.domains.review.domain.Tag;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TagCategory {
    private List<TagDto> menu = new ArrayList<>();
    private List<TagDto> interior = new ArrayList<>();

    @Builder
    public TagCategory (List<TagDto> menu, List<TagDto> interior) {
        this.menu = menu;
        this.interior = interior;
    }

    // 리뷰 엔티티의 tagIds를 TagCategory로 변환시 사용
    public TagCategory(List<Integer> tagIds) {
        for (Integer tagId : tagIds) {
            Tag tag = Tag.findByNum(tagId);
            TagDto tagDto = new TagDto(tag.getNum(), tag.getDescription());
            if (tagId < 100) {
                menu.add(tagDto);
            } else {
                interior.add(tagDto);
            }
        }
    }

    public List<Integer> getAllIds() {
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
