package co.netguru.android.inbbbox.feature.details.recycler;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.utils.DateTimeFormatUtil;

import static co.netguru.android.inbbbox.utils.StringUtils.PARAGRAPH_TAG_END;
import static co.netguru.android.inbbbox.utils.StringUtils.PARAGRAPH_TAG_START;
import static co.netguru.android.inbbbox.utils.StringUtils.getParsedHtmlTextSpanned;

class ShotDetailsCommentViewHolder extends ShotDetailsViewHolder {

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

    private Comment currentComment;

    ShotDetailsCommentViewHolder(View view, DetailsViewActionCallback actionCallback) {
        super(view, actionCallback);
    }

    @OnClick(R.id.comment_action_edit)
    void onEditCommentClick() {
        actionMenu.setVisibility(View.GONE);
        actionCallbackListener.onCommentEditSelected(currentComment);
    }

    @OnClick(R.id.comment_action_delete)
    void onDeleteCommentClick() {
        actionMenu.setVisibility(View.GONE);
        actionCallbackListener.onCommentDeleteSelected(currentComment);
    }

    @OnClick(R.id.comment_action_cancel)
    void onCancelClick() {
        actionMenu.setVisibility(View.GONE);
    }

    @OnLongClick(R.id.comment_text_textView)
    boolean onCommentLongClick() {
        if (currentComment.isCurrentUserAuthor()) {
            actionMenu.setVisibility(View.VISIBLE);
        }
        return true;
    }

    @Override
    protected void handleBinding() {
        actionMenu.setVisibility(View.GONE);
        currentComment = commentList
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
