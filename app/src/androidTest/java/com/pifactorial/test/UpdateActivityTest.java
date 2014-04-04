package com.pifactorial.test;

import android.test.ActivityInstrumentationTestCase2;
import com.pifactorial.*;

public class UpdateActivityTest extends ActivityInstrumentationTestCase2<UpdateActivity> {

    public UpdateActivityTest() {
        super(UpdateActivity.class); 
    }

    public void testActivity() {
    	UpdateActivity activity = getActivity();
        assertNotNull(activity);
    }
}

