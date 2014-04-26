package com.pifactorial.test;

import android.test.ActivityInstrumentationTestCase2;
import com.pifactorial.*;
import com.squareup.spoon.Spoon;
import com.robotium.solo.*;

public class UpdateActivityTest extends ActivityInstrumentationTestCase2<UpdateActivity> {
    private Solo solo;

    public UpdateActivityTest() {
        super(UpdateActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    public void testUpdateActivity() {

        int timeout = 5000;

        // Wait for activity: 'course.labs.fragmentslab.MainActivity'
        assertTrue("Update activity not found", solo.waitForActivity(
                       com.pifactorial.UpdateActivity.class, timeout));

        UpdateActivity activity = getActivity();
        Spoon.screenshot(activity, "update_activity_start");
    }
}

