package com.example.catchthecode;
import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
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
import static java.lang.Thread.sleep;

public class QRCodeActivityTest {
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
            solo.assertCurrentActivity("Failed to switch to QRCodeActivity", QRCodeActivity.class);
        }
//        solo.clickOnView(solo.getView(R.id.button_add)); //Click ADD CITY Button
//        //Get view for EditText and enter a city name
//        solo.enterText((EditText) solo.getView(R.id.editText_name), "Edmonton");
//        solo.clickOnView(solo.getView(R.id.button_confirm)); //Select CONFIRM Button
//        solo.clearEditText((EditText) solo.getView(R.id.editText_name)); //Clear the EditText
//        solo.clickInList(0);
//        solo.assertCurrentActivity("Failed to switch to ShowActivity", ShowActivity.class);
    }

    /**
     * check if we can delete it
     */
    @Test
    public void checkDelete() throws InterruptedException {
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.collection));
        sleep(2000);
        ListView listView = (ListView) solo.getView(R.id.collection_rank);
        String name = (String) listView.getItemAtPosition(0);
        if (name != null) {
            solo.clickInList(0);
            solo.assertCurrentActivity("Failed to switch to QRCodeActivity", QRCodeActivity.class);

            name = ((TextView) solo.getView(R.id.textViewName)).getText().toString();
            solo.clickOnView(solo.getView(R.id.buttonDelete));
            assertFalse(solo.waitForText(name, 1, 2000));
        }
    }
}
