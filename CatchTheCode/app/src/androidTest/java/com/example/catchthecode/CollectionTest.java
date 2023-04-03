package com.example.catchthecode;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

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
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Collection;

public class CollectionTest {
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
     * Check whether activity correctly switched
     */
    @Test
    public void checkSwitch() throws InterruptedException {
        //Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.collection));
        solo.assertCurrentActivity("Failed to switch to ShowActivity", CollectionActivity.class);
    }

    @Test
    public void checkCollection(){
        solo.clickOnButton("My Profile");
        solo.assertCurrentActivity("Wrong activity", UserActivity.class);
        solo.clickOnButton("Collection");
        solo.assertCurrentActivity("Wrong activity", CollectionActivity.class);
    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }


}
