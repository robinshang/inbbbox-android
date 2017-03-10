package co.netguru.android.inbbbox;

import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import co.netguru.android.inbbbox.data.bucket.model.api.Bucket;
import co.netguru.android.inbbbox.data.db.BucketDB;
import co.netguru.android.inbbbox.data.db.ShotDB;
import co.netguru.android.inbbbox.data.dribbbleuser.Links;
import co.netguru.android.inbbbox.data.dribbbleuser.team.Team;
import co.netguru.android.inbbbox.data.dribbbleuser.user.User;
import co.netguru.android.inbbbox.data.dribbbleuser.user.model.api.UserEntity;
import co.netguru.android.inbbbox.data.follower.model.api.FollowerEntity;
import co.netguru.android.inbbbox.data.shot.model.api.CommentEntity;
import co.netguru.android.inbbbox.data.shot.model.api.Image;
import co.netguru.android.inbbbox.data.shot.model.api.ShotEntity;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.model.api.ProjectEntity;
import co.netguru.android.inbbbox.feature.shot.detail.Comment;

public final class Statics {

    public static final int ITEM_COUNT = 15;
    private static final int COMMENTS_COUNT = 15;
    private static final int SHOTS_COUNT = 10;

    private static List<ShotEntity> getFollowingMock(int count, String label) {
        List<ShotEntity> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Image image = Image.builder().build();
            result.add(
                    ShotEntity.builder()
                            .id(i)
                            .title(label + i)
                            .image(image)
                            .createdAt(ZonedDateTime.now())
                            .animated(false)
                            .animated(false)
                            .likesCount(2)
                            .bucketsCount(3)
                            .createdAt(ZonedDateTime.now())
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

    public static final ProjectEntity PROJECT_ENTITY = ProjectEntity.builder()
            .id(1)
            .name("")
            .description("")
            .shotsCount(SHOTS_COUNT)
            .createdAt(ZonedDateTime.now())
            .updatedAt(ZonedDateTime.now())
            .build();

    public static final UserEntity USER_ENTITY = UserEntity.builder()
            .avatarUrl("")
            .bio("")
            .bucketsCount(1)
            .projectsCount(2)
            .likesCount(2)
            .updatedAt(ZonedDateTime.now().minusDays(2))
            .createdAt(ZonedDateTime.now().minusDays(3))
            .bucketsUrl("")
            .bio("")
            .canUploadShot(false)
            .commentsReceivedCount(1)
            .createdAt(ZonedDateTime.now())
            .followersCount(1)
            .followersUrl("")
            .followingsCount(1)
            .followingUrl("")
            .htmlUrl("")
            .id(1)
            .likesCount(1)
            .likesReceivedCount(1)
            .likesUrl("")
            .links(Links.create("https://www.netguru.co/", "https://twitter.com/netguru"))
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

    public static final Bucket BUCKET = Bucket.builder()
            .createdAt(ZonedDateTime.now())
            .description("")
            .id(1)
            .name("some bucket")
            .shotsCount(2)
            .build();

    public static final Team TEAM = Team.builder()
            .id(1)
            .name("team")
            .username("team")
            .shotsCount(2)
            .projectsCount(2)
            .bucketsCount(2)
            .avatarUrl("")
            .build();

    public static final Shot NOT_LIKED_SHOT = Shot.builder()
            .id(1)
            .author(User.create(USER_ENTITY))
            .title("title")
            .description("description")
            .team(TEAM)
            .bucketCount(123)
            .likesCount(321)
            .creationDate(ZonedDateTime.now().minusDays(2))
            .isGif(false)
            .isLiked(false)
            .isBucketed(false)
            .hiDpiImageUrl("")
            .commentsCount(3)
            .normalImageUrl("")
            .thumbnailUrl("")
            .build();

    public static final Shot NOT_LIKED_SHOT_WITHOUT_AUTHOR = Shot.builder()
            .id(1)
            .title("title")
            .description("description")
            .team(TEAM)
            .bucketCount(123)
            .likesCount(321)
            .creationDate(ZonedDateTime.now().minusDays(2))
            .isGif(false)
            .isLiked(false)
            .isBucketed(false)
            .hiDpiImageUrl("")
            .commentsCount(3)
            .normalImageUrl("")
            .thumbnailUrl("")
            .build();

    public static final Shot LIKED_SHOT_NOT_BUCKETED = Shot.builder()
            .id(1)
            .author(User.create(USER_ENTITY))
            .title("title")
            .description("description")
            .team(TEAM)
            .bucketCount(1)
            .likesCount(321)
            .commentsCount(3)
            .creationDate(ZonedDateTime.now().minusDays(2))
            .isGif(false)
            .isLiked(true)
            .isBucketed(false)
            .hiDpiImageUrl("")
            .normalImageUrl("")
            .thumbnailUrl("")
            .build();

    public static final Shot NOT_LIKED_SHOT_NOT_BUCKETED = Shot.builder()
            .id(1)
            .author(User.create(USER_ENTITY))
            .title("title")
            .description("description")
            .team(TEAM)
            .bucketCount(1)
            .likesCount(0)
            .commentsCount(3)
            .creationDate(ZonedDateTime.now().minusDays(2))
            .isGif(false)
            .isLiked(false)
            .isBucketed(false)
            .hiDpiImageUrl("")
            .normalImageUrl("")
            .thumbnailUrl("")
            .build();

    public static final Shot LIKED_SHOT_BUCKETED = Shot.builder()
            .id(1)
            .author(User.create(USER_ENTITY))
            .title("title")
            .description("description")
            .team(TEAM)
            .bucketCount(123)
            .likesCount(321)
            .commentsCount(3)
            .creationDate(ZonedDateTime.now().minusDays(2))
            .isGif(false)
            .isLiked(true)
            .isBucketed(true)
            .hiDpiImageUrl("")
            .normalImageUrl("")
            .thumbnailUrl("")
            .build();


    public static final ShotDB LIKED_SHOT_DB_NOT_BUCKETED = new ShotDB(LIKED_SHOT_NOT_BUCKETED.id(), "title",
            ZonedDateTime.now().minusDays(2), "", 14, 4, 21, "description",  false,  "", "", "", false, true,
            User.create(USER_ENTITY).id(), TEAM.id());

    public static final FollowerEntity FOLLOWER_ENTITY = FollowerEntity.builder()
            .id(1)
            .createdAt(ZonedDateTime.now())
            .user(USER_ENTITY)
            .build();

    public static final User USER = User.builder()
            .id(1)
            .name("name")
            .avatarUrl("")
            .username("username")
            .shotsCount(0)
            .type("Player")
            .followersCount(1)
            .followingsCount(1)
            .projectsCount(1)
            .bucketsCount(1)
            .bio("")
            .location("")
            .build();

    public static final BucketDB BUCKET_DB = new BucketDB(1L, "test", "test", 2, ZonedDateTime.now());

    public static final List<Comment> COMMENTS = generateComments();
    public static final List<Shot> SHOT_LIST = Collections.emptyList();

    private static List<Comment> generateComments() {
        List<Comment> comments = new ArrayList<>();
        for (int i = 0; i < COMMENTS_COUNT; i++) {
            Comment comment = Comment.builder()
                    .id(i)
                    .author("Author: " + i)
                    .authorAvatarUrl("")
                    .date(ZonedDateTime.now())
                    .text("text: " + i)
                    .isCurrentUserAuthor(false)
                    .build();
            comments.add(comment);
        }
        return comments;
    }

    public static List<Shot> generateShots() {
        final List<Shot> shots = new LinkedList<>();
        for (int i = 0; i < SHOTS_COUNT; i++) {
            shots.add(LIKED_SHOT_NOT_BUCKETED);
        }
        return shots;
    }

    public static List<CommentEntity> generateCommentsEntity() {
        List<CommentEntity> comments = new ArrayList<>();
        for (int i = 0; i < COMMENTS_COUNT; i++) {
            CommentEntity comment = new CommentEntity();
            comment.setUser(USER_ENTITY);
            comment.setCreatedAt(ZonedDateTime.now());
            comment.setBody("test");
            comments.add(comment);
        }
        return comments;
    }

    public static List<ShotEntity> getShotsEntityList(int count) {
        List<ShotEntity> result = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Image image = Image.builder().build();
            result.add(
                    ShotEntity.builder()
                            .id(i)
                            .title("test: " + i)
                            .image(image)
                            .createdAt(ZonedDateTime.now())
                            .animated(false)
                            .likesCount(2)
                            .bucketsCount(3)
                            .createdAt(ZonedDateTime.now())
                            .commentsCount(2)
                            .build());
        }
        return result;
    }
}