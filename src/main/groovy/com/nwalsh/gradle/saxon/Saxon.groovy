package com.nwalsh.gradle.saxon

import org.gradle.api.artifacts.Configuration

/**
 * Common interface to the SaxonXsltTask and SaxonXQueryTask tasks.
 */
interface Saxon {
  // Only relevent when executing Java as an external process
  /**
   * Add arg to the Java arguments.
   * @param arg The Java argument (e.g., -Xmx... or -Dxxx)
   */
  void javaArg(String arg)

  /**
   * Add args to the Java arguments.
   * @param args The Java arguments (e.g., -Xmx... or -Dxxx)
   */
  void javaArgs(List<String> argList)

  /**
   * Set environment variables before running Java.
   * @param env The environment
   */
  void env(Map<String,String> env)

  /**
   * Set the classath before running Java.
   *
   * <p>AFAICT, this is only possible if you use the 'java' plugin as well.</p>
   * <p>If no classpath is provided, the task will attempt to find a configuration
   * named <code>runtimeClasspath</code> for the classpath.</p>
   *
   * @param config A configuration with a Java classpath
   */
  void classpath(Configuration config)

  /**
   * Run the task by executing a separate Java process?
   *
   * <p>Setting any of the java-specific properties will enable this by default.</p>
   *
   * @param exec Exec an external process?
   */
  void exec(Boolean exec)

  /**
   * Set the main class to instantiate.
   *
   * <p>This defaults to the appropriate class for SaxonXsltTasks and SaxonXQueryTasks.
   *
   * @param name The (fully qualified) class name
   */
  void mainClass(String name)

  /**
   * Add the argument to the XSLT or XQuery invocation.
   *
   * <p>You <em>should not</em> set the <code>-s:</code>, <code>-xsl:</code>, <code>-q:</code>,
   * or <code>-o:</code> options this way!
   *
   * @param arg The argument and its value, if appropriate
   */
  void arg(String arg)

  /**
   * Add the arguments to the XSLT or XQuery invocation.
   *
   * <p>You <em>should not</em> set the <code>-s:</code>, <code>-xsl:</code>, <code>-q:</code>,
   * or <code>-o:</code> options this way!
   *
   * @param args The arguments and their values, if appropriate
   */
  void args(List<String> argList)

  /**
   * Set the input (XML) file to be processed.
   *
   * @param input The input, as a path name or file
   */
  void input(Object input)

  /**
   * Parse the input XML for dependencies?
   *
   * <p>If this value is <code>true</code>, then the input XML will be parsed for XIncludes and any documents
   * found will be added to the task inputs. If the value is a file or string, it's assumed to be the stylesheet
   * that should be run to find dependencies. The stylesheet should return a plain text list of file: URIs.</p>
   *
   * <p>Note that this is performed during configuration so it will be applied to every task on every run.
   * As a matter of performance, it may be wise to limit use of this option if you have many XSLT or XQuery tasks.
   * </p>
   *
   * @param depends The depends setting, either a boolean or a string or file.
   */
  void xmlDepends(Object depends)

  /**
   * Set the output file.
   *
   * @param output The output file
   */
  void output(Object output)

  /**
   * Set the transform or query parameters.
   *
   * @param parameters The parameters
   */
  void parameters(Map<String, String> parameters)

  /**
   * Enable debugging.
   *
   * @param debug Enable debugging?
   */
  void debug(Boolean debug)

  /**
   * Set the base URI for resolving inputs and outputs.
   *
   * @param uri The base URI
   */
  void baseURI(Object uri)
}
