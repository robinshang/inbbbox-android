package co.netguru.android.inbbbox.feature.likes;

import java.util.List;

import co.netguru.android.inbbbox.feature.common.BaseViewState;
import co.netguru.android.inbbbox.model.ui.Shot;

class LikesViewState extends BaseViewState<LikesViewContract.View, Shot> {

    @Override
    public void apply(LikesViewContract.View view, boolean retained) {
        super.apply(view, retained);
        final List<Shot> shotList = getDataList();
        if (shotList.isEmpty()) {
            view.showEmptyLikesInfo();
        } else {
            view.hideEmptyLikesInfo();
            view.showLikes(shotList);
        }
    }
}
