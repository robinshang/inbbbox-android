package co.netguru.android.inbbbox.feature.details.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.utils.LocalTimeFormatter;

class ShotDetailsCommentViewHolder extends ShotDetailsViewHolder {

    @BindView(R.id.comment_author_textView)
    TextView authorTextView;

    @BindView(R.id.comment_date_textView)
    TextView dateTextView;

    @BindView(R.id.comment_text_textView)
    TextView commentTextTextView;

    @BindView(R.id.comment_avatar_imageView)
    ImageView authorAvatarImageView;

    private final LocalTimeFormatter localTimeFormatter;

    ShotDetailsCommentViewHolder(View view,
                                 LocalTimeFormatter localTimeFormatter,
                                 DetailsViewActionCallback actionCallback) {
        super(view, actionCallback);
        this.localTimeFormatter = localTimeFormatter;
    }

    @Override
    protected void handleBinding() {
        Comment currentComment = commentList
                .get(getAdapterPosition() - ShotDetailsAdapter.STATIC_ITEMS_COUNT);

        authorTextView.setText(currentComment.author());
        commentTextTextView.setText(displayHtml(currentComment.text()));

        showAvatar(currentComment.authorAvatarUrl());
        dateTextView.setText(localTimeFormatter
                .getTimeLabel(itemView.getContext(), currentComment.date()));
    }

    private void showAvatar(String authorAvatarUrl) {
        Glide.with(itemView.getContext())
                .load(authorAvatarUrl)
                .fitCenter()
                .into(authorAvatarImageView);

    }
}
