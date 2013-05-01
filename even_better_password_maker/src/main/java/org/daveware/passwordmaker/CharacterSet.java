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

/**
 * Some preset encodings which can be used.
 * @author Dave Marotti
 */
public class CharacterSet {

    public static String NUMERIC = "0123456789";
    public static String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    public static String SPECIAL_CHARS = "`~!@#$%^&*()_-+={}|[]\\:\";'<>?,./";

    private String charset = "";

    public static String [] CHARSETS = {
        UPPERCASE,
        LOWERCASE,
        NUMERIC,
        SPECIAL_CHARS
    };

    public CharacterSet(String charset) {
        this.charset = charset; 
    }

    public CharacterSet(String[] charset) {
        for (String c : charset)
            this.charset.concat(c); 
    }

    public String getChars() {
        return charset;
    }

    public void join(String charset) {
        this.charset.concat(charset); 
    }

    public void join(String[] charset) {
        for (String c : charset)
            this.charset.concat(c); 
    }

    public static CharacterSet getDefaultCharacterset() {
        return new CharacterSet(new String[]{UPPERCASE, LOWERCASE});
    }

    public Boolean contains(String s) {
        return this.charset.contains(s);
    }

    public void setUppercase() {
        concat(UPPERCASE);
    }

    public void setSpecialChars() {
        concat(SPECIAL_CHARS);
    }

    public void setNumbers() {
        concat(NUMERIC);
    }

    private void concat(String s) {
        this.charset.concat(s);
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
            charset = charset.replace(""+c, "");
        }
    }

    public void removeSpecialChars() {
        remove(SPECIAL_CHARS);
    }

    public void removeNumbers() {
        remove(NUMERIC);
    }

    public int length(){
        return charset.length();
    }

    @Override
    public String toString(){
        return this.charset;
    }
}
