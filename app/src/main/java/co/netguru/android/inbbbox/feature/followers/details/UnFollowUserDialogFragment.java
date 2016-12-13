package co.netguru.android.inbbbox.feature.followers.details;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import co.netguru.android.inbbbox.R;
import timber.log.Timber;

public class UnFollowUserDialogFragment extends DialogFragment {

    public static final String TAG = UnFollowUserDialogFragment.class.getSimpleName();
    private static final String USERNAME_KEY = "usernameKey";
    public static final int TARGET_FRAGMENT_REQUESET_CODE = 201;

    private OnUnFollowClickedListener onUnFollowClickedListener;

    public static <T extends Fragment & OnUnFollowClickedListener>
    UnFollowUserDialogFragment newInstance(T targetFragment, String username) {
        final Bundle args = new Bundle();
        args.putString(USERNAME_KEY, username);

        final UnFollowUserDialogFragment fragment = new UnFollowUserDialogFragment();
        fragment.setArguments(args);
        fragment.setTargetFragment(targetFragment, TARGET_FRAGMENT_REQUESET_CODE);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onUnFollowClickedListener = (OnUnFollowClickedListener) getTargetFragment();
        } catch (ClassCastException e) {
            Timber.e(e.getMessage());
            throw new ClassCastException(getTargetFragment().toString()
                    + " must implement OnUnFollowClickedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                .setMessage(getContext().getString(R.string.fragment_follower_details_dialog_text,
                        getArguments().getString(USERNAME_KEY)))
                .setPositiveButton(R.string.action_unfollow, (dialog, which) ->
                        onUnFollowClickedListener.onUnFollowClicked())
                .setNegativeButton(R.string.action_cancel, (dialog, which) -> dialog.dismiss())
                .create();
    }

    public interface OnUnFollowClickedListener {

        void onUnFollowClicked();
    }
}
