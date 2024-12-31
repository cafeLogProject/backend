package cafeLogProject.cafeLog.api.review.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class TagCategory {
    private List<TagDto> menu;
    private List<TagDto> interior;

    @Builder
    public TagCategory (List<TagDto> menu, List<TagDto> interior) {
        this.menu = menu;
        this.interior = interior;
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
