import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.NoteDetailsDialogFragment;
import com.database.Note;
import com.schedulewizard.NotesAdapter;
import com.schedulewizard.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment implements GestureDetector.OnGestureListener {

    private RecyclerView recyclerView;
    private NotesAdapter adapter;

    private GestureDetector gestureDetector;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.notes_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Set up the adapter with your note data
        List<Note> notesList = getNotesData(); // Replace with your own function to retrieve note data
        adapter = new NotesAdapter(notesList);
        recyclerView.setAdapter(adapter);

        // Set up Add Note button click listener
        Button addNoteButton = view.findViewById(R.id.add_note_button);
        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNoteDialog();
            }
        });

        // Initialize GestureDetector
        gestureDetector = new GestureDetector(requireContext(), this);

        // Attach the gesture detector to the RecyclerView
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        return view;
    }

    private List<Note> getNotesData() {
        List<Note> notesList = new ArrayList<>();

        // Implement your database retrieval logic here
        // For example, you can use a SQLiteOpenHelper to query the notes table

        // Assuming you have a database helper class named NoteDatabaseHelper
        NoteDatabaseHelper databaseHelper = new NoteDatabaseHelper(requireContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        // Define the columns you want to retrieve from the database
        String[] projection = {"noteID", "noteTitle", "noteText", "noteType"};

        // Define any selection criteria if needed
        String selection = null;
        String[] selectionArgs = null;

        // Execute the query
        Cursor cursor = database.query("notes", projection, selection, selectionArgs, null, null, null);

        // Iterate through the cursor and create Note objects
        while (cursor.moveToNext()) {
            int noteId = cursor.getInt(cursor.getColumnIndexOrThrow("noteID"));
            String noteTitle = cursor.getString(cursor.getColumnIndexOrThrow("noteTitle"));
            String noteText = cursor.getString(cursor.getColumnIndexOrThrow("noteText"));
            String noteType = cursor.getString(cursor.getColumnIndexOrThrow("noteType"));

            Note note = new Note(noteTitle, noteText, noteType);
            notesList.add(note);
        }

        // Close the cursor and database connection
        cursor.close();
        //database.close();

        // Return the list of Note objects
        return notesList;
    }


    private void showAddNoteDialog() {
        AddNoteDialogFragment dialogFragment = new AddNoteDialogFragment();
        dialogFragment.setOnNoteCreatedListener(new AddNoteDialogFragment.OnNoteCreatedListener() {
            @Override
            public void onNoteCreated(Note note) {
                // Add the note to the database
                addNoteToDatabase(note);

                // Add the note to the RecyclerView
                adapter.addNote(note);
            }
        });
        dialogFragment.show(getChildFragmentManager(), "AddNoteDialog");
    }

    private void addNoteToDatabase(Note note) {
        NoteDatabaseHelper dbHelper = new NoteDatabaseHelper(this.getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("noteTitle", note.getTitle());
        values.put("noteText", note.getText());
        values.put("noteType", note.getType());

        long newRowId = db.insert("notes", null, values);

        // Close the database connection
        //db.close();
    }


    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // Find the position of the item that was long-pressed
        View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
        int position = recyclerView.getChildAdapterPosition(childView);

        // Retrieve the note at the long-pressed position
        Note note = adapter.getNoteAtPosition(position);

        // Show the detail dialog for the long-pressed note
        showNoteDetailDialog(note);
    }


    @Override
    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private void showNoteDetailDialog(Note note) {
        NoteDetailsDialogFragment dialogFragment = NoteDetailsDialogFragment.newInstance(note.getTitle(), note.getType(), note.getText());
        dialogFragment.show(getChildFragmentManager(), "NoteDetailDialog");
    }

}
