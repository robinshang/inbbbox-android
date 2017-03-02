package co.netguru.android.inbbbox.feature.user.info.singleuser;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.OnClick;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.data.dribbbleuser.Links;
import co.netguru.android.inbbbox.feature.shared.base.BaseViewHolder;

public class UserInfoLinksViewHolder extends BaseViewHolder<Links> {

    private LinkClickListener linkClickListener;
    private Links links;

    public UserInfoLinksViewHolder(ViewGroup parent, LinkClickListener linkClickListener) {
        super(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_info_item_links, parent, false));
        this.linkClickListener = linkClickListener;
    }

    @Override
    public void bind(Links links) {
        this.links = links;
    }

    @OnClick(R.id.link_twitter)
    void onClickTwitter() {
        linkClickListener.onLinkClicked(links.twitter());
    }

    @OnClick(R.id.link_web)
    void onCLickWeb() {
        linkClickListener.onLinkClicked(links.web());
    }

}
