package com.fakeurldetector.model;

/**
 * URLLog.java
 *
 * This is a MODEL class in MVC architecture.
 * It represents a single record from the url_logs database table.
 *
 * Previously, this was an inner class inside URLDao.
 * We moved it to its own file for better:
 * - Separation of Concerns: Model data should not be nested inside DAO
 * - Reusability: Can be used by multiple servlets and JSPs independently
 * - Testability: Easier to unit test as a standalone class
 * - Readability: Clearer project structure
 *
 * Fields match the database table columns:
 *   url_logs(id, url, score, result, checked_at)
 */

import java.sql.Timestamp;

public class URLLog {

    // ============================================================
    // FIELDS (Private - Encapsulated)
    // ============================================================

    /** Unique auto-increment primary key from database */
    private int id;

    /** The URL that was scanned */
    private String url;

    /** The calculated risk score */
    private int score;

    /** Classification: SAFE, SUSPICIOUS, or DANGEROUS */
    private String result;

    /** Timestamp of when the scan was performed */
    private Timestamp checkedAt;

    // ============================================================
    // CONSTRUCTORS
    // ============================================================

    /** Default constructor (required for JavaBean compatibility). */
    public URLLog() {
    }

    /**
     * Parameterized constructor used when mapping database ResultSet rows.
     *
     * @param id        Primary key
     * @param url       Scanned URL
     * @param score     Risk score
     * @param result    Classification
     * @param checkedAt Timestamp from database
     */
    public URLLog(int id, String url, int score, String result, Timestamp checkedAt) {
        this.id = id;
        this.url = url;
        this.score = score;
        this.result = result;
        this.checkedAt = checkedAt;
    }

    // ============================================================
    // GETTERS AND SETTERS (JavaBean convention for JSP EL)
    // ============================================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Timestamp getCheckedAt() {
        return checkedAt;
    }

    public void setCheckedAt(Timestamp checkedAt) {
        this.checkedAt = checkedAt;
    }

    /**
     * Returns lower-case result for CSS class matching.
     * Used in JSP as: ${log.resultCssClass}
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
        return "URLLog{id=" + id + ", url='" + url + "', score=" + score +
               ", result='" + result + "', checkedAt=" + checkedAt + "}";
    }
}

