/*
 * Copyright (C) 2015 Systematic A/S
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.systematic.healthcare.gradle;

import org.gradle.api.*
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory;
// https://github.com/rakeshcusat/Code4Reference/tree/master/GradleExample/custom-plugin-2
class StructureDefinitionToJavaArg {
    @InputFiles
    FileTree files
    @OutputDirectory
    String outDirectory
    @Input
    String packageName
}
class StructureDefinitionToJavaPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create("sdToJavaArg", StructureDefinitionToJavaArg)
        project.task('sdToJavaTask', type: StructureDetinitionToJavaTask)
    }
}
