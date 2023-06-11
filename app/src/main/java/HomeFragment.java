import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.schedulewizard.ActivityAdapter;
import com.schedulewizard.R;

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
}
