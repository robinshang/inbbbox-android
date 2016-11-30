package co.netguru.android.inbbbox.feature.buckets;

import co.netguru.android.inbbbox.feature.common.BaseViewState;
import co.netguru.android.inbbbox.model.ui.BucketWithShots;

class BucketViewState extends BaseViewState<BucketsFragmentContract.View, BucketWithShots> {

    @Override
    public void apply(BucketsFragmentContract.View view, boolean retained) {
        super.apply(view, retained);
        view.showBucketsWithShots(getDataList());
    }
}
