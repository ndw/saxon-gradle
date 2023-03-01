package com.nwalsh.gradle.saxon

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException

class SaxonXQueryImpl extends SaxonTaskImpl {
  SaxonXQueryImpl(DefaultTask task) {
    super(task)
    mainClassName = 'net.sf.saxon.Query'
    taskType = 'XQuery'
  }

  // ============================================================

  void query(Object xq) {
    xqueryFile = resolveResource(xq)
    show("Xqy: ${xqueryFile}")
  }

  void queryString(String xq) {
    xqueryString = xq
    show("Xqs: ${queryString}")
  }

  // ============================================================

  Object getQueryFile() {
    return xqueryFile
  }

  String getQueryString() {
    return xqueryString
  }

  // ============================================================

  void run() {
    if (queryFile == null && queryString == null) {
      throw new GradleException("You must specify a query")
    }
    if (queryFile != null && queryString != null) {
      throw new GradleException("You must specify either a query or a queryString")
    }

    def cmdline = transformArgs()
    if (execJava) {
      if (debugFlag) {
        println("Exec query ${cmdline.join(SPACE)}")
      }
      cmdline.add(0, QUIT)
      exec(cmdline)
    } else {
      if (debugFlag) {
        println("Query ${cmdline.join(SPACE)}")
      }
      cmdline.add(0, QUIT)
      invoke(mainClassName, cmdline)
    }
  }
}
