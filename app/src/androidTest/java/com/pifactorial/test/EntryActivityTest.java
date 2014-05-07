package com.pifactorial.test;

import android.test.ActivityInstrumentationTestCase2;

import android.widget.EditText;

import com.pifactorial.EntryActivity;

import com.robotium.solo.*;

import com.squareup.spoon.Spoon;
import android.widget.TextView;

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
        assertTrue("MainActivity not found", solo.waitForActivity(com.pifactorial.EntryActivity.class, timeout));

        EntryActivity activity = getActivity();
        Spoon.screenshot(activity, "initial_state");
    }

    public void testDefaultPass() {
        int timeout = 5000;

        // Wait for activity: 'course.labs.fragmentslab.MainActivity'
        assertTrue("MainActivity not found", solo.waitForActivity(com.pifactorial.EntryActivity.class, timeout));

        EntryActivity activity = getActivity();

        // Choose default on the spinner
        boolean actual = solo.searchText("Default");
        assertEquals("Spinner text not found", true, actual);

        solo.pressSpinnerItem(0, 1);
        actual = solo.isSpinnerTextSelected(0, "Default");
        assertEquals("spinner item Default is not selected",true, actual);

        // Enter www.google.com on the etUrl
        EditText etURL = (EditText) solo.getView(com.pifactorial.R.id.etURL);
        solo.enterText(etURL, "google.com");

        // Enter abc in the master pass
        EditText etMasterPass = (EditText) solo.getView(com.pifactorial.R.id.etMasterPass);
        solo.enterText(etMasterPass, "abc");

        // Check the generated password does not show up
        actual = solo.searchText("Click to show/hide");
        assertEquals("password is showing before clicking",true, actual);
        Spoon.screenshot(activity, "BeforeClickGeneratedPassword");

        // CLick on the password text
        TextView tvResultPass = (TextView) solo.getView(com.pifactorial.R.id.tvResultPass);
        solo.clickOnView(tvResultPass);

        // check the generated password shows up
        actual = solo.searchText("Click to show/hide");
        assertEquals("password text field still showing default message", false, actual);

        actual = solo.searchText("FDyXQbFs");
        assertEquals("password is not showing after click", true, actual);
        Spoon.screenshot(activity, "AfterClickGeneratedPassword");

        // Click again and the pass has to disappear
        solo.clickOnView(tvResultPass);
        Spoon.screenshot(activity, "AfterSecondClickGeneratedPassword");
        actual = solo.searchText("Click to show/hide");
        assertEquals("password text field still showing password after click", true, actual);
    }
}
