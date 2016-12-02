package co.netguru.android.inbbbox.feature.buckets.createbucket;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import butterknife.BindView;
import co.netguru.android.inbbbox.App;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.feature.common.BaseMvpDialogFragment;

public class CreateBucketDialogFragment extends BaseMvpDialogFragment<CreateBucketContract.View, CreateBucketContract.Presenter>
        implements CreateBucketContract.View {

    public static final String TAG = CreateBucketDialogFragment.class.getSimpleName();

    @BindView(R.id.name_text_input_layout)
    TextInputLayout nameTextInputLayout;
    @BindView(R.id.description_text_input_layout)
    TextInputLayout descriptionTextInputLayout;
    @BindView(R.id.creating_new_bucket_progress_view)
    LinearLayout creatingNewBucketProgressView;

    private Button createButton;
    private Button cancelButton;

    public static CreateBucketDialogFragment newInstance() {
        return new CreateBucketDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View dialogContentView = View.inflate(getContext(), R.layout.dialog_fragment_create_bucket_view, null);
        super.onViewCreated(dialogContentView, savedInstanceState);
        Dialog dialog = new AlertDialog.Builder(getContext(), R.style.NoTitleDialog)
                .setTitle(R.string.dialog_fragment_create_bucket_title)
                .setPositiveButton(R.string.dialog_fragment_create_bucket_create, null)
                .setNegativeButton(R.string.dialog_fragment_create_bucket_cancel, null)
                .setView(dialogContentView)
                .create();

        setupDialogButtons(dialog);
        return dialog;
    }

    @NonNull
    @Override
    public CreateBucketContract.Presenter createPresenter() {
        return App.getAppComponent(getContext()).plusCreateBucketComponent().getPresenter();
    }

    @Override
    public void close() {
        dismiss();
    }

    @Override
    public void hideErrorMessages(){
        nameTextInputLayout.setErrorEnabled(false);
    }

    @Override
    public void showErrorEmptyBucket() {
        nameTextInputLayout.setError("Name cannot be empty. Please enter valid name.");
    }

    @Override
    public void showProgressView() {
        creatingNewBucketProgressView.setVisibility(View.VISIBLE);
        createButton.setEnabled(false);
        cancelButton.setEnabled(false);
        setCancelable(false);
    }

    @Override
    public void hideProgressView() {
        creatingNewBucketProgressView.setVisibility(View.GONE);
        createButton.setEnabled(true);
        cancelButton.setEnabled(true);
        setCancelable(true);
    }

    private void setupDialogButtons(Dialog dialog) {
        dialog.setOnShowListener(shownDialog -> {
            createButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
            cancelButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
            createButton.setOnClickListener(v -> {
                String name = nameTextInputLayout.getEditText().getText().toString();
                String description = descriptionTextInputLayout.getEditText().getText().toString();
                getPresenter().handleCreateBucket(name, description);
            });
        });
    }
}
