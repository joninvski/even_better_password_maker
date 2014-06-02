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

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.security.MessageDigest;

import org.daveware.passwordmaker.Profile.UrlComponents;


/**
 * This class is used to generate passwords from a master password and an
 * profile.
 *
 * @author Dave Marotti
 */
public class PasswordMaker {

    private static Pattern urlRegex = Pattern.compile("([^:\\/\\/]*:\\/\\/)?([^:\\/]*)([^#]*).*");

    /**
     * Maps an array of characters to another character set.
     *
     * This is the magic which allows an encrypted password to be mapped into a
     * a specific character set.
     *
     * @param input
     *            The array of characters to map.
     * @param encoding
     *            The list of characters to map to.
     * @param trim
     *            Whether to trim leading zeros ... I think.
     * @return The mapped string.
     * @throws Exception
     *             On odd length!?
     */
    public static SecureCharArray rstr2any(char[] input, String encoding)
        throws Exception {
        int length = input.length;
        int divisor;
        int full_length;
        int[] dividend;
        int[] remainders;
        int dividend_length;
        int i, j;

        int outputPosition = 0;
        SecureCharArray output = new SecureCharArray();

        // can't handle odd lengths
        if ((length % 2) != 0) {
            return output;
        }

        divisor = encoding.length();
        dividend_length = (int) Math.ceil((double) length / 2.0);
        dividend = new int[dividend_length];
        for (i = 0; i < dividend_length; i++) {
            dividend[i] = (((int) input[i * 2]) << 8) | ((int) input[i * 2 + 1]);
        }

        full_length = (int) Math.ceil(
                (double) length * 8 /
                (Math.log((double) encoding.length()) / Math.log((double) 2)));
        remainders = new int[full_length];

        for (j = 0; j < full_length; j++) {
            int[] quotient;
            int quotient_length = 0;
            int qCounter = 0;
            int x = 0;

            quotient = new int[dividend_length];
            for (i = 0; i < dividend_length; i++) {
                int q;
                x = (x << 16) + dividend[i];
                q = (int) Math.floor((double) x / divisor);
                x -= q * divisor;
                if (quotient_length > 0 || q > 0) {
                    quotient[qCounter++] = q;
                    quotient_length++;
                }
            }
            remainders[j] = x;
            dividend_length = quotient_length;
            dividend = quotient;
        }

        if (output.size() < full_length)
            output.resize(full_length, false);

        for (i = full_length - 1; i >= 0; i--) {
            output.setCharAt(outputPosition++, encoding.charAt(remainders[i]));
        }

        return output;
    }

    public static final String getModifiedInputText(final String inputText,
            final Profile profile) {
        final Set<UrlComponents> uriComponents = profile.getUrlComponents();
        if (uriComponents.isEmpty()) {
            return "";
        }
        Matcher matcher = urlRegex.matcher(inputText);
        if (!matcher.matches())
            return inputText;
        String protocol = matcher.group(1);
        String domainText = matcher.group(2);
        String portPath = matcher.group(3);
        if (protocol == null)
            protocol = "";
        if (domainText == null)
            domainText = "";
        if (portPath == null)
            portPath = "";

        StringBuilder retVal = new StringBuilder(inputText.length());
        if (uriComponents.contains(UrlComponents.Protocol) && protocol.length() > 0) {
            retVal.append(protocol);
        }

        if (domainText != null) {

            final Boolean joinSpecialTopDomains = profile.getJoinTopLevel();
            final String topDomainText = calculateDomain(domainText, joinSpecialTopDomains);
            final String subDomainText = calculateSubDomain(domainText, topDomainText);

            final boolean hasSubDomain = uriComponents.contains(UrlComponents.Subdomain) && subDomainText != "";
            if (hasSubDomain) {
                retVal.append(subDomainText);
            }
            if (uriComponents.contains(UrlComponents.Domain)) {
                if (hasSubDomain)
                    retVal.append('.');
                retVal.append(topDomainText);
            }
        }

        if (uriComponents.contains(UrlComponents.PortPathAnchorQuery) && portPath.length() > 0) {
            retVal.append(portPath);
        }
        return retVal.toString();
    }

    private static String calculateSubDomain(String domainText, String topDomainText) {
        int lengthToRemove = topDomainText.length();
        int rightmostSubdomainIndex = domainText.length() - lengthToRemove;
        String subdomain = domainText.substring(0, rightmostSubdomainIndex);

        return subdomain;
    }

    private static String calculateTopDomain(String domainText, Boolean joinSpecialTopDomains) {
        if(joinSpecialTopDomains) {
            for(String topDomain : TOPLEVELDOMAINS) {
                if(domainText.endsWith(topDomain)) {
                    return topDomain;
                }
            }
        }

        // If it is not a special top domain then it is just the substring after the last dot
        int lastDot = domainText.lastIndexOf('.');
        String domain = domainText.substring(lastDot + 1);
        return domain;
    }

    private static String calculateMiddleDomain(String domainText, String top) {
        int lengthToRemove = top.length() + 1; // The +1 is for the dot
        int rightmostSubdomainIndex = domainText.length() - lengthToRemove;
        String domainAndSubDomain = domainText.substring(0, rightmostSubdomainIndex);

        int lastDot = domainAndSubDomain.lastIndexOf('.');
        String middleDomain = domainAndSubDomain.substring(lastDot + 1);

        return middleDomain;
    }

    private static String calculateDomain(String domainText, Boolean joinSpecialTopDomains) {
        String top = calculateTopDomain(domainText, joinSpecialTopDomains);
        String middle = calculateMiddleDomain(domainText, top);

        if (middle != "")
            return middle + '.' + top;
        else
            return top;
    }

    /**
     * Generates a hash of the master password with settings from the profile.
     *
     * @param masterPassword
     *            The password to use as a key for the various algorithms.
     * @param profile
     *            The profile with the specific settings for the hash.
     * @param inputText
     *            The text to use as the input into the password maker algorithm
     * @return A SecureCharArray with the hashed data.
     * @throws Exception
     *             if something bad happened.
     */
    public static SecureCharArray makePassword(SecureCharArray masterPassword, Profile profile, final String inputText)
        throws PasswordGenerationException {

        LeetLevel leetLevel = profile.getLeetLevel();
        int length = profile.getLength();
        SecureCharArray output = null;
        SecureCharArray data = null;

        try {
            if (profile.getCompleteCharacterSet().length() < 2)
                throw new Exception(
                        "profile contains a character set that is too short: " +
                        profile.getCompleteCharacterSet());

            data = new SecureCharArray(getModifiedInputText(inputText, profile)
                    + profile.getUsername() + profile.getModifier());

            // Use leet before hashing
            if (profile.getLeetType() == LeetType.BEFORE
                    || profile.getLeetType() == LeetType.BOTH) {
                LeetEncoder.leetConvert(leetLevel, masterPassword);
                LeetEncoder.leetConvert(leetLevel, data);
                    }

            // Perform the actual hashing
            output = hashTheData(masterPassword, data, profile);

            // Use leet after hashing
            if (profile.getLeetType() == LeetType.AFTER
                    || profile.getLeetType() == LeetType.BOTH) {
                LeetEncoder.leetConvert(leetLevel, output);
                    }

            // Apply the prefix
            if (profile.getPrefix().length() > 0) {
                SecureCharArray prefix = new SecureCharArray(
                        profile.getPrefix());
                output.prepend(prefix);
                prefix.erase();
            }

            // Handle the suffix
            output.resize(length, true);
            if (profile.getSuffix().length() > 0) {
                SecureCharArray suffix = new SecureCharArray(
                        profile.getSuffix());

                // If the suffix is larger than the entire password (not smart),
                // then
                // just replace the output with a section of the suffix that
                // fits
                if (length < suffix.size()) {
                    output.replace(suffix);
                    output.resize(length, true);
                }
                // Otherwise insert the prefix where it fits
                else {
                    output.resize(length - suffix.size(), true);
                    output.append(suffix);
                }

                suffix.erase();
            }
        } catch (Exception e) {
            if (output != null)
                output.erase();
            throw new PasswordGenerationException(e);
        } finally {
            // not really needed... but here for completeness
            if (data != null)
                data.erase();
        }

        return output;
    }

    /**
     * Intermediate step of generating a password. Performs constant hashing
     * until the resulting hash is long enough.
     *
     * @param masterPassword
     *            You should know by now.
     * @param data
     *            Not much has changed.
     * @param profile
     *            A donut?
     * @return A suitable hash.
     * @throws Exception
     *             if we ran out of donuts.
     */
    private static SecureCharArray hashTheData(SecureCharArray masterPassword, SecureCharArray data, Profile profile) throws Exception {
        SecureCharArray output = new SecureCharArray();
        SecureCharArray secureIteration = new SecureCharArray();
        SecureCharArray intermediateOutput = null;
        SecureCharArray interIntermediateOutput = null;
        int count = 0;
        int length = profile.getLength();

        try {
            while (output.size() < length) {
                if (count == 0) {
                    intermediateOutput = runAlgorithm(masterPassword, data,
                            profile);
                } else {
                    // add ye bit'o chaos
                    secureIteration.replace(masterPassword);
                    secureIteration.append(new SecureCharArray("\n"));
                    secureIteration.append(new SecureCharArray(Integer
                                .toString(count)));

                    interIntermediateOutput = runAlgorithm(secureIteration,
                            data, profile);
                    intermediateOutput.append(interIntermediateOutput);
                    interIntermediateOutput.erase();

                    secureIteration.erase();
                }
                output.append(intermediateOutput);
                intermediateOutput.erase();

                count++;
            }
        } catch (Exception e) {
            if (output != null)
                output.erase();
            throw e;
        } finally {
            if (intermediateOutput != null)
                intermediateOutput.erase();
            if (interIntermediateOutput != null)
                interIntermediateOutput.erase();
            if (secureIteration != null)
                secureIteration.erase();
        }

        return output;
    }

    /**
     * This performs the actual hashing. It obtains an instance of the hashing
     * algorithm and feeds in the necessary data.
     *
     * @param masterPassword
     *            The master password to use as a key.
     * @param data
     *            The data to be hashed.
     * @param profile
     *            The profile with the hash settings to use.
     * @return A SecureCharArray of the hash.
     * @throws Exception
     *             if something bad happened.
     */
    private static SecureCharArray runAlgorithm(SecureCharArray masterPassword, SecureCharArray data, Profile profile) throws Exception {
        SecureCharArray output = null;
        SecureCharArray digestChars = null;
        SecureByteArray masterPasswordBytes = null;
        SecureByteArray dataBytes = null;

        try {
            masterPasswordBytes = new SecureByteArray(masterPassword.getData());
            dataBytes = new SecureByteArray(data.getData());

            if (profile.isHMAC()) {
                Mac mac;
                String algoName = "HMAC" + profile.getAlgorithm().getName();
                mac = Mac.getInstance(algoName, "SC");
                mac.init(new SecretKeySpec(masterPasswordBytes.getData(), algoName));
                mac.reset();
                mac.update(dataBytes.getData());
                digestChars = new SecureCharArray(mac.doFinal());
            } else {
                dataBytes.prepend(masterPasswordBytes);
                MessageDigest md = MessageDigest.getInstance(profile.getAlgorithm().getName(), "SC");
                digestChars = new SecureCharArray(md.digest(dataBytes.getData()));
            }

            output = rstr2any(digestChars.getData(), profile.getCompleteCharacterSet());
        } catch (Exception e) {
            if (output != null)
                output.erase();
            throw e;
        } finally {
            if (masterPasswordBytes != null)
                masterPasswordBytes.erase();
            if (dataBytes != null)
                dataBytes.erase();
            if (digestChars != null)
                digestChars.erase();
        }

        return output;
    }

    static String[] TOPLEVELDOMAINS = {
        "aland.fi", "wa.edu.au", "nsw.edu.au", "vic.edu.au", "csiro.au",
        "conf.au", "info.au", "oz.au", "telememo.au", "sa.edu.au",
        "nt.edu.au", "tas.edu.au", "act.edu.au", "wa.gov.au", "nsw.gov.au",
        "vic.gov.au", "qld.gov.au", "sa.gov.au", "tas.gov.au", "nt.gov.au",
        "act.gov.au", "archie.au", "edu.au", "gov.au", "id.au", "org.au",
        "asn.au", "net.au", "com.au", "qld.edu.au", "com.bb", "net.bb",
        "org.bb", "gov.bb", "agr.br", "am.br", "art.br", "edu.br",
        "com.br", "coop.br", "esp.br", "far.br", "fm.br", "g12.br",
        "gov.br", "imb.br", "ind.br", "inf.br", "mil.br", "net.br",
        "org.br", "psi.br", "rec.br", "srv.br", "tmp.br", "tur.br",
        "tv.br", "etc.br", "adm.br", "adv.br", "arq.br", "ato.br",
        "bio.br", "bmd.br", "cim.br", "cng.br", "cnt.br", "ecn.br",
        "eng.br", "eti.br", "fnd.br", "fot.br", "fst.br", "ggf.br",
        "jor.br", "lel.br", "mat.br", "med.br", "mus.br", "not.br",
        "ntr.br", "odo.br", "ppg.br", "pro.br", "psc.br", "qsl.br",
        "slg.br", "trd.br", "vet.br", "zlg.br", "nom.br", "ab.ca",
        "bc.ca", "mb.ca", "nb.ca", "nf.ca", "nl.ca", "ns.ca", "nt.ca",
        "nu.ca", "on.ca", "pe.ca", "qc.ca", "sk.ca", "yk.ca", "com.cd",
        "net.cd", "org.cd", "ac.cn", "com.cn", "edu.cn", "gov.cn",
        "net.cn", "org.cn", "ah.cn", "bj.cn", "cq.cn", "fj.cn",
        "gd.cn", "gs.cn", "gz.cn", "gx.cn", "ha.cn", "hb.cn", "he.cn",
        "hi.cn", "hl.cn", "hn.cn", "jl.cn", "js.cn", "jx.cn", "ln.cn",
        "nm.cn", "nx.cn", "qh.cn", "sc.cn", "sd.cn", "sh.cn", "sn.cn",
        "sx.cn", "tj.cn", "xj.cn", "xz.cn", "yn.cn", "zj.cn", "co.ck",
        "org.ck", "edu.ck", "gov.ck", "net.ck", "ac.cr", "co.cr",
        "ed.cr", "fi.cr", "go.cr", "or.cr", "sa.cr", "eu.int", "ac.in",
        "co.in", "edu.in", "firm.in", "gen.in", "gov.in", "ind.in",
        "mil.in", "net.in", "org.in", "res.in", "ac.id", "co.id",
        "or.id", "net.id", "web.id", "sch.id", "go.id", "mil.id",
        "war.net.id", "ac.nz", "co.nz", "cri.nz", "gen.nz", "geek.nz",
        "govt.nz", "iwi.nz", "maori.nz", "mil.nz", "net.nz", "org.nz",
        "school.nz", "aid.pl", "agro.pl", "atm.pl", "auto.pl", "biz.pl",
        "com.pl", "edu.pl", "gmina.pl", "gsm.pl", "info.pl", "mail.pl",
        "miasta.pl", "media.pl", "nil.pl", "net.pl", "nieruchomosci.pl",
        "nom.pl", "pc.pl", "powiat.pl", "priv.pl", "realestate.pl",
        "rel.pl", "sex.pl", "shop.pl", "sklep.pl", "sos.pl", "szkola.pl",
        "targi.pl", "tm.pl", "tourism.pl", "travel.pl", "turystyka.pl",
        "com.pt", "edu.pt", "gov.pt", "int.pt", "net.pt", "nome.pt",
        "org.pt", "publ.pt", "com.tw", "club.tw", "ebiz.tw", "game.tw",
        "gov.tw", "idv.tw", "net.tw", "org.tw", "av.tr", "bbs.tr",
        "bel.tr", "biz.tr", "com.tr", "dr.tr", "edu.tr", "gen.tr",
        "gov.tr", "info.tr", "k12.tr", "mil.tr", "name.tr", "net.tr",
        "org.tr", "pol.tr", "tel.tr", "web.tr", "ac.za", "city.za",
        "co.za", "edu.za", "gov.za", "law.za", "mil.za", "nom.za",
        "org.za", "school.za", "alt.za", "net.za", "ngo.za", "tm.za",
        "web.za", "bourse.za", "agric.za", "cybernet.za", "grondar.za",
        "iaccess.za", "inca.za", "nis.za", "olivetti.za", "pix.za",
        "db.za", "imt.za", "landesign.za", "co.kr", "pe.kr", "or.kr",
        "go.kr", "ac.kr", "mil.kr", "ne.kr", "chiyoda.tokyo.jp",
        "tcvb.or.jp", "ac.jp", "ad.jp", "co.jp", "ed.jp", "go.jp",
        "gr.jp", "lg.jp", "ne.jp", "or.jp", "com.mx", "net.mx",
        "org.mx", "edu.mx", "gob.mx", "ac.uk", "co.uk", "gov.uk",
        "ltd.uk", "me.uk", "mod.uk", "net.uk", "nic.uk", "nhs.uk",
        "org.uk", "plc.uk", "police.uk", "sch.uk", "ak.us", "al.us",
        "ar.us", "az.us", "ca.us", "co.us", "ct.us", "dc.us", "de.us",
        "dni.us", "fed.us", "fl.us", "ga.us", "hi.us", "ia.us",
        "id.us", "il.us", "in.us", "isa.us", "kids.us", "ks.us",
        "ky.us", "la.us", "ma.us", "md.us", "me.us", "mi.us", "mn.us",
        "mo.us", "ms.us", "mt.us", "nc.us", "nd.us", "ne.us", "nh.us",
        "nj.us", "nm.us", "nsn.us", "nv.us", "ny.us", "oh.us", "ok.us",
        "or.us", "pa.us", "ri.us", "sc.us", "sd.us", "tn.us", "tx.us",
        "ut.us", "vt.us", "va.us", "wa.us", "wi.us", "wv.us", "wy.us",
        "com.ua", "edu.ua", "gov.ua", "net.ua", "org.ua", "cherkassy.ua",
        "chernigov.ua", "chernovtsy.ua", "ck.ua", "cn.ua", "crimea.ua",
        "cv.ua", "dn.ua", "dnepropetrovsk.ua", "donetsk.ua", "dp.ua",
        "if.ua", "ivano-frankivsk.ua", "kh.ua", "kharkov.ua", "kherson.ua",
        "kiev.ua", "kirovograd.ua", "km.ua", "kr.ua", "ks.ua", "lg.ua",
        "lugansk.ua", "lutsk.ua", "lviv.ua", "mk.ua", "nikolaev.ua",
        "od.ua", "odessa.ua", "pl.ua", "poltava.ua", "rovno.ua", "rv.ua",
        "sebastopol.ua", "sumy.ua", "te.ua", "ternopil.ua", "vinnica.ua",
        "vn.ua", "zaporizhzhe.ua", "zp.ua", "uz.ua", "uzhgorod.ua",
        "zhitomir.ua", "zt.ua", "ac.il", "co.il", "org.il", "net.il",
        "k12.il", "gov.il", "muni.il", "idf.il", "co.im", "org.im", "com.sg"
    };
}
