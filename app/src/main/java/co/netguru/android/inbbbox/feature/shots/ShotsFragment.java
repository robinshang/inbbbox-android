package co.netguru.android.inbbbox.feature.shots;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.application.App;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.di.component.ShotsComponent;
import co.netguru.android.inbbbox.di.module.ShotsModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragment;
import co.netguru.android.inbbbox.feature.shots.recycler.ShotsAdapter;

public class ShotsFragment
        extends BaseMvpFragment<ShotsContract.View, ShotsContract.Presenter>
        implements ShotsContract.View, ShotsAdapter.OnItemLeftSwipeListener {

    @BindView(R.id.shots_recycler_view)
    RecyclerView shotsRecyclerView;

    @Inject
    ShotsAdapter adapter;

    private ShotsComponent component;
    private OnShotLikeStatusChanged onShotLikeStatusChanged;

    public static ShotsFragment newInstance() {
        return new ShotsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof OnShotLikeStatusChanged)) {
            throw new IllegalStateException("Activity should implement OnShotLikeStatusChanged!");
        }
        onShotLikeStatusChanged = (OnShotLikeStatusChanged) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponent();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shots, container, false);
    }

    private void initComponent() {
        component = App.getAppComponent(getContext())
                .plus(new ShotsModule());
        component.inject(this);
    }

    @Override
    public ShotsContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler();
    }

    private void initRecycler() {
        adapter.setOnLeftSwipeListener(this);
        shotsRecyclerView.setAdapter(adapter);
        shotsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shotsRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void showItems(List<Shot> items) {
        adapter.setItems(items);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(shotsRecyclerView, error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(@StringRes int res) {
        Toast.makeText(getContext(), res, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void changeShotLikeStatus(Shot shot) {
        adapter.changeShotLikeStatus(shot);
        onShotLikeStatusChanged.shotLikeStatusChanged();
    }

    @Override
    public void onItemLeftSwipe(Shot shot) {
        getPresenter().likeShot(shot);
    }

    public interface OnShotLikeStatusChanged {
        void shotLikeStatusChanged();
    }
}
