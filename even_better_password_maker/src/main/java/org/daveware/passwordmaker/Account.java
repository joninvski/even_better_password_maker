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

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Represents an account. This object also functions as a parent account for
 * which it can have any number of child accounts.
 * 
 * @author Dave Marotti
 */
public final class Account {

	public enum UrlComponents {
		Protocol, Subdomain, Domain, PortPathAnchorQuery
	}

	public static String ROOT_ACCOUNT_URI = "http://passwordmaker.mozdev.org/accounts";
	public static String DEFAULT_ACCOUNT_URI = "http://passwordmaker.mozdev.org/defaults";

	public static int DEFAULT_LENGTH = 8;

	private String name = "";
	private String username = "";
	private AlgorithmType algorithm = AlgorithmType.MD5;
	private int length = 8;
	private String characterSet = CharacterSets.BASE_93_SET;
	private LeetType leetType = LeetType.NONE;
	private LeetLevel leetLevel = LeetLevel.LEVEL1;
	private String modifier = "";
	private String prefix = "";
	private String suffix = "";
	private boolean sha256Bug = false;
	private EnumSet<UrlComponents> urlComponents = defaultUrlComponents();


	public Account() {

	}

	public Account(String name) {
		this.name = name;
	}

	public Account(String name, String url, String username) {
		this.name = name;
		this.username = username;
	}

	/**
	 * Constructor which allows all members.
	 * 
	 * @param name
	 * @param username
	 * @param algorithm
	 * @param length
	 * @param characterSet
	 * @param leetType
	 * @param leetLevel
	 * @param modifier
	 * @param prefix
	 * @param suffix
	 * @param sha256Bug
	 */
	public Account(String name, String username, AlgorithmType algorithm,
			int length, String characterSet, LeetType leetType,
			LeetLevel leetLevel, String modifier, String prefix, String suffix,
			boolean sha256Bug) throws Exception {
		this.name = name;
		this.username = username;
		this.algorithm = algorithm;
		this.length = length;
		this.characterSet = characterSet;
		this.leetType = leetType;
		this.leetLevel = leetLevel;
		this.modifier = modifier;
		this.prefix = prefix;
		this.suffix = suffix;
		this.sha256Bug = sha256Bug;
	}

	/**
	 * Copies the settings (not including children or ID) from another account.
	 * 
	 * LEAVE THIS FUNCTION HERE so it's easy to see if new members are ever
	 * added so I don't forget to update it.
	 * 
	 * @param a
	 *            The other account to copy from.
	 */
	public void copySettings(Account a) {
		this.name = a.name;
		this.username = a.username;
		this.algorithm = a.algorithm;
		this.length = a.length;
		this.characterSet = a.characterSet;
		this.leetType = a.leetType;
		this.leetLevel = a.leetLevel;
		this.modifier = a.modifier;
		this.prefix = a.prefix;
		this.suffix = a.suffix;
		this.sha256Bug = a.sha256Bug;

		// The documentation says EnumSet.copyOf() will fail on empty sets.
		if (a.urlComponents.isEmpty() == false)
			this.urlComponents = EnumSet.copyOf(a.urlComponents);
		else
			this.urlComponents = defaultUrlComponents();
	}

	/**
	 * Gets the default set of UrlComponents (empty set).
	 * 
	 * @return
	 */
	private static EnumSet<UrlComponents> defaultUrlComponents() {
		return EnumSet.noneOf(UrlComponents.class);
	}

	public String getName() {
		return name;
	}

	public void setName(String s) {
		name = s;
	}

	/**
	 * @return the algorithm
	 */
	public AlgorithmType getAlgorithm() {
		return algorithm;
	}

	/**
	 * @param algorithm
	 *            the algorithm to set
	 */
	public void setAlgorithm(AlgorithmType algorithm) {
		this.algorithm = algorithm;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the characterSet
	 */
	public String getCharacterSet() {
		return characterSet;
	}

	/**
	 * @param characterSet
	 *            the characterSet to set
	 */
	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}

	/**
	 * @return the leetType
	 */
	public LeetType getLeetType() {
		return leetType;
	}

	/**
	 * @param leetType
	 *            the leetType to set
	 */
	public void setLeetType(LeetType leetType) {
		this.leetType = leetType;
	}

	/**
	 * @return the leetLevel
	 */
	public LeetLevel getLeetLevel() {
		return leetLevel;
	}

	/**
	 * @param leetLevel
	 *            the leetLevel to set
	 */
	public void setLeetLevel(LeetLevel leetLevel) {
		this.leetLevel = leetLevel;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the modifier
	 */
	public String getModifier() {
		return modifier;
	}

	/**
	 * @param modifier
	 *            the modifier to set
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix
	 *            the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix
	 *            the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * @return the sha256Bug
	 */
	public boolean isSha256Bug() {
		return sha256Bug;
	}

	/**
	 * @param sha256Bug
	 *            the sha256Bug to set
	 */
	public void setSha256Bug(boolean sha256Bug) {
		this.sha256Bug = sha256Bug;
	}

	/**
	 * Clears the UrlComponents used with this account
	 */
	public final void clearUrlComponents() {
		this.urlComponents.clear();
	}

	/**
	 * @param urlComponent
	 *            - Add a component of the url to be used as the input text for
	 *            the generated password
	 */
	public final void addUrlComponent(UrlComponents urlComponent) {
		this.urlComponents.add(urlComponent);
	}

	/**
	 * @param urlComponents
	 *            - the Components to use of the url as the input text for the
	 *            generated password
	 */
	public final void setUrlComponents(Set<UrlComponents> urlComponents) {
		this.urlComponents.clear();
		this.urlComponents.addAll(urlComponents);
	}

	/**
	 * If the urlComponents field is empty then the entire getUrl field will be
	 * used. This set is unmodifiable. Use the helper functions to set or modify
	 * the set.
	 * 
	 * @return the url components specified for this account (may be empty)
	 */
	public final Set<UrlComponents> getUrlComponents() {
		return Collections.unmodifiableSet(urlComponents);
	}

	/**
	 * Implements the Comparable<Account> interface, this is based
	 *  on the name.
	 * 
	 * @param o The other account to compare to.
	 * @return this.name.compareTo(otherAccount.name);
	 */
	public int compareTo(Account o) {

		// First ignore case, if they equate, use case.
		int result = name.compareToIgnoreCase(o.name);
		if (result == 0)
			return name.compareTo(o.name);
		else
			return result;
	}

	@Override
	public String toString() {
		return this.name + " " + this.algorithm;
	}
}
