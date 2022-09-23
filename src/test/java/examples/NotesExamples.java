package examples;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Note.note;

class NotesExamples {

    @Test
    void notesWithNotesOnMultipleActivities() {
        flowchart()
                .withTitle("Hello")
                .withStartNode()
                .then(doActivity("action1").with(note("Example of Tree\n" +
                                                      "|_ First line\n" +
                                                      "|_ **Bom (Model)**\n" +
                                                      "  |_ prop1\n" +
                                                      "  |_ prop2\n" +
                                                      "  |_ prop3\n" +
                                                      "|_ Last line")))
                .then(doActivity("action1").with(note("Example of simple table\n|= |= table |= header |\n| a | table | row |\n| b | table | row |")))
                .withStopNode()
                .createFile("./test1.html");
    }

    @Nested
    class Formatting{
        // https://plantuml.com/creole
        @Test
        void notesWithDifferentFormatting () {
            flowchart()
                    .withTitle("Hello")
                    .withStartNode()
                    .then(doActivity("action1").with(note("A Note\n" +
                                                          "____\n" +
                                                          "new //italic//\n" +
                                                          "quoted is monospaced good for code \"\"foo()\"\"\n" +
                                                          "--stricken-out--\n" +
                                                          "__underlined__\n" +
                                                          "....\n" +
                                                          "~~wave-underlined~~\n" +
                                                          "* List 1\n" +
                                                          "** Sub item\n" +
                                                          "* List 2\n" +
                                                          "----\n" +
                                                          "# Numbered list\n" +
                                                          "# Second item\n" +
                                                          "## Sub item\n" +
                                                          "## Another sub item\n" +
                                                          "    * Can't quite mix\n" +
                                                          "    * Numbers and bullets\n" +
                                                          "# Third item\n" +
                                                          "====\n" +
                                                          "This is not ~\"\"monospaced\"\" escaping characters\n" +
                                                          "====\n" +
                                                          "contain <b>HTML</b>\n" +
                                                          "..My title..\n" +
                                                          "= Extra-large heading\n" +
                                                          "Some text\n" +
                                                          "== Large heading\n" +
                                                          "Other text\n" +
                                                          "=== Medium heading\n" +
                                                          "Information\n" +
                                                          "....\n" +
                                                          "==== Small heading\n")))
                    .withStopNode()
                    .createFile("./test1.html");
        }

        @Test
        void notesWithCode () {
            flowchart()
                    .withTitle("Hello")
                    .withStartNode()
                    .then(doActivity("action1").with(note("<code>\n" +
                                                          "This is <b>bold</b>\n" +
                                                          "This is <i>italics</i>\n" +
                                                          "This is <font:monospaced>monospaced</font>\n" +
                                                          "This is <s>stroked</s>\n" +
                                                          "This is <u>underlined</u>\n" +
                                                          "This is <w>waved</w>\n" +
                                                          "This is <s:green>stroked</s>\n" +
                                                          "This is <u:red>underlined</u>\n" +
                                                          "This is <w:#0000FF>waved</w>\n" +
                                                          "This is <b>a bold text containing <plain>plain text</plain> inside</b>\n" +
                                                          "-- other examples --\n" +
                                                          "This is <color:blue>Blue</color>\n" +
                                                          "This is <back:orange>Orange background</back>\n" +
                                                          "This is <size:20>big</size>\n" +
                                                          "</code>")))
                    .withStopNode()
                    .createFile("./test1.html");
        }

        @Test
        void notesWithTable () {
            flowchart()
                    .withTitle("Hello")
                    .withStartNode()
                    .then(doActivity("action1").with(note("Example of simple table\n" +
                                                          "|= |= table |= header |\n" +
                                                          "| a | table | row |\n" +
                                                          "| b | table | row |")))
                    .withStopNode()
                    .createFile("./test1.html");
        }

        @Test
        void notesWithTree () {
            flowchart()
                    .withTitle("Hello")
                    .withStartNode()
                    .then(doActivity("action1").with(note("Example of Tree\n" +
                                                          "|_ First line\n" +
                                                          "|_ **Bom (Model)**\n" +
                                                          "  |_ prop1\n" +
                                                          "  |_ prop2\n" +
                                                          "  |_ prop3\n" +
                                                          "|_ Last line")))
                    .withStopNode()
                    .createFile("./test1.html");
        }
    }
}
