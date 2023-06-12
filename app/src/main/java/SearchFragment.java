import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private ImageView searchButton;
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
        searchButton = view.findViewById(R.id.search_note_btn);
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
        activitiesList.addAll(retrieveSchoolActivitiesFromDatabase("school_activities", searchText));
        activitiesList.addAll(retrieveExtraActivitiesFromDatabase("extracurricular_activities", searchText));

        // If the search text is empty, retrieve all activities from both tables
        if (TextUtils.isEmpty(searchText)) {
            activitiesList.addAll(retrieveSchoolActivitiesFromDatabase("school_activities", ""));
            activitiesList.addAll(retrieveExtraActivitiesFromDatabase("extracurricular_activities", ""));
        }

        // Clear existing table rows
        activitiesTable.removeAllViews();

        // Create table header row
        TableRow headerRow = new TableRow(getActivity());
        TextView nameHeaderTextView = createTableHeaderCell(getString(R.string.activity_name));
        TextView typeHeaderTextView = createTableHeaderCell(getString(R.string.activity_type));
        TextView actionsHeaderTextView = createTableHeaderCell(getString(R.string.actions));
        headerRow.addView(nameHeaderTextView);
        headerRow.addView(typeHeaderTextView);
        headerRow.addView(actionsHeaderTextView);
        activitiesTable.addView(headerRow);

        // Create and populate table rows for each activity
        for (SearchActivity activity : activitiesList) {
            TableRow row = new TableRow(getActivity());

            TextView nameTextView = createTableCell(activity.getName());
            row.addView(nameTextView);

            // Inside the loop in performSearch method
            TextView typeTextView = createTableCell(activity.getActivityType());
            row.addView(typeTextView);


            ImageView actionsButton = createActionsImageView(activity);
            row.addView(actionsButton);

            activitiesTable.addView(row);
        }
    }

    private TextView createTableHeaderCell(String text) {
        TextView textView = new TextView(getActivity());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setPadding(16, 8, 16, 8); // Adjust padding as needed
        textView.setTextColor(getResources().getColor(R.color.purple_200)); // Set desired color for the header
        textView.setTextSize(16); // Set desired text size for the header
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD); // Set bold style for the header
        return textView;
    }

    private TextView createTableCell(String text) {
        TextView textView = new TextView(getActivity());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(22, 12, 22, 12); // Adjust the margin values as needed
        textView.setLayoutParams(layoutParams);
        textView.setText(text);
        textView.setGravity(Gravity.START); // Align text at the start
        return textView;
    }


    private ImageView createActionsImageView(final SearchActivity activity) {
        ImageView imageView = new ImageView(getActivity());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        imageView.setLayoutParams(layoutParams);
        imageView.setPadding(16, 8, 16, 8); // Adjust the padding values as needed
        imageView.setImageResource(R.drawable.ic_delete); // Set the appropriate image resource
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteActivityFromDatabase(activity);
            }
        });
        return imageView;
    }

    private List<SearchActivity> retrieveSchoolActivitiesFromDatabase(String tableName, String searchText) {
        List<SearchActivity> activitiesList = new ArrayList<>();

        SchoolActivitiesDatabaseHelper schoolActivitiesDatabaseHelper = new SchoolActivitiesDatabaseHelper(requireContext());
        SQLiteDatabase schoolDB = schoolActivitiesDatabaseHelper.getReadableDatabase();

        // Define the selection criteria (WHERE clause)
        String selection = "name LIKE ?"; // Adjust this according to your search criteria
        String[] selectionArgs = new String[]{"%" + searchText + "%"};

        String[] schoolCols = {"name"};

        Cursor schoolCursor = schoolDB.query(tableName, schoolCols, selection, selectionArgs, null, null, null);

        while (schoolCursor.moveToNext()) {
            @SuppressLint("Range") String name = schoolCursor.getString(schoolCursor.getColumnIndex("name"));

            // Create an Activity object and add it to the list
            SearchActivity activity = new SearchActivity(name, "SCHOOL 📕📚");
            this.activityType = "SCHOOL 📕📚";
            activitiesList.add(activity);
        }

        schoolCursor.close();
        schoolDB.close();

        return activitiesList;
    }

    private List<SearchActivity> retrieveExtraActivitiesFromDatabase(String tableName, String searchText) {
        List<SearchActivity> activitiesList = new ArrayList<>();

        ExtracurricularActivitiesDatabaseHelper extracurricularActivitiesDatabaseHelper = new ExtracurricularActivitiesDatabaseHelper(requireContext());
        SQLiteDatabase extraDB = extracurricularActivitiesDatabaseHelper.getReadableDatabase();

        // Define the selection criteria (WHERE clause)
        String selection = "activityName LIKE ?"; // Adjust this according to your search criteria
        String[] selectionArgs = new String[]{"%" + searchText + "%"};

        String[] extraCols = {"activityName"};

        Cursor extraCursor = extraDB.query(tableName, extraCols, selection, selectionArgs, null, null, null);

        while (extraCursor.moveToNext()) {
            @SuppressLint("Range") String name = extraCursor.getString(extraCursor.getColumnIndex("activityName"));

            // Create an Activity object and add it to the list
            SearchActivity activity = new SearchActivity(name, "OTHER 💫🪄");
            this.activityType = "OTHER 💫🪄";
            activitiesList.add(activity);
        }

        extraCursor.close();
        extraDB.close();

        return activitiesList;
    }

    private void deleteActivityFromDatabase(SearchActivity activity) {
        if (activity.getActivityType().equals("SCHOOL 📕📚")) {
            SchoolActivitiesDatabaseHelper schoolActivitiesDatabaseHelper = new SchoolActivitiesDatabaseHelper(requireContext());
            SQLiteDatabase schoolDB = schoolActivitiesDatabaseHelper.getWritableDatabase();

            String whereClause = "name = ?";
            String[] whereArgs = {activity.getName()};

            int rowsDeleted = schoolDB.delete(SchoolActivitiesDatabaseHelper.TABLE_SCHOOL_ACTIVITIES, whereClause, whereArgs);
            schoolDB.close();

            if (rowsDeleted > 0) {
                Toast.makeText(getActivity(), "Activity deleted: " + activity.getName(), Toast.LENGTH_SHORT).show();
                performSearch(""); // Refresh the search results after deletion
            }
        } else if (activity.getActivityType().equals("OTHER 💫🪄")) {
            ExtracurricularActivitiesDatabaseHelper extracurricularActivitiesDatabaseHelper = new ExtracurricularActivitiesDatabaseHelper(requireContext());
            SQLiteDatabase extraDB = extracurricularActivitiesDatabaseHelper.getWritableDatabase();

            String whereClause = "activityName = ?";
            String[] whereArgs = {activity.getName()};

            int rowsDeleted = extraDB.delete(ExtracurricularActivitiesDatabaseHelper.TABLE_EXTRACURRICULAR_ACTIVITIES, whereClause, whereArgs);
            extraDB.close();

            if (rowsDeleted > 0) {
                Toast.makeText(getActivity(), "Activity deleted: " + activity.getName(), Toast.LENGTH_SHORT).show();
                performSearch(""); // Refresh the search results after deletion
            }
        }
    }


}
