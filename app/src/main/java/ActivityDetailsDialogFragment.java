import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ActivityDetailsDialogFragment extends DialogFragment {

    private static final String ARG_ACTIVITY_NAME = "activityName";

    public static ActivityDetailsDialogFragment newInstance(String activityName) {
        ActivityDetailsDialogFragment fragment = new ActivityDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACTIVITY_NAME, activityName);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null || !args.containsKey(ARG_ACTIVITY_NAME)) {
            throw new IllegalArgumentException("Invalid arguments provided to ActivityDetailsDialogFragment");
        }

        String activityName = args.getString(ARG_ACTIVITY_NAME);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Activity Details")
                .setMessage("Activity Name: " + activityName)
                .setPositiveButton("OK", (dialog, which) -> dismiss());
        return builder.create();
    }


}
