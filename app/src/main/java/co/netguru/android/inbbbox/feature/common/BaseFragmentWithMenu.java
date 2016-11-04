package co.netguru.android.inbbbox.feature.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;


public abstract class BaseFragmentWithMenu<V extends MvpView, P extends MvpPresenter<V>> extends BaseMvpFragment<V, P> {

    @BindDrawable(R.drawable.ic_listview)
    Drawable icListView;
    @BindDrawable(R.drawable.ic_listview_active)
    Drawable icListViewActive;
    @BindDrawable(R.drawable.ic_gridview)
    Drawable icGridView;
    @BindDrawable(R.drawable.ic_gridview_active)
    Drawable icGridViewActive;

    @BindView(R.id.fragment_likes_recycler_view)
    RecyclerView recyclerView;

    @Inject
    LinearLayoutManager linearLayoutManager;
    @Inject
    GridLayoutManager gridLayoutManager;

    private MenuItem listViewItem;
    private MenuItem gridViewItem;
//    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//    private GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
        recyclerView.setAdapter(getRecyclerViewAdapter());
        return true;
    }

    protected abstract RecyclerView.Adapter getRecyclerViewAdapter();

    private void changeMenuItemIcons(boolean isGridViewClicked) {
        listViewItem.setIcon(isGridViewClicked ? icListView : icListViewActive);
        gridViewItem.setIcon(isGridViewClicked ? icGridViewActive : icGridView);
    }
}
