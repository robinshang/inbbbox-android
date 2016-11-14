package co.netguru.android.inbbbox.feature.details.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.netguru.android.inbbbox.R;

public class ShotDetailsViewFactory {

    private static final int USER_INFO_VIEW_TYPE = 0;
    private static final int DESCRIPTION_VIEW_TYPE = 1;
    private static final int COMMENT_VIEW_TYPE = 2;

    private ShotDetailsViewFactory() {
        throw new AssertionError();
    }


    public static ShotDetailsViewHolder getViewHolder(int viewType, ViewGroup parent) {
        View view;
        ShotDetailsViewHolder viewHolder;
        switch (viewType) {
            case USER_INFO_VIEW_TYPE:
                view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.shot_info_user_info_layout, parent, false);
                viewHolder = new ShotDetailsUserInfoViewHolder(view);
                break;
            case DESCRIPTION_VIEW_TYPE:
                view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.shot_info_description_layout, parent, false);
                viewHolder = new ShotDetailsDescriptionViewHolder(view);
                break;
            default:
                view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_shot_comment_layout, parent, false);
                viewHolder = new ShotDetailsCommentViewHolder(view);
                break;
        }
        return viewHolder;
    }
}
