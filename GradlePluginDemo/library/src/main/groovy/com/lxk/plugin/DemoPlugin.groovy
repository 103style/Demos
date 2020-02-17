package com.lxk.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class DemoPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        println "hello, this is cooker plugin!"

        project.task('cooker-test-task') << {
            println 'Hello from the DemoPlugin'
        }
    }
}