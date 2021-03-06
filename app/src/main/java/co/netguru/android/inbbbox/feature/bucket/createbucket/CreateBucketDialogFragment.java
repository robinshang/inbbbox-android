package co.netguru.android.inbbbox.feature.bucket.createbucket;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import javax.inject.Inject;

import butterknife.BindView;
import co.netguru.android.inbbbox.R;
import co.netguru.android.inbbbox.app.App;
import co.netguru.android.inbbbox.common.analytics.AnalyticsEventLogger;
import co.netguru.android.inbbbox.feature.shared.base.BaseMvpDialogFragment;

public class CreateBucketDialogFragment extends BaseMvpDialogFragment<CreateBucketContract.View, CreateBucketContract.Presenter>
        implements CreateBucketContract.View {

    public static final String TAG = CreateBucketDialogFragment.class.getSimpleName();

    private static final int FRAGMENT_REQUEST_CODE = 101;

    @BindView(R.id.name_text_input_layout)
    TextInputLayout nameTextInputLayout;
    @BindView(R.id.description_text_input_layout)
    TextInputLayout descriptionTextInputLayout;
    @BindView(R.id.creating_new_bucket_progress_view)
    LinearLayout creatingNewBucketProgressView;

    private Button createButton;
    private Button cancelButton;
    private CreateBucketComponent component;

    @Inject
    AnalyticsEventLogger analyticsEventLogger;

    public static CreateBucketDialogFragment newInstance(Fragment targetFragment) {
        final CreateBucketDialogFragment createBucketDialogFragment = new CreateBucketDialogFragment();
        createBucketDialogFragment.setTargetFragment(targetFragment, FRAGMENT_REQUEST_CODE);

        return createBucketDialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initComponent();
        View dialogContentView = View.inflate(getContext(), R.layout.dialog_fragment_create_bucket_view, null);
        super.onViewCreated(dialogContentView, savedInstanceState);
        Dialog dialog = new AlertDialog.Builder(getContext(), R.style.NoTitleDialog)
                .setTitle(R.string.dialog_fragment_create_bucket_title)
                .setPositiveButton(R.string.action_create, null)
                .setNegativeButton(R.string.action_cancel, null)
                .setView(dialogContentView)
                .create();

        setupDialogButtons(dialog);
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        analyticsEventLogger.logEventDialogCreateBucket();
    }

    private void initComponent() {
        component = App.getUserComponent(getContext()).plusCreateBucketComponent();
        component.inject(this);
    }

    @NonNull
    @Override
    public CreateBucketContract.Presenter createPresenter() {
        return component.getPresenter();
    }

    @Override
    public void close() {
        dismiss();
    }

    @Override
    public void hideErrorMessages() {
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

    @Override
    public void showMessageOnServerError(String errorText) {
        Snackbar.make(getTargetFragment().getView(), errorText, Snackbar.LENGTH_LONG).show();
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
