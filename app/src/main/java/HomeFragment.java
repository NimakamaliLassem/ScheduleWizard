import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.schedulewizard.ActivityAdapter;
import com.schedulewizard.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HomeFragment extends Fragment implements ActivityAdapter.ItemDoubleTapListener {

    private RecyclerView recyclerCal;
    private ActivityAdapter calAdapter;
    private RecyclerView recyclerEx;
    private ActivityAdapter exAdapter;
    private GestureDetector gestureDetector;
    private SparseBooleanArray itemBackgroundStates; // Map item positions to their background states

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerCal = view.findViewById(R.id.recyclerSch);
        recyclerCal.setLayoutManager(new LinearLayoutManager(getActivity()));

        SchoolActivitiesDatabaseHelper schoolDatabaseHelper = new SchoolActivitiesDatabaseHelper(getActivity());
        List<String> schoolActivities = schoolDatabaseHelper.getActivitiesWithTodayDeadline();
        calAdapter = new ActivityAdapter(schoolActivities);
        calAdapter.setItemDoubleTapListener(this);
        recyclerCal.setAdapter(calAdapter);

        // Set the gesture detector on the RecyclerView to detect double taps
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                View view = recyclerCal.findChildViewUnder(e.getX(), e.getY());
                int position = recyclerCal.getChildAdapterPosition(view);

                if (position != RecyclerView.NO_POSITION) {
                    String activityName = schoolActivities.get(position);
                    toggleItemBackgroundColor(view, position);
                    showToast(activityName, view);
                    // Handle other logic here if needed
                    return true;
                }

                return super.onDoubleTap(e);
            }
        });

        recyclerCal.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                gestureDetector.onTouchEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                // Do nothing
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                // Do nothing
            }
        });

        recyclerEx = view.findViewById(R.id.recyclerEx);
        recyclerEx.setLayoutManager(new LinearLayoutManager(getActivity()));

        ExtracurricularActivitiesDatabaseHelper extraDatabaseHelper = new ExtracurricularActivitiesDatabaseHelper(getActivity());
        List<String> extraActivities = extraDatabaseHelper.getFirstTwoActivities();
        exAdapter = new ActivityAdapter(extraActivities);
        exAdapter.setItemDoubleTapListener(this);
        recyclerEx.setAdapter(exAdapter);

        itemBackgroundStates = new SparseBooleanArray(); // Initialize the map to store item background states

        return view;
    }

    @Override
    public void onItemDoubleTap(String activity) {
        // Handle other logic here if needed
    }

    private void showToast(String activityName, View itemView) {
        int backgroundColor = Color.TRANSPARENT;

        if (itemView != null) {
            int position = recyclerCal.getChildAdapterPosition(itemView);
            boolean isSelected = itemBackgroundStates.get(position, false);

            backgroundColor = isSelected ? Color.RED : Color.GREEN;
        }

        if (backgroundColor == Color.GREEN) {
            Toast.makeText(requireActivity(), activityName + " DEADLINE MISSED!", Toast.LENGTH_SHORT).show();
        } else if (backgroundColor == Color.RED) {
            Toast.makeText(requireActivity(), activityName + " DONE ON TIME!", Toast.LENGTH_SHORT).show();
        }
    }

    private void toggleItemBackgroundColor(View itemView, int position) {
        boolean isSelected = itemBackgroundStates.get(position, false);
        int newColor = isSelected ? Color.RED : Color.GREEN;

        itemView.setBackgroundColor(newColor);
        itemBackgroundStates.put(position, !isSelected);
    }

    private void retrieveAndInsertDataFromJSON(SchoolActivitiesDatabaseHelper schoolDatabaseHelper) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                // Instantiate your S3 client
                AmazonS3 s3Client = new AmazonS3Client(new BasicAWSCredentials("AKIAX2KQIPRANNOR3QIA", "P0y7qCsRuIvhPbI0AjSVj+8l8PsmODlQY9fHz6hr"));
                String bucketName = "schedulewizard";
                String objectKey = "SchoolData.json";


                try {
                    // Download the JSON file from S3
                    S3Object s3Object = s3Client.getObject(new GetObjectRequest(bucketName, objectKey));
                    S3ObjectInputStream inputStream = s3Object.getObjectContent();

                    // Read the JSON data from the input stream
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                    StringBuilder jsonBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonBuilder.append(line);
                    }
                    JSONArray jsonArray;

                    try {
                        // Parse the JSON data
                        jsonArray = new JSONArray(jsonBuilder.toString());
                        Log.d("JSONData", jsonBuilder.toString());

                        // Rest of the code...

                        // Clear the existing data in the database
                        schoolDatabaseHelper.getWritableDatabase().delete(SchoolActivitiesDatabaseHelper.TABLE_SCHOOL_ACTIVITIES, null, null);

                        // Insert the data into the database
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String activityName = jsonObject.getString("activity_name");
                            String deadline = jsonObject.getString("deadline");
                            String type = jsonObject.getString("type");
                            long userId = jsonObject.getLong("userId");

                            schoolDatabaseHelper.addActivity(activityName, deadline, type, userId);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONParsingError", e.getMessage());

                        // Show an error message with the specific exception
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(requireActivity(), "Failed to parse JSON data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    // ... remaining code ...

                    // Close the input stream
                    inputStream.close();

                    // Show a success message on the main UI thread
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireActivity(), "Data loaded successfully!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();

                    // Show an error message on the main UI thread
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireActivity(), "Failed to retrieve JSON data", Toast.LENGTH_SHORT).show();
                        }
                    });



                    // Show an error message on the main UI thread
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireActivity(), "Failed to parse JSON data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                return null;
            }
        }.execute();
    }
}
