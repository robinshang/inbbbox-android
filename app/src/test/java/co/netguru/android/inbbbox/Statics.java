package co.netguru.android.inbbbox;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import co.netguru.android.inbbbox.model.api.Bucket;
import co.netguru.android.inbbbox.model.api.CommentEntity;
import co.netguru.android.inbbbox.model.api.Links;
import co.netguru.android.inbbbox.model.api.UserEntity;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.Shot;
import co.netguru.android.inbbbox.model.ui.Team;
import co.netguru.android.inbbbox.model.ui.User;

public final class Statics {

    public static final UserEntity USER_ENTITY = UserEntity.builder()
            .avatarUrl("")
            .bio("")
            .bucketsCount(1)
            .likesCount(2)
            .updatedAt(LocalDateTime.now().minusDays(2))
            .createdAt(LocalDateTime.now().minusDays(3))
            .bucketsUrl("")
            .bio("")
            .canUploadShot(false)
            .commentsReceivedCount(1)
            .createdAt(LocalDateTime.now())
            .followersCount(1)
            .followersUrl("")
            .followingsCount(1)
            .followingUrl("")
            .htmlUrl("")
            .id(1)
            .likesCount(1)
            .likesReceivedCount(1)
            .likesUrl("")
            .links(new Links())
            .location("")
            .name("")
            .pro(true)
            .projectsCount(1)
            .reboundsReceivedCount(1)
            .shotsCount(1)
            .shotsUrl("")
            .teamsCount(1)
            .teamsUrl("")
            .type("")
            .username("")
            .build();

    public static final Bucket BUCKET = Bucket.builder().createdAt(LocalDateTime.now()).description("").id(1).name("some bucket").shotsCount(2).build();

    public static final Team TEAM = Team.builder()
            .id(1)
            .name("team")
            .build();

    public static final Shot NOT_LIKED_SHOT = Shot.builder()
            .id(1)
            .author(User.create(USER_ENTITY))
            .title("title")
            .description("description")
            .team(TEAM)
            .bucketCount(123)
            .likesCount(321)
            .creationDate(LocalDateTime.now().minusDays(2))
            .isGif(false)
            .isLiked(false)
            .isBucketed(false)
            .hdpiImageUrl("")
            .normalImageUrl("")
            .thumbnailUrl("")
            .build();

    public static final Shot LIKED_SHOT = Shot.builder()
            .id(1)
            .author(User.create(USER_ENTITY))
            .title("title")
            .description("description")
            .team(TEAM)
            .bucketCount(123)
            .likesCount(321)
            .creationDate(LocalDateTime.now().minusDays(2))
            .isGif(false)
            .isLiked(true)
            .isBucketed(false)
            .hdpiImageUrl("")
            .normalImageUrl("")
            .thumbnailUrl("")
            .build();

    public static final List<Comment> COMMENTS = generateComments();
    private static final int COMMENTS_COUNT = 15;

    public static List<Comment> generateComments() {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < COMMENTS_COUNT; i++) {
            Comment comment = Comment.builder()
                    .id(i)
                    .author("Author: " + i)
                    .authorAvatarUrl("")
                    .date(LocalDateTime.now())
                    .text("text: " + i)
                    .build();
            comments.add(comment);
        }
        return comments;
    }

    public static List<CommentEntity> generateCommentsEntity() {
        List<CommentEntity> comments = new ArrayList<>();
        for (int i = 0; i < COMMENTS_COUNT; i++) {
            CommentEntity comment = new CommentEntity();
            comments.add(comment);
        }
        return comments;
    }
}
