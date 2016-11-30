package co.netguru.android.inbbbox.feature.shots;

import co.netguru.android.inbbbox.feature.common.BaseViewState;
import co.netguru.android.inbbbox.model.ui.Shot;

class ShotsViewState extends BaseViewState<ShotsContract.View, Shot> {

    @Override
    public void apply(ShotsContract.View view, boolean retained) {
        super.apply(view, retained);
        view.showItems(getDataList());
    }
}
