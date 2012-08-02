package com.passwordmaker;

import android.test.ActivityInstrumentationTestCase;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.passwordmaker.PwMakerTest \
 * com.passwordmaker.tests/android.test.InstrumentationTestRunner
 */
public class PwMakerTest extends ActivityInstrumentationTestCase<PwMaker> {

    public PwMakerTest() {
        super("com.passwordmaker", PwMaker.class);
    }

}
