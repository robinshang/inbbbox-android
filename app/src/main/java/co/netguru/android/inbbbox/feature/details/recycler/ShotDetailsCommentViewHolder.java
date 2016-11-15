package co.netguru.android.inbbbox.feature.details.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.threeten.bp.LocalDateTime;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.utils.LocalTimeFormatter;

class ShotDetailsCommentViewHolder extends ShotDetailsViewHolder {

    private final LocalTimeFormatter localTimeFormatter;

    @BindView(R.id.comment_author_textView)
    TextView authorTextView;

    @BindView(R.id.comment_date_textView)
    TextView dateTextView;

    @BindView(R.id.comment_text_textView)
    TextView commentTextTextView;

    @BindView(R.id.comment_avatar_imageView)
    ImageView authorAvatarImageView;

    private Comment currentComment;

    ShotDetailsCommentViewHolder(View view, LocalTimeFormatter localTimeFormatter) {
        super(view);
        this.localTimeFormatter = localTimeFormatter;
    }

    @Override
    protected void handleBinding() {
        this.currentComment = item.comments()
                .get(getAdapterPosition() - ShotDetailsAdapter.STATIC_ITEMS_COUNT);

        authorTextView.setText(currentComment.author());
        commentTextTextView.setText(currentComment.text());

        showDate(currentComment.date());
        showAvatar(currentComment.authorAvatarUrl());
    }

    private void showAvatar(String authorAvatarUrl) {
        Glide.with(itemView.getContext())
                .load(authorAvatarUrl)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(authorAvatarImageView);

    }

    private void showDate(LocalDateTime date) {
        dateTextView.setText(localTimeFormatter.getTimeLabel(itemView.getContext(), date));
    }
}
