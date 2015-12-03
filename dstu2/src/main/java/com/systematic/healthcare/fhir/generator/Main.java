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

import java.io.File;
import java.nio.file.Files;

import org.jboss.forge.roaster.model.source.JavaClassSource;

public class Main {


	public static void main(String [] args) throws Exception {
		String someOutputPackageName = "com.systematic.healthcare.fhir.generator.generated";
		String [] fileNames = {"FunctioningCondition.xml"};
		
		String someReadingDirectory = new File(".").getAbsolutePath() + "\\dstu2\\src\\test\\resources";
		String someWritingDirectory = new File(".").getAbsolutePath() + "\\dstu2\\src\\test\\java\\" + someOutputPackageName.replaceAll("\\.", "/");
		new File(someWritingDirectory).mkdirs();
		

        for (String s : fileNames) {
            StructureDefinitionProvider provider = new FileStructureDefinitionProvider(
                    someOutputPackageName,
                    new File(someReadingDirectory, s));
            JavaClassSource javaClass = Generator.generate(provider);
            Files.write(new File(new File(someWritingDirectory), javaClass.getName()+".java").toPath(), javaClass.toString().getBytes("UTF-8"));
        }
        System.out.println("Success");
    }
}
