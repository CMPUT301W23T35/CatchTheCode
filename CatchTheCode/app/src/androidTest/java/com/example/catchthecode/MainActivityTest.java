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
/**
 * Test class for MainActivity. All the UI tests are written here. Robotium test framework is used
 */
public class MainActivityTest {
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
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void checkActivity() {
        solo.assertCurrentActivity("Wrong activty", MainActivity.class);
    }

    @Test
    public void checkMyProfile() {
        solo.assertCurrentActivity("Wrong activty", MainActivity.class);
        solo.clickOnButton("My Profile");
        solo.assertCurrentActivity("Wrong activty", UserActivity.class);
        solo.goBack();
    }

    @Test
    public void checkCollectionButton() {
        solo.assertCurrentActivity("Wrong activty", MainActivity.class);
        solo.clickOnButton("Collection");
        solo.assertCurrentActivity("Wrong activity", CollectionActivity.class);
        solo.goBack();
    }

    @Test
    public void checkSearch() {
        solo.assertCurrentActivity("Wrong activty", MainActivity.class);
        solo.clickOnButton("Search");
        solo.assertCurrentActivity("Wrong activity", SearchActivity.class);
        solo.goBack();
    }

    @Test
    public void checkBoard() {
        solo.assertCurrentActivity("Wrong activty", MainActivity.class);
        solo.clickOnButton("Board");
        solo.assertCurrentActivity("Wrong activity", ScoreBoardActivity.class);
        solo.goBack();
    }

    @Test
    public void CheckMap(){
        solo.assertCurrentActivity("Wrong activty", MainActivity.class);
        solo.clickOnButton("Map");
        solo.assertCurrentActivity("Wrong activity",MapsActivity.class);
        solo.goBack();
    }

    @Test
    public void checkScanButton(){
        solo.assertCurrentActivity("Wrong activty", MainActivity.class);
        solo.clickOnButton("Add QR Code");
        solo.assertCurrentActivity("Wrong activity", ScannedBarcodeActivity.class);
        solo.goBack();
    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}