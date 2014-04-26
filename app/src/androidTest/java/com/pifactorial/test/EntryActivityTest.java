package com.pifactorial.test;

import android.test.ActivityInstrumentationTestCase2;
import com.pifactorial.*;
import com.squareup.spoon.Spoon;
import com.robotium.solo.*;

public class EntryActivityTest extends ActivityInstrumentationTestCase2<EntryActivity> {
    private Solo solo;

    public EntryActivityTest() {
        super(EntryActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation());
        getActivity();
    }

    public void testEntryActivity() {

        int timeout = 5000;

        // Wait for activity: 'course.labs.fragmentslab.MainActivity'
        assertTrue("MainActivity not found", solo.waitForActivity(
                       com.pifactorial.EntryActivity.class, timeout));

        EntryActivity activity = getActivity();
        Spoon.screenshot(activity, "initial_state");
    }
}
