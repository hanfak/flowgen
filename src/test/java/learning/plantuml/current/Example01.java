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
        String source = "@startuml\n" +
                        "skin rose\n";
        source += "legend right\n" +
                  "  Short\n" +
                  "  legend\n" +
                  "endlegend\n" +
                  "title PlantUML workflow\n" +
                  "caption figure 1\n" +
                  "start\n" +
                  "-> You can put text on arrows;\n" +
                  "group\n" +
                  "    repeat:\n" +
                  "        -[dotted]->\n" +
                  "        :action1\nhello]\n" +
                  "        floating note left: This is a note\n" +
                  "        :action2;\n" +
                  "        -[dashed]-> then;\n" +
                  "        backward :This is repeated;\n" +
                  "    repeat while (is big?) is (yes) -[dotted]->\n" +
                  "    -> then;\n" +
                  "    :action3>\n" +
                  "    -[dashed]->\n" +
                  "end group\n" +
                  "group nextStep\n" +
                  "    fork\n" +
                  "        -[dashed]->\n" +
                  "        group section1\n" +
                  "        :action4<\n" +
                  "        while (is Big?) is (yes)\n" +
                  "            -[dotted]->\n" +
                  "            :action13;\n" +
                  "            :action23;\n" +
                  "            -[dashed]->\n" +
                  "        end while (no)\n" +
                  "        -[dotted]->\n" +
                  "        :action33;\n" +
                  "        end group\n" +
                  "    fork again\n" +
                  "        group section2\n" +
                  "        :action5;\n" +
                  "        if (color?) is (<color:red>red) then\n" +
                  "            -[dotted]->\n" +
                  "            :action5<\n" +
                  "            -[dashed]->\n" +
                  "            :print red;\n" +
                  "        else\n" +
                  "            -[dashed]->\n" +
                  "            :print not red;\n" +
                  "            -[dotted]->\n" +
                  "        endif\n" +
                  "        -[dashed]->\n" +
                  "        end group\n" +
                  "    fork again\n" +
                  "        split\n" +
                  "            -[dotted]->\n" +
                  "            :action2a;\n" +
                  "        split again\n" +
                  "            -[dotted]->\n" +
                  "            :action2b;\n" +
                  "        split again\n" +
                  "            if (big?) then (yes)\n" +
                  "                -[dashed]->\n" +
                  "                :action;\n" +
                  "            (no) elseif (condition 1?) then (yes)\n" +
                  "                -[dashed]->\n" +
                  "                :action1;\n" +
                  "                :action3;\n" +
                  "            (no) elseif (condition 2?) then (yes)\n" +
                  "                :action2;\n" +
                  "            (no) elseif (condition 3?) then (yes)\n" +
                  "                :action3;\n" +
                  "            else (none)\n" +
                  "                -[dotted]->\n" +
                  "                :action4;\n" +
                  "            endif\n" +
                  "            -[dotted]->\n" +
                  "        end split\n" +
                  "        -[dashed]->\n" +
                  "    end fork\n" +
                  "    if (color?) is (<color:red>red) then\n" +
                  "        :print red;\n" +
                  "        detach\n" +
                  "    else\n" +
                  "        :print not red;\n" +
                  "        detach\n" +
                  "    en\n" +
                  "end group\n";
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
