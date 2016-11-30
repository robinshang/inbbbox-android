package co.netguru.android.inbbbox.feature.common;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState;

import java.util.LinkedList;
import java.util.List;

import timber.log.Timber;


public abstract class BaseViewState<V extends MvpView, D> implements ViewState<V> {

    private List<D> dataList;

    public BaseViewState() {
        dataList = new LinkedList<>();
    }

    @Override
    public void apply(V view, boolean retained) {
        Timber.d("%s retained %s its state", view.getClass().getSimpleName(), retained);
    }

    public void setDataList(List<D> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
    }

    public void addMoreData(List<D> dataList) {
        this.dataList.addAll(dataList);
    }

    protected List<D> getDataList() {
        return new LinkedList<>(dataList);
    }
}
