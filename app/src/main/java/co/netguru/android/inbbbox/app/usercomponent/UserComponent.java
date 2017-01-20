package co.netguru.android.inbbbox.app.usercomponent;

import co.netguru.android.inbbbox.feature.bucket.BucketsFragmentComponent;
import co.netguru.android.inbbbox.feature.bucket.createbucket.CreateBucketComponent;
import co.netguru.android.inbbbox.feature.bucket.detail.BucketsDetailsComponent;
import co.netguru.android.inbbbox.feature.follower.FollowersFragmentComponent;
import co.netguru.android.inbbbox.feature.follower.detail.FollowerDetailsFragmentComponent;
import co.netguru.android.inbbbox.feature.like.LikesFragmentComponent;
import co.netguru.android.inbbbox.feature.shot.ShotsComponent;
import co.netguru.android.inbbbox.feature.shot.addtobucket.AddToBucketComponent;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsComponent;
import co.netguru.android.inbbbox.feature.shot.detail.ShotsDetailsModule;
import co.netguru.android.inbbbox.feature.shot.detail.fullscreen.ShotFullscreenComponent;
import co.netguru.android.inbbbox.feature.shot.detail.fullscreen.ShotFullscreenModule;
import co.netguru.android.inbbbox.feature.shot.removefrombucket.RemoveFromBucketComponent;
import co.netguru.android.inbbbox.feature.team.TeamDetailsFragmentComponent;

import dagger.Subcomponent;

@UserScope
@Subcomponent(modules = {UserModule.class})
public interface UserComponent {

    ShotsComponent getShotsComponent();

    LikesFragmentComponent getLikesFragmentComponent();

    ShotDetailsComponent plus(ShotsDetailsModule module);

    ShotFullscreenComponent plus(ShotFullscreenModule module);

    FollowersFragmentComponent plusFollowersFragmentComponent();

    FollowerDetailsFragmentComponent plusFollowersDetailsFragmentComponent();

    BucketsFragmentComponent plusBucketsFragmentComponent();

    BucketsDetailsComponent plusBucketDetailsComponent();

    AddToBucketComponent plusAddToBucketComponent();

    RemoveFromBucketComponent plusRemoveFromBucketComponent();

    CreateBucketComponent plusCreateBucketComponent();

    TeamDetailsFragmentComponent plusTeamDetailsFragmentComponent();

    @Subcomponent.Builder
    interface Builder {
        UserComponent.Builder userModule(UserModule userModule);

        UserComponent build();
    }
}
