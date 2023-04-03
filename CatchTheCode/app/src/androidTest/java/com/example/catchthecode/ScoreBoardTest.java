package com.example.catchthecode;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


/**
 * Test class for the ScoreBoardActivity.
 */
public class ScoreBoardTest {
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

    /**
     * Tests if clicking the Board button opens the ScoreBoardActivity.
     */
    @Test
    public void checkBoard() {
        solo.clickOnButton("Board");
        solo.assertCurrentActivity("Wrong activity", ScoreBoardActivity.class);
    }

    /**
     * Tests if the scores are ordered in descending order of total points.
     */
    @Test
    public void checkTotal() {
        solo.clickOnButton("Board");
        solo.assertCurrentActivity("Wrong activity", ScoreBoardActivity.class);
        View b1 =  solo.getView(R.id.my_spinner);
        solo.clickOnView(b1);
        TextView text1 = (TextView) solo.getView(R.id.text1);
        TextView text2 = (TextView) solo.getView(R.id.text2);
        TextView text3 = (TextView) solo.getView(R.id.text3);
        int FP = 0;
        int SP = 0;
        int TP = 0;
        if (text1.getText().toString().split(" ").length >= 3) {
            FP = Integer.parseInt(text1.getText().toString().split(" ")[3]);
        }
        if (text2.getText().toString().split(" ").length >= 3) {
            SP = Integer.parseInt(text2.getText().toString().split(" ")[3]);
        }
        assertTrue(FP>=SP);
        if (text3.getText().toString().split(" ").length >= 3) {
            TP = Integer.parseInt(text3.getText().toString().split(" ")[3]);
        }
        assertTrue(SP>=TP);
    }

    /**
     * Tests if the scores are ordered in descending order of total points.
     */
    @Test
    public void checkSingle() {
        solo.clickOnButton("Board");
        solo.assertCurrentActivity("Wrong activity", ScoreBoardActivity.class);
        View b1 =  solo.getView(R.id.my_spinner);
        solo.clickOnView(b1);
        solo.clickOnText("by unique");
        TextView text1 = (TextView) solo.getView(R.id.text1);
        TextView text2 = (TextView) solo.getView(R.id.text2);
        TextView text3 = (TextView) solo.getView(R.id.text3);
        int FP = 0;
        int SP = 0;
        int TP = 0;
        if (text1.getText().toString().split(" ").length >= 3) {
            FP = Integer.parseInt(text1.getText().toString().split(" ")[3]);
        }
        if (text2.getText().toString().split(" ").length >= 3) {
            SP = Integer.parseInt(text2.getText().toString().split(" ")[3]);
        }
        assertTrue(FP>=SP);
        if (text3.getText().toString().split(" ").length >= 3) {
            TP = Integer.parseInt(text3.getText().toString().split(" ")[3]);
        }
        assertTrue(SP>=TP);
    }

    /**
     * This test checks the count of items displayed in the ScoreBoardActivity.
     * It clicks on the "Board" button to navigate to the ScoreBoardActivity, selects the "by count" option from the spinner,
     * and retrieves the count of items displayed in the text views.
     * Then, it compares the counts of the first and second text views, and the second and third text views.
     * The test passes if the counts are in decreasing order.
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void checkCount() {
        solo.clickOnButton("Board");
        solo.assertCurrentActivity("Wrong activity", ScoreBoardActivity.class);
        View b1 =  solo.getView(R.id.my_spinner);
        solo.clickOnView(b1);
        solo.clickOnText("by count");
        TextView text1 = (TextView) solo.getView(R.id.text1);
        TextView text2 = (TextView) solo.getView(R.id.text2);
        TextView text3 = (TextView) solo.getView(R.id.text3);
        int FP = 0;
        int SP = 0;
        int TP = 0;
        if (text1.getText().toString().split(" ").length >= 3) {
            FP = Integer.parseInt(text1.getText().toString().split(" ")[3]);
        }
        if (text2.getText().toString().split(" ").length >= 3) {
            SP = Integer.parseInt(text2.getText().toString().split(" ")[3]);
        }
        assertTrue(FP>=SP);
        if (text3.getText().toString().split(" ").length >= 3) {
            TP = Integer.parseInt(text3.getText().toString().split(" ")[3]);
        }
        assertTrue(SP>=TP);
    }

    /**
     This method is called after each test to finish all opened activities.
     @throws Exception if an error occurs while finishing the activities
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
