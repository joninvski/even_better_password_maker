package org.daveware.passwordmaker;

import static org.junit.Assert.*;
import org.junit.*;

import org.daveware.passwordmaker.PasswordMaker;


public class PasswordMakerTest {

    static Profile pDomain = Profile.getDefaultProfile();

    static Profile pDomainAndSub = Profile.getDefaultProfile();

    @BeforeClass
    public static void testSetup() {
        pDomainAndSub.setUrlComponentSubDomain(true);
    }

    @Test
    public void testGetModifiedInputText() {
        assertEquals("google.com", PasswordMaker.getModifiedInputText("google.com", pDomain));
        assertEquals("google", PasswordMaker.getModifiedInputText("google", pDomain));
        assertEquals("google.com", PasswordMaker.getModifiedInputText("a.google.com", pDomain));
        assertEquals("google.com", PasswordMaker.getModifiedInputText("abc.abc.google.com", pDomain));

        assertEquals("google.com", PasswordMaker.getModifiedInputText("google.com", pDomainAndSub));
        assertEquals("google", PasswordMaker.getModifiedInputText("google", pDomainAndSub));
        assertEquals("a.google.com", PasswordMaker.getModifiedInputText("a.google.com", pDomainAndSub));
        assertEquals("abc.abc.google.com", PasswordMaker.getModifiedInputText("abc.abc.google.com", pDomainAndSub));
        assertEquals("z.abc.abc.google.com", PasswordMaker.getModifiedInputText("z.abc.abc.google.com", pDomainAndSub));

        /* Should i fix this?? Isn't my output more logical? */
        // assertEquals(".google.com", PasswordMaker.getModifiedInputText(".google.com", pDomainAndSub));
        assertEquals("google.com", PasswordMaker.getModifiedInputText(".google.com", pDomainAndSub));
    }
}
