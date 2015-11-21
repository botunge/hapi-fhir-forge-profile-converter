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
package com.systematic.healthcare.gradle

import com.systematic.healthcare.fhir.generator.FileStructureDefinitionProvider
import com.systematic.healthcare.fhir.generator.Generator
import com.systematic.healthcare.fhir.generator.StructureDefinitionProvider
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.jboss.forge.roaster.model.source.JavaClassSource

import java.nio.file.Files

class StructureDetinitionToJavaTask extends DefaultTask {

    @TaskAction
    def convert() {
        getLogger().info("------------ Convert -------------------")
        getLogger().info("Out folder: ${project.sdToJavaArg.outDirectory}")
        getLogger().info("Files: ${project.sdToJavaArg.files}")
        project.sdToJavaArg.files.each { File f ->
            getLogger().info("Converting file: ${f}")
            StructureDefinitionProvider provider = new FileStructureDefinitionProvider(
                    project.sdToJavaArg.packageName,
                    f)
            JavaClassSource javaClass = Generator.generate(provider)
            File outDir = new File(project.sdToJavaArg.outDirectory);
            for (String s : project.sdToJavaArg.packageName.split("\\.")) {
                outDir = new File(outDir, s);
            }
            outDir.mkdirs()
            Files.write(
                    new File(outDir, javaClass.getName() + ".java").toPath(),
                    javaClass.toString().getBytes("UTF-8"))
        }
    }
}
