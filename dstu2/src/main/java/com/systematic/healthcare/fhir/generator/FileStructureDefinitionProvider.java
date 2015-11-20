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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.ElementDefinitionDt;
import ca.uhn.fhir.model.dstu2.resource.StructureDefinition;
import ca.uhn.fhir.parser.IParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileStructureDefinitionProvider implements StructureDefinitionProvider {
    private final FhirContext context;
    private final String outPackage;
    private final File structureFile;

    public FileStructureDefinitionProvider(String outPackage, File structureFile) {
        this.outPackage = outPackage;
        this.structureFile = structureFile;
        context = FhirContext.forDstu2();
    }

    public String getOutPackage() {
        return outPackage;
    }

    @Override
    public StructureDefinition getDefinition() throws IOException {
        IParser parser = context.newXmlParser();
        return parser.parseResource(StructureDefinition.class, fileToContentString(structureFile));
    }

    @Override
    public StructureDefinition provideReferenceDefinition(ElementDefinitionDt element) throws IOException {
        String fileStr = element.getTypeFirstRep().getProfileFirstRep().getValue().substring(element.getTypeFirstRep().getProfileFirstRep().getValue().lastIndexOf('/') + 1) + ".xml";
        File file = new File(structureFile.getParent(), fileStr);
        if (!file.isFile()) {
            return null;
        }
        IParser parser = context.newXmlParser();
        return parser.parseResource(StructureDefinition.class, fileToContentString(file));
    }

    private String fileToContentString(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()), "UTF-8");
    }
}
