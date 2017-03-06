package co.netguru.android.inbbbox.feature.user.followdialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.exceptions.InterfaceNotImplementedException;
import timber.log.Timber;

public class UnFollowUserDialogFragment extends DialogFragment {

    public static final String TAG = UnFollowUserDialogFragment.class.getSimpleName();
    private static final String USERNAME_KEY = "usernameKey";

    private OnUnFollowClickedListener onUnFollowClickedListener;

    public static UnFollowUserDialogFragment newInstance(String username) {
        final Bundle args = new Bundle();
        args.putString(USERNAME_KEY, username);

        final UnFollowUserDialogFragment fragment = new UnFollowUserDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onUnFollowClickedListener = (OnUnFollowClickedListener) context;
        } catch (Exception e) {
            Timber.e(e, e.getMessage());
            throw new InterfaceNotImplementedException(e, context.toString(),
                    OnUnFollowClickedListener.class.getSimpleName());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
