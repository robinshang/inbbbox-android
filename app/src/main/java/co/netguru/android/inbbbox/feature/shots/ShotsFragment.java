package co.netguru.android.inbbbox.feature.shots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.application.App;
import co.netguru.android.inbbbox.data.ui.Shot;
import co.netguru.android.inbbbox.di.component.ShotsComponent;
import co.netguru.android.inbbbox.di.module.ShotsModule;
import co.netguru.android.inbbbox.feature.shots.recycler.ShotsAdapter;

public class ShotsFragment
        extends MvpFragment<ShotsContract.View, ShotsContract.Presenter>
        implements ShotsContract.View {

    @BindView(R.id.shots_recycler_view)
    RecyclerView shotsRecyclerView;

    @Inject
    ShotsAdapter adapter;

    private ShotsComponent component;

    public static ShotsFragment newInstance() {
        return new ShotsFragment();
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
        ButterKnife.bind(this, view);
        initRecycler();
    }

    private void initRecycler() {
        shotsRecyclerView.setAdapter(adapter);
        shotsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void showItems(List<Shot> items) {
        adapter.setItems(items);
    }

    @Override
    public void showError(String error) {
        Snackbar.make(shotsRecyclerView, error, Snackbar.LENGTH_LONG).show();
    }
}
