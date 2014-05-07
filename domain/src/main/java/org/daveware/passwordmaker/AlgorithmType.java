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

import java.util.Locale;

/**
 * Object representing an algorithm type.
 *
 * All algorithms should be available in plain variants (HMAC not yet available).
 *
 * @author Dave Marotti
 */
public class AlgorithmType implements Comparable<AlgorithmType> {

    private static final String TAG = AlgorithmType.class.getName();

    public static final AlgorithmType MD4       = new AlgorithmType(1, "MD4");
    public static final AlgorithmType MD5       = new AlgorithmType(2, "MD5");
    public static final AlgorithmType SHA1      = new AlgorithmType(3, "SHA1");
    public static final AlgorithmType SHA256    = new AlgorithmType(4, "SHA256");
    public static final AlgorithmType RIPEMD160 = new AlgorithmType(5, "RIPEMD160");

    private static final AlgorithmType[] TYPES = { MD4, MD5, SHA1, SHA256, RIPEMD160 };

    private int type;
    private String name;

    private AlgorithmType() {
        type = 2;
        name = "MD5";
    }

    private AlgorithmType(int i, String n) {
        type = i;
        name = n;
    }

    public String getName() {
        return this.toString();
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }

    public static AlgorithmType[] getTypes() {
        return TYPES;
    }

    public int compareTo(AlgorithmType o) {
        if (type < o.type)
            return -1;
        if (type > o.type)
            return 1;
        return 0;
    }

    /**
     * Converts a string to an algorithm type.
     *
     * @param str
     *            The algorithm type. Valid values are: md5, sha1, sha256.
     *            Any of those valid types are prefixed with "hmac".
     * @return The algorithm type.
     * @throws Exception
     *             upon invalid algorithm string.
     */
    public static AlgorithmType fromRdfString(String str) throws Exception {
        String lower_str = str.toLowerCase(Locale.US);

        // default
        if (str.length() == 0)
            return MD5;

        // Search the list of registered algorithms
        for (AlgorithmType algoType : TYPES) {
            // TODO - Remove this lowercase
            String lower_name = algoType.name.toLowerCase(Locale.US);

            if (lower_str.equals(lower_name)) {
                return algoType;
            }
        }

        throw new Exception(String.format("Invalid algorithm type '%1s'", str));
    }
}
