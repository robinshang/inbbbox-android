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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.application.App;
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

    @BindView(R.id.likes_recycler_view)
    RecyclerView recyclerView;

    @Inject
    LikesPresenter presenter;
    @Inject
    LikesAdapter likesAdapter;

    private MenuItem listViewItem;
    private MenuItem gridViewItem;

    public static LikesFragment newInstance() {
        return new LikesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        App.getAppComponent(getContext())
                .plus(new LikesFragmentModule())
                .inject(this);
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(likesAdapter);
        List<Object> test = new LinkedList<>();
        test.add(new Object());
        test.add(new Object());
        test.add(new Object());
        test.add(new Object());
        test.add(new Object());
        test.add(new Object());
        likesAdapter.setLikeList(test);
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
        switch (item.getItemId()) {
            case R.id.action_grid_view:
                changeMenuItemIcons(true);
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                return true;
            case R.id.action_list_view:
                changeMenuItemIcons(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    @Override
    public LikesViewContract.Presenter createPresenter() {
        return presenter;
    }

    private void changeMenuItemIcons(boolean isGridViewClicked) {
        listViewItem.setIcon(isGridViewClicked ? icListView : icListViewActive);
        gridViewItem.setIcon(isGridViewClicked ? icGridViewActive : icGridView);
    }
}
