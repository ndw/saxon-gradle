package com.nwalsh.gradle.saxon

/**
 * The interface to the SaxonXQueryTask.
 */
interface SaxonXQuery extends Saxon {
  /**
   * Set the XQuery file to be used for transformation.
   *
   * <p>Note: you must specify either a file or a query string, not both.
   *
   * @param xq The XQuery file, as a path name or file
   */
  void query(Object xq)

  /**
   * Set the XQuery to be used for transformation as a literal string.
   *
   * <p>Note: you must specify either a file or a query string, not both.
   *
   * @param xq The XQuery, as a literal string
   */
  void queryString(String xq)
}
