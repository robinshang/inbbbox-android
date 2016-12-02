package co.netguru.android.inbbbox.feature.buckets.details;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import co.netguru.android.inbbbox.R;

public class DeleteBucketDialogFragment extends DialogFragment {

    public static final String TAG = DeleteBucketDialogFragment.class.getSimpleName();

    private static final String BUCKET_NAME_ARG_KEY = "bucket_name_arg_key";
    private static final int TARGET_REQUEST_CODE = 1;

    public static <T extends Fragment & DeleteBucketDialogListener> DeleteBucketDialogFragment
    newInstance(T targetFragment, @NonNull String bucketName) {
        Bundle args = new Bundle();
        args.putString(BUCKET_NAME_ARG_KEY, bucketName);

        DeleteBucketDialogFragment fragment = new DeleteBucketDialogFragment();
        fragment.setTargetFragment(targetFragment, TARGET_REQUEST_CODE);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String bucketName = getArguments().getString(BUCKET_NAME_ARG_KEY);
        DeleteBucketDialogListener deleteBucketDialogListener = (DeleteBucketDialogListener) getTargetFragment();

        return new AlertDialog.Builder(getContext(), R.style.AlertDialog)
                .setMessage(String.format("Delete this %s bucket?", bucketName))
                .setPositiveButton(R.string.action_delete, (dialog, which) -> deleteBucketDialogListener.onDeleteBucket())
                .setNegativeButton(R.string.action_cancel, null)
                .create();
    }

    @FunctionalInterface
    public interface DeleteBucketDialogListener {
        void onDeleteBucket();
    }
}