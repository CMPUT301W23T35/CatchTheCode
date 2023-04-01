package com.example.catchthecode;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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

    @Test
    public void checkBoard() {
        solo.clickOnButton("Board");
        solo.assertCurrentActivity("Wrong activity", ScoreBoardActivity.class);
    }
    @Test
    public void checkOrder() {
        solo.clickOnButton("Board");
        solo.assertCurrentActivity("Wrong activity", ScoreBoardActivity.class);
        View b1 =  solo.getView(R.id.my_spinner);
        solo.clickOnView(b1);
        TextView text1 = (TextView) solo.getView(R.id.text1);
        TextView text2 = (TextView) solo.getView(R.id.text2);
        TextView text3 = (TextView) solo.getView(R.id.text3);
        int FP = Integer.parseInt(text1.getText().toString().split(" ")[3]);
        int SP = Integer.parseInt(text2.getText().toString().split(" ")[3]);
        assertTrue(FP>=SP);
        int TP = Integer.parseInt(text3.getText().toString().split(" ")[3]);
        assertTrue(SP>=TP);
    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
