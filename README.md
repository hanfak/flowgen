# FlowGen

A Flowchart generator fluent builder library. To help write plantuml description to be able to generate plantuml activity diagrams

Instead of writing plantuml syntax, you write java with the help of a builder to guide you to avoid mistakes

## Benefits

- Compile-time type safety
- More readable interface
- Lots of ways to build your activity diagram using human readable language
- The more complex your activity diagram the easier it is to write and reread
- Run as tests to generate flowcharts which can be stored as html for use as documentation
- Focus on the content of the flowchart/activity diagram instead of syntax
- Easier to style using custom styles or themes
- Generate plantuml markdown as string to be shared or used in web generator
- Generate SVG or PNG files
- (FUTURE) style individual actions
- (FUTURE) Can add custom javascript to html file generated, and make the diagram interactive (ie A glossary where hover over word pop up with definition)
- (FUTURE) copy generated uml and pass to plantuml web UI/api to create link

## Usage

### Pre requisites

- Java 11
- There is only one dependency: plantuml

### For usage 

Here is a simple example which creates a PNG file

```java 
class SimpleExample {
    public static void main(String... args) {
        flowchart()
             .then(ifThe("house is big?")
                    .then(doActivity("sell house"))
                    .or(elseDo(activity("stay"))))
            .createFile(Paths.get("./output.html"));
    }
}
```

Here is a more involved example which creates a html containing the SVG version of the uml diagram, using several features such as groups, loops, decisions, parallel processing, themes etc

```java
class ComplexExample {
    public static void main(String... args) {
        flowchartWith(CLASSIC)
                .withTitle("No Breakfast At Home Journey")
                .withStartNode()
                .with(group("Buy")
                        .containing(an(activity("Go to shop"))
                                .then(doInParallel()
                                        .the(activity("buy butter"), then("buy jam"))
                                        .the(following(activity("buy bread"))
                                                .and(ifIt("is sourdough bread?")
                                                        .then(doActivity("buy"))
                                                        .or(elseDo(activity("ask staff for bread"))))))))
                .with(group("Cook")
                        .containing(an(activity("Put bread in toaster"))
                                .and(then("toast"))
                                .then(check("bread is toasting?").is("yes")
                                        .then(doActivity("wait"))
                                        .leaveWhen("no"))
                                .then(doActivity("take toast and put on plate"))
                                .then(doActivity("spread butter on toast"))))
                .with(group("Dine")
                        .with(doActivity("eat toast")))
                .thenEnd()
                .createFile("./output.html");
    } 
}
```
See for output: [output.html](src/test/java/examples/output.html)

For use of custom formatting, you can use the creole syntax (see nested class Formatting in src/test/java/examples/NotesExamples.java for the different possibilities). In later release, there will be builders to add formatting.

```java 
class CustomFormattingExample {
    public static void main(String... args) {
            flowchart()
                .withTitle("Custom Formatting")
                .with(activity("do some //code// like:\n" +
                               "\"\"foo()\"\""))
                .createFile("./output.html");
    }
}
```
- see tests and examples in src/test folder, for more uses, features and examples.

### Maven instructions

```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
	    <groupId>com.github.hanfak</groupId>
	    <artifactId>flowgen</artifactId>
	    <version>0.0.1</version>
	</dependency>
```

### Gradle instructions

```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
	
	dependencies {
	        implementation 'com.github.hanfak:flowgen:0.0.1'
	}
```

## Issues 

if there are any issues or missing features, please raise an issue and feel free to write a pull request.

Not all features within PlantUML activity diagrams have been accommodated, but I feel that this current release is enough to write the majority of flowcharts (or activity diagrams) that you will need.

I have a list of features that I wish to add see TODO.md file but I do not know when I will get round to them

## CI

TBF

## Sonarcloud

TBF

## Links

- [PlantUML](https://plantuml.com/)
- [For syntax for activity diagram in PlantUML](https://plantuml.com/activity-diagram-beta)
- [PlantUML playground to create your own uml diagrams](http://www.plantuml.com/plantuml/uml)
- [Common syntax that applies to all PlantUML diagrams](https://plantuml.com/commons)