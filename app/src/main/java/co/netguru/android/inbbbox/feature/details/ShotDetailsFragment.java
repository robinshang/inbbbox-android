package co.netguru.android.inbbbox.feature.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.threeten.bp.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.di.component.ShotDetailsComponent;
import co.netguru.android.inbbbox.di.module.ShotsDetailsModule;
import co.netguru.android.inbbbox.feature.details.recycler.ShotDetailsAdapter;
import co.netguru.android.inbbbox.model.ui.Comment;
import co.netguru.android.inbbbox.model.ui.ShotDetails;

public class ShotDetailsFragment extends BottomSheetDialogFragment {

    private ShotDetailsComponent component;

    @BindView(R.id.shot_details_toolbar)
    Toolbar toolbar;

    @BindView(R.id.shot_details_recyclerView)
    RecyclerView shotRecyclerView;

    @BindView(R.id.parallax_image_view)
    ImageView parallaxImageView;

    @Inject
    ShotDetailsAdapter adapter;

    public static ShotDetailsFragment newInstance(int shotId) {
        return new ShotDetailsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initComponent();
        return LayoutInflater
                .from(getContext())
                .inflate(R.layout.fragment_shot_details, container, false);
    }

    private void initComponent() {
        component = App.getAppComponent(getContext()).plus(new ShotsDetailsModule());
        component.inject(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initRecycler();

        // TODO: 14.11.2016 FOR TESTS
        List<Comment> comments = new ArrayList<>();

        Comment exampleComment1 = Comment.builder()
                .author("Author authorski")
                .authorAvatarUrl("http://findicons.com/files/icons/1072/face_avatars/300/i03.png")
                .date(LocalDateTime.now())
                .text("Example comments")
                .build();

        Comment exampleComment2 = Comment.builder()
                .author("Gonzo")
                .authorAvatarUrl("http://findicons.com/files/icons/1072/face_avatars/300/i03.png")
                .date(LocalDateTime.now().plusSeconds(2))
                .text("Example comment so lorem ipsum here will be nice")
                .build();

        Comment exampleComment3 = Comment.builder()
                .author("Pigeon orSomething")
                .authorAvatarUrl("https://cdn0.iconfinder.com/data/icons/iconshock_guys/512/matthew.png")
                .date(LocalDateTime.now().plusMinutes(30))
                .text("Example comment so lorem ipsum here will be nice. Something new here will be nice")
                .build();

        comments.add(exampleComment1);
        comments.add(exampleComment2);
        comments.add(exampleComment1);
        comments.add(exampleComment2);
        comments.add(exampleComment1);
        comments.add(exampleComment1);
        comments.add(exampleComment1);
        comments.add(exampleComment3);
        ShotDetails details = ShotDetails
                .builder()
                .id(1)
                .title("Awsome Title homie")
                .comments(comments)
                .userAvatarUrl("https://d13yacurqjgara.cloudfront.net/users/653174/avatars/normal/4765adea2b386b03231d10f37d786f8e.jpg?1475482306")
                .authorUrl("https://d13yacurqjgara.cloudfront.net/users/653174/avatars/normal/4765adea2b386b03231d10f37d786f8e.jpg?1475482306")
                .authorName("demo author")
                .appName("demo app")
                .bucketCount(123)
                .likesCount(321)
                .companyName("Netguru dmeos")
                .companyProfileUrl("http://google.com")
                .date("25 dev 2016")
                .description("\"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\"")
                .build();
//
        showItems(details);
        showMainImage("https://d13yacurqjgara.cloudfront.net/users/653174/avatars/normal/4765adea2b386b03231d10f37d786f8e.jpg?1475482306");
    }

    private void initRecycler() {
        shotRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        shotRecyclerView.setAdapter(adapter);
    }

    public void showItems(ShotDetails details) {
        adapter.setDetails(details);
    }

    public void showMainImage(String imageUrl) {
        Glide.with(getContext())
                .load(imageUrl)
                .into(parallaxImageView);
    }
}
