# Saxon Gradle Plugin

A plugin for running [Saxon][saxon] from [Gradle][gradle]. This plugin
used to be a fork of Eero Helenius’s
[saxon-gradle](https://github.com/eerohele/saxon-gradle) project, but
I’ve essentially rewritten it so I had GitHub “detach” the fork.

I learned a lot from Eero and his project is still very much the
inspiration for this one. But there’s no reason that I’d expect any of
the changes here to be accepted back into his project.

I’m leaving the `master` branch, the last updates from the previous
fork, moribund and putting new work on the `main` branch.

## Features and differences

Eero’s approach was to make many configuration options visible in the
plugin DSL. That’s convenient, but also a little bit misleading. All
of the actual processing is filtered through `doTransform()` which
takes string arguments (exactly the same arguments as you are allowed
to pass on the command line).

I was initially inspired to try to do a deeper integration, but
decided that was more work than was justified. Usually what I want to
do in a build script is simply transform a document. I don’t need to,
for example, write a `ModuleURIHander` in Groovy and integrate it into
the build, even if that would be (a) cool and (b) very, *very*
occasionally useful.

I’ve simplified the DSL so that it has only a few methods and almost
all of the configuration is done by specifying arguments that are
exactly the same as they would be on the command line. This allows the
plugin to be completely divorced from any particular version of
SaxonJ, which will simplify things for projects that are using
different versions of Saxon.

Eero’s plugin is also capable of processing entire directories of XML
documents with multiple threads. That added a fair bit of complexity
and it’s just something I’ve never wanted to do. So I removed it.

I did add a few things:

1. It’s now possible to run XQuery transformations in addition to XSLT
   transformations.
2. It’s possible to request that the transformation be performed in a
   separate Java process. This means you can run transforms with
   different Java properties or environment variables, or with a different classpath.
3. The process for examining XML documents and stylesheets to work out
   if they have additional dependencies has been ported to stylesheets
   that you can override if you have more specific requirements. Note
   that Gradle does this during the *configuration* phase, so it will
   happen for every task that uses the feature, on every run,
   irrespective of what tasks actually get run. Useful, but perhaps
   best used sparingly.

## Example

There are three example projects that demonstrate the features of this
plugin:

1. [Simple](https://github.com/ndw/saxon-gradle/blob/main/examples/simple/)
   is the simplest example. It demonstrates using XSLT and XQuery to
   transform documents using the same process as the build script. This is
   likely to be the fastest approach.
2. [JExec](https://github.com/ndw/saxon-gradle/blob/main/examples/jexec/) is
   the same example, but the XSLT and XQuery processes are run in a separate
   process. 
3. [Depends](https://github.com/ndw/saxon-gradle/blob/main/examples/depends/)
   uses the dependency feature to work out what stylesheet sub-modules
   are used by the stylesheets and what sub-documents are XIncluded
   into the source. These are automatically added to Gradle’s
   understanding of what files to examine for changes when determining
   if a task needs to be re-run.

## Benefits

This project has many of the same benefits as the original:

- A clean and Saxon-version agnostic syntax for running XSLT
  transformations with Saxon.
- Better performance via the [Gradle Daemon][gradle-daemon].
- Easily configure Saxon either in the Gradle buildfile or via a
  [Saxon configuration file][saxon-config-file].
- Only rerun the transformation if the input file(s) or the stylesheet has
  changed (or if forced with `--rerun-tasks`).
- Rapid XSLT development via Gradle's `--continuous` option: automatically
  run your stylesheet every time it or your input file changes.
- Support for running XQuery transformations.

## Options

Options are specified with methods on the task. 

### Saxon options

These options control what’s passed to the Saxon processor.

* `input`, the input document
* `output`, the output document
* `stylesheet`, the XSL stylesheet (for `SaxonXsltTask` tasks)
* `query` or `queryString` the XQuery (for `SaxonXQueryTask` tasks)
* `arg` or `args`, add arguments to the process
* `parameters`, a map of stylesheet or query parameters.

### Plugin options

These options control other aspects of the plugin.

* `debug`, print debugging information about task configuration and execution
* `mainClass`, the class to instantiate (defaults appropriately for XSLT and XQuery)
* `xmlDepends`, process the source file for dependencies? Can be `true` or the
  name of a stylesheet. The stylesheet must return a list of absolute `file:` URIs, one
  per line.
* `xslDepends`, like `xmlDepends` but for the XSLT stylesheet.
* `baseURI`, specify the base URI against which input documents and
  stylesheets should be resolved. Defaults to the current working directory.

### Java options

These options control how an external Java process is configured.
Setting any of these options implies `exec true`.

To use these options, you must also use the Gradle `java` plugin. That
plugin modifies the configuration for running Java, including adding
support for using different class paths.

* `exec`, if `true`, run the transformation as an external Java process
* `javaArg` or `javaArgs`, add arguments to the Java process
* `env`, additional environment mappings for the process
* `workingDir`, the working directory where the process will run
* `classpath`, specify a different classpath for the process

[gradle]: http://gradle.org/
[gradle-daemon]: https://docs.gradle.org/current/userguide/gradle_daemon.html
[saxon]: http://saxonica.com/
[saxon-command-line]: http://www.saxonica.com/html/documentation/using-xsl/commandline/
[saxon-config-file]: http://saxonica.com/html/documentation/configuration/configuration-file
