package com.nwalsh.gradle.saxon

import org.gradle.api.Project
import org.gradle.api.Plugin

class SaxonPlugin implements Plugin<Project> {
  @Override
  void apply(Project project) {
    // On balance, I've decided not to do this. It's easy
    // enough to create your own tasks and then you don't
    // have "magic names" to contend with.
    
    // project.task('xslt', type: SaxonXsltTask)
    // project.task('xquery', type: SaxonXQueryTask)
  }
}
