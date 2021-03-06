/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.gliderpilot.gradle.gettext

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet

class GradleGettextPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.extensions.create('gettext', GradleGettextPluginExtension, this, project)
        def poDir = "src/main/i18n"
        def propertiesDir = "src/main/i18n"
        def updatePropertiesTask = project.tasks.create('updateProperties', UpdatePropertiesTask) {
            poFiles poDir
            propertiesTemplateFiles propertiesDir
            delegate.propertiesDir propertiesDir
        }
        def updatePoTask = project.tasks.create('updatePo', UpdatePoTask) {
            finalizedBy updatePropertiesTask
            poTemplateFiles poDir
            poFiles poDir
        }
        def updatePotTask = project.tasks.create('updatePot', UpdatePotFromPropertiesTask) {
            finalizedBy updatePoTask
            propertiesTemplateFiles propertiesDir
            poTemplateDir poDir
        }
        project.tasks.create('importResourceBundles', ImportResourceBundlesTask) {
            finalizedBy updatePotTask
            propertiesTemplateFiles propertiesDir
            propertiesFiles propertiesDir
            delegate.poDir poDir
        }
    }
}
