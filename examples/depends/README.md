# Simple example

This example demonstrates using “depends” properties. These tell the
tasks to examine the (XML and XSL) input documents to find other files
that they include. These are automatically added to the set of inputs
that Gradle knows about.

Pro: these tasks will be marked “out-of-date” if you edit one of these
dependent files.

Con: the computation is performed during the configuration phase, so
it happens for every task on every run, irrespective of what tasks are
executed.
