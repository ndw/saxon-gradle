package com.nwalsh.gradle.saxon

import org.gradle.api.Project
import org.gradle.api.Plugin

abstract class SaxonPluginExtension {
    public static final String DEFAULT = "com.nwalsh.gradle.saxon.DEFAULT"
    private Map<String,SaxonPluginConfiguration> configs = new HashMap<>()

    void configure(Closure cl) {
        this.configure(SaxonPluginConfigurations.DEFAULT, cl)
    }
    
    void configure(String name, Closure cl) {
        if (!(name in configs)) {
          configs[name] = new SaxonPluginConfiguration(name)
        }
        cl.delegate = configs[name]
        cl()
    }

    Map<String,Object> getOptions(String name) {
        Map<String,Object> opts = [:]
        if (DEFAULT in configs) {
            configs[DEFAULT].getOptions().each { key, val ->
              opts[key] = val
            }
        }
        if (name in configs) {
            configs[name].getOptions().each { key, val ->
              opts[key] = val
          }
        }
        return opts
    }

    Map<String,Object> getPluginOptions(String name) {
        Map<String,Object> opts = [:]
        if (DEFAULT in configs) {
            configs[DEFAULT].getPluginOptions().each { key, val ->
                opts[key] = val
            }
        }
        if (name in configs) {
            configs[name].getPluginOptions().each { key, val ->
                opts[key] = val
            }
        }
        return opts
    }

    Map<String,Object> getAdvancedOptions(String name) {
        Map<String,Object> opts = [:]
        if (DEFAULT in configs) {
            configs[DEFAULT].getAdvancedOptions().each { key, val ->
                opts[key] = val
            }
        }
        if (name in configs) {
            configs[name].getAdvancedOptions().each { key, val ->
                opts[key] = val
            }
        }
        return opts
    }

    Set<String> configurations() {
        return configs.keySet()
    }
}

class SaxonPlugin implements Plugin<Project> {
    final String XSLT = 'xslt'
    final String SAXON = 'saxon'

    @Override
    void apply(Project project) {
        project.extensions.create(SAXON, SaxonPluginExtension)
        project.task(XSLT, type: SaxonXsltTask)
    }
}
