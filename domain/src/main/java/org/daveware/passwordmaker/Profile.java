
/*
 * This class was adapted from the project
 *
 * PasswordMaker Java Edition - One Password To Rule Them All
 * Copyright (C) 2011 Dave Marotti
 *
 * Url: https://code.google.com/p/passwordmaker-je/
 *
 * License
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
import java.io.Serializable;

/**
 * Represents a profile for rules to generate passwords
 */
public class Profile implements Serializable {

    public enum UrlComponents {
        Protocol, Subdomain, Domain, PortPathAnchorQuery
    }

    public static final int DEFAULT_LENGTH = 8;
    public static final String DEFAULT_NAME = "Default";

    private String name;
    private String username;
    private AlgorithmType algorithm;
    private int length;
    private CharacterSet characterSet;
    private LeetType leetType;
    private LeetLevel leetLevel;
    private String modifier;
    private String prefix;
    private String suffix;
    private EnumSet<UrlComponents> urlComponents;
    private Boolean isHMAC;
    private Boolean joinTopLevel;

    public Profile() {
        copySettings(Profile.getDefaultProfile());
        this.name = DEFAULT_NAME;
    }

    public Profile(String name) {
        super();
        this.name = name;

    }

    public Profile(String name, String url, String username) {
        super();
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
     */
    public Profile(String name, String username, AlgorithmType algorithm,
                   int length, CharacterSet characterSet, LeetType leetType,
                   LeetLevel leetLevel, String modifier, String prefix, String suffix,
                   boolean isHMAC, Boolean joinTopLevel, EnumSet<UrlComponents> urlComponents) {
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
        this.isHMAC = isHMAC;
        this.joinTopLevel = joinTopLevel;
        this.urlComponents = urlComponents;
    }

    /**
     * Copies the settings (not including children or ID) from another profile
     *
     * LEAVE THIS FUNCTION HERE so it's easy to see if new members are ever
     * added so I don't forget to update it.
     *
     * @param a
     *            The other profile to copy from.
     */
    public void copySettings(Profile a) {
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
        this.isHMAC = a.isHMAC;
        this.joinTopLevel = a.joinTopLevel;

        // The documentation says EnumSet.copyOf() will fail on empty sets.
        if (a.urlComponents.isEmpty() == false)
            this.urlComponents = EnumSet.copyOf(a.urlComponents);
        else
            this.urlComponents = getDefaultUrlComponents();
    }

    /**
     * Gets the default set of UrlComponents (empty set).
     *
     * @return
     */
    private static EnumSet<UrlComponents> getDefaultUrlComponents() {
        return EnumSet.of(UrlComponents.Domain);
    }

    public String getName() {
        return name;
    }

    public void setName(String s) {
        name = s;
    }

    public Boolean getJoinTopLevel() {
        return this.joinTopLevel;
    }

    public void setJoinTopLevel(Boolean b) {
        this.joinTopLevel = b;
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
    public String getCompleteCharacterSet() {
        return characterSet.getChars();
    }

    /**
     * @param characterSet
     *            the characterSet to set
     */
    public void setCharacterSet(CharacterSet characterSet) {
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
     * Clears the UrlComponents used with this profile
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

    public final void setUrlComponents(UrlComponents urlComponent) {
        this.urlComponents.clear();
        this.urlComponents.add(urlComponent);
    }

    public final void setIsHMAC(Boolean isHMAC) {
        this.isHMAC = isHMAC;
    }

    /**
     * If the urlComponents field is empty then the entire getUrl field will be
     * used. This set is unmodifiable. Use the helper functions to set or modify
     * the set.
     *
     * @return the url components specified for this profile (may be empty)
     */
    public final Set<UrlComponents> getUrlComponents() {
        return Collections.unmodifiableSet(urlComponents);
    }

    /**
     * Implements the Comparable<Profile> interface, this is based on the name.
     *
     * @param o
     *            The other profile to compare to.
     * @return this.name.compareTo(otherProfile.name);
     */
    public int compareTo(Profile o) {

        // First ignore case, if they equate, use case.
        int result = name.compareToIgnoreCase(o.name);
        if (result == 0)
            return name.compareTo(o.name);
        else
            return result;
    }

    @Override
    public String toString() {
        return this.name + ": "    + this.algorithm                 + "\n"
               + "Username: "      + this.username                  + "\n"
               + "Alghorithm: "    + this.algorithm                 + "\n"
               + "Lenght: "        + Integer.toString(this.length)  + "\n"
               + "CharacterSet: "  + this.characterSet              + "\n"
               + "LeetType: "      + this.leetType                  + "\n"
               + "LeetLevel: "     + this.leetLevel                 + "\n"
               + "Modifier: "      + this.modifier                  + "\n"
               + "Prefix: "        + this.prefix                    + "\n"
               + "Suffix: "        + this.suffix                    + "\n"
               + "UrlComponents: " + this.urlComponents             + "\n"
               + "isHMAC: "        + this.isHMAC                    + "\n"
               + "joinTopLevel: "  + this.joinTopLevel;
    }

    public static Profile getDefaultProfile() {
            return new Profile(DEFAULT_NAME, "", AlgorithmType.MD5, 8,
                               CharacterSet.getDefaultCharacterset(), LeetType.NONE, LeetLevel.LEVEL1,
                               "", "", "", false, true, getDefaultUrlComponents());
    }

    public String getCustomCharset() {
        return characterSet.getCustomCharset();
    }

    public Boolean isCustomCharsetActive() {
        return characterSet.isCustomCharsetActive();
    }

    public void setCustomCharset(String customCharset) {
        characterSet.setCustomCharset(customCharset);
    }

    public void setCharSetCustomActive(boolean active) {
        characterSet.setCustomCharsetActive(active);
    }

    public Boolean getUrlComponentProtocol() {
        return getUrlComponents().contains(UrlComponents.Protocol);
    }

    public Boolean getUrlComponentSubDomain() {
        return getUrlComponents().contains(UrlComponents.Subdomain);
    }

    public Boolean getUrlComponentDomain() {
        return getUrlComponents().contains(UrlComponents.Domain);
    }

    public Boolean getUrlComponentPortParameters() {
        return getUrlComponents().contains(UrlComponents.PortPathAnchorQuery);
    }

    public void setUrlComponentProtocol(boolean isSet) {
        if(isSet)
            addUrlComponentProtocol();
        else
            removeUrlComponentProtocol();
    }

    public void setUrlComponentSubDomain(boolean isSet) {
        if(isSet)
            addUrlComponentSubDomain();
        else
            removeUrlComponentSubDomain();
    }

    public void setUrlComponentDomain(boolean isSet) {
        if(isSet)
            addUrlComponentDomain();
        else
            removeUrlComponentDomain();
    }

    public void setUrlComponentPortParameters(boolean isSet) {
        if(isSet)
            addUrlComponentPortParameters();
        else
            removeUrlComponentPortParameters();
    }

    public Boolean addUrlComponentProtocol() {
        return this.urlComponents.add(UrlComponents.Protocol);
    }

    public Boolean addUrlComponentSubDomain() {
        return this.urlComponents.add(UrlComponents.Subdomain);
    }

    public Boolean addUrlComponentDomain() {
        return this.urlComponents.add(UrlComponents.Domain);
    }

    public Boolean addUrlComponentPortParameters() {
        return this.urlComponents.add(UrlComponents.PortPathAnchorQuery);
    }

    public Boolean removeUrlComponentProtocol() {
        return this.urlComponents.remove(UrlComponents.Protocol);
    }

    public Boolean removeUrlComponentSubDomain() {
        return this.urlComponents.remove(UrlComponents.Subdomain);
    }

    public Boolean removeUrlComponentDomain() {
        return this.urlComponents.remove(UrlComponents.Domain);
    }

    public Boolean removeUrlComponentPortParameters() {
        return this.urlComponents.remove(UrlComponents.PortPathAnchorQuery);
    }

    public Boolean hasCharSetUppercase() {
        return characterSet.hasUppercase();
    }

    public Boolean hasCharSetLowercase() {
        return characterSet.hasLowercase();
    }

    public Boolean hasCharSetNumbers() {
        return characterSet.hasNumbers();
    }

    public Boolean hasCharSetSymbols() {
        return characterSet.hasSpecialChars();
    }

    public void setCharSetUppercase(boolean isSet) {
        if(isSet)
            characterSet.setUppercase();
        else
            characterSet.removeUppercase();
    }

    public void setCharSetLowercase(boolean isSet) {
        if(isSet)
            characterSet.setLowercase();
        else
            characterSet.removeLowercase();
    }

    public void setCharSetSymbols(boolean isSet) {
        if(isSet)
            characterSet.setSpecialChars();
        else
            characterSet.removeSpecialChars();
    }

    public void setCharSetNumbers(boolean isSet) {
        if(isSet)
            characterSet.setNumbers();
        else
            characterSet.removeNumbers();
    }

    public String getCharSetCustom() {
        return characterSet.getCustomCharset();
    }

    public void setLeetLevel(int intLevel) {
        this.leetLevel = LeetLevel.fromInt(intLevel);
    }

    public void setLeetType(String leetString) {
        this.leetType = LeetType.fromRdfString(leetString);
    }

    public void setAlgorithm(String algorithmString) throws Exception {
        this.algorithm = AlgorithmType.fromRdfString(algorithmString);
    }

    public Boolean isHMAC() {
        return this.isHMAC;
    }
}
