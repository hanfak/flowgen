package learning.plantuml.current;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Example01 {

    public static void main(String... args) throws IOException {
//
        String source = """
                @startuml
                skin rose
                """;
        source += """
                
                title PlantUML workflow
                
                start
                -> You can put text on arrows;
                group Initialization
                    repeat:
                        -[dotted]->
                        :action1]
                        floating note left: This is a note
                        :action2;
                        -[dashed]-> then;
                        backward :This is repeated;
                    repeat while (is big?) is (yes) -[dotted]->
                    -> then;
                    :action3>
                    -[dashed]->
                end group
                group nextStep
                    fork
                        -[dashed]->
                        group section1
                        :action4<
                        while (is Big?) is (yes)
                            -[dotted]->
                            :action13;
                            :action23;
                            -[dashed]->
                        endwhile (no)
                        -[dotted]->
                        :action33;
                        end group
                    fork again
                        group section2
                        :action5;
                        if (color?) is (<color:red>red) then
                            -[dotted]->
                            :action5<
                            -[dashed]->
                            :print red;
                        else
                            -[dashed]->
                            :print not red;
                            -[dotted]->
                        endif
                        -[dashed]->
                        end group
                    fork again
                        split
                            -[dotted]->
                            :action2a;
                        split again
                            -[dotted]->
                            :action2b;
                        split again
                            if (big?) then (yes)
                                -[dashed]->
                                :action;
                            (no) elseif (condition 1?) then (yes)
                                -[dashed]->
                                :action1;
                                :action3;
                            (no) elseif (condition 2?) then (yes)
                                :action2;
                            (no) elseif (condition 3?) then (yes)
                                :action3;
                            else (none)
                                -[dotted]->
                                :action4;
                            endif
                            -[dotted]->
                        end split
                        -[dashed]->
                    end fork
                    if (color?) is (<color:red>red) then
                        :print red;
                        detach
                    else
                        :print not red;
                        detach
                    endif
                end group
                """;
        source += "@enduml\n";

        SourceStringReader reader = new SourceStringReader(source);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // Write the first image to "os"
        DiagramDescription desc = reader.outputImage(os, new FileFormatOption(FileFormat.SVG));
        System.out.println("desc = " + desc);
        os.close();

        // The XML is stored into svg
        final String svg = os.toString(UTF_8);
        System.out.println("svg = \n" + svg);

        try {
            Files.write(Paths.get("./test.html"), svg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
