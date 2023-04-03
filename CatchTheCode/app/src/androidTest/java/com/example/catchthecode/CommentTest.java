package com.example.catchthecode;
import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import android.widget.EditText;
import android.widget.ListView;
import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import static java.lang.Thread.sleep;

import java.util.logging.Handler;

/**
 The CommentTest class is used to test the comment functionality of the application
 */

public class CommentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);
    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{

        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }
    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }
    /**
     * Check whether activity correctly switched
     * @throws InterruptedException
     */
    @Test
    public void checkSwitch() throws InterruptedException {
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.collection));
        sleep(2000);
        ListView listView = (ListView) solo.getView(R.id.collection_rank);
        String name = (String) listView.getItemAtPosition(0);
        if (name != null) {
            solo.clickInList(0);
            solo.clickOnView(solo.getView(R.id.comment_button));
            solo.assertCurrentActivity("Failed to switch to commentActivity", CommentActivity.class);
        }
    }

    /**
     * Check whether the comment is added correctly
     * @throws InterruptedException
     */
    @Test
    public void checkAddComment() throws InterruptedException {
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.collection));
        sleep(2000);

        ListView listView = (ListView) solo.getView(R.id.collection_rank);
        String name = (String) listView.getItemAtPosition(0);
        if (name != null) {
            solo.clickInList(0);
            solo.clickOnView(solo.getView(R.id.comment_button));
            solo.assertCurrentActivity("Failed to switch to commentActivity", CommentActivity.class);
            solo.enterText((EditText) solo.getView(R.id.enter_comment_box), "test");
            solo.clickOnView(solo.getView(R.id.addCommentButton));
            assertTrue(solo.waitForText("test", 1, 2000));
        }
    }
}
