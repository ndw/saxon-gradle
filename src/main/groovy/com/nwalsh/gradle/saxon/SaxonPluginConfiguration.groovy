package com.nwalsh.gradle.saxon

class SaxonPluginConfiguration implements SaxonPluginOptions {
    private String configname
    protected final Map<String,Object> options = [:]
    protected final Map<String,Object> pluginOptions = [:]
    protected final Map<String,Object> advancedOptions = [:]

    SaxonPluginConfiguration(String name) {
        configname = name
    }

    Map<String,Object> getOptions() {
        return options
    }

    Map<String,Object> getPluginOptions() {
        return pluginOptions
    }

    Map<String,Object> getAdvancedOptions() {
        return advancedOptions
    }

    void setOption(String name, Object value) {
        options[name] = value
    }

    void setAdvancedOption(String name, Object value) {
        advancedOptions[name] = value
    }

    void setPluginOption(String name, Object value) {
        pluginOptions[name] = value
    }
}
