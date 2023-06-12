import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.database.Note;
import com.schedulewizard.R;

public class AddNoteDialogFragment extends DialogFragment {

    private OnNoteCreatedListener onNoteCreatedListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_note, null);

        builder.setView(view)
                .setTitle("Add Note")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText titleEditText = view.findViewById(R.id.edit_text_title);
                        EditText noteEditText = view.findViewById(R.id.edit_text_note);
                        Spinner typeSpinner = view.findViewById(R.id.spinner_type);

                        String title = titleEditText.getText().toString();
                        String text = noteEditText.getText().toString();
                        String type = typeSpinner.getSelectedItem().toString();

                        Note note = new Note(title, text, type);

                        if (onNoteCreatedListener != null) {
                            onNoteCreatedListener.onNoteCreated(note);
                        }

                        dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        return builder.create();
    }

    public void setOnNoteCreatedListener(OnNoteCreatedListener listener) {
        onNoteCreatedListener = listener;
    }

    public interface OnNoteCreatedListener {
        void onNoteCreated(Note note);
    }
}
