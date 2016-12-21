package co.netguru.android.inbbbox.data;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.model.api.Image;
import co.netguru.android.inbbbox.model.api.ShotEntity;

public class MockData {

    public static int ITEM_COUNT = 15;

    private static List<ShotEntity> getFollowingMock(int count, String label) {
        List<ShotEntity> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Image image = Image.builder().build();
            result.add(
                    ShotEntity.builder()
                            .id(i)
                            .title(label + i)
                            .image(image)
                            .createdAt(LocalDateTime.now())
                            .animated(false)
                            .animated(false)
                            .likesCount(2)
                            .bucketsCount(3)
                            .createdAt(LocalDateTime.now())
                            .commentsCount(2)
                            .build());
        }
        return result;
    }

    public static List<ShotEntity> getFollowingMockedData() {
        return getFollowingMock(ITEM_COUNT, "following");
    }

    public static List<ShotEntity> getFilteredMockedData() {
        return getFollowingMock(ITEM_COUNT, "filtered");
    }

}
