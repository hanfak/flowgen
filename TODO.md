# TODO

## Devops

- Github actions 
- jacoco
- sonarcloud
- Add to jitpack
- Add to maven repository

## Features

- Current release
  - TODO: All P1 todos throughout code base, incl failing tests
  - TODO: detach on activity
  - TODO: In all classes Instead of queue, use delegate to hide queue, with delegate using the queue underneath easier to change
  - TODO: In Activity, do labels
  - TODO: In Conditional, pass in factory builder
  - TODO: In Conditional,  naming of ifIsTrue() and orElse()- branchWhen()?
  - TODO: In Conditional,  and() method to chain on to then and/or orElse
  - TODO: In multiConditional, pass in factory builder
  - TODO: In multiConditional, better naming
  - TODO: In Repeat, better naming: is? repeatAgainFor? for isTrueFor; repeatLoopAction? for labelRepeat; exitLoopFor?? for exitOn
  - TODO: In while, better naming: withActivities, doesAction for execute; exitLoopFor? for exitLabel

- NExt release https://plantuml.com/commons#8413c683b4b27cc3
  - TODO: All P2 todos
  - TODO: zoom https://plantuml.com/commons#8413c683b4b27cc3 - TODO: Add examples in docs in prod code
  - TODO: in FlowchartGenerator for each setter Param should be builder, and create object (xxx) in line below for multi line
  - TODO: in FlowchartGenerator for each setter duplicate and add config param
  - TODO: in FlowchartGenerator constructor can pass in <style> to allow user to pass in custom style for all elements
  - TODO: in FlowchartGenerator for setters and, then, last should pass in string param and create the activity in the method
  - TODO: In multiConditional, combine with conditional ??
  - TODO: In RepeatWhen param should be builder, and can remove isTrueFor()/exitOn()/
  - TODO: in Activity Add a builder (Activities) implement Action ie Activities.activity("action2").thenDo("action2")
  - TODO: in Nodes, Might move enums to individual classes, to allow for styling
  - TODO: General styling use of <style>...</style>
  - TODO: arrows, kill, hidden, dotted|dashed|bold|hidden|
  - TODO: STyling on text -> add to individual words or or substring
  - TODO: Add custom themes
  - TODO: styling on actions etc using <color:red> etc
  - TODO: box type - slanted, bars


- Further releases
  - TODO: activity uses a builder to add styling, pass in Content factory (build to string). a builder that build a multiline content (using queue) and have methods for bold, tables, list, lines etc
  - TODO: skinparams for styling
  - TODO: add links to activities, notes, partitions(low priority)
  - TODO: switch (low priority)
  - TODO: partitions
  - TODO: set PLANTUML_LIMIT_SIZE=8192 (low priority)
  - TODO: config, diamond style
  - TODO: Different file formats ie gifs see FileFormat, do some refactoring around generating instead of repetition
  - TODO: write code to generate actions, preprocessing
      - http://www.plantuml.com/plantuml/uml/LOxB2eCm44NtynNZu59ST1aHR3-XxmTT39hIW4d5n3ug_dkZBMYpkZTdEDpCEgvTeqi8migdKffqvdF1ZjEM-Y-LgugDiuJY12qPrf84qlvm98o8RL-UhpTrqOGkL-kHrjRzqyrsDzBc_g0Epj01Y7a2mULMIywl62edDIg3mvXuEWNmzyHm5FTUP8lVnjPRf2cy2CGYWSpdHfSV
    
  - TODO: arrow direction ??? Not available yet