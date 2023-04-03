package com.example.catchthecode;

import static org.junit.Assert.assertFalse;

import android.widget.EditText;
import android.widget.ListView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 The SearchTest class contains test cases for testing search functionality.
 */

public class SearchTest {
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
    public void checkGeoSearch(){
        solo.assertCurrentActivity("Not in mainactivity", MainActivity.class);
        solo.clickOnButton("Search");
        solo.assertCurrentActivity("Wrong activity", SearchActivity.class);
        solo.clickOnButton("Geo-Search");
        solo.assertCurrentActivity("Not in proper activity", SearchGeoActivity.class);


    }

    @Test
    public void checkFriendSearch(){
        solo.assertCurrentActivity("Not in mainactivity", MainActivity.class);
        solo.clickOnButton("Search");
        solo.assertCurrentActivity("Wrong activity", SearchActivity.class);
        solo.clickOnButton("Name-Search");
        solo.assertCurrentActivity("Not in proper activity", SearchFriendsActivity.class);
        solo.enterText((EditText) solo.getView(R.id.searchEditText), "Definitely can't find this name");
        solo.clickOnButton("Search");
        ListView view = (ListView) solo.getView(R.id.resultsList);
        assert view.getCount() == 0;

    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
