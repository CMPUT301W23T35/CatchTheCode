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
 This class contains the test cases for scanning QR codes functionality in the app.
 */

public class scanTest {
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
    public void checkScan(){
        solo.clickOnButton("scan_qr_code");
        solo.assertCurrentActivity("Wrong activity", ScannedBarcodeActivity.class);
        if (solo.searchText("No Barcode Detected")) {
            solo.clickOnButton("Scan");
            solo.assertCurrentActivity("Wrong activity", ScanFailMsg.class);
            solo.clickOnButton("Retake");
            solo.assertCurrentActivity("Wrong activity", ScannedBarcodeActivity.class);
        }else{
            solo.clickOnButton("Scan");
            solo.assertCurrentActivity("Wrong activity", TestAct.class);
            solo.clickOnButton("Back");
            solo.assertCurrentActivity("Wrong activity", ScannedBarcodeActivity.class);
        }

    }
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }

}
