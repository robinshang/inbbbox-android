package co.netguru.android.inbbbox.feature.likes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.BindDrawable;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.application.App;
import co.netguru.android.inbbbox.di.module.LikesFragmentModule;
import co.netguru.android.inbbbox.feature.common.BaseMvpFragment;

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

    @Inject
    LikesPresenter presenter;

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
        listViewItem = menu.findItem(R.id.action_list_view);
        gridViewItem = menu.findItem(R.id.action_grid_view);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_grid_view:
                changeMenuItemIcons(true);
                return true;
            case R.id.action_list_view:
                changeMenuItemIcons(false);
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
