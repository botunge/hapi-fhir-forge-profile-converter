# hapi-fhir-forge-profile-converter
A Converter which converts Fhir (http://fhir.furore.com/forge) profiles to Hapi Fhir Resources

### Using the generator directly from java
```java
String xmlFile = "SomeStructureDefinition.xml";
String packageTargetName = "com.systematic.healthcare.fhir.generator.generated";
StructureDefinitionProvider provider = new FileStructureDefinitionProvider(
        packageTargetName,
        new File(directory, xmlFile));
JavaClassSource javaClass = Generator.generate(provider);
new File(directory, "generated/"+packageTargetName.replace("\\.", "/")).mkdirs();
Files.write(new File(new File(directory, "generated"), javaClass.getName()+".java"
```

### Using the gradle plugin to convert structure definition files
This project contains a samle gradle module (gradle-plugin-example) which if enabled will
convert all files in a specified folder to hapi fhir dstu2 java.
```gradle
//Enable the structure definition to java plugin
apply plugin: 'sdToJava'
// Arguments for the converter.
sdToJavaArg {
    //The build folder where the generated files should be placed
    outDirectory = "$buildDir/generated-resources/main/java"
    //The files to convert from structure definition to dstu2 java.
    files = project.fileTree("testdata").include("*.xml")
    //The package name for the generated files
    packageName = "com.systematic.healthcare.fhir.generator.generated"
}

build.dependsOn sdToJavaTask
```

### Get the jars
To get the StructureDefinition to DSTU2 converter use the following gradle dependency
```gradle
compile 'com.systematic.healthcare.fhir.generator:dstu2:1.0'
```
To use the gradle plugin to convert files from gradle, add the following to the buildscript classpath
```gradle
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'com.systematic.healthcare.fhir.generator:gradle-plugin:1.0'
    }
}
apply plugin: 'sdToJava'

sdToJavaArg {
    outDirectory = "$buildDir/generated-resources/main/java"
    files = project.fileTree("testdata").include("*.xml")
    packageName = "com.systematic.healthcare.fhir.generator.generated"
}
build.dependsOn sdToJavaTask
```


### To come
- Tests needs to be written and added..
- Add support for writing @Override when overriding super methods..
- Slicing is still under development. Waiting for HAPI FHIR support for slicing annotation.

### Status
[![Build Status](https://travis-ci.org/botunge/hapi-fhir-forge-profile-converter.png)](https://travis-ci.org/botunge/hapi-fhir-forge-profile-converter)
[![Dependency Status](https://www.versioneye.com/user/projects/565431d9ff016c003a000916/badge.svg?style=flat)](https://www.versioneye.com/user/projects/565431d9ff016c003a000916)
