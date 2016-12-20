package co.netguru.android.inbbbox.feature.followers.details;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import co.netguru.android.inbbbox.R;

public class FollowUserDialogFragment extends DialogFragment {

    public static final String TAG = FollowUserDialogFragment.class.getSimpleName();
    private static final String USERNAME_KEY = "usernameKey";
    private static final int TARGET_FRAGMENT_REQUEST_CODE = 201;

    private FollowUserDialogFragment.OnFollowClickedListener onFollowClickedListener;

    public static <T extends Fragment & FollowUserDialogFragment.OnFollowClickedListener>
    FollowUserDialogFragment newInstance(T targetFragment, String username) {
        final Bundle args = new Bundle();
        args.putString(USERNAME_KEY, username);

        final FollowUserDialogFragment fragment = new FollowUserDialogFragment();
        fragment.setArguments(args);
        fragment.setTargetFragment(targetFragment, TARGET_FRAGMENT_REQUEST_CODE);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        onFollowClickedListener = (FollowUserDialogFragment.OnFollowClickedListener) getTargetFragment();
        return new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                .setMessage(getContext().getString(R.string.fragment_follow_details_dialog_text,
                        getArguments().getString(USERNAME_KEY)))
                .setPositiveButton(R.string.action_follow, (dialog, which) ->
                        onFollowClickedListener.onFollowClicked())
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.dismiss())
                .create();
    }

    public interface OnFollowClickedListener {

        void onFollowClicked();
    }
}
