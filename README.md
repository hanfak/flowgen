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

Here is a simple example

```java 
class ComplexExample {
    public static void main(String... args) {
    
    }
}
```

Here is a more involved example which creates a html containing the SVG version of the uml diagram, using several features such as groups, loops, decisions, parallel processing, themes etc

```java
class ComplexExample {
    public static void main(String... args) {
        flowchartWith(CLASSIC)
                .withTitle("No Breakfast Journey")
                .withStartNode()
                .then(group("Buy")
                        .containing(an(activity("Go to shop"))
                                .then(doInParallel()
                                        .and(activity("buy butter"), and("buy jam"))
                                        .the(following(activity("buy bread")).and(ifIsTrue("is sourdough bread?")
                                                .then(doActivity("buy"))
                                                .or(elseDo(activity("ask staff for bread"))))))))
                .then(group("Cook")
                        .containing(an(activity("Put bread in toaster"))
                                .and(then("toast"))
                                .then(check("bread is toasting?").then(doActivity("wait")).leaveWhen("no"))
                                .then(doActivity("take toast and put on plate"))
                                .then(doActivity("spread butter on toast"))))
                .then(group("Dine")
                        .with(doActivity("eat toast")))
                .thenEnd()
                .createFile(Paths.get("./output.html"));
    } 
}
```
- see tests and examples in src/test folder, for more uses, features and examples.

### Maven instructions

TBF

### Gradle instructions

TBF

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