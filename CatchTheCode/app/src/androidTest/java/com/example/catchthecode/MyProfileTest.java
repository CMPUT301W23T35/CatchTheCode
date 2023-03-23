package com.example.catchthecode;
import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void testModify(){
        solo.clickOnButton("my profile");
        solo.assertCurrentActivity("Wrong activity",UserActivity.class);
        solo.clickOnButton("Modify Profile");
        solo.enterText((EditText) solo.getView(R.id.contactInfoText), "123333");
        solo.enterText((EditText) solo.getView(R.id.enterUserName), "Jason");


        Button positiveButton = solo.getButton("Save");
        assertNotNull("Positive button not found", positiveButton);
        solo.clickOnView(positiveButton);

        TextView name = (TextView) solo.getView(R.id.playerID);
        TextView contact = (TextView) solo.getView(R.id.info);


        assertEquals(name.getText().toString(),"Jason");
        assertEquals(contact.getText().toString(),"Phone number is123333");
    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
