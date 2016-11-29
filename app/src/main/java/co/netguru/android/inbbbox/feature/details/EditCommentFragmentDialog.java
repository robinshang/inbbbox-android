package co.netguru.android.inbbbox.feature.details;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.details.recycler.DetailsViewActionCallback;
import timber.log.Timber;

public class EditCommentFragmentDialog extends DialogFragment {

    public static final String TAG = EditCommentFragmentDialog.class.getSimpleName();
    private static final String ARG_STRING_COMMENT = "arg:comment_text";
    private static final int TARGET_REQUEST_CODE = 9901;

    @BindView(R.id.comment_edit_editText)
    EditText editCommentEditText;

    private DetailsViewActionCallback callback;

    public static EditCommentFragmentDialog newInstance(Fragment targetFragment,
                                                        String currentComment) {
        Bundle args = new Bundle();
        args.putString(ARG_STRING_COMMENT, currentComment);
        EditCommentFragmentDialog fragmentDialog = new EditCommentFragmentDialog();
        fragmentDialog.setArguments(args);
        fragmentDialog.setTargetFragment(targetFragment, TARGET_REQUEST_CODE);
        return fragmentDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View contentView = LayoutInflater
                .from(getContext())
                .inflate(R.layout.edit_comment_dialog_layout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.NoTitleDialog)
                .setTitle(R.string.edit_comment_title)
                .setPositiveButton(R.string.edit_comment_update, createDialogListener())
                .setNegativeButton(R.string.edit_comment_cancel, createDialogListener())
                .setView(contentView);

        ButterKnife.bind(this, contentView);
        editCommentEditText.setText(getArguments().getString(ARG_STRING_COMMENT));
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (DetailsViewActionCallback) getTargetFragment();
        } catch (ClassCastException e) {
            Timber.e(e.getMessage());
            throw new ClassCastException(context.toString()
                    + " must implement DetailsViewActionCallback");
        }
    }

    private DialogInterface.OnClickListener createDialogListener() {
        return (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                String updatedComment = editCommentEditText.getText().toString();
                callback.onCommentUpdated(updatedComment);
            }
            dismiss();
        };
    }
}
