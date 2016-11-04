package co.netguru.android.inbbbox.feature.likes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.models.ui.LikedShot;
import co.netguru.android.inbbbox.di.component.LikesFragmentComponent;
import co.netguru.android.inbbbox.di.module.LikesFragmentModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragment;
import co.netguru.android.inbbbox.feature.likes.adapter.LikesAdapter;

public class LikesFragment extends BaseMvpFragment<LikesViewContract.View, LikesViewContract.Presenter>
        implements LikesViewContract.View {

    @BindDrawable(R.drawable.ic_listview)
    Drawable icListView;
    @BindDrawable(R.drawable.ic_listview_active)
    Drawable icListViewActive;
    @BindDrawable(R.drawable.ic_gridview)
    Drawable icGridView;
    @BindDrawable(R.drawable.ic_gridview_active)
    Drawable icGridViewActive;
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
    private MenuItem listViewItem;
    private MenuItem gridViewItem;

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
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_likes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setAdapter(likesAdapter);
        getPresenter().getLikesFromServer();
        emptyTextDrawable.setBounds(0, 0, emptyViewText.getLineHeight(), emptyViewText.getLineHeight());
        getPresenter().addIconToText(emptyString, emptyTextDrawable);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
        listViewItem = menu.findItem(R.id.action_list_view);
        gridViewItem = menu.findItem(R.id.action_grid_view);
        onOptionsItemSelected(listViewItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean isGridView;
        switch (item.getItemId()) {
            case R.id.action_grid_view:
                isGridView = true;
                break;
            case R.id.action_list_view:
                isGridView = false;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        changeMenuItemIcons(isGridView);
        recyclerView.setLayoutManager(isGridView ? gridLayoutManager : linearLayoutManager);
        return true;
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
    public void hideEmptyLikesInfo() {
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyLikesInfo() {
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setEmptyViewText(SpannableStringBuilder spannableStringBuilder) {
        emptyViewText.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
    }

    private void changeMenuItemIcons(boolean isGridViewClicked) {
        listViewItem.setIcon(isGridViewClicked ? icListView : icListViewActive);
        gridViewItem.setIcon(isGridViewClicked ? icGridViewActive : icGridView);
    }
}
