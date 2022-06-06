package examples;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.FlowchartGenerator.flowchart;
import static com.hanfak.flowgen.Note.note;

class NotesExamples {

    @Test
    void notesWithNotesOnMultipleActivities() {
        flowchart()
                .withTitle("Hello")
                .withStartNode()
                .then(doActivity("action1").with(note("""
                        Example of Tree
                        |_ First line
                        |_ **Bom (Model)**
                          |_ prop1
                          |_ prop2
                          |_ prop3
                        |_ Last line""")))
                .then(doActivity("action1").with(note("Example of simple table\n|= |= table |= header |\n| a | table | row |\n| b | table | row |")))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }

    @Nested
    class Formatting{
        // TODO: Different formatting with a note
        // https://plantuml.com/creole
        @Test
        void notesWithDifferentFormatting () {
            flowchart()
                    .withTitle("Hello")
                    .withStartNode()
                    .then(doActivity("action1").with(note("""
                            A Note
                            ____
                            new //italic//
                            quoted is monospaced good for code ""foo()""
                            --stricken-out--
                            __underlined__
                            ....
                            ~~wave-underlined~~
                            * List 1
                            ** Sub item
                            * List 2
                            ----
                            # Numbered list
                            # Second item
                            ## Sub item
                            ## Another sub item
                                * Can't quite mix
                                * Numbers and bullets
                            # Third item
                            ====
                            This is not ~""monospaced"" escaping characters
                            ====
                            contain <b>HTML</b>
                            ..My title..
                            = Extra-large heading
                            Some text
                            == Large heading
                            Other text
                            === Medium heading
                            Information
                            ....
                            ==== Small heading
                            """)))
                    .withStopNode()
                    .createFile(Paths.get("./test1.html"));
        }

        @Test
        void notesWithCode () {
            flowchart()
                    .withTitle("Hello")
                    .withStartNode()
                    .then(doActivity("action1").with(note("""
                            <code>
                            This is <b>bold</b>
                            This is <i>italics</i>
                            This is <font:monospaced>monospaced</font>
                            This is <s>stroked</s>
                            This is <u>underlined</u>
                            This is <w>waved</w>
                            This is <s:green>stroked</s>
                            This is <u:red>underlined</u>
                            This is <w:#0000FF>waved</w>
                            This is <b>a bold text containing <plain>plain text</plain> inside</b>
                            -- other examples --
                            This is <color:blue>Blue</color>
                            This is <back:orange>Orange background</back>
                            This is <size:20>big</size>
                            </code>""")))
                    .withStopNode()
                    .createFile(Paths.get("./test1.html"));
        }

        @Test
        void notesWithTable () {
            flowchart()
                    .withTitle("Hello")
                    .withStartNode()
                    .then(doActivity("action1").with(note("""
                            Example of simple table
                            |= |= table |= header |
                            | a | table | row |
                            | b | table | row |""")))
                    .withStopNode()
                    .createFile(Paths.get("./test1.html"));
        }

        @Test
        void notesWithTree () {
            flowchart()
                    .withTitle("Hello")
                    .withStartNode()
                    .then(doActivity("action1").with(note("""
                            Example of Tree
                            |_ First line
                            |_ **Bom (Model)**
                              |_ prop1
                              |_ prop2
                              |_ prop3
                            |_ Last line""")))
                    .withStopNode()
                    .createFile(Paths.get("./test1.html"));
        }
    }
}
