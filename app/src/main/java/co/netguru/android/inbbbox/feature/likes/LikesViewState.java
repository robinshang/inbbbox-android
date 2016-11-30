package co.netguru.android.inbbbox.feature.likes;

import co.netguru.android.inbbbox.feature.common.BaseViewState;
import co.netguru.android.inbbbox.model.ui.Shot;

class LikesViewState extends BaseViewState<LikesViewContract.View, Shot> {

    @Override
    public void apply(LikesViewContract.View view, boolean retained) {
        super.apply(view, retained);
        view.showLikes(getDataList());
    }
}
