import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.database.SearchActivity;
import com.database.UserDatabaseHelper;
import com.schedulewizard.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText searchEditText;
    private Button searchButton;
    private TableLayout activitiesTable;

    public String activityType;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = view.findViewById(R.id.search_edit_text);
        searchButton = view.findViewById(R.id.search_button);
        activitiesTable = view.findViewById(R.id.activities_table);

        performSearch("");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = searchEditText.getText().toString();
                performSearch(searchText);
            }
        });

        return view;
    }

    private void performSearch(String searchText) {
        List<SearchActivity> activitiesList = new ArrayList<>();

        // Retrieve activities based on the search text from the respective tables
        activitiesList.addAll(retrieveActivitiesFromDatabase("school_activities", searchText));
        activitiesList.addAll(retrieveActivitiesFromDatabase("extracurricular_activities", searchText));

        // If the search text is empty, retrieve all activities from both tables
        if (TextUtils.isEmpty(searchText)) {
            activitiesList.addAll(retrieveActivitiesFromDatabase("school_activities", ""));
            activitiesList.addAll(retrieveActivitiesFromDatabase("extracurricular_activities", ""));
        }

        // Clear existing table rows
        activitiesTable.removeAllViews();

        // Create and populate table rows for each activity
        for (SearchActivity activity : activitiesList) {
            TableRow row = new TableRow(getActivity());

            TextView nameTextView = createTableCell(activity.getName());
            row.addView(nameTextView);

            //TextView typeTextView = createTableCell(this.activityType);
            TextView typeTextView = createTableCell("HEY");
            row.addView(typeTextView);

            Button actionsButton = createActionsButton(activity);
            row.addView(actionsButton);

            activitiesTable.addView(row);
        }
    }


    private TextView createTableCell(String text) {
        TextView textView = new TextView(getActivity());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        return textView;
    }

    private Button createActionsButton(final SearchActivity activity) {
        Button button = new Button(getActivity());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(layoutParams);
        button.setText("Delete");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the action button click (e.g., delete or update)
                // You can use the activity object to perform the corresponding action
                // For example, deleteActivity(activity) or updateActivity(activity)
                Toast.makeText(getActivity(), "Delete button clicked for activity: " + activity.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return button;
    }

    private List<SearchActivity> retrieveActivitiesFromDatabase(String tableName, String searchText) {
        List<SearchActivity> activitiesList = new ArrayList<>();

        SchoolActivitiesDatabaseHelper schoolActivitiesDatabaseHelper = new SchoolActivitiesDatabaseHelper(getActivity());
        SQLiteDatabase schoolDB = schoolActivitiesDatabaseHelper.getReadableDatabase();

        ExtracurricularActivitiesDatabaseHelper extracurricularActivitiesDatabaseHelper = new ExtracurricularActivitiesDatabaseHelper(getActivity());
        SQLiteDatabase extraDB = extracurricularActivitiesDatabaseHelper.getReadableDatabase();

        // Define the selection criteria (WHERE clause)
        String selection = "name LIKE ?"; // Adjust this according to your search criteria
        String[] selectionArgs = new String[]{"%" + searchText + "%"};

        String[] schoolCols = {"name"};
        String[] extraCols = {"activityName"};

        Cursor schoolCursor = schoolDB.query(tableName, schoolCols, selection, selectionArgs, null, null, null);

        while (schoolCursor.moveToNext()) {
            @SuppressLint("Range") String name = schoolCursor.getString(schoolCursor.getColumnIndex("name"));

            // Create an Activity object and add it to the list
            SearchActivity activity = new SearchActivity(name);
            this.activityType = "SCHOOL 📕📚";
            activitiesList.add(activity);
        }

        //schoolCursor.close();
        //schoolDB.close();

        Cursor extraCursor = extraDB.query(tableName, extraCols, selection, selectionArgs, null, null, null);

        while (extraCursor.moveToNext()) {
            @SuppressLint("Range") String name = extraCursor.getString(extraCursor.getColumnIndex("activityName"));

            // Create an Activity object and add it to the list
            SearchActivity activity = new SearchActivity(name);
            this.activityType = "OTHER 💫🪄";
            activitiesList.add(activity);
        }

        //extraCursor.close();
        //extraDB.close();

        return activitiesList;
    }

}


