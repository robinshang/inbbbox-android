package co.netguru.android.inbbbox.feature.main;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import co.netguru.android.inbbbox.R;
import timber.log.Timber;

public class TimePickerDialogFragment extends DialogFragment {

    public static final String TAG = TimePickerDialogFragment.class.getSimpleName();

    private static final String START_HOUR = "startHour";
    private static final String START_MINUTE = "startMinute";

    private OnTimePickedListener onTimePickedListener;

    public static TimePickerDialogFragment newInstance(int startHour, int startMinute) {
        final Bundle args = new Bundle();
        args.putInt(START_HOUR, startHour);
        args.putInt(START_MINUTE, startMinute);

        final TimePickerDialogFragment fragment = new TimePickerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onTimePickedListener = (OnTimePickedListener) context;
        } catch (ClassCastException e) {
            Timber.e(e.getMessage());
            throw new ClassCastException(context.toString()
                    + " must implement OnTimePickedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getContext(),
                R.style.TimePickerDialog,
                (view, hourOfDay, minute) -> onTimePickedListener.timePicked(hourOfDay, minute),
                getArguments().getInt(START_HOUR),
                getArguments().getInt(START_MINUTE), false);
    }

    public interface OnTimePickedListener {

        void timePicked(int hour, int minute);
    }
}
