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
        String source = "@startuml";
        source += "\n" +
                  "skinparam backgroundColor #AAFFFF\n" +
                  "skinparam linetype polyline\n" +
                  "skinparam activity {\n" +
                  "  StartColor red\n" +
                  "  BarColor SaddleBrown\n" +
                  "  EndColor Silver\n" +
                  "  BackgroundColor Peru\n" +
                  "  BackgroundColor<< Begin >> Olive\n" +
                  "  BorderColor Peru\n" +
                  "  FontName Impact\n" +
                  "}\n";
        source += "(*) -down-> \"First Action\"\n" +
                  "\"First Action\" -down->[You can put also labels] \"Second Action\"\n" +
                  "\"Second Action\" -up->[label] \"First Action\"\n" +
                  "\n" +
                  "\"Second Action\" -down-> if \"Some Test\" then\n" +
                  "  -left->[true] \"Some Action\"\n" +
                  "  \"Some Action\" --> \"Another Action\"\n" +
                  "  \"Another Action\" -down-> (*)\n" +
                  "else\n" +
                  " -->[false] \"Something else\"\n" +
                  " \"Something else\" -->[Ending process] (*)\n" +
                  "endif\n";
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
