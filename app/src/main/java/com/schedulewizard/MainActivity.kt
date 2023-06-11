package com.schedulewizard

import ExtracurricularActivitiesDatabaseHelper
import HomeFragment
import NotificationsFragment
import ProfileFragment
import SchoolActivitiesDatabaseHelper
import SearchFragment
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.database.UserDatabaseHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    //  ⚠️⚠️⚠️ PLEASE REMOVE THIS WHILE MERGING ⚠️⚠️⚠️
    private lateinit var userDbHelper: UserDatabaseHelper
    private lateinit var schoolActivitiesDbHelper: SchoolActivitiesDatabaseHelper
    private lateinit var extracurricularActivitiesDbHelper: ExtracurricularActivitiesDatabaseHelper


    //sound file integration
    private lateinit var mediaPlayer: MediaPlayer
    private val startTimeInMillis = 1000 // Set the start time in milliseconds
    private val durationInMillis = 5000 // Set the duration in milliseconds

    private fun testDatabaseOperations() {

        // Add a user
        val userId = userDbHelper.addUser("John", "Doe", "2nd Semester", "Class A")
        Toast.makeText(this, "User added with ID: $userId", Toast.LENGTH_SHORT).show()

        // Add a school activity
        val schoolActivityId = schoolActivitiesDbHelper.addActivity("Quiz 1", "2023-06-15", "Quiz", userId)
        Toast.makeText(this, "School activity added with ID: $schoolActivityId", Toast.LENGTH_SHORT).show()

        // Add an extracurricular activity
        val extracurricularActivityId = extracurricularActivitiesDbHelper.addActivity("Chess Club", "Weekly meetings", userId)
        Toast.makeText(this, "Extracurricular activity added with ID: $extracurricularActivityId", Toast.LENGTH_SHORT).show()

        // Retrieve activities for a user
        val userActivities = getUserActivities(userId)
        Toast.makeText(this, "User Activities: $userActivities", Toast.LENGTH_SHORT).show()
    }

    private fun getUserActivities(userId: Long): List<String> {
        val activities = mutableListOf<String>()

        // Retrieve school activities for the user
        val schoolActivitiesCursor = schoolActivitiesDbHelper.readableDatabase.query(
            SchoolActivitiesDatabaseHelper.TABLE_SCHOOL_ACTIVITIES,
            arrayOf(SchoolActivitiesDatabaseHelper.COLUMN_NAME),
            "${SchoolActivitiesDatabaseHelper.COLUMN_USER_ID} = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )
        while (schoolActivitiesCursor.moveToNext()) {
            val activityName = schoolActivitiesCursor.getString(schoolActivitiesCursor.getColumnIndexOrThrow(SchoolActivitiesDatabaseHelper.COLUMN_NAME))
            activities.add(activityName)
        }
        schoolActivitiesCursor.close()

        // Retrieve extracurricular activities for the user
        val extracurricularActivitiesCursor = extracurricularActivitiesDbHelper.readableDatabase.query(
            ExtracurricularActivitiesDatabaseHelper.TABLE_EXTRACURRICULAR_ACTIVITIES,
            arrayOf(ExtracurricularActivitiesDatabaseHelper.COLUMN_ACTIVITY_NAME),
            "${ExtracurricularActivitiesDatabaseHelper.COLUMN_USER_ID} = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )
        while (extracurricularActivitiesCursor.moveToNext()) {
            val activityName = extracurricularActivitiesCursor.getString(extracurricularActivitiesCursor.getColumnIndexOrThrow(ExtracurricularActivitiesDatabaseHelper.COLUMN_ACTIVITY_NAME))
            activities.add(activityName)
        }
        extracurricularActivitiesCursor.close()

        return activities
    }



    private lateinit var navigation: BottomNavigationView

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Replace with your home fragment
                    replaceFragment(HomeFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    // Replace with your search fragment
                    replaceFragment(SearchFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    // Replace with your notifications fragment
                    replaceFragment(NotificationsFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    // Replace with your profile fragment
                    replaceFragment(ProfileFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

// Hiding the status bar
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation = findViewById(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // Set the initial fragment
        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }
}
