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

    private String mCustomCharset = "";
    private boolean mIsCustomCharsetActive = false;
    private boolean mIsNumericActive = false;
    private boolean mIsUppercaseActive = false;
    private boolean mIsLowercaseActive = false;
    private boolean mIsSpecialCharsActive = false;

    public static final String [] CHARSETS = {
        UPPERCASE,
        LOWERCASE,
        NUMERIC,
        SPECIAL_CHARS
    };

    public CharacterSet() { }

    public String getChars() {
        StringBuilder b = new StringBuilder();

        if( mIsUppercaseActive )
            b.append( UPPERCASE );

        if( mIsLowercaseActive )
            b.append( LOWERCASE );

        if( mIsNumericActive )
            b.append( NUMERIC );

        if( mIsSpecialCharsActive )
            b.append( SPECIAL_CHARS );

        if( mIsCustomCharsetActive )
            b.append( mCustomCharset );

        return b.toString();
    }

    public String getCustomCharset() {
        return mCustomCharset;
    }

    public boolean isCustomCharsetActive() {
        return mIsCustomCharsetActive;
    }

    public void setCustomCharsetActive( boolean value ) {
        mIsCustomCharsetActive = value;
    }

    public static CharacterSet getDefaultCharacterset() {
        CharacterSet charset = new CharacterSet();
        charset.setUppercase();
        charset.setLowercase();
        charset.setNumbers();
        charset.setSpecialChars();
        return charset;
    }


    public boolean hasUppercase() {
        return mIsUppercaseActive;
    }

    public void setUppercase() {
        mIsUppercaseActive = true;
    }

    public boolean hasLowercase() {
        return mIsLowercaseActive;
    }

    public void setLowercase() {
        mIsLowercaseActive = true;
    }

    public boolean hasSpecialChars() {
        return mIsSpecialCharsActive;
    }

    public void setSpecialChars() {
        mIsSpecialCharsActive = true;
    }

    public boolean hasNumbers() {
        return mIsNumericActive;
    }

    public void setNumbers() {
        mIsNumericActive = true;
    }

    public void setCustomCharset( String customChars ) {
        mIsCustomCharsetActive = true;
    }

    public void removeUppercase() {
        mIsUppercaseActive = false;
    }

    public void removeLowercase() {
        mIsLowercaseActive = false;
    }

    public void removeSpecialChars() {
        mIsSpecialCharsActive = false;
    }

    public void removeNumbers() {
        mIsNumericActive = false;
    }

    @Override
    public String toString() {
        return getChars();
    }
}
