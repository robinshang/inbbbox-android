package co.netguru.android.inbbbox.feature.shot.detail.recycler;

import android.support.annotation.NonNull;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.utils.DateTimeFormatUtil;
import co.netguru.android.inbbbox.feature.shot.detail.Comment;

import static co.netguru.android.inbbbox.common.utils.StringUtil.PARAGRAPH_TAG_END;
import static co.netguru.android.inbbbox.common.utils.StringUtil.PARAGRAPH_TAG_START;
import static co.netguru.android.inbbbox.common.utils.StringUtil.getParsedHtmlTextSpanned;

class ShotDetailsCommentViewHolder extends ShotDetailsViewHolder<Comment> {

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

    private Comment currentComment;

    ShotDetailsCommentViewHolder(ViewGroup parent, DetailsViewActionCallback actionCallback) {
        super(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_shot_comment_layout, parent, false), actionCallback);
    }

    @Override
    public void bind(@NonNull Comment comment) {
        actionMenu.setVisibility(View.GONE);
        currentComment = comment;

        authorTextView.setText(currentComment.author());

        setCommentText(currentComment.text());

        showAvatar(currentComment.authorAvatarUrl());
        dateTextView.setText(DateTimeFormatUtil
                .getTimeLabel(itemView.getContext(), currentComment.date()));
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

    @OnLongClick(R.id.comment_container)
    boolean onCommentLongClick() {
        if (currentComment.isCurrentUserAuthor()) {
            actionMenu.setVisibility(View.VISIBLE);
        }
        return true;
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
