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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.exceptions.InterfaceNotImplementedException;
import co.netguru.android.inbbbox.feature.details.recycler.DetailsViewActionCallback;
import co.netguru.android.inbbbox.utils.StringUtils;
import timber.log.Timber;

public class EditCommentFragmentDialog extends DialogFragment {

    public static final String TAG = EditCommentFragmentDialog.class.getSimpleName();
    private static final String ARG_STRING_COMMENT = "arg:comment_text";
    private static final int TARGET_REQUEST_CODE = 9901;
    private static final float DISABLE_VIEW_ALPHA_VALUE = 0.2f;
    private static final float ENABLED_VIEW_ALPHA_VALUE = 1;

    @BindView(R.id.comment_edit_editText)
    EditText editCommentEditText;

    @BindView(R.id.update_comment_progressBar)
    ProgressBar updateCommentProgressBar;

    private DetailsViewActionCallback callback;
    private Button positiveButton;
    private Button negativeButton;

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
                .setPositiveButton(R.string.edit_comment_update, null)
                .setNegativeButton(R.string.edit_comment_cancel, null)
                .setView(contentView);

        AlertDialog dialog = builder.create();
        ButterKnife.bind(this, contentView);

        dialog.setOnShowListener(this::initButtonsOnShow);
        String text = getArguments().getString(ARG_STRING_COMMENT);
        editCommentEditText.setText(StringUtils.removeHtml(text));
        return dialog;
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

    public void enableInProgressMode() {
        updateCommentProgressBar.setVisibility(View.VISIBLE);
        editCommentEditText.setEnabled(false);
        editCommentEditText.setAlpha(DISABLE_VIEW_ALPHA_VALUE);
        negativeButton.setEnabled(false);
        positiveButton.setEnabled(false);
    }

    public void disableProgressMode() {
        updateCommentProgressBar.setVisibility(View.GONE);
        editCommentEditText.setEnabled(true);
        editCommentEditText.setAlpha(ENABLED_VIEW_ALPHA_VALUE);
        negativeButton.setEnabled(true);
        positiveButton.setEnabled(true);
    }

    private void initButtonsOnShow(DialogInterface dialogInterface) {
        positiveButton = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(view -> onPositiveClick());
        negativeButton = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(view -> dismiss());
    }

    private void onPositiveClick() {
        String updatedComment = editCommentEditText.getText().toString();
        enableInProgressMode();
        callback.onCommentUpdated(updatedComment);
    }
}
