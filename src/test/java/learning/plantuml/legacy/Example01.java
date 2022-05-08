package learning.plantuml.legacy;

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
                @startuml""";
        source +="""
                
                skinparam backgroundColor #AAFFFF
                skinparam linetype polyline
                skinparam activity {
                  StartColor red
                  BarColor SaddleBrown
                  EndColor Silver
                  BackgroundColor Peru
                  BackgroundColor<< Begin >> Olive
                  BorderColor Peru
                  FontName Impact
                }
                """;
        source += """
                (*) -down-> "First Action"
                "First Action" -down->[You can put also labels] "Second Action"
                "Second Action" -up->[label] "First Action"
                 
                "Second Action" -down-> if "Some Test" then
                  -left->[true] "Some Action"
                  "Some Action" --> "Another Action"
                  "Another Action" -down-> (*)
                else
                 -->[false] "Something else"
                 "Something else" -->[Ending process] (*)
                endif
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
