package co.netguru.android.inbbbox.feature.details.recycler;

import android.animation.LayoutTransition;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.utils.DateTimeFormatUtil;
import co.netguru.android.inbbbox.utils.ViewAnimator;

import static co.netguru.android.inbbbox.utils.StringUtils.getParsedHtmlTextSpanned;

class ShotDetailsCommentViewHolder extends ShotDetailsViewHolder {

    private static final String PARAGRAPH_TAG_START = "<p>";
    private static final String PARAGRAPH_TAG_END = "</p>";
    @BindView(R.id.comment_author_textView)
    TextView authorTextView;

    @BindView(R.id.comment_date_textView)
    TextView dateTextView;

    @BindView(R.id.comment_text_textView)
    TextView commentTextTextView;

    @BindView(R.id.comment_avatar_imageView)
    ImageView authorAvatarImageView;

    @BindView(R.id.comment_action_menu)
    View actionMenu;

    @BindView(R.id.comment_container)
    ViewGroup commentContainerLayout;

    ShotDetailsCommentViewHolder(View view, DetailsViewActionCallback actionCallback) {
        super(view, actionCallback);
        initTransition();
    }

    private void initTransition() {
        final LayoutTransition transition = new LayoutTransition();
        transition.setDuration(700);

        commentContainerLayout.setLayoutTransition(transition);
    }

    @OnClick(R.id.comment_action_cancel)
    void onCancelClick() {
        ViewAnimator.startSlideInFromRightHideAnimation(actionMenu);
    }

    @OnLongClick(R.id.comment_text_textView)
    boolean onCommentLongClick() {
        ViewAnimator.startSlideInFromRightShowAnimation(actionMenu);
        return true;
    }

    @Override
    protected void handleBinding() {
        Comment currentComment = commentList
                .get(getAdapterPosition() - ShotDetailsAdapter.STATIC_ITEMS_COUNT);

        authorTextView.setText(currentComment.author());
        setCommentText(currentComment.text());

        showAvatar(currentComment.authorAvatarUrl());
        dateTextView.setText(DateTimeFormatUtil
                .getTimeLabel(itemView.getContext(), currentComment.date()));
    }

    private void setCommentText(String text) {

        commentTextTextView.setText(getParsedHtmlTextSpanned(text
                .replace(PARAGRAPH_TAG_START, "")
                .replace(PARAGRAPH_TAG_END, "")));
        commentTextTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void showAvatar(String authorAvatarUrl) {
        Glide.with(itemView.getContext())
                .load(authorAvatarUrl)
                .fitCenter()
                .into(authorAvatarImageView);

    }
}
