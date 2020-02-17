package com.lxk.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class PluginDemo implements Plugin<Project> {
    void apply(Project project) {
        project.task('myTask') {
            doLast {
                println "Hi, this is a plugin demo."
            }
        }
    }
}
