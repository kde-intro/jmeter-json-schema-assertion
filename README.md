# JSON Schema Assertion component for Apache JMeter

This component allows the user to validate a response against an JSON Schema.

![Screenshot for Control-Panel of JSON Schema Assertion](/JSONSchemaAssertion.png)

### Parameters

Attribute | Description | Required
------------ | ------------- | -------------
_Name_ | Descriptive name for this element that is shown in the tree. | No
_File Name_ | Specify JSON Schema File Name. | Yes


## Installation

_(jmeter.version: 3.2-SNAPSHOT)_

- Download / Pull JMeter source
- Put the java files (JSONSchemaAssertionGUI.java, JSONSchemaAssertion.java) into the corresponding packages
- Append the lines to the file _messages.properties_
```
   jsonschema_assertion_label=File Name:
   jsonschema_assertion_title=JSON Schema Assertion
```
- Append the lines to the file _build.xml_ as the subelements to ``` <path id="classpath"> ```
```xml
<pathelement location="${lib.dir}/${msg-simple.jar}"/>
<pathelement location="${lib.dir}/${uri-template.jar}"/>
<pathelement location="${lib.dir}/${jsr305.jar}"/>
<pathelement location="${lib.dir}/${guava.jar}"/>
<pathelement location="${lib.dir}/${libphonenumber.jar}"/>
<pathelement location="${lib.dir}/${activation.jar}"/>
<pathelement location="${lib.dir}/${mailapi.jar}"/>
<pathelement location="${lib.dir}/${joda-time.jar}"/>
<pathelement location="${lib.dir}/${jopt-simple.jar}"/>
<pathelement location="${lib.dir}/${rhino2.jar}"/>
<pathelement location="${lib.dir}/${jackson-core.jar}"/>
<pathelement location="${lib.dir}/${jackson-databind.jar}"/>
<pathelement location="${lib.dir}/${jackson-annotations.jar}"/>
<pathelement location="${lib.dir}/${jackson-coreutils.jar}"/>
<pathelement location="${lib.dir}/${json-schema-core.jar}"/>
<pathelement location="${lib.dir}/${json-schema-validator.jar}"/>
<pathelement location="${lib.dir}/${btf.jar}"/>
```
- Append the lines to the file _build.xml_ as the subelements to ``` <target name="_process_all_jars"> ```
```xml
<process_jarfile jarname="msg-simple"/>
<process_jarfile jarname="uri-template"/>
<process_jarfile jarname="jsr305"/>
<process_jarfile jarname="guava"/>
<process_jarfile jarname="libphonenumber"/>
<process_jarfile jarname="activation"/>
<process_jarfile jarname="mailapi"/>
<process_jarfile jarname="joda-time"/>
<process_jarfile jarname="jopt-simple"/>
<process_jarfile jarname="rhino2"/>
<process_jarfile jarname="jackson-core"/>
<process_jarfile jarname="jackson-databind"/>
<process_jarfile jarname="jackson-annotations"/>
<process_jarfile jarname="jackson-coreutils"/>
<process_jarfile jarname="json-schema-core"/>
<process_jarfile jarname="json-schema-validator"/>
<process_jarfile jarname="btf"/>
```
- Append the lines to the file _build.xml_ as the subelements to ``` <patternset id="external.jars"> ```
 ```xml
<include name="${lib.dir}/${msg-simple.jar}"/>
<include name="${lib.dir}/${uri-template.jar}"/>
<include name="${lib.dir}/${jsr305.jar}"/>
<include name="${lib.dir}/${guava.jar}"/>
<include name="${lib.dir}/${libphonenumber.jar}"/>
<include name="${lib.dir}/${activation.jar}"/>
<include name="${lib.dir}/${mailapi.jar}"/>
<include name="${lib.dir}/${joda-time.jar}"/>
<include name="${lib.dir}/${jopt-simple.jar}"/>
<include name="${lib.dir}/${rhino2.jar}"/>
<include name="${lib.dir}/${jackson-core.jar}"/>
<include name="${lib.dir}/${jackson-databind.jar}"/>
<include name="${lib.dir}/${jackson-annotations.jar}"/>
<include name="${lib.dir}/${jackson-coreutils.jar}"/>
<include name="${lib.dir}/${json-schema-core.jar}"/>
<include name="${lib.dir}/${json-schema-validator.jar}"/>
<include name="${lib.dir}/${btf.jar}"/>
```
- Append the lines to the file _build.properties_
```
#For JSON Schema Assertion component
#http://central.maven.org/maven2/com/github/fge/json-schema-validator/2.2.6/json-schema-validator-2.2.6.jar
#http://central.maven.org/maven2/com/github/fge/json-schema-core/1.2.5/json-schema-core-1.2.5.jar
#http://central.maven.org/maven2/com/github/fge/jackson-coreutils/1.8/jackson-coreutils-1.8.jar
#http://central.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.2.3/jackson-databind-2.2.3.jar
#http://central.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.2.3/jackson-core-2.2.3.jar
#http://central.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.2.3/jackson-annotations-2.2.3.jar
#http://central.maven.org/maven2/com/github/fge/btf/1.2/btf-1.2.jar
#http://central.maven.org/maven2/com/github/fge/msg-simple/1.1/msg-simple-1.1.jar
#http://central.maven.org/maven2/com/github/fge/uri-template/0.9/uri-template-0.9.jar
#http://central.maven.org/maven2/com/google/code/findbugs/jsr305/3.0.0/jsr305-3.0.0.jar
#http://central.maven.org/maven2/com/google/guava/guava/16.0.1/guava-16.0.1.jar
#http://central.maven.org/maven2/com/googlecode/libphonenumber/libphonenumber/6.2/libphonenumber-6.2.jar
#http://central.maven.org/maven2/javax/activation/activation/1.1/activation-1.1.jar
#http://central.maven.org/maven2/javax/mail/mailapi/1.4.3/mailapi-1.4.3.jar
#http://central.maven.org/maven2/joda-time/joda-time/2.3/joda-time-2.3.jar
#http://central.maven.org/maven2/net/sf/jopt-simple/jopt-simple/4.6/jopt-simple-4.6.jar
#http://central.maven.org/maven2/org/mozilla/rhino/1.7R4/rhino-1.7R4.jar

c.maven2.repo  = http://central.maven.org/maven2

json-schema-validator.version   = 2.2.6
json-schema-validator.jar       = json-schema-validator-${json-schema-validator.version}.jar
json-schema-validator.loc       = ${c.maven2.repo}/com/github/fge/json-schema-validator/${json-schema-validator.version}
json-schema-validator.md5       = cffdf67fe8a1fd5b7b0ab609b5946fc0

json-schema-core.version    = 1.2.5
json-schema-core.jar        = json-schema-core-${json-schema-core.version}.jar
json-schema-core.loc        = ${c.maven2.repo}/com/github/fge/json-schema-core/${json-schema-core.version}
json-schema-core.md5        = 8cc58c15c524af64c25b1ee3c5c4fda1

jackson-coreutils.version   = 1.8
jackson-coreutils.jar       = jackson-coreutils-${jackson-coreutils.version}.jar
jackson-coreutils.loc       = ${c.maven2.repo}/com/github/fge/jackson-coreutils/${jackson-coreutils.version}
jackson-coreutils.md5       = 30bdf82d78a5fc03de6779595731e241

jackson-databind.version    = 2.2.3
jackson-databind.jar        = jackson-databind-${jackson-databind.version}.jar
jackson-databind.loc        = ${c.maven2.repo}/com/fasterxml/jackson/core/jackson-databind/${jackson-databind.version}
jackson-databind.md5        = 6e8d8b13d5d4200b782516d2cd6c9469

jackson-core.version    = 2.2.3
jackson-core.jar        = jackson-core-${jackson-core.version}.jar
jackson-core.loc        = ${c.maven2.repo}/com/fasterxml/jackson/core/jackson-core/${jackson-core.version}
jackson-core.md5        = d30b99b7e07a8a35205c8b4eb3064746

jackson-annotations.version = 2.2.3
jackson-annotations.jar     = jackson-annotations-${jackson-annotations.version}.jar
jackson-annotations.loc     = ${c.maven2.repo}/com/fasterxml/jackson/core/jackson-annotations/${jackson-annotations.version}
jackson-annotations.md5     = f7f09fe009c61b73514fb59efe957a1b

btf.version = 1.2
btf.jar     = btf-${btf.version}.jar
btf.loc     = ${c.maven2.repo}/com/github/fge/btf/${btf.version}
btf.md5     = 5c91cd1157e0bb99e77a33b6f42a457c

msg-simple.version  = 1.1
msg-simple.jar      = msg-simple-${msg-simple.version}.jar
msg-simple.loc      = ${c.maven2.repo}/com/github/fge/msg-simple/${msg-simple.version}
msg-simple.md5      = b0d8d70468edff2e223b3d2f07cc5de1

uri-template.version    = 0.9
uri-template.jar        = uri-template-${uri-template.version}.jar
uri-template.loc        = ${c.maven2.repo}/com/github/fge/uri-template/${uri-template.version}
uri-template.md5        = f0bfa64e2bbbb4da7d1913f47bcee3d7

jsr305.version  = 3.0.0
jsr305.jar      = jsr305-${jsr305.version}.jar
jsr305.loc      = ${c.maven2.repo}/com/google/code/findbugs/jsr305/${jsr305.version}
jsr305.md5      = 195d5db8981fbec5fa18d5df9fad95ed

guava.version   = 16.0.1
guava.jar       = guava-${guava.version}.jar
guava.loc       = ${c.maven2.repo}/com/google/guava/guava/${guava.version}
guava.md5       = a68693df58191585d9af914cfbe6067a

libphonenumber.version  = 6.2
libphonenumber.jar      = libphonenumber-${libphonenumber.version}.jar
libphonenumber.loc      = ${c.maven2.repo}/com/googlecode/libphonenumber/libphonenumber/${libphonenumber.version}
libphonenumber.md5      = dc69cb4dbdd3241e0d2a23ceb1514372

activation.version  = 1.1
activation.jar      = activation-${activation.version}.jar
activation.loc      = ${c.maven2.repo}/javax/activation/activation/${activation.version}
activation.md5      = 8ae38e87cd4f86059c0294a8fe3e0b18

mailapi.version = 1.4.3
mailapi.jar     = mailapi-${mailapi.version}.jar
mailapi.loc     = ${c.maven2.repo}/javax/mail/mailapi/${mailapi.version}
mailapi.md5     = de1f54df6a55c4e77258cc77b51d3828

joda-time.version   = 2.3
joda-time.jar       = joda-time-${joda-time.version}.jar
joda-time.loc       = ${c.maven2.repo}/joda-time/joda-time/${joda-time.version}
joda-time.md5       = ff85fe8f3ab26b36092475a95f43fb7e

jopt-simple.version = 4.6
jopt-simple.jar     = jopt-simple-${jopt-simple.version}.jar
jopt-simple.loc     = ${c.maven2.repo}/net/sf/jopt-simple/jopt-simple/${jopt-simple.version}
jopt-simple.md5     = 13560a58a79b46b82057686543e8d727

rhino2.version  = 1.7R4
rhino2.jar      = rhino-${rhino2.version}.jar
rhino2.loc      = ${c.maven2.repo}/org/mozilla/rhino/${rhino2.version}
rhino2.md5      = 3850097fb5c9aa1065cc198f1b82dcf1
```
- Run _Ant_ task _download_jars_ or download the neccessary jars manually (see the links above in the previous point) and put them into _lib_ folder
- Compile JMeter application


## Notes

- The file _build.parameters_ contains the library _rhino_ already but its version is _1.7.7.1_.
- This assertion are costly and avoid using its during load tests.


## Tasks List
- [ ] Create Maven version (plugin)
- [ ] Add test class JSONSchemaAssertionTest


## Changelog



## Roadmap



## Troubleshooting



## Support

