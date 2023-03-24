package com.nwalsh.gradle.saxon

import org.gradle.api.DefaultTask

class SaxonXsltImpl extends SaxonTaskImpl {
  protected Object xslDependFlag = false

  SaxonXsltImpl(DefaultTask task) {
    super(task)
    mainClassName = 'net.sf.saxon.Transform'
    taskType = 'XSLT'
  }

  // ============================================================

  void stylesheet(Object stylesheet) {
    stylesheetFile = resolveResource(stylesheet)
    show("Sty: ${stylesheetFile}")
  }

  void xslDepends(Object depends) {
    xslDependFlag = checkDepends("xslDepends", depends)
    show("Sdp: ${xmlDepends}")
  }

  // ============================================================

  Object getStylesheet() {
    return stylesheetFile
  }

  Object getXslDepends() {
    return xslDependFlag
  }

  // ============================================================

  void run() {
    def cmdline = transformArgs()
    if (execJava) {
      if (debugFlag) {
        println("Exec transform ${cmdline.join(SPACE)}")
      }
      cmdline.add(0, QUIT)
      exec(cmdline)
    } else {
      if (debugFlag) {
        println("Transform ${cmdline.join(SPACE)}")
      }
      cmdline.add(0, QUIT)
      println("invoke(mainClassName, cmdline)")
    }
  }
}
