package com.pifactorial.test;

import android.test.ActivityInstrumentationTestCase2;
import com.pifactorial.*;

public class EntryActivityTest extends ActivityInstrumentationTestCase2<EntryActivity> {

    public EntryActivityTest() {
        super(EntryActivity.class);
    }

    public void testActivity() {
        EntryActivity activity = getActivity();
        assertNotNull(activity);
    }
}

