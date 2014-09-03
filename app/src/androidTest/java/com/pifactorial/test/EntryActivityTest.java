package com.pifactorial.test;

import android.test.ActivityInstrumentationTestCase2;

import android.widget.EditText;
import android.widget.TextView;

import com.pifactorial.ebpm.ui.activity.EntryActivity;

import com.robotium.solo.Solo;

import com.squareup.spoon.Spoon;

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
        int timeout = 8000;

        // Wait for activity: 'course.labs.fragmentslab.MainActivity'
        assertTrue("MainActivity not found", solo.waitForActivity(EntryActivity.class, timeout));

        EntryActivity activity = getActivity();
        Spoon.screenshot(activity, "initial_state");
    }

    public void testDefaultPass() {
        int timeout = 8000;

        // Wait for activity: 'course.labs.fragmentslab.MainActivity'
        assertTrue("MainActivity not found", solo.waitForActivity(EntryActivity.class, timeout));

        EntryActivity activity = getActivity();

        // Choose default on the spinner
        boolean actual = solo.waitForText("Default");
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
        actual = solo.waitForText("Show password");
        assertEquals("password is showing before clicking", true, actual);
        Spoon.screenshot(activity, "BeforeClickGeneratedPassword");

        // CLick on the password text
        TextView tvResultPass = (TextView) solo.getView(com.pifactorial.R.id.tvResultPass);
        solo.clickOnView(tvResultPass);

        actual = solo.waitForText("Jb6uZjZ@");
        assertEquals("correct password is not showing after click", true, actual);
        Spoon.screenshot(activity, "AfterClickGeneratedPassword");

        // Click again and the pass has to disappear
        solo.clickOnView(tvResultPass);
        Spoon.screenshot(activity, "AfterSecondClickGeneratedPassword");
        actual = solo.waitForText("Show password");
        assertEquals("password text field still showing password after click", true, actual);
    }
}
