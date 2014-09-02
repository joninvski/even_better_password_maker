package org.daveware.passwordmaker;

import java.security.Security;

import org.daveware.passwordmaker.PasswordMaker;
import org.junit.*;
import org.spongycastle.jce.provider.BouncyCastleProvider;

import static org.junit.Assert.*;


public class PasswordMakerTest {

    private static Profile pDefault;

    private static String PASS_A = "a";
    private static String PASS_B = "b";
    private static String PASS_C = "c";
    private static SecureCharArray masterPassA;
    private static SecureCharArray masterPassB;
    private static SecureCharArray masterPassC;
    private static String github = "github.com";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    @BeforeClass
    public static void testSetup() {
        pDefault = Profile.getDefaultProfile();

        masterPassA = new SecureCharArray(PASS_A);
        masterPassB = new SecureCharArray(PASS_B);
        masterPassC = new SecureCharArray(PASS_C);
    }

    @Test
    public void testGetModifiedInputText() {
        Profile pDomain = pDefault;

        assertEquals("google.com", PasswordMaker.getModifiedInputText("google.com", pDomain));
        assertEquals("google", PasswordMaker.getModifiedInputText("google", pDomain));
        assertEquals("google.com", PasswordMaker.getModifiedInputText("a.google.com", pDomain));
        assertEquals("google.com", PasswordMaker.getModifiedInputText("abc.abc.google.com", pDomain));

        Profile pDomainAndSub = Profile.getDefaultProfile();
        pDomainAndSub.setUrlComponentSubDomain(true);

        assertEquals("google.com", PasswordMaker.getModifiedInputText("google.com", pDomainAndSub));
        assertEquals("google", PasswordMaker.getModifiedInputText("google", pDomainAndSub));
        assertEquals("a.google.com", PasswordMaker.getModifiedInputText("a.google.com", pDomainAndSub));
        assertEquals("abc.abc.google.com", PasswordMaker.getModifiedInputText("abc.abc.google.com", pDomainAndSub));
        assertEquals("z.abc.abc.google.com", PasswordMaker.getModifiedInputText("z.abc.abc.google.com", pDomainAndSub));

        /* Should i fix this?? Isn't my output more logical? */
        // assertEquals(".google.com", PasswordMaker.getModifiedInputText(".google.com", pDomainAndSub));
        assertEquals("google.com", PasswordMaker.getModifiedInputText(".google.com", pDomainAndSub));
    }

    @Test
    public void testGeneratePasswordLowercaseChars() throws PasswordGenerationException {
        Profile pLowercase = pDefault;
        pLowercase.setCharacterSet(new CharacterSet(CharacterSet.LOWERCASE));
        assertEquals("o9a4e1r5", PasswordMaker.makePassword(masterPassA, pLowercase, github).getData());

        assertEquals("o9a4e1r5", PasswordMaker.makePassword(masterPassA, pLowercase, github).getData());
    }

    @Test
    public void testGeneratePasswordUppercaseChars() {
    }

    @Test
    public void testGeneratePasswordNumbersChars() {
    }

    @Test
    public void testGeneratePasswordSymbolsChars() {
    }

    @Test
    public void testGeneratePasswordCustomChars() {
    }
}
