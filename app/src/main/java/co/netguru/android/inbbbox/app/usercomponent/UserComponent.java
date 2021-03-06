package co.netguru.android.inbbbox.app.usercomponent;

import co.netguru.android.inbbbox.feature.bucket.currentuser.BucketsFragmentComponent;
import co.netguru.android.inbbbox.feature.bucket.createbucket.CreateBucketComponent;
import co.netguru.android.inbbbox.feature.bucket.detail.BucketsDetailsComponent;
import co.netguru.android.inbbbox.feature.user.buckets.UserBucketsComponent;
import co.netguru.android.inbbbox.feature.user.buckets.UserBucketsModule;
import co.netguru.android.inbbbox.feature.follower.FollowersFragmentComponent;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPopComponent;
import co.netguru.android.inbbbox.feature.shared.peekandpop.ShotPeekAndPopModule;
import co.netguru.android.inbbbox.feature.user.info.singleuser.UserInfoComponent;
import co.netguru.android.inbbbox.feature.user.info.singleuser.UserInfoModule;
import co.netguru.android.inbbbox.feature.user.info.team.TeamInfoComponent;
import co.netguru.android.inbbbox.feature.user.info.team.TeamInfoModule;
import co.netguru.android.inbbbox.feature.user.shots.UserShotsComponent;
import co.netguru.android.inbbbox.feature.like.LikesFragmentComponent;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingComponent;
import co.netguru.android.inbbbox.feature.onboarding.OnboardingModule;
import co.netguru.android.inbbbox.feature.project.ProjectComponent;
import co.netguru.android.inbbbox.feature.project.ProjectModule;
import co.netguru.android.inbbbox.feature.shot.ShotsComponent;
import co.netguru.android.inbbbox.feature.shot.ShotsModule;
import co.netguru.android.inbbbox.feature.shot.addtobucket.AddToBucketComponent;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsComponent;
import co.netguru.android.inbbbox.feature.shot.detail.ShotsDetailsModule;
import co.netguru.android.inbbbox.feature.shot.detail.fullscreen.ShotFullscreenComponent;
import co.netguru.android.inbbbox.feature.shot.detail.fullscreen.ShotFullscreenModule;
import co.netguru.android.inbbbox.feature.shot.removefrombucket.RemoveFromBucketComponent;
import co.netguru.android.inbbbox.feature.user.UserActivityComponent;
import co.netguru.android.inbbbox.feature.user.projects.ProjectsComponent;
import co.netguru.android.inbbbox.feature.user.shots.UserShotsModule;
import dagger.Subcomponent;

@UserScope
@Subcomponent(modules = {UserModule.class})
public interface UserComponent {

    ShotsComponent getShotsComponent(ShotsModule module);

    LikesFragmentComponent getLikesFragmentComponent();

    ShotDetailsComponent plus(ShotsDetailsModule module);

    ShotFullscreenComponent plus(ShotFullscreenModule module);

    FollowersFragmentComponent plusFollowersFragmentComponent();

    UserShotsComponent plusUserShotsComponent(UserShotsModule module);

    TeamInfoComponent plusTeamInfoComponent(TeamInfoModule module);

    UserInfoComponent plusUserInfoComponent(UserInfoModule module);

    BucketsFragmentComponent plusBucketsFragmentComponent();

    BucketsDetailsComponent plusBucketDetailsComponent();

    AddToBucketComponent plusAddToBucketComponent();

    RemoveFromBucketComponent plusRemoveFromBucketComponent();

    CreateBucketComponent plusCreateBucketComponent();

    OnboardingComponent plusOnboardingComponent(OnboardingModule module);

    ProjectsComponent plusProjectsComponent();

    UserActivityComponent plusUserActivityComponent();

    ProjectComponent plusProjectComponent(ProjectModule projectModule);

    ShotPeekAndPopComponent plusPeekAndPopComponent(ShotPeekAndPopModule module);

    UserBucketsComponent plusUserBucketsComponent(UserBucketsModule module);

    @Subcomponent.Builder
    interface Builder {
        UserComponent.Builder userModule(UserModule userModule);

        UserComponent build();
    }
}
