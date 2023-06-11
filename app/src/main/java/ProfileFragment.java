import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.database.Note;
import com.schedulewizard.NotesAdapter;
import com.schedulewizard.R;

import java.util.List;

public class ProfileFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.notes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set up the adapter with your note data
        List<Note> notesList = getNotesData(); // Replace with your own function to retrieve note data
        if(notesList != null){
            adapter = new NotesAdapter(notesList);
            recyclerView.setAdapter(adapter);
        }

        // Set up Add Note button click listener
        Button addNoteButton = view.findViewById(R.id.add_note_button);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNoteDialog();
            }
        });

        return view;
    }

    private List<Note> getNotesData() {
        // Implement your own logic to retrieve note data
        // and return a list of Note objects
        return null;
    }

    private void showAddNoteDialog() {
        AddNoteDialogFragment dialogFragment = new AddNoteDialogFragment();
        dialogFragment.setOnNoteCreatedListener(new AddNoteDialogFragment.OnNoteCreatedListener() {
            @Override
            public void onNoteCreated(Note note) {
                // Add the note to the database
                // You can write the code to add the note to the database here

                // Add the note to the RecyclerView
                adapter.addNote(note);
            }
        });
        dialogFragment.show(getFragmentManager(), "AddNoteDialog");
    }
}
