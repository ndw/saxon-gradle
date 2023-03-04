package com.nwalsh.gradle.saxon

import org.gradle.api.DefaultTask
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.TaskExecutionException
import org.gradle.api.GradleException

/**
 * The bulk of the actual implementation. This is the base class for the
 * SaxonXsltImpl and SaxonXQueryImpl classes.
 */
class SaxonTaskImpl {
  protected static final String QUIT = '-quit:off'
  protected static final String SPACE = ' '

  protected final List<String> javaOptions = []
  protected final List<String> saxonOptions = []

  protected boolean debugStarted = false
  protected boolean debugFlag = false
  protected URI theBaseURI = null
  protected boolean execJava = false
  protected final List<String> javaClasspath = []
  protected Object xmlDependFlag = false

  // This is a mixture of XSLT and XQuery arguments; don't cross the streams!
  protected Object inputFile = null
  protected Object outputFile = null
  protected Object stylesheetFile = null
  protected Object xqueryFile = null
  protected Object xqueryString = null
  protected final Map<String,String> stylesheetParameters = [:]

  protected final Map<String,String> javaEnv = [:]
  protected File workdir = null
  protected String mainClassName = null
  protected String taskType = null
  protected final DefaultTask task

  private SaxonTaskImpl() {
    // no can do
  }

  protected SaxonTaskImpl(DefaultTask task) {
    this.task = task
    File cwd = new File(System.getProperty("user.dir"))
    theBaseURI = cwd.toURI()
  }

  // ============================================================

  void javaArg(String arg) {
    show("Jrg: ${arg}")
    javaOptions.add(arg)
    execJava = true
  }

  void javaArgs(List<String> argList) {
    for (anarg in argList) {
      javaArg(anarg)
    }
  }

  void workingDir(Object dir) {
    if (dir instanceof File) {
      workdir = dir
    } else {
      workdir = new File("${dir}")
    }
    if (!workdir.exists() && workdir.isDirectory()) {
      throw new GradleException("Cannot set workingDir to '${dir}'")
    }
    show("Dir: ${workdir}")
    execJava = true
  }

  void env(Map<String,String> dir) {
    for (name in dir.keySet()) {
      name = name.toString()
      javaEnv[name] = (dir[name]).toString()
      show("Env: ${name}=${javaEnv[name]}")
    }
    execJava = true
  }

  void classpath(Configuration config) {
    config.each { path ->
      javaClasspath.add(path)
    }
    execJava = true
    show("Opt: classpath=${javaClasspath.join(':')}")
  }

  void exec(Boolean exec) {
    execJava = exec
    show("Opt: exec=${execJava}")
  }

  void mainClass(String name) {
    mainClassName = name
    show("Cls: ${name}")
  }

  void arg(String arg) {
    show("Arg: ${arg}")
    saxonOptions.add(arg)
  }

  void args(List<String> argList) {
    for (anarg in argList) {
      arg(anarg)
    }
  }

  void input(Object input) {
    this.inputFile = resolveResource(input)
    show("Inp: ${this.input}")
  }

  void xmlDepends(Object depends) {
    xmlDependFlag = checkDepends("xmlDepends", depends)
    show("Xdp: ${xmlDepends}")
  }

  void output(Object output) {
    this.outputFile = resolveResource(output)
    show("Out: ${this.output}")
  }

  void parameters(Map<String, String> parameters) {
    // Attempt to avoid weird side effects from pass by reference
    parameters.collect { name, value ->
      stylesheetParameters[name] = value
    }
  }

  void debug(Boolean debug) {
    this.debugFlag = debug
    // We don't show this change. If you put it at the end of a task
    // configuration, then you'll see debug information for the execution
    // phase but not the config phase. If you put it first, you can
    // get debug information for both.
  }

  void baseURI(Object uri) {
    if (uri instanceof URI) {
      theBaseURI = uri
    } else if (uri instanceof File) {
      theBaseURI = uri.toURI()
    } else {
      theBaseURI = theBaseURI.resolve("${uri}")
    }
    show("Opt: baseURI=${theBaseURI}")
  }

  // ============================================================

  Object getInput() {
    return inputFile
  }

  Object getXmlDepends() {
    return xmlDependFlag
  }

  Object getOutput() {
    return outputFile
  }

  // ============================================================

  protected void show(String message) {
    if (debugFlag) {
      if (!debugStarted) {
        println("Configuring ${taskType} task ${task.getName()}:")
        debugStarted = true
      }
      println(message)
    }
  }

  protected Object checkDepends(String name, Object depends) {
    if (depends instanceof URI) {
      depends = depends.getPath()
    }
    if (depends instanceof String) {
      depends = new File(depends)
    }
    if (depends instanceof File) {
      if (depends.exists()) {
        return depends.toString()
      }
      throw new GradleException("Cannot ${depends} for ${name}, file does not exist")
    }
    if (depends instanceof Boolean) {
      return depends
    }
    throw new GradleException("Cannot set ${name} with ${depends.getClass()}")
  }

  protected Object resolveResource(Object input) {
    if (input == null) {
      return null
    }

    if (input instanceof File) {
      return input
    }

    if (input instanceof URI) {
      return input
    }

    try {
      URI uri = new URI(input)
      if (uri.isAbsolute()) {
        return uri
      }
    } catch (URISyntaxException ex) {
      // nop
    }

    URI uri = theBaseURI.resolve("${input}");
    if (uri.getScheme() == "file") {
      return new File(uri.getPath())
    }

    return uri
  }

  protected File resolveFile(Object input) {
    if (input instanceof File) {
      return input
    }

    if (input instanceof URI) {
      if (input.getScheme() == "file") {
        return new File(input)
      }
    }

    return null
  }

  // ============================================================

  protected List<String> transformArgs() {
    def parameters = []
    parameters.addAll(saxonOptions)

    if (inputFile != null) {
      String inputName = input.toString()
      if (inputName.endsWith('.json') or inputName.endsWith('.js')) {
        parameters.add("-json:${input}")
      } else {
        parameters.add("-s:${input}")
      }
    }
    if (stylesheetFile != null) {
      parameters.add("-xsl:${stylesheetFile}")
    }
    if (xqueryFile != null) {
      parameters.add("-q:${xqueryFile}")
    }
    if (xqueryString != null) {
      parameters.add("-qs:${xqueryString}")
    }
    if (outputFile != null) {
      parameters.add("-o:${output}")
    }

    stylesheetParameters.collect { name, value ->
      parameters.add("${name}=${value}")
    }

    return parameters
  }

  @SuppressWarnings('CatchException')
  private List<String> findDependsOn(String prefix, Object dependSetting, String defaultStylesheet, String sourceFile) {
    def dependencies = new ArrayList<String>()

    if (dependSetting instanceof Boolean && !dependSetting) {
      dependSetting.add(sourceFile)
      return dependencies
    }

    try {
      File depends;
      File urilist = File.createTempFile("saxon-txt-depends", ".txt")
      urilist.deleteOnExit()

      if (dependSetting instanceof Boolean) {
        depends = File.createTempFile("saxon-xsl-depends", ".xsl")
        depends.deleteOnExit()
        // We can't use -xsl:classpath: because the thread class loader doesn't include
        // the task resources. I think it should, but no one asked me.
        InputStream xslin = getClass().getResourceAsStream(defaultStylesheet)
        OutputStream xslout = new FileOutputStream(depends)
        byte[] buffer = new byte[4096]
        int len = 0
        while ((len = xslin.read(buffer)) >= 0) {
          xslout.write(buffer, 0, len)
        }
        xslin.close();
        xslout.close();
      } else {
        depends = new File((String) dependSetting)
      }

      invoke("net.sf.saxon.Transform",
              [QUIT,
               "-s:${sourceFile}",
               "-xsl:${depends}",
               "-o:${urilist}"])

      BufferedInputStream uris = new BufferedInputStream(new FileInputStream(urilist))
      for (line in uris.readLines()) {
        URI uri = new URI(line)
        dependencies.add(uri.getPath())
      }
    } catch (Exception ex) {
      show("Failed to read dependencies from ${sourceFile}: ${ex.getMessage()}")
      dependencies.add(sourceFile)
    }

    if (debugFlag) {
      for (int pos = 1; pos < dependencies.size(); pos++) {
        show("${prefix}: ${dependencies.get(pos)}")
      }
    }

    return dependencies
  }

  protected List<String> xslDependsOn(String stylesheetFile) {
    return findDependsOn("Sty", xslDepends, '/com/nwalsh/gradle/saxon/xsl-depends.xsl', stylesheetFile)
  }

  protected List<String> xmlDependsOn(String sourceFile) {
    return findDependsOn("Src", xmlDependFlag, '/com/nwalsh/gradle/saxon/xml-depends.xsl', sourceFile)
  }

  protected void invoke(String mainClass, List<String> cmdline) {
    String methodName = mainClass.indexOf("Transform") >= 0 ? "doTransform" : "doQuery"
    String[] arguments = cmdline as String[]

    Class transformClass = getClass().getClassLoader().loadClass(mainClass)
    try {
      Class[] param = new Class[] { String[].class, String.class };
      def method = transformClass.getDeclaredMethod(methodName, param);
      method.invoke(transformClass.getDeclaredConstructor().newInstance(), new Object[] { arguments, '' })
    } catch (NoSuchMethodException ex) {
      param = new Class[] { String[].class };
      def method = transformClass.getDeclaredMethod(methodName, param);
      method.invoke(transformClass.getDeclaredConstructor().newInstance(), new Object[] { arguments })
    }
  }

  protected void exec(List<String> cmdline) {
    String jexec = System.getProperty('java.home')
    String psep = System.getProperty('path.separator')
    String fsep = System.getProperty('file.separator')

    if (javaClasspath.size() == 0) {
      Configuration config = task.getProject().getConfigurations().getByName('runtimeClasspath')
      config.resolve().forEach { cp ->
        javaClasspath.add(cp.toString())
      }
    }

    def javaparam = []
    javaparam.addAll("${jexec}${fsep}bin${fsep}java")
    javaparam.addAll(javaOptions)
    javaparam.add("-cp")
    javaparam.add(javaClasspath.join(psep))
    javaparam.add(mainClassName)

    ProcessBuilder builder = new ProcessBuilder((javaparam + cmdline) as String[])
    if (workdir != null) {
      builder = builder.directory(workdir)
    }
    if (javaEnv.size() != 0) {
      Map<String,String> benv = builder.environment()
      javaEnv.collect { name, value ->
        benv[name] = value
      }
    }

    Process proc = builder.start()

    // The lines are buffered because if they're unbuffered, they appear
    // before the task appears to run. I don't know why
    ProcessOutputReader stdoutReader = new ProcessOutputReader(proc.getInputStream())
    ProcessOutputReader stderrReader = new ProcessOutputReader(proc.getErrorStream())
    Thread stdoutThread = new Thread(stdoutReader)
    Thread stderrThread = new Thread(stderrReader)
    stdoutThread.start()
    stderrThread.start()

    int rc = proc.waitFor()
    stdoutThread.join()
    stderrThread.join()

    for (line in stdoutReader.lines) {
      println(line)
    }
    for (line in stderrReader.lines) {
      println(line)
    }

    if (rc != 0) {
      throw new TaskExecutionException(task,
              new Exception("Attempt to run java returned error code"))
    }
  }

  @SuppressWarnings('ClassName')
  private class ProcessOutputReader implements Runnable {
    private final InputStream is;
    List<String> lines = []

    ProcessOutputReader(InputStream is) {
      this.is = is;
    }

    void run() {
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      String line = br.readLine();
      while (line != null) {
        lines.add(line);
        line = br.readLine();
      }
    }
  }
}
