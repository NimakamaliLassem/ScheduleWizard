package com;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.database.Note;
import com.schedulewizard.R;

public class NoteDetailsDialogFragment extends DialogFragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_TYPE = "type";
    private static final String ARG_DESCRIPTION = "description";

    public static NoteDetailsDialogFragment newInstance(String title, String type, String description) {
        NoteDetailsDialogFragment fragment = new NoteDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_TYPE, type);
        args.putString(ARG_DESCRIPTION, description);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_note_details, null);

        TextView titleTextView = view.findViewById(R.id.title_text_view);
        TextView typeTextView = view.findViewById(R.id.type_text_view);
        TextView descriptionTextView = view.findViewById(R.id.description_text_view);

        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString(ARG_TITLE);
            String type = args.getString(ARG_TYPE);
            String description = args.getString(ARG_DESCRIPTION);

            titleTextView.setText(title);
            typeTextView.setText(type);
            descriptionTextView.setText(description);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view)
                .setTitle(R.string.note_details)
                .setPositiveButton(R.string.ok, null);

        return builder.create();
    }
}
