package co.netguru.android.inbbbox.feature.likes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.di.component.LikesFragmentComponent;
import co.netguru.android.inbbbox.di.module.LikesFragmentModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragmentWithWithListTypeSelection;
import co.netguru.android.inbbbox.feature.likes.adapter.LikesAdapter;
import co.netguru.android.inbbbox.model.ui.LikedShot;
import co.netguru.android.inbbbox.utils.TextFormatter;
import co.netguru.android.inbbbox.view.LoadMoreScrollListener;

public class LikesFragment extends BaseMvpFragmentWithWithListTypeSelection<LikesViewContract.View, LikesViewContract.Presenter>
        implements LikesViewContract.View {

    public static final int GRID_VIEW_COLUMN_COUNT = 2;
    private static final int LIKES_TO_LOAD_MORE = 10;

    @BindDrawable(R.drawable.ic_like_emptystate)
    Drawable emptyTextDrawable;

    @BindString(R.string.fragment_like_empty_text)
    String emptyString;

    @BindView(R.id.fragment_likes_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_likes_empty_view)
    ScrollView emptyView;
    @BindView(R.id.fragment_like_empty_text)
    TextView emptyViewText;

    @Inject
    LikesAdapter likesAdapter;
    @Inject
    GridLayoutManager gridLayoutManager;
    @Inject
    LinearLayoutManager linearLayoutManager;

    private LikesFragmentComponent component;

    public static LikesFragment newInstance() {
        return new LikesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        component = App.getAppComponent(getContext())
                .plus(new LikesFragmentModule(context));
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_likes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        initEmptyView();
        getPresenter().getLikesFromServer();
    }

    @Override
    protected void changeGridMode(boolean isGridMode) {
        likesAdapter.setGridMode(isGridMode);
        recyclerView.setLayoutManager(isGridMode ? gridLayoutManager : linearLayoutManager);
    }

    @NonNull
    @Override
    public LikesViewContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    @Override
    public void showLikes(List<LikedShot> likedShotList) {
        likesAdapter.setLikeList(likedShotList);
    }

    @Override
    public void showMoreLikes(List<LikedShot> likedShotList) {
        likesAdapter.addMoreLikes(likedShotList);
    }

    @Override
    public void hideEmptyLikesInfo() {
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyLikesInfo() {
        emptyView.setVisibility(View.VISIBLE);
    }

    public void refreshFragmentData() {
        getPresenter().getLikesFromServer();
    }

    private void initEmptyView() {
        emptyTextDrawable.setBounds(0, 0, emptyViewText.getLineHeight(), emptyViewText.getLineHeight());
        emptyViewText.setText(TextFormatter
                .addDrawableToTextAtFirstSpace(emptyString, emptyTextDrawable), TextView.BufferType.SPANNABLE);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(likesAdapter);
        recyclerView.addOnScrollListener(new LoadMoreScrollListener(LIKES_TO_LOAD_MORE) {
            @Override
            public void requestMoreData() {
                presenter.getMoreLikesFromServer();
            }
        });
    }
}