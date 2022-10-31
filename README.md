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

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?><svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" contentStyleType="text/css" height="950px" preserveAspectRatio="none" style="width:405px;height:950px;background:#FFFFFF;" version="1.1" viewBox="0 0 405 950" width="405px" zoomAndPan="magnify"><defs><filter height="300%" id="f1luvia3l4d82t" width="300%" x="-1" y="-1"><feGaussianBlur result="blurOut" stdDeviation="2.0"/><feColorMatrix in="blurOut" result="blurOut2" type="matrix" values="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 .4 0"/><feOffset dx="4.0" dy="4.0" in="blurOut2" result="blurOut3"/><feBlend in="SourceGraphic" in2="blurOut3" mode="normal"/></filter></defs><g><rect fill="none" height="27.6094" id="_title" style="stroke:none;stroke-width:1.0;" width="223" x="89.75" y="15"/><text fill="#000000" font-family="sans-serif" font-size="14" font-weight="bold" lengthAdjust="spacing" textLength="213" x="94.75" y="34.5332">No Breakfast At Home Journey</text><ellipse cx="204.75" cy="58.6094" fill="#000000" filter="url(#f1luvia3l4d82t)" rx="10" ry="10" style="stroke:#000000;stroke-width:1.0;"/><rect fill="none" filter="url(#f1luvia3l4d82t)" height="325.8906" style="stroke:#000000;stroke-width:1.0;" width="377.5" x="11" y="78.6094"/><path d="M45,78.6094 L45,89.2188 L35,99.2188 L11,99.2188 " fill="none" style="stroke:#000000;stroke-width:1.0;"/><text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="24" x="14" y="94.1426">Buy</text><rect fill="#FEFECE" filter="url(#f1luvia3l4d82t)" height="35.0938" rx="12.5" ry="12.5" style="stroke:#A80036;stroke-width:1.5;" width="80" x="164.75" y="116.2188"/><text fill="#000000" font-family="sans-serif" font-size="12" lengthAdjust="spacing" textLength="60" x="174.75" y="138.6758">Go to shop</text><rect fill="#000000" filter="url(#f1luvia3l4d82t)" height="6" rx="2.5" ry="2.5" style="stroke:#000000;stroke-width:1.0;" width="357.5" x="21" y="171.3125"/><rect fill="#FEFECE" filter="url(#f1luvia3l4d82t)" height="35.0938" rx="12.5" ry="12.5" style="stroke:#A80036;stroke-width:1.5;" width="73" x="33" y="229.3125"/><text fill="#000000" font-family="sans-serif" font-size="12" lengthAdjust="spacing" textLength="53" x="43" y="251.7695">buy butter</text><rect fill="#FEFECE" filter="url(#f1luvia3l4d82t)" height="35.0938" rx="12.5" ry="12.5" style="stroke:#A80036;stroke-width:1.5;" width="63" x="38" y="299.4063"/><text fill="#000000" font-family="sans-serif" font-size="12" lengthAdjust="spacing" textLength="43" x="48" y="321.8633">buy jam</text><rect fill="#FEFECE" filter="url(#f1luvia3l4d82t)" height="35.0938" rx="12.5" ry="12.5" style="stroke:#A80036;stroke-width:1.5;" width="74" x="198.5" y="197.3125"/><text fill="#000000" font-family="sans-serif" font-size="12" lengthAdjust="spacing" textLength="54" x="208.5" y="219.7695">buy bread</text><polygon fill="#FEFECE" filter="url(#f1luvia3l4d82t)" points="185.5,267.4063,285.5,267.4063,297.5,279.4063,285.5,291.4063,185.5,291.4063,173.5,279.4063,185.5,267.4063" style="stroke:#A80036;stroke-width:1.5;"/><text fill="#000000" font-family="sans-serif" font-size="11" lengthAdjust="spacing" textLength="100" x="185.5" y="283.9072">is sourdough bread?</text><rect fill="#FEFECE" filter="url(#f1luvia3l4d82t)" height="35.0938" rx="12.5" ry="12.5" style="stroke:#A80036;stroke-width:1.5;" width="39" x="144" y="301.4063"/><text fill="#000000" font-family="sans-serif" font-size="12" lengthAdjust="spacing" textLength="19" x="154" y="323.8633">buy</text><rect fill="#FEFECE" filter="url(#f1luvia3l4d82t)" height="35.0938" rx="12.5" ry="12.5" style="stroke:#A80036;stroke-width:1.5;" width="118" x="248.5" y="301.4063"/><text fill="#000000" font-family="sans-serif" font-size="12" lengthAdjust="spacing" textLength="98" x="258.5" y="323.8633">ask staff for bread</text><polygon fill="#FEFECE" filter="url(#f1luvia3l4d82t)" points="235.5,342.5,247.5,354.5,235.5,366.5,223.5,354.5,235.5,342.5" style="stroke:#A80036;stroke-width:1.5;"/><rect fill="#000000" filter="url(#f1luvia3l4d82t)" height="6" rx="2.5" ry="2.5" style="stroke:#000000;stroke-width:1.0;" width="357.5" x="21" y="386.5"/><rect fill="none" filter="url(#f1luvia3l4d82t)" height="383.9971" style="stroke:#000000;stroke-width:1.0;" width="184" x="112.75" y="414.5"/><path d="M155.75,414.5 L155.75,425.1094 L145.75,435.1094 L112.75,435.1094 " fill="none" style="stroke:#000000;stroke-width:1.0;"/><text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="33" x="115.75" y="430.0332">Cook</text><rect fill="#FEFECE" filter="url(#f1luvia3l4d82t)" height="35.0938" rx="12.5" ry="12.5" style="stroke:#A80036;stroke-width:1.5;" width="127" x="141.25" y="452.1094"/><text fill="#000000" font-family="sans-serif" font-size="12" lengthAdjust="spacing" textLength="107" x="151.25" y="474.5664">Put bread in toaster</text><rect fill="#FEFECE" filter="url(#f1luvia3l4d82t)" height="35.0938" rx="12.5" ry="12.5" style="stroke:#A80036;stroke-width:1.5;" width="47" x="181.25" y="507.2031"/><text fill="#000000" font-family="sans-serif" font-size="12" lengthAdjust="spacing" textLength="27" x="191.25" y="529.6602">toast</text><rect fill="#FEFECE" filter="url(#f1luvia3l4d82t)" height="35.0938" rx="12.5" ry="12.5" style="stroke:#A80036;stroke-width:1.5;" width="42" x="183.75" y="619.2158"/><text fill="#000000" font-family="sans-serif" font-size="12" lengthAdjust="spacing" textLength="22" x="193.75" y="641.6729">wait</text><polygon fill="#FEFECE" filter="url(#f1luvia3l4d82t)" points="161.75,562.2969,247.75,562.2969,259.75,574.2969,247.75,586.2969,161.75,586.2969,149.75,574.2969,161.75,562.2969" style="stroke:#A80036;stroke-width:1.5;"/><text fill="#000000" font-family="sans-serif" font-size="11" lengthAdjust="spacing" textLength="18" x="208.75" y="597.7158">yes</text><text fill="#000000" font-family="sans-serif" font-size="11" lengthAdjust="spacing" textLength="86" x="161.75" y="578.7979">bread is toasting?</text><text fill="#000000" font-family="sans-serif" font-size="11" lengthAdjust="spacing" textLength="12" x="137.75" y="571.8799">no</text><rect fill="#FEFECE" filter="url(#f1luvia3l4d82t)" height="35.0938" rx="12.5" ry="12.5" style="stroke:#A80036;stroke-width:1.5;" width="164" x="122.75" y="696.3096"/><text fill="#000000" font-family="sans-serif" font-size="12" lengthAdjust="spacing" textLength="144" x="132.75" y="718.7666">take toast and put on plate</text><rect fill="#FEFECE" filter="url(#f1luvia3l4d82t)" height="35.0938" rx="12.5" ry="12.5" style="stroke:#A80036;stroke-width:1.5;" width="140" x="134.75" y="751.4033"/><text fill="#000000" font-family="sans-serif" font-size="12" lengthAdjust="spacing" textLength="120" x="144.75" y="773.8604">spread butter on toast</text><rect fill="none" filter="url(#f1luvia3l4d82t)" height="84.7031" style="stroke:#000000;stroke-width:1.0;" width="87" x="161.25" y="808.4971"/><path d="M200.25,808.4971 L200.25,819.1064 L190.25,829.1064 L161.25,829.1064 " fill="none" style="stroke:#000000;stroke-width:1.0;"/><text fill="#000000" font-family="sans-serif" font-size="14" lengthAdjust="spacing" textLength="29" x="164.25" y="824.0303">Dine</text><rect fill="#FEFECE" filter="url(#f1luvia3l4d82t)" height="35.0938" rx="12.5" ry="12.5" style="stroke:#A80036;stroke-width:1.5;" width="67" x="171.25" y="846.1064"/><text fill="#000000" font-family="sans-serif" font-size="12" lengthAdjust="spacing" textLength="47" x="181.25" y="868.5635">eat toast</text><ellipse cx="204.75" cy="923.2002" fill="#FFFFFF" filter="url(#f1luvia3l4d82t)" rx="10" ry="10" style="stroke:#000000;stroke-width:1.5;"/><line style="stroke:#000000;stroke-width:2.5;" x1="198.5628" x2="210.9372" y1="917.013" y2="929.3874"/><line style="stroke:#000000;stroke-width:2.5;" x1="210.9372" x2="198.5628" y1="917.013" y2="929.3874"/><line style="stroke:#A80036;stroke-width:1.5;" x1="69.5" x2="69.5" y1="264.4063" y2="299.4063"/><polygon fill="#A80036" points="65.5,289.4063,69.5,299.4063,73.5,289.4063,69.5,293.4063" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="173.5" x2="163.5" y1="279.4063" y2="279.4063"/><line style="stroke:#A80036;stroke-width:1.5;" x1="163.5" x2="163.5" y1="279.4063" y2="301.4063"/><polygon fill="#A80036" points="159.5,291.4063,163.5,301.4063,167.5,291.4063,163.5,295.4063" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="297.5" x2="307.5" y1="279.4063" y2="279.4063"/><line style="stroke:#A80036;stroke-width:1.5;" x1="307.5" x2="307.5" y1="279.4063" y2="301.4063"/><polygon fill="#A80036" points="303.5,291.4063,307.5,301.4063,311.5,291.4063,307.5,295.4063" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="163.5" x2="163.5" y1="336.5" y2="354.5"/><line style="stroke:#A80036;stroke-width:1.5;" x1="163.5" x2="223.5" y1="354.5" y2="354.5"/><polygon fill="#A80036" points="213.5,350.5,223.5,354.5,213.5,358.5,217.5,354.5" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="307.5" x2="307.5" y1="336.5" y2="354.5"/><line style="stroke:#A80036;stroke-width:1.5;" x1="307.5" x2="247.5" y1="354.5" y2="354.5"/><polygon fill="#A80036" points="257.5,350.5,247.5,354.5,257.5,358.5,253.5,354.5" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="235.5" x2="235.5" y1="232.4063" y2="267.4063"/><polygon fill="#A80036" points="231.5,257.4063,235.5,267.4063,239.5,257.4063,235.5,261.4063" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="69.5" x2="69.5" y1="177.3125" y2="229.3125"/><polygon fill="#A80036" points="65.5,219.3125,69.5,229.3125,73.5,219.3125,69.5,223.3125" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="235.5" x2="235.5" y1="177.3125" y2="197.3125"/><polygon fill="#A80036" points="231.5,187.3125,235.5,197.3125,239.5,187.3125,235.5,191.3125" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="69.5" x2="69.5" y1="334.5" y2="386.5"/><polygon fill="#A80036" points="65.5,376.5,69.5,386.5,73.5,376.5,69.5,380.5" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="235.5" x2="235.5" y1="366.5" y2="386.5"/><polygon fill="#A80036" points="231.5,376.5,235.5,386.5,239.5,376.5,235.5,380.5" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="204.75" x2="204.75" y1="151.3125" y2="171.3125"/><polygon fill="#A80036" points="200.75,161.3125,204.75,171.3125,208.75,161.3125,204.75,165.3125" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="204.75" x2="204.75" y1="68.6094" y2="116.2188"/><polygon fill="#A80036" points="200.75,106.2188,204.75,116.2188,208.75,106.2188,204.75,110.2188" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="204.75" x2="204.75" y1="487.2031" y2="507.2031"/><polygon fill="#A80036" points="200.75,497.2031,204.75,507.2031,208.75,497.2031,204.75,501.2031" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="204.75" x2="204.75" y1="586.2969" y2="619.2158"/><polygon fill="#A80036" points="200.75,609.2158,204.75,619.2158,208.75,609.2158,204.75,613.2158" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="204.75" x2="204.75" y1="654.3096" y2="664.3096"/><line style="stroke:#A80036;stroke-width:1.5;" x1="204.75" x2="271.75" y1="664.3096" y2="664.3096"/><polygon fill="#A80036" points="267.75,627.8447,271.75,617.8447,275.75,627.8447,271.75,623.8447" style="stroke:#A80036;stroke-width:1.5;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="271.75" x2="271.75" y1="574.2969" y2="664.3096"/><line style="stroke:#A80036;stroke-width:1.5;" x1="271.75" x2="259.75" y1="574.2969" y2="574.2969"/><polygon fill="#A80036" points="269.75,570.2969,259.75,574.2969,269.75,578.2969,265.75,574.2969" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="149.75" x2="137.75" y1="574.2969" y2="574.2969"/><polygon fill="#A80036" points="133.75,613.8447,137.75,623.8447,141.75,613.8447,137.75,617.8447" style="stroke:#A80036;stroke-width:1.5;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="137.75" x2="137.75" y1="574.2969" y2="676.3096"/><line style="stroke:#A80036;stroke-width:1.5;" x1="137.75" x2="204.75" y1="676.3096" y2="676.3096"/><line style="stroke:#A80036;stroke-width:1.5;" x1="204.75" x2="204.75" y1="676.3096" y2="696.3096"/><polygon fill="#A80036" points="200.75,686.3096,204.75,696.3096,208.75,686.3096,204.75,690.3096" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="204.75" x2="204.75" y1="542.2969" y2="562.2969"/><polygon fill="#A80036" points="200.75,552.2969,204.75,562.2969,208.75,552.2969,204.75,556.2969" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="204.75" x2="204.75" y1="731.4033" y2="751.4033"/><polygon fill="#A80036" points="200.75,741.4033,204.75,751.4033,208.75,741.4033,204.75,745.4033" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="204.75" x2="204.75" y1="392.5" y2="452.1094"/><polygon fill="#A80036" points="200.75,442.1094,204.75,452.1094,208.75,442.1094,204.75,446.1094" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="204.75" x2="204.75" y1="786.4971" y2="846.1064"/><polygon fill="#A80036" points="200.75,836.1064,204.75,846.1064,208.75,836.1064,204.75,840.1064" style="stroke:#A80036;stroke-width:1.0;"/><line style="stroke:#A80036;stroke-width:1.5;" x1="204.75" x2="204.75" y1="881.2002" y2="913.2002"/><polygon fill="#A80036" points="200.75,903.2002,204.75,913.2002,208.75,903.2002,204.75,907.2002" style="stroke:#A80036;stroke-width:1.0;"/><!--MD5=[4cc6d6415dd58358b7a1ff7b87e0153b]
```

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
