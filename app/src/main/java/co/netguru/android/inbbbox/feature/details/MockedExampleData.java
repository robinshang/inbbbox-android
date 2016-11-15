package co.netguru.android.inbbbox.feature.details;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.ShotDetails;

class MockedExampleData {

    // TODO: 15.11.2016 this class will be removed after api integration in task IA-146
    private MockedExampleData() {
        throw new AssertionError();
    }

    public static ShotDetails getMocketShotDetailsData() {
        List<Comment> comments = new ArrayList<>();
        Comment sec = Comment.builder()
                .author("sec")
                .authorAvatarUrl("http://findicons.com/files/icons/1072/face_avatars/300/i03.png")
                .date(LocalDateTime.now())
                .text("Example comments")
                .build();

        Comment fewSec = Comment.builder()
                .author("fewSec")
                .authorAvatarUrl("http://findicons.com/files/icons/1072/face_avatars/300/i03.png")
                .date(LocalDateTime.now().plusSeconds(20))
                .text("Example comments")
                .build();

        Comment min = Comment.builder()
                .author("min")
                .authorAvatarUrl("http://findicons.com/files/icons/1072/face_avatars/300/i03.png")
                .date(LocalDateTime.now().plusSeconds(45))
                .text("Example comment so lorem ipsum here will be nice")
                .build();

        Comment min2 = Comment.builder()
                .author("min2")
                .authorAvatarUrl("http://findicons.com/files/icons/1072/face_avatars/300/i03.png")
                .date(LocalDateTime.now().plusSeconds(65))
                .text("Example comment so lorem ipsum here will be nice")
                .build();

        Comment fewMin = Comment.builder()
                .author("fewMin")
                .authorAvatarUrl("https://cdn0.iconfinder.com/data/icons/iconshock_guys/512/matthew.png")
                .date(LocalDateTime.now().plusMinutes(3))
                .text("Example comment so lorem ipsum here will be nice. Something new here will be nice")
                .build();

        Comment hour = Comment.builder()
                .author("hour")
                .authorAvatarUrl("https://cdn0.iconfinder.com/data/icons/iconshock_guys/512/matthew.png")
                .date(LocalDateTime.now().plusMinutes(60 - 15))
                .text("Example comment so lorem ipsum here will be nice. Something new here will be nice")
                .build();

        Comment hour2 = Comment.builder()
                .author("hour2")
                .authorAvatarUrl("https://cdn0.iconfinder.com/data/icons/iconshock_guys/512/matthew.png")
                .date(LocalDateTime.now().plusMinutes(60))
                .text("Example comment so lorem ipsum here will be nice. Something new here will be nice")
                .build();

        Comment hour3 = Comment.builder()
                .author("hour3")
                .authorAvatarUrl("https://cdn0.iconfinder.com/data/icons/iconshock_guys/512/matthew.png")
                .date(LocalDateTime.now().plusMinutes(60 + 15))
                .text("Example comment so lorem ipsum here will be nice. Something new here will be nice")
                .build();

        Comment fewH = Comment.builder()
                .author("fewH =")
                .authorAvatarUrl("https://cdn0.iconfinder.com/data/icons/iconshock_guys/512/matthew.png")
                .date(LocalDateTime.now().plusHours(12))
                .text("Example comment so lorem ipsum here will be nice. Something new here will be nice")
                .build();

        Comment yesterday = Comment.builder()
                .author("yesterday")
                .authorAvatarUrl("https://cdn0.iconfinder.com/data/icons/iconshock_guys/512/matthew.png")
                .date(LocalDateTime.now().plusHours(25))
                .text("Example comment so lorem ipsum here will be nice. Something new here will be nice")
                .build();

        Comment date = Comment.builder()
                .author("date")
                .authorAvatarUrl("https://cdn0.iconfinder.com/data/icons/iconshock_guys/512/matthew.png")
                .date(LocalDateTime.now().plusHours(48))
                .text("Example comment so lorem ipsum here will be nice. Something new here will be nice")
                .build();

        comments.add(sec);
        comments.add(fewSec);
        comments.add(min);
        comments.add(min2);
        comments.add(fewMin);
        comments.add(hour);
        comments.add(hour2);
        comments.add(hour3);
        comments.add(fewH);
        comments.add(yesterday);
        comments.add(date);
        ShotDetails details = ShotDetails
                .builder()
                .id(1)
                .title("Awsome Title homie")
                .comments(comments)
                .userAvatarUrl("https://d13yacurqjgara.cloudfront.net/users/653174/avatars/normal/4765adea2b386b03231d10f37d786f8e.jpg?1475482306")
                .authorUrl("https://d13yacurqjgara.cloudfront.net/users/653174/avatars/normal/4765adea2b386b03231d10f37d786f8e.jpg?1475482306")
                .authorName("demo author")
                .appName("demo app")
                .bucketCount(123)
                .likesCount(321)
                .companyName("Netguru dmeos")
                .companyProfileUrl("http://google.com")
                .date("25 dev 2016")
                .authorId(99)
                .description("\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\"")
                .build();
        return details;
    }

    public static String getExampleImageUrl() {
        return "https://d13yacurqjgara.cloudfront.net/users/653174/avatars/normal/4765adea2b386b03231d10f37d786f8e.jpg?1475482306";
    }
}
