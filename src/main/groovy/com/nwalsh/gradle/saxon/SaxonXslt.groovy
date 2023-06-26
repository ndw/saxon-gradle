package com.nwalsh.gradle.saxon

/**
 * The interface to the SaxonXsltTask.
 */
interface SaxonXslt extends Saxon {
  /**
   * Set the stylesheet file to be used for transformation.
   *
   * @param stylesheet The stylesheet, as a path name or file
   */
  void stylesheet(Object stylesheet)

  /**
   * Set the export file.
   *
   * @param export The export file
   */
  void export(Object export)

  /**
   * Parse the stylesheet for dependencies?
   *
   * <p>If this value is <code>true</code>, then the stylesheet will be parsed for xsl:import and xsl:include elements.
   * Any modules found will be added to the task inputs. If the value is a file or string, it's assumed to be the stylesheet
   * that should be run to find dependencies. The stylesheet should return a plain text list of file: URIs.</p>
   *
   * <p>Note that this is performed during configuration so it will be applied to every task on every run.
   * As a matter of performance, it may be wise to limit use of this option if you have many XSLT or XQuery tasks.
   * </p>
   *
   * @param depends The depends setting, either a boolean or a string or file.
   */
  void xslDepends(Object depends)
}
