package com.fakeurldetector.model;

/**
 * URLResult.java
 *
 * This is a MODEL class in MVC architecture.
 * It encapsulates the result of a URL scan operation.
 *
 * Why use a dedicated Result Model?
 * ---------------------------------
 * 1. Encapsulation: Groups related data (url, score, result) into one object
 * 2. Clean Data Transfer: Servlet passes ONE object to JSP instead of multiple attributes
 * 3. Type Safety: Strong typing prevents accidental misuse
 * 4. Maintainability: Easy to add new fields (e.g., scanTime, detailedReport) later
 *
 * In JSP, we access it using Expression Language (EL):
 *   ${urlResult.url}    -> getUrl()
 *   ${urlResult.score}  -> getScore()
 *   ${urlResult.result} -> getResult()
 *
 * This follows JavaBean convention: private fields + public getters/setters.
 */

public class URLResult {

    // ============================================================
    // FIELDS (Private - Encapsulated)
    // ============================================================

    /** The URL that was analyzed */
    private String url;

    /** The calculated risk score (0 = safe, higher = more dangerous) */
    private int score;

    /** The classification: SAFE, SUSPICIOUS, or DANGEROUS */
    private String result;

    // ============================================================
    // CONSTRUCTORS
    // ============================================================

    /**
     * Default constructor (required for JavaBean compatibility and frameworks).
     */
    public URLResult() {
    }

    /**
     * Parameterized constructor to create a result object quickly.
     *
     * @param url    The scanned URL
     * @param score  The calculated risk score
     * @param result The classification result
     */
    public URLResult(String url, int score, String result) {
        this.url = url;
        this.score = score;
        this.result = result;
    }

    // ============================================================
    // GETTERS AND SETTERS
    // ============================================================
    // JavaBean convention: methods named getXxx() and setXxx()
    // Expression Language (EL) in JSP uses these to read values.

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    /**
     * Returns a lower-case version of the result for CSS class names.
     * Example: "SAFE" -> "safe", "SUSPICIOUS" -> "suspicious"
     *
     * Used in JSP as: ${urlResult.resultCssClass}
     */
    public String getResultCssClass() {
        if (result == null) {
            return "";
        }
        return result.toLowerCase();
    }

    // ============================================================
    // toString() - helpful for debugging
    // ============================================================

    @Override
    public String toString() {
        return "URLResult{url='" + url + "', score=" + score + ", result='" + result + "'}";
    }
}

