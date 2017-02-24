package co.netguru.android.inbbbox.feature.user.followdialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import co.netguru.android.inbbbox.R;

public class UnFollowUserDialogFragment extends DialogFragment {

    public static final String TAG = UnFollowUserDialogFragment.class.getSimpleName();
    private static final String USERNAME_KEY = "usernameKey";
    private static final int TARGET_FRAGMENT_REQUEST_CODE = 201;

    private OnUnFollowClickedListener onUnFollowClickedListener;

    public static <T extends Fragment & OnUnFollowClickedListener>
    UnFollowUserDialogFragment newInstance(T targetFragment, String username) {
        final Bundle args = new Bundle();
        args.putString(USERNAME_KEY, username);

        final UnFollowUserDialogFragment fragment = new UnFollowUserDialogFragment();
        fragment.setArguments(args);
        fragment.setTargetFragment(targetFragment, TARGET_FRAGMENT_REQUEST_CODE);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        onUnFollowClickedListener = (OnUnFollowClickedListener) getTargetFragment();
        return new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                .setMessage(getContext().getString(R.string.fragment_unfollow_details_dialog_text,
                        getArguments().getString(USERNAME_KEY)))
                .setPositiveButton(R.string.action_unfollow, (dialog, which) ->
                        onUnFollowClickedListener.onUnFollowClicked())
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.dismiss())
                .create();
    }

    @FunctionalInterface
    public interface OnUnFollowClickedListener {

        void onUnFollowClicked();
    }
}
