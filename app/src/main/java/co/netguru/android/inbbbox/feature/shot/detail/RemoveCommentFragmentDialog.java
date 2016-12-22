package co.netguru.android.inbbbox.feature.shot.detail;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.common.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.feature.shot.detail.recycler.DetailsViewActionCallback;
import timber.log.Timber;

public class RemoveCommentFragmentDialog extends DialogFragment {

    public static final String TAG = RemoveCommentFragmentDialog.class.getSimpleName();
    private static final int TARGET_REQUEST_CODE = 9902;

    @BindView(R.id.comment_edit_editText)
    EditText editCommentEditText;

    private DetailsViewActionCallback callback;

    public static RemoveCommentFragmentDialog newInstance(Fragment targetFragment) {
        Bundle args = new Bundle();
        RemoveCommentFragmentDialog fragmentDialog = new RemoveCommentFragmentDialog();
        fragmentDialog.setArguments(args);
        fragmentDialog.setTargetFragment(targetFragment, TARGET_REQUEST_CODE);
        return fragmentDialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.NoTitleDialog)
                .setTitle(R.string.action_warning)
                .setPositiveButton(R.string.action_ok, createDialogListener())
                .setNegativeButton(R.string.action_cancel, createDialogListener())
                .setMessage(R.string.delete_comment_waring);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (DetailsViewActionCallback) getTargetFragment();
        } catch (ClassCastException e) {
            Timber.e(e.getMessage());
            throw new InterfaceNotImplementedException(e, getTargetFragment().getClass().getSimpleName(), DetailsViewActionCallback.class.getSimpleName());
        }
    }

    private DialogInterface.OnClickListener createDialogListener() {
        return (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                callback.onCommentDeleteConfirmed();
            }
            dismiss();
        };
    }
}
