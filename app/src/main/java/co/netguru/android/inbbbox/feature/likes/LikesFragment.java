package co.netguru.android.inbbbox.feature.likes;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.application.App;
import co.netguru.android.inbbbox.data.ui.LikedShot;
import co.netguru.android.inbbbox.di.component.LikesFragmentComponent;
import co.netguru.android.inbbbox.di.module.LikesFragmentModule;
import co.netguru.android.inbbbox.feature.common.BaseFragmentWithMenu;
import co.netguru.android.inbbbox.feature.likes.adapter.LikesAdapter;

public class LikesFragment extends BaseFragmentWithMenu<LikesViewContract.View, LikesViewContract.Presenter>
        implements LikesViewContract.View {

    @BindDrawable(R.drawable.ic_like_emptystate)
    Drawable emptyTextDrawable;

    @BindString(R.string.fragment_like_empty_text)
    String emptyString;

    @BindView(R.id.fragment_likes_empty_view)
    ScrollView emptyView;
    @BindView(R.id.fragment_like_empty_text)
    TextView emptyViewText;

    @Inject
    LikesAdapter likesAdapter;

    private LikesFragmentComponent component;

    public static LikesFragment newInstance() {
        return new LikesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        component = App.getAppComponent(getContext())
                .plus(new LikesFragmentModule(context));
        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_likes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPresenter().getLikesFromServer();
        emptyTextDrawable.setBounds(0, 0, emptyViewText.getLineHeight(), emptyViewText.getLineHeight());
        getPresenter().addIconToText(emptyString, emptyTextDrawable);
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        return likesAdapter;
    }

    @NonNull
    @Override
    public LikesViewContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    @Override
    public void showLikes(List<LikedShot> likedShotList) {
        likesAdapter.setLikeList(likedShotList);
    }

    @Override
    public void hideEmptyLikesInfo() {
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyLikesInfo() {
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setEmptyViewText(SpannableStringBuilder spannableStringBuilder) {
        emptyViewText.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);
    }

    public void refreshFragmentData() {
        getPresenter().getLikesFromServer();
    }
}
