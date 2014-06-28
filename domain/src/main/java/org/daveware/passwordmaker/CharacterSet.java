/*
 * PasswordMaker Java Edition - One Password To Rule Them All
 * Copyright (C) 2011 Dave Marotti
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.daveware.passwordmaker;
import java.io.Serializable;

/**
 * Some preset encodings which can be used.
 * @author Dave Marotti
 */
public final class CharacterSet implements Serializable {

    public static final String NUMERIC = "0123456789";
    public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    public static final String SPECIAL_CHARS = "`~!@#$%^&*()_-+={}|[]\\:\";'<>?,./";
    public static final String EMPTY = "";

    private String mCharset = "";
    private String mCustomCharset = "";
    private boolean mIsCustomCharsetActive = false;

    public static final String [] CHARSETS = {
        UPPERCASE,
        LOWERCASE,
        NUMERIC,
        SPECIAL_CHARS
    };



    public CharacterSet() {
        this.mCharset = EMPTY;
    }

    public CharacterSet(String charset) {
        this.mCharset = charset;
    }

    public CharacterSet(String[] charset_array) {
        for (String c : charset_array)
            mCharset += c;
    }

    public String getChars() {
        if(mIsCustomCharsetActive)
            return mCharset + mCustomCharset;
        else {
            return mCharset;
        }
    }

    public String getCustomCharset() {
        return mCustomCharset;
    }

    public boolean isCustomCharsetActive() {
        return mIsCustomCharsetActive;
    }

    public void setCustomCharsetActive(boolean value) {
        mIsCustomCharsetActive = value;
    }

    public void join(String new_charset) {
        mCharset += new_charset;
    }

    public void join(String[] charset_array) {
        for (String c : charset_array)
            mCharset += c;
    }

    public static CharacterSet getDefaultCharacterset() {
        return new CharacterSet(new String[] {UPPERCASE, LOWERCASE, NUMERIC, SPECIAL_CHARS});
    }

    public Boolean contains(String s) {
        return mCharset.contains(s);
    }

    public void setUppercase() {
        append(UPPERCASE);
    }

    public void setLowercase() {
        append(LOWERCASE);
    }

    public void setSpecialChars() {
        append(SPECIAL_CHARS);
    }

    public void setNumbers() {
        append(NUMERIC);
    }

    public void setCustomCharset(String customChars) {
        mCustomCharset = customChars;
    }

    private void append(String s) {
        mCharset += s;
    }

    public void removeUppercase() {
        remove(UPPERCASE);
    }

    public void removeLowercase() {
        remove(LOWERCASE);
    }

    private void remove(String s) {
        char[] ca = s.toCharArray();
        for (char c : ca) {
            mCharset = mCharset.replace(""+c, "");
        }
    }

    public void removeSpecialChars() {
        remove(SPECIAL_CHARS);
    }

    public void removeNumbers() {
        remove(NUMERIC);
    }

    public int length() {
        return mCharset.length();
    }

    @Override
    public String toString() {
        return getChars();
    }
}
