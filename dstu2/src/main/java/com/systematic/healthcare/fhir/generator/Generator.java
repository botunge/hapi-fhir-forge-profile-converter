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
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.composite.BoundCodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ElementDefinitionDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.StructureDefinition;
import ca.uhn.fhir.model.primitive.BoundCodeDt;
import ca.uhn.fhir.parser.IParser;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.AnnotationSource;
import org.jboss.forge.roaster.model.source.FieldSource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.file.Files;
import java.util.*;

import static org.reflections.ReflectionUtils.getAllFields;
import static org.reflections.ReflectionUtils.withAnnotation;

public class Generator {

    private static final String DSTU2_PACKAGE = "ca.uhn.fhir.model.dstu2";
    private static final String DSTU2_RESOURCE_PACKAGE = DSTU2_PACKAGE + ".resource";
    private static final String DSTU2_COMPOSITE_PACKAGE = DSTU2_PACKAGE + ".composite";
    private static final String DSTU2_PRIMITIVE_PACKAGE = "ca.uhn.fhir.model.primitive";

    public String convertDefinitionToJavaFile(String outPackage, String structureDefString) throws Exception {
        IParser parser = FhirContext.forDstu2().newXmlParser();
        StructureDefinition def = parser.parseResource(StructureDefinition.class, structureDefString);

        final JavaClassSource javaClass = Roaster.create(JavaClassSource.class);
        Class classType = Class.forName(DSTU2_RESOURCE_PACKAGE + "." + def.getConstrainedType());
        javaClass.setPackage(outPackage).setName(def.getName().replace(" ", "")).extendSuperType(classType);
        addClassResourceDefAnnotation(def, javaClass);

        Set<Field> fields = getAllFields(classType, withAnnotation(Child.class));
        Map<String, Field> nameToField = new HashMap<>();
        for (Field f : fields) {
            Child annotation = f.getAnnotation(Child.class);
            nameToField.put(annotation.name(), f);
        }

        StructureDefinition.Differential dif = def.getDifferential();
        List<ElementDefinitionDt> elements = dif.getElement();
        for (ElementDefinitionDt element : elements) {
            if (element.getPath().indexOf('.') == element.getPath().lastIndexOf('.') && element.getPath().indexOf('.') != -1) {
                String name = element.getPath().substring(def.getConstrainedType().length() + 1);
                if (name.endsWith("[x]")) {
                    name = name.substring(0, name.indexOf("[x]"));
                }
                if (name.equals("extension")) {
                    addExtensionField(javaClass, element);
                } else {
                    addField(javaClass, nameToField, element, name);
                }

            }
        }
        return javaClass.toString();
    }

    private Set<String> sliced = new HashSet<>();
    private void addField(JavaClassSource javaClass, Map<String, Field> nameToField, ElementDefinitionDt element, String name) {

        if (!element.getSlicing().getDiscriminator().isEmpty()) {
            sliced.add(element.getPath());
        } else if (sliced.contains(element.getPath())) {
            System.out.println(element.getName());
            return;
        }

        Field originalField = nameToField.get(name);
        FieldSource<JavaClassSource> field = javaClass.addField().setName("my" + StringUtils.capitalize(name));
        if (Collection.class.isAssignableFrom(originalField.getType())) {
            List<Class> cl = FluentIterable.from(element.getType()).transform(new TypeClassFunction(originalField)).toList();
            if (cl.size() == 0) {
                setFieldTypeGeneric(javaClass, originalField, field);
            } else {
                String args = Joiner.on(',').join(FluentIterable.from(cl).transform(new ClassToSimpleNameFunction()));
                field.setType(originalField.getType().getCanonicalName() + "<" + args + ">");
            }
        } else {
            if (element.getBinding() != null && isStrengthNotExample(element.getBinding()) && element.getBinding().getValueSet() instanceof ResourceReferenceDt) {
                ResourceReferenceDt ref = (ResourceReferenceDt) element.getBinding().getValueSet();
                if (ref.getReference().getValue().startsWith("http://hl7.org/fhir")) {
                    if (BoundCodeableConceptDt.class.isAssignableFrom(originalField.getType())) {
                        setFieldTypeGeneric(javaClass, originalField, field);
                    } else if (BoundCodeDt.class.isAssignableFrom(originalField.getType())) {
                        setFieldTypeGeneric(javaClass, originalField, field);
                    } else {
                        field.setType(originalField.getType());
                    }
                } else {
                    field.setType(originalField.getType());
                }
            } else {
                field.setType(originalField.getType());
            }
        }

        List<Class> fieldType = FluentIterable.from(element.getType()).transform(new TypeClassFunction(originalField)).toList();
        AnnotationSource<JavaClassSource> childAnnotation = addChildAnnotation(element, name, field);
        childAnnotation.setClassArrayValue("type", fieldType.toArray(new Class[fieldType.size()]));
        addDescriptionAnnotation(element, field);
    }

    private void setFieldTypeGeneric(JavaClassSource javaClass, Field originalField, FieldSource<JavaClassSource> field) {
        Class typeClass = (Class) ((ParameterizedType) originalField.getGenericType()).getActualTypeArguments()[0];
        field.setType(originalField.getType().getCanonicalName() + "<" + typeClass.getSimpleName() + ">");
        javaClass.addImport(typeClass);
    }

    private boolean isStrengthNotExample(ElementDefinitionDt.Binding binding) {
        return binding.getStrength() == null || !binding.getStrength().equals("example");
    }

    private void addClassResourceDefAnnotation(StructureDefinition def, JavaClassSource javaClass) {
        javaClass.addAnnotation(ResourceDef.class).setStringValue("name", def.getConstrainedType()).setStringValue("id", def.getName());
    }

    private void addExtensionField(JavaClassSource javaClass, ElementDefinitionDt element) throws Exception {
        if (element.getType().size() > 1) {
            throw new IllegalStateException("WTF");
        } else {
            if (element.getName() == null) {
                return;
            }
            Class extensionType = getExtensionType(element);

            FieldSource<JavaClassSource> field = javaClass.addField()
                    .setName("my" + StringUtils.capitalize(element.getName()));

            if (extensionType != null) {
                field.setType(extensionType);
            } else {
                field.setType(Roaster.parse("public class " + StringUtils.capitalize(element.getName()) + " {}"));
            }
            field.getJavaDoc().addTagValue("TODO:", "Replace Void.class with correct extension type");
            field.getJavaDoc().addTagValue("@deprecated", "Replace Void.class with correct extension type");
            field.addAnnotation(Extension.class)
                    .setLiteralValue("definedLocally", "false")
                    .setLiteralValue("isModifier", "false")
                    .setStringValue("url", element.getTypeFirstRep().getProfileFirstRep().getValue());
            addChildAnnotation(element, element.getName(), field);
            addDescriptionAnnotation(element, field);
        }
    }

    private Class getExtensionType(ElementDefinitionDt element) throws IOException {
        String file = element.getTypeFirstRep().getProfileFirstRep().getValue().substring(element.getTypeFirstRep().getProfileFirstRep().getValue().lastIndexOf('/') + 1) + ".xml";
        IParser parser = FhirContext.forDstu2().newXmlParser();
        String lines = new String(Files.readAllBytes(new File("E:/FHIR/" + file).toPath()), "UTF-8");
        StructureDefinition def = parser.parseResource(StructureDefinition.class, lines);
        for (ElementDefinitionDt el : def.getDifferential().getElement()) {
            if (el.getPath().equals("Extension.value[x]")) {
                return getClassFromType(el.getTypeFirstRep(), null);
            }
        }
        throw new IllegalArgumentException("Could not find type for extension: " + element);
    }

    private AnnotationSource<JavaClassSource> addChildAnnotation(ElementDefinitionDt element, String name, FieldSource<JavaClassSource> field) {
        AnnotationSource<JavaClassSource> childAnnotation = field.addAnnotation(Child.class);
        childAnnotation.setStringValue("name", name);
        childAnnotation.setLiteralValue("min", element.getMin().toString());
        childAnnotation.setLiteralValue("max", "*".equals(element.getMax()) ? "Child.MAX_UNLIMITED" : element.getMax());
        childAnnotation.setLiteralValue("order", "Child.REPLACE_PARENT");
        childAnnotation.setLiteralValue("summary", element.getIsSummary() != null ? element.getIsSummary().toString() : "false");
        childAnnotation.setLiteralValue("modifier", element.getIsModifier() != null ? element.getIsModifier().toString() : "false");

        return childAnnotation;
    }

    private void addDescriptionAnnotation(ElementDefinitionDt element, FieldSource<JavaClassSource> field) {
        AnnotationSource<JavaClassSource> descriptionAnnotation = field.addAnnotation(Description.class);
        if (element.getShort() != null) {
            descriptionAnnotation.setStringValue("shortDefinition", element.getShort());
        }
        if (element.getDefinition() != null) {
            descriptionAnnotation.setStringValue("formalDefinition", element.getDefinition());
        }

    }

    private static class TypeClassFunction implements Function<ElementDefinitionDt.Type, Class> {

        private Field field;

        public TypeClassFunction(Field field) {
            this.field = field;
        }

        @Nullable
        @Override
        public Class apply(@Nullable ElementDefinitionDt.Type input) {
            return getClassFromType(input, field);
        }
    }

    private static Class getClassFromType(@Nullable ElementDefinitionDt.Type input, Field originalField) {
        try {
            switch (input.getCode()) {
                case "Extension":
                    return Extension.class;
                case "BackboneElement":
                    if (originalField.getGenericType() instanceof ParameterizedType) {
                        return (Class) ((ParameterizedType) originalField.getGenericType()).getActualTypeArguments()[0];
                    } else {
                        return originalField.getType();
                    }
                case "Reference":
                    return ResourceReferenceDt.class;
                default:
                    Class cls;
                    try {
                        cls = Class.forName(DSTU2_PRIMITIVE_PACKAGE + "." + StringUtils.capitalize(input.getCode()) + "Dt");
                    } catch (ClassNotFoundException ee) {
                        cls = Class.forName(DSTU2_COMPOSITE_PACKAGE + "." + input.getCode() + "Dt");
                    }
                    return cls;
            }
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot locate class", e);
        }
    }

    private static class ClassToSimpleNameFunction implements Function<Class, String> {
        @Nullable
        @Override
        public String apply(@Nullable Class input) {
            return input.getSimpleName();
        }
    }
}
