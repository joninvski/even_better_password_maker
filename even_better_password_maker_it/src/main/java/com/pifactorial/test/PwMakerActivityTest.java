package com.pifactorial.test;

import android.test.ActivityInstrumentationTestCase2;
import com.pifactorial.*;

public class PwMakerActivityTest extends ActivityInstrumentationTestCase2<EntryActivity> {

    public PwMakerActivityTest() {
        super(EntryActivity.class); 
    }

    public void testActivity() {
    	EntryActivity activity = getActivity();
        assertNotNull(activity);
    }
}

