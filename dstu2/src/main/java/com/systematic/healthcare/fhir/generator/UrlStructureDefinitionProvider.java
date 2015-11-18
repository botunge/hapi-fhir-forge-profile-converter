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
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UrlStructureDefinitionProvider implements StructureDefinitionProvider {
    private final FhirContext context;
    private final String outPackage;
    private final String structureUrl;

    public UrlStructureDefinitionProvider(String outPackage, String structureUrl) {
        this.outPackage = outPackage;
        this.structureUrl = structureUrl;
        context = FhirContext.forDstu2();
    }

    public String getOutPackage() {
        return outPackage;
    }

    @Override
    public StructureDefinition getDefinition() throws IOException {
        IParser parser = context.newXmlParser();
        return parser.parseResource(StructureDefinition.class, urlToContentString(new URL(structureUrl)));
    }

    @Override
    public StructureDefinition provideReferenceDefinition(ElementDefinitionDt element) throws IOException {
        String urlStr = element.getTypeFirstRep().getProfileFirstRep().getValue();
        URL url = new URL(urlStr);
        IParser parser = context.newXmlParser();
        return parser.parseResource(StructureDefinition.class, urlToContentString(url));
    }

    private String urlToContentString(URL url) throws IOException {
        try (InputStream in = url.openStream()) {
            return new String(IOUtils.toByteArray(in), "UTF-8");
        }
    }
}
