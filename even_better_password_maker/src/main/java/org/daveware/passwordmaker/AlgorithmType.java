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

import android.util.Log;

/**
 * Object representing an algorithm type.
 * 
 * All algorithms should be available in both plain and HMAC variants.
 * 
 * @author Dave Marotti
 */
public class AlgorithmType implements Comparable<AlgorithmType> {

    private static final String TAG = AlgorithmType.class.getName();

	public static final AlgorithmType MD4 = new AlgorithmType(1, "MD4",
			"HMAC-MD4", "md4", "hmac-md4", true);
	public static final AlgorithmType MD5 = new AlgorithmType(2, "MD5",
			"HMAC-MD5", "md5", "hmac-md5", true);
	public static final AlgorithmType SHA1 = new AlgorithmType(3, "SHA1",
			"HMAC-SHA1", "sha-1", "hmac-sha1", true);
	public static final AlgorithmType RIPEMD160 = new AlgorithmType(4,
			"RIPEMD160", "HMAC-RIPEMD160", "ripemd160", "hmac-rmd160", true);
	public static final AlgorithmType SHA256 = new AlgorithmType(5, "SHA256",
			"HMAC-SHA256", "sha-256", "hmac-sha256-fixed", true);

	private static final AlgorithmType[] TYPES = { MD4, MD5, SHA1, RIPEMD160,
			SHA256 };

	private int type;
	private String name;
	private String hmacName;
	private boolean compatible;

	private String rdfName;
	private String rdfHmacName;

	private AlgorithmType() {
		type = 2;
		name = "";
		compatible = false;
		rdfName = "";
		rdfHmacName = "";
	}

	private AlgorithmType(int i, String n, String hmac, String rdfN,
			String rdfH, boolean c) {
		type = i;
		name = n;
		hmacName = hmac;
		compatible = c;
		rdfName = rdfN;
		rdfHmacName = rdfH;
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

	public String toRdfString() {
		return rdfName;
	}

	public String toHmacRdfString() {
		return rdfHmacName;
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

	public boolean isCompatible() {
		return compatible;
	}

	public String getHmacName() {
		return hmacName;
	}

	/**
	 * Converts a string to an algorithm type.
	 * 
	 * @param str
	 *            The algorithm type. Valid values are: md4, md5, sha1, sha256,
	 *            rmd160. Any of those valid types can be prefixed with "hmac-".
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
			String lower_name = algoType.name.toLowerCase(Locale.US);
			String lower_rdfname = algoType.rdfName.toLowerCase(Locale.US);
			String lower_hmacName = algoType.rdfHmacName.toLowerCase(Locale.US);
			Log.i(TAG, "Comparing: " + lower_name + " / " + lower_rdfname + " / " + lower_hmacName + " --> " + lower_str);

			if (lower_str.equals(lower_name) || lower_str.equals(lower_hmacName) || lower_str.equals(lower_rdfname)) {
				return algoType;
			}
		}

		throw new Exception(String.format("Invalid algorithm type '%1s'", str));
	}
}
