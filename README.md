#JSTestRunner Ant Task

Automate Javascript unit tests with Apache Ant. The task uses [Phantom JS](http://phantomjs.org/), a headless webkit browser, to open and run html unit test files. Any test failures will (optionally) cause a build to fail.

NB: Currently support is limited to [QUnit](http://docs.jquery.com/Qunit).

To get started [download](https://github.com/philmander/ant-jstestrunner/downloads) the ant-jstestrunner jar file, get a [copy of Phantom JS](http://code.google.com/p/phantomjs/downloads/list) and include the following code in your Ant build file.

```xml
<!-- Define the task -->
<taskdef name="jstest" classname="com.philmander.jstest.ant.JsTestRunnerTask" 
    classpath="${basedir}/jstestrunner/ant-jstestrunner-0.1-deps.jar" />

<target name="runJsTests">
  
  <!-- Run the tests -->
  <jstest dir="${basedir}/src/tests" includes="**/*.html" phantomWin="${basedir}/phantom/win/phantomjs.exe" />
    
</target>
```

##Parameters

###Task attributes

Attribute    | Description | Required
------------ | ----------- | ------------------
dir          | The directory to scan for test files | yes
phantomWin   | The location of the Phantom JS executable when running on Windows*  | yes (if running on Windows)
phantomMac   | The location of the Phantom JS executable when running on a Mac*    | yes (if running on Mac)
phantomLinux | The location of the Phantom JS executable when running on a Linux*    | yes (if running on Linux)
fail         | Instructs the task to fail the build if there are any JS test failures or errors | no (defaults to true)

The task is an implicit fileset. See http://ant.apache.org/manual/Types/fileset.html for more parameters used for file matching or see the usage examples below.

\* The task is not bundled with Phantom JS, you must specify its location. To ensure the build is portable across different operating systems you must specify the location of the Phantom JS executable for each OS the build may run on.
It is recommended to include the Phantom JS executables within your project.

###Nested elements

####report

Attribute    | Description | Required
------------ | ----------- | ------------------
type         | The type of report (currently only 'plain' is supported) | no (defaults to 'plain')
destFile     | The file to write the report to | yes

##Usage examples

###Typical fileset
Run JS test files. In this example a convention is used where all JS test files have the -test.html suffix

```xml
<jstest dir="${basedir}/src/tests" phantomWin="${basedir}/phantom/win/phantomjs.exe">
    <include name="**/*-test.html"/>
</jstest>
```

###Stop tests failing the build
If you wish, the task can be set to not fail the build if any assertion failures or errors are found.

```xml
<jstest dir="${basedir}/src/tests" fail="false" phantomWin="${basedir}/phantom/win/phantomjs.exe">
    <include name="**/*-test.html"/>
</jstest>
```

###Write results to a file

```xml
<jstest dir="${basedir}/src/tests" phantomWin="${basedir}/phantom/win/phantomjs.exe">
	<report type="plain" destFile="${basdir}/reports/qunit-results.txt"/>
    <include name="**/*-test.html"/>
</jstest>
```

## Running the task in Maven

Ant-jstestrunner isn't deployed to the Maven Central Repository yet, but you can install locally either by cloning the 
project and running

`mvn install`

or downloading the jar release and running

`mvn install:install-file -Dfile=/path/to/ant-jstestrunner-0.1.deps.jar -DgroupId=com.philmander.jstest.ant -DartifactId=ant-jstestrunner -Dversion=0.1 -Dpackaging=jar`

Now use the antrun plugin to add the js test runner to your Maven build

```xml
<plugin>
	<groupId>org.apache.maven.plugins</groupId>
	<artifactId>maven-antrun-plugin</artifactId>
	<version>1.7</version>
	<executions>
		<execution>
			<id>jstest</id>
			<phase>test</phase>
			<configuration>
				<target>
					<!-- Define the task -->
					<taskdef name="jstest" classname="com.philmander.jstest.ant.JsTestRunnerTask" 
					    classpath="maven.plugin.classpath" />
										 
					<!-- Lint the code -->
					<jstest dir="${project.basedir}/src/tests" includes="**/*.html" phantomWin="${project.basedir}/phantom/win/phantomjs.exe" />					    
				</target>
			</configuration>
			<goals>
				<goal>run</goal>
			</goals>
		</execution>
	</executions>
	<dependencies>
		<dependency>
			<groupId>com.philmander.jstest.ant</groupId>
			<artifactId>ant-jstestrunner</artifactId>
			<version>0.1</version>
		</dependency>
	</dependencies>
</plugin>
```
## Fork and run locally ##

Ant-Jstestrunner is built using Apache Maven. 

To run tests against your code run `mvn test`

To create a jar file run `mvn package`