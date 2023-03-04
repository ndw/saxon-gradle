# Simple example

This example demonstrates the simplest use of the tasks.

The XSLT task does a simple transformation. To demonstrate how
arguments are used, the task uses URIs for the documents and the `-u`
argument.

The XQuery task is just a silly query that multiplies the number of
elements in the document by an externally specified `multiplier`.

Note: In order to use the XQuery task this way, you must be using
Saxon version 11.6 (not released at the time of this writing) or 12.0
or later. But see [the jexec example](../jexec/).
