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
                !theme spacelab
                """;
        source += """
                
                title PlantUML workflow
                
                start
                group Initialization\s
                    repeat:
                        :action1]
                        floating note right: This is a note
                        :action2;
                    repeat while
                    :action3>
                end group
                group nextStep\s
                    fork
                        group section1
                        :action4<
                        end group
                    fork again
                        group section2
                        :action5;
                        if (color?) is (<color:red>red) then
                            :print red;
                        else\s
                            :print not red;
                        endif
                        end group
                    end fork
                    if (color?) is (<color:red>red) then
                        :print red;
                    else\s
                        :print not red;
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
