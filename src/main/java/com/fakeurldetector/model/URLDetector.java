package com.fakeurldetector.model;

/**
 * URLDetector.java
 * 
 * This is the MODEL component in MVC architecture.
 * It contains the core business logic for analyzing URLs and detecting potential threats.
 * 
 * How Rule-Based Detection Works:
 * --------------------------------
 * Each URL is analyzed against multiple security rules.
 * Each rule violation adds points to a "risk score".
 * The total score determines the final classification.
 * 
 * Scoring System:
 * - SAFE:        Score 0-3    (Low risk, trustworthy URL)
 * - SUSPICIOUS:  Score 4-7    (Medium risk, exercise caution)
 * - DANGEROUS:   Score 8+     (High risk, potential phishing/scam)
 */

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class URLDetector {
    
    // ============================================================
    // RISK SCORE CONSTANTS
    // ============================================================
    private static final int AT_SYMBOL_RISK = 3;
    private static final int HTTP_RISK = 2;
    private static final int LONG_URL_RISK = 2;
    private static final int VERY_LONG_URL_RISK = 3;
    private static final int IP_ADDRESS_RISK = 4;
    private static final int SUSPICIOUS_KEYWORD_RISK = 2;
    private static final int BRAND_IMPERSONATION_RISK = 3;
    private static final int URL_SHORTENER_RISK = 3;
    private static final int SUSPICIOUS_TLD_RISK = 3;
    private static final int EXCESSIVE_SUBDOMAINS_RISK = 2;
    private static final int NUMBERS_IN_DOMAIN_RISK = 2;
    private static final int MULTIPLE_HYPHENS_RISK = 2;
    private static final int EXCESSIVE_DOTS_RISK = 2;
    private static final int PORT_IN_URL_RISK = 2;
    private static final int ENCODED_AT_RISK = 3;
    private static final int DOUBLE_SLASH_RISK = 2;
    private static final int EXCESSIVE_PARAMS_RISK = 2;
    private static final int HEX_OBFUSCATION_RISK = 2;
    private static final int MIXED_CASE_DOMAIN_RISK = 1;
    private static final int UNICODE_HOMOGRAPH_RISK = 4;
    
    // Suspicious keywords commonly used in phishing
    private static final String[] SUSPICIOUS_KEYWORDS = {
        "login", "verify", "bank", "secure", "account", "update", "confirm",
        "password", "credential", "wallet", "crypto", "paypal", "signin",
        "free", "prize", "winner", "click", "urgent", "alert", "warning",
        "suspended", "limited", "locked", "verify-now", "sign-in", "auth",
        "authenticate", "billing", "payment", "invoice", "refund", "claim",
        "reward", "bonus", "gift", "offer", "deal", "discount", "coupon",
        "validate", "activate", "restore", "recover", "unlock", "access",
        "admin", "support", "service", "helpdesk", "security", "fraud",
        "unusual", "activity", "suspicious", "unauthorized", "breach",
        "expires", "expiration", "renew", "update-info", "verify-account"
    };
    
    // Brand names commonly impersonated
    private static final String[] BRAND_NAMES = {
        "google", "facebook", "instagram", "twitter", "amazon", "apple",
        "microsoft", "netflix", "paypal", "ebay", "linkedin", "whatsapp",
        "telegram", "snapchat", "tiktok", "youtube", "gmail", "outlook",
        "yahoo", "hotmail", "chase", "wellsfargo", "citibank", "hsbc",
        "sbi", "icici", "hdfc", "axis", "pnb", "boi", "canara",
        "amazon", "flipkart", "myntra", "snapdeal", "paytm", "phonepe",
        "gpay", "googlepay", "bhim", "upi", "ola", "uber", "zomato",
        "swiggy", "irctc", "railway", "passport", "gov", "incometax"
    };
    
    // Known URL shorteners (often used to hide malicious destinations)
    private static final String[] URL_SHORTENERS = {
        "bit.ly", "tinyurl.com", "t.co", "ow.ly", "goo.gl", "short.link",
        "is.gd", "cli.gs", "pic.gd", "DwarfURL.com", "ow.ly", "yfrog.com",
        "migre.me", "ff.im", "tiny.cc", "url4.eu", "tr.im", "twit.ac",
        "su.pr", "twurl.nl", "snipurl.com", "short.to", "BudURL.com",
        "ping.fm", "post.ly", "Just.as", "bkite.com", "snipr.com",
        "flic.kr", "loopt.us", "doiop.com", "redir.ec", "ow.ly",
        "kl.am", "wp.me", "rubyurl.com", "om.ly", "linkd.in",
        "short.ie", "klck.me", "x.co", "bc.vc", "po.st", "v.gd",
        "shorturl.at", "rebrand.ly", "cutt.ly", "short.io", "rb.gy"
    };
    
    // Suspicious TLDs frequently used for malicious sites
    private static final String[] SUSPICIOUS_TLDS = {
        ".tk", ".ml", ".ga", ".cf", ".top", ".xyz", ".club", ".online",
        ".site", ".work", ".date", ".racing", ".win", ".download",
        ".men", ".party", ".review", ".science", ".stream", ".trade",
        ".wang", ".link", ".click", ".gdn", ".bid", ".lol", ".country"
    };
    
    /**
     * Analyzes a URL against multiple security rules and calculates a risk score.
     */
    public static int calculateScore(String url) {
        if (url == null || url.trim().isEmpty()) {
            return 0;
        }
        
        String lowerUrl = url.toLowerCase();
        String domain = extractDomain(url);
        int score = 0;
        
        // RULE 1: @ symbol (credential stuffing / redirect trick)
        if (url.contains("@")) {
            score += AT_SYMBOL_RISK;
        }
        
        // RULE 2: Encoded @ symbol (%40)
        if (lowerUrl.contains("%40")) {
            score += ENCODED_AT_RISK;
        }
        
        // RULE 3: HTTP instead of HTTPS
        if (lowerUrl.startsWith("http://")) {
            score += HTTP_RISK;
        }
        
        // RULE 4: URL length checks
        if (url.length() > 100) {
            score += VERY_LONG_URL_RISK;
        } else if (url.length() > 60) {
            score += LONG_URL_RISK;
        }
        
        // RULE 5: IP address instead of domain
        String ipPattern = "^(http[s]?://)?(\\d{1,3}\\.){3}\\d{1,3}.*";
        if (Pattern.matches(ipPattern, url)) {
            score += IP_ADDRESS_RISK;
        }
        
        // RULE 6: Suspicious keywords
        for (String keyword : SUSPICIOUS_KEYWORDS) {
            if (lowerUrl.contains(keyword)) {
                score += SUSPICIOUS_KEYWORD_RISK;
            }
        }
        
        // RULE 7: Brand impersonation (brand name + suspicious pattern)
        for (String brand : BRAND_NAMES) {
            if (lowerUrl.contains(brand)) {
                // If a known brand is in the URL but the domain doesn't match the brand
                boolean isLegitimate = false;
                String[] legitDomains = getLegitimateDomains(brand);
                for (String legit : legitDomains) {
                    if (domain.contains(legit)) {
                        isLegitimate = true;
                        break;
                    }
                }
                if (!isLegitimate) {
                    score += BRAND_IMPERSONATION_RISK;
                }
            }
        }
        
        // RULE 8: URL shorteners
        for (String shortener : URL_SHORTENERS) {
            if (lowerUrl.contains(shortener)) {
                score += URL_SHORTENER_RISK;
            }
        }
        
        // RULE 9: Suspicious TLDs
        for (String tld : SUSPICIOUS_TLDS) {
            if (lowerUrl.contains(tld)) {
                score += SUSPICIOUS_TLD_RISK;
            }
        }
        
        // RULE 10: Excessive subdomains (e.g., login.secure.bank.evil.com)
        if (!domain.isEmpty()) {
            int dotCount = countOccurrences(domain, ".");
            if (dotCount > 3) {
                score += EXCESSIVE_SUBDOMAINS_RISK;
            }
        }
        
        // RULE 11: Numbers in domain name (e.g., bank-secure-123.com)
        if (domain.matches(".*\\d+.*")) {
            score += NUMBERS_IN_DOMAIN_RISK;
        }
        
        // RULE 12: Multiple consecutive hyphens or dots
        if (url.contains("--") || url.contains("..") || url.contains("-.") || url.contains(".-")) {
            score += MULTIPLE_HYPHENS_RISK;
        }
        
        // RULE 13: Excessive dots in domain
        if (countOccurrences(domain, ".") > 2) {
            score += EXCESSIVE_DOTS_RISK;
        }
        
        // RULE 14: Port number in URL (non-standard ports are suspicious)
        if (Pattern.matches(".*:\\d{2,5}.*", url)) {
            score += PORT_IN_URL_RISK;
        }
        
        // RULE 15: Double slash in path (redirect attempts)
        if (url.replaceFirst("https?://", "").contains("//")) {
            score += DOUBLE_SLASH_RISK;
        }
        
        // RULE 16: Excessive query parameters
        if (countOccurrences(url, "&") > 3 || countOccurrences(url, "?") > 1) {
            score += EXCESSIVE_PARAMS_RISK;
        }
        
        // RULE 17: Hex obfuscation (e.g., %20%2F%3D)
        if (countOccurrences(url, "%") > 3) {
            score += HEX_OBFUSCATION_RISK;
        }
        
        // RULE 18: Mixed case domain (e.g., GoOgLe.com)
        boolean hasUpper = false, hasLower = false;
        for (char c : domain.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
        }
        if (hasUpper && hasLower) {
            score += MIXED_CASE_DOMAIN_RISK;
        }
        
        // RULE 19: Unicode/homograph attack detection
        for (char c : url.toCharArray()) {
            if (c > 127) {
                score += UNICODE_HOMOGRAPH_RISK;
                break;
            }
        }
        
        // RULE 20: Data URI scheme (data:text/html,...)
        if (lowerUrl.startsWith("data:")) {
            score += 8;
        }
        
        // RULE 21: JavaScript protocol
        if (lowerUrl.startsWith("javascript:") || lowerUrl.contains("javascript:")) {
            score += 8;
        }
        
        // Cap the score at a reasonable maximum
        return Math.min(score, 25);
    }
    
    /**
     * Classifies the risk score into a user-friendly category.
     */
    public static String classify(int score) {
        if (score <= 3) {
            return "SAFE";
        } else if (score <= 7) {
            return "SUSPICIOUS";
        } else {
            return "DANGEROUS";
        }
    }
    
    /**
     * Extracts the domain from a URL.
     */
    private static String extractDomain(String url) {
        String domain = url;
        // Remove protocol
        if (domain.contains("://")) {
            domain = domain.substring(domain.indexOf("://") + 3);
        }
        // Remove path and query
        if (domain.contains("/")) {
            domain = domain.substring(0, domain.indexOf('/'));
        }
        // Remove port
        if (domain.contains(":")) {
            domain = domain.substring(0, domain.indexOf(':'));
        }
        // Remove username/password if present
        if (domain.contains("@")) {
            domain = domain.substring(domain.indexOf('@') + 1);
        }
        return domain.toLowerCase();
    }
    
    /**
     * Returns known legitimate domains for a brand.
     */
    private static String[] getLegitimateDomains(String brand) {
        switch (brand) {
            case "google":
            case "gmail":
            case "gpay":
            case "googlepay":
            case "youtube":
                return new String[]{"google.com", "google.co", "gmail.com", "youtube.com", "googleapis.com", "googlesyndication.com", "googleusercontent.com", "google.co.in", "google.co.uk"};
            case "facebook":
                return new String[]{"facebook.com", "fb.com", "fbcdn.net", "facebook.net"};
            case "instagram":
                return new String[]{"instagram.com", "cdninstagram.com"};
            case "twitter":
                return new String[]{"twitter.com", "x.com", "twimg.com"};
            case "amazon":
                return new String[]{"amazon.com", "amazon.in", "amazon.co.uk", "amazon.co.jp", "amazon.de", "amazon.fr", "amazon.ca", "amazon.com.au", "amazonaws.com"};
            case "apple":
                return new String[]{"apple.com", "icloud.com", "me.com", "mac.com"};
            case "microsoft":
            case "outlook":
            case "hotmail":
                return new String[]{"microsoft.com", "outlook.com", "hotmail.com", "live.com", "msn.com", "windows.com", "office.com", "azure.com"};
            case "netflix":
                return new String[]{"netflix.com"};
            case "paypal":
                return new String[]{"paypal.com", "paypal.co.uk", "paypal.me"};
            case "ebay":
                return new String[]{"ebay.com", "ebay.co.uk", "ebay.de"};
            case "linkedin":
                return new String[]{"linkedin.com", "licdn.com"};
            case "whatsapp":
                return new String[]{"whatsapp.com", "wa.me"};
            case "telegram":
                return new String[]{"telegram.org", "t.me"};
            case "snapchat":
                return new String[]{"snapchat.com"};
            case "tiktok":
                return new String[]{"tiktok.com", "tiktokcdn.com"};
            case "yahoo":
                return new String[]{"yahoo.com", "yahoo.co.in", "yahoo.co.uk", "yahoo.co.jp"};
            case "chase":
                return new String[]{"chase.com"};
            case "wellsfargo":
                return new String[]{"wellsfargo.com"};
            case "citibank":
            case "citi":
                return new String[]{"citi.com", "citibank.com", "citibank.co.in"};
            case "hsbc":
                return new String[]{"hsbc.com", "hsbc.co.in", "hsbc.co.uk"};
            case "sbi":
                return new String[]{"sbi.co.in", "onlinesbi.sbi", "sbicard.com"};
            case "icici":
                return new String[]{"icicibank.com", "icici.com"};
            case "hdfc":
                return new String[]{"hdfcbank.com", "hdfc.com"};
            case "axis":
                return new String[]{"axisbank.com"};
            case "pnb":
                return new String[]{"pnbindia.in"};
            case "boi":
                return new String[]{"bankofindia.co.in"};
            case "canara":
                return new String[]{"canarabank.com"};
            case "flipkart":
                return new String[]{"flipkart.com", "flipkart.net"};
            case "myntra":
                return new String[]{"myntra.com"};
            case "snapdeal":
                return new String[]{"snapdeal.com"};
            case "paytm":
                return new String[]{"paytm.com", "paytm.in", "paytm.me"};
            case "phonepe":
                return new String[]{"phonepe.com", "phonepe.in"};
            case "bhim":
                return new String[]{"bhimupi.org.in"};
            case "upi":
                return new String[]{"upi.org.in", "npci.org.in"};
            case "ola":
                return new String[]{"olacabs.com", "ola.com"};
            case "uber":
                return new String[]{"uber.com"};
            case "zomato":
                return new String[]{"zomato.com"};
            case "swiggy":
                return new String[]{"swiggy.com"};
            case "irctc":
            case "railway":
                return new String[]{"irctc.co.in", "indianrail.gov.in"};
            case "passport":
                return new String[]{"passportindia.gov.in"};
            case "gov":
                return new String[]{".gov.in", ".gov", "nic.in"};
            case "incometax":
                return new String[]{"incometaxindia.gov.in", "incometaxindiaefiling.gov.in"};
            default:
                return new String[]{brand + ".com", brand + ".in", brand + ".co", brand + ".org"};
        }
    }
    
    /**
     * Counts occurrences of a substring in a string.
     */
    private static int countOccurrences(String str, String sub) {
        int count = 0;
        int idx = 0;
        while ((idx = str.indexOf(sub, idx)) != -1) {
            count++;
            idx += sub.length();
        }
        return count;
    }
}
