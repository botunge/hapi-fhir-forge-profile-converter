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
package com.systematic.healthcare.fhir.generator;

import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.io.File;
import java.nio.file.Files;

public class Main {

    public static void main(String [] args) throws Exception {
        String directory = "dstu2\\src\\test\\java\\com\\systematic\\healthcare\\fhir\\generator\\";

        for (String s : new String[] {"Cura General Assessments Profile", "CuraFunctioningCondition", "FunctioningCondition"}) {
            Generator gen = new Generator();
            StructureDefinitionProvider provider = new FileStructureDefinitionProvider(
                    "com.systematic.healthcare.fhir.generator.generated",
                    new File(directory, s + ".xml"));
            JavaClassSource javaClass = gen.convertDefinitionToJavaFile(provider);
            new File(directory, "generated").mkdirs();
            Files.write(new File(new File(directory, "generated"), javaClass.getName()+".java").toPath(), javaClass.toString().getBytes("UTF-8"));
        }
        System.out.println("Success");
    }
}
