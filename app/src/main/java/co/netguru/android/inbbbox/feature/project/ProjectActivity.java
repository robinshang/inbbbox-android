package co.netguru.android.inbbbox.feature.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.shot.model.ui.Shot;
import co.netguru.android.inbbbox.data.user.projects.model.ui.ProjectWithShots;
import co.netguru.android.inbbbox.feature.shared.base.BaseActivity;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsFragment;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsRequest;
import co.netguru.android.inbbbox.feature.shot.detail.ShotDetailsType;

public class ProjectActivity extends BaseActivity implements ProjectShotClickListener {

    private static final String KEY_PROJECT = "key:project";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindColor(R.color.white)
    int colorWhite;

    public static void startActivity(Context context, ProjectWithShots projectWithShots) {
        final Intent intent = new Intent(context, ProjectActivity.class);
        intent.putExtra(KEY_PROJECT, projectWithShots);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        initializeToolbar();
        if (savedInstanceState == null) {
            initializeFragment();
        }
    }

    @Override
    public void onShotDetailsRequest(Shot shot, List<Shot> allShots) {
        ShotDetailsRequest request = ShotDetailsRequest.builder()
                .detailsType(ShotDetailsType.DEFAULT)
                .id(shot.id())
                .build();

        final Fragment fragment = ShotDetailsFragment.newInstance(shot, allShots, request);
        showBottomSheet(fragment, ShotDetailsFragment.TAG);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeFragment() {
        ProjectWithShots projectWithShots = getIntent().getParcelableExtra(KEY_PROJECT);

        if (projectWithShots != null) {
            replaceFragment(R.id.project_fragment_container,
                    ProjectFragment.newInstance(projectWithShots), ProjectFragment.TAG).commit();
        } else {
            throw new IllegalArgumentException("Project cannot be null");
        }
    }

    private void initializeToolbar() {
        toolbar.setTitleTextColor(colorWhite);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        ProjectWithShots projectWithShots = getIntent().getParcelableExtra(KEY_PROJECT);
        if (actionBar != null) {
            actionBar.setTitle(projectWithShots.name());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}