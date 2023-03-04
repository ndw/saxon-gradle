package com.nwalsh.gradle.saxon

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFiles
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction

/**
 * The SaxonXsltTask runs an XSLT transformation.
 *
 * <p>The task interface and the implementation are separated in order to "hide" utility
 * methods from the task closure. It seems like there should be a better way, but I wasn't
 * able to find it.</p>
 */
class SaxonXsltTask extends DefaultTask implements SaxonXslt {
  private final SaxonXsltImpl impl = new SaxonXsltImpl(this)
  protected List<String> cachedXml = null
  protected List<String> cachedXsl = null

  void javaArg(String arg) {
    impl.javaArg(arg)
  }

  void javaArgs(List<String> argList) {
    impl.javaArgs(argList)
  }

  void workingDir(Object dir) {
    impl.workingDir(dir)
  }

  void env(Map<String,String> env) {
    impl.env(env)
  }

  void classpath(Configuration config) {
    impl.classpath(config)
  }
  
  void exec(Boolean exec) {
    impl.exec(exec)
  }

  void mainClass(String name) {
    impl.mainClass(name)
  }

  void arg(String arg) {
    impl.arg(arg)
  }

  void args(List<String> argList) {
    impl.args(argList)
  }

  void input(Object input) {
    impl.input(input)
  }

  void xmlDepends(Object depends) {
    impl.xmlDepends(depends)
  }

  void output(Object output) {
    impl.output(output)
  }

  void parameters(Map<String, String> parameters) {
    impl.parameters(parameters)
  }

  void debug(Boolean debug) {
    impl.debug(debug)
  }

  void baseURI(Object uri) {
    impl.baseURI(uri)
  }

  void stylesheet(Object stylesheet) {
    impl.stylesheet(stylesheet)
  }

  void xslDepends(Object depends) {
    impl.xslDepends(depends)
  }

  @InputFiles
  @SkipWhenEmpty
  FileCollection getInputFiles() {
    FileCollection files = project.files([])
    Object xmlDepends = impl.getXmlDepends()
    Object xslDepends = impl.getXslDepends()
    File inputFile = null

    // This method is called twice; we cache the results to avoid doing the work twice

    inputFile = impl.resolveFile(impl.getInput())
    if (inputFile != null) {
      if (xmlDepends != null && !(xmlDepends instanceof Boolean && !xmlDepends)) {
        if (cachedXml == null) {
          cachedXml = impl.xmlDependsOn("${inputFile}")
        }
        files += project.files(cachedXml as String[])
      } else {
        files += project.files(inputFile)
      }
    }

    inputFile = impl.resolveFile(impl.getStylesheet())
    if (inputFile != null) {
      if (xslDepends != null && !(xslDepends instanceof Boolean && !xslDepends)) {
        if (cachedXsl == null) {
          cachedXsl = impl.xslDependsOn("${inputFile}")
        }
        files += project.files(cachedXsl as String[])
      } else {
        files += project.files(inputFile)
      }
    }

    return files
  }

  @OutputFiles
  @Optional
  FileCollection getOutputFiles() {
    FileCollection files = project.files([])
    File outputFile = impl.resolveFile(impl.getOutput())
    if (outputFile != null) {
      files += project.files(outputFile)
    }
    return files
  }

  @TaskAction
  void run() {
    impl.run()
    cachedXml = null
    cachedXsl = null
  }
}
