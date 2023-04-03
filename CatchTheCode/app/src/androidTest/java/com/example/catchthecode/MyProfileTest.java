package com.example.catchthecode;
import static android.content.ContentValues.TAG;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;



/**
 This class represents a unit test for the CatchTheCode app.
 The purpose of this test is to ensure that the user's profile can be modified correctly.
 */

public class MyProfileTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<MainActivity>(MainActivity.class, true, true) {
            };

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }


    /**
     * Check whether activity correctly switched
     */
    @Test
    public void checkSwitch() throws InterruptedException {
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.profile));
        solo.assertCurrentActivity("Failed to switch to MyProfile", UserActivity.class);
    }
    @Test
    public void testFirstTime() throws InterruptedException {

        // Must give time for the app to display
        boolean isDialogOpen = solo.waitForDialogToOpen(2000);
        if (isDialogOpen == true){
            solo.enterText((EditText) solo.getView(R.id.enterUserName), "first");
            solo.enterText((EditText) solo.getView(R.id.contactInfoText), "111");
            solo.clickOnButton("Save");
            solo.clickOnButton("My Profile");
            solo.assertCurrentActivity("Wrong activity", UserActivity.class);
            // Give db time to update
            Thread.sleep(5000);
            TextView name = (TextView) solo.getView(R.id.playerID);
            TextView contact = (TextView) solo.getView(R.id.info);
            assertEquals(name.getText().toString(), "first");
            assertEquals(contact.getText().toString(), "111");
        }
        else{
            // Do nothing

        }
    }




    @Test
    public void testModify() throws InterruptedException {
        solo.clickOnButton("My Profile");
        solo.assertCurrentActivity("Wrong activity",UserActivity.class);
        solo.clickOnButton("Modify Profile");

        // Clear editText
        solo.clearEditText((EditText) solo.getView(R.id.enterUserName));
        solo.clearEditText((EditText) solo.getView(R.id.contactInfoText));

        // Enter text
        solo.enterText((EditText) solo.getView(R.id.enterUserName), "Test");
        solo.enterText((EditText) solo.getView(R.id.contactInfoText), "123333");


        assertTrue(solo.searchButton("Save"));
        solo.clickOnButton("Save");
        Thread.sleep(5000);
        TextView name = (TextView) solo.getView(R.id.playerID);
        TextView contact = (TextView) solo.getView(R.id.info);

//        assertEquals(name.getText().toString(),"Test");
        assertEquals(contact.getText().toString(),"123333");
    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
