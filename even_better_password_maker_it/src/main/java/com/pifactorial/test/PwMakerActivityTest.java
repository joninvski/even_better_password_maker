package com.pifactorial.test;

import android.test.ActivityInstrumentationTestCase2;
import com.pifactorial.*;

public class PwMakerActivityTest extends ActivityInstrumentationTestCase2<PwMaker> {

    public PwMakerActivityTest() {
        super(PwMaker.class); 
    }

    public void testActivity() {
    	PwMaker activity = getActivity();
        assertNotNull(activity);
    }
}

