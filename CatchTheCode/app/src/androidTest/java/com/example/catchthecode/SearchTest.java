package com.example.catchthecode;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
