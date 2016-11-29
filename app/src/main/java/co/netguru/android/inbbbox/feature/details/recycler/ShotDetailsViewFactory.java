package co.netguru.android.inbbbox.feature.details.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;

public class ShotDetailsViewFactory {

    static final int LOAD_MORE_ITEM_TYPE = -99;
    static final int USER_INFO_VIEW_TYPE = 0;
    static final int DESCRIPTION_VIEW_TYPE = 1;

    private ShotDetailsViewFactory() {
        throw new AssertionError();
    }


    static ShotDetailsViewHolder getViewHolder(int viewType,
                                               ViewGroup parent,
                                               DetailsViewActionCallback actionCallback) {
        View view;
        ShotDetailsViewHolder viewHolder;
        switch (viewType) {
            case USER_INFO_VIEW_TYPE:
                view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_shot_info_user_info_layout, parent, false);
                viewHolder = new ShotDetailsUserInfoViewHolder(view, actionCallback);
                break;
            case DESCRIPTION_VIEW_TYPE:
                view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_shot_info_description_layout, parent, false);
                viewHolder = new ShotDetailsDescriptionViewHolder(view, actionCallback);
                break;
            case LOAD_MORE_ITEM_TYPE:
                view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_shot_load_more_layout, parent, false);
                viewHolder = new ShotDetailsLoadMoreViewHolder(view, actionCallback);
                break;
            default:
                view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_shot_comment_layout, parent, false);
                viewHolder = new ShotDetailsCommentViewHolder(view, actionCallback);
                break;
        }
        return viewHolder;
    }
}
