package co.netguru.android.inbbbox.feature.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.lce.MvpLceView;

import butterknife.BindDrawable;
import co.netguru.android.inbbbox.R;

public abstract class BaseMvpLceFragmentWithListTypeSelection<CV extends View,M, V extends MvpLceView<M>, P extends MvpPresenter<V>>
        extends BaseMvpViewStateFragment<CV, M, V, P> {

    @BindDrawable(R.drawable.ic_listview)
    Drawable icListView;
    @BindDrawable(R.drawable.ic_listview_active)
    Drawable icListViewActive;
    @BindDrawable(R.drawable.ic_gridview)
    Drawable icGridView;
    @BindDrawable(R.drawable.ic_gridview_active)
    Drawable icGridViewActive;

    protected boolean isGridMode;

    private MenuItem listViewItem;
    private MenuItem gridViewItem;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_menu, menu);
        listViewItem = menu.findItem(R.id.action_list_view);
        gridViewItem = menu.findItem(R.id.action_grid_view);
        onOptionsItemSelected(isGridMode ? gridViewItem : listViewItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_grid_view:
                isGridMode = true;
                break;
            case R.id.action_list_view:
                isGridMode = false;
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        changeMenuItemIcons();
        changeGridMode(isGridMode);
        return true;
    }

    protected abstract void changeGridMode(boolean isGridMode);

    private void changeMenuItemIcons() {
        listViewItem.setIcon(isGridMode ? icListView : icListViewActive);
        gridViewItem.setIcon(isGridMode ? icGridViewActive : icGridView);
    }
}
