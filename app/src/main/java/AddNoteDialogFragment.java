import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.database.Note;
import com.schedulewizard.R;

public class AddNoteDialogFragment extends DialogFragment {

    private EditText titleEditText;
    private Spinner typeSpinner;
    private OnNoteCreatedListener onNoteCreatedListener;

    public void setOnNoteCreatedListener(OnNoteCreatedListener onNoteCreatedListener) {
    }

    public interface OnNoteCreatedListener {
        void onNoteCreated(Note note);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onNoteCreatedListener = (OnNoteCreatedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnNoteCreatedListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_note, null);

        titleEditText = view.findViewById(R.id.edit_text_title);
        typeSpinner = view.findViewById(R.id.spinner_type);

        builder.setView(view)
                .setTitle("Add Note")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = titleEditText.getText().toString();
                        String type = typeSpinner.getSelectedItem().toString();

                        // Create the note object
                        Note note = new Note(title, type);

                        // Notify the listener
                        if (onNoteCreatedListener != null) {
                            onNoteCreatedListener.onNoteCreated(note);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddNoteDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }
}
