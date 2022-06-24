package examples;

import com.hanfak.flowgen.Theme;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static com.hanfak.flowgen.Activity.andActivity;
import static com.hanfak.flowgen.Activity.doActivity;
import static com.hanfak.flowgen.Conditional.ifIsTrue;
import static com.hanfak.flowgen.FlowchartGenerator.flowchartWith;
import static com.hanfak.flowgen.Label.label;
import static com.hanfak.flowgen.Note.note;

class SwimLaneExamples {

    @Test
    void swimLane() {
        flowchartWith(Theme.CLASSIC)
                .withTitle("Hello")
                .withStartNode()
                .then(doActivity("action1").with(note("some info")).inSwimLane("S1")).with(label("then"))
                .then(doActivity("action2").inSwimLane("S2")).with(label("then next"))
                .then(ifIsTrue("is big?")
                        .thenFor("yes", doActivity("action1").inSwimLane("S1"), andActivity("action3").inSwimLane("S3"))
                        .orElseFor("no", doActivity("action2").with(note("some info")).inSwimLane("S2"))).with(label("finally"))
                .then(doActivity("action4").with(note("some info")).inSwimLane("S4"))
                .then(doActivity("action4").inSwimLane("S3"))
                .withStopNode()
                .createFile(Paths.get("./test1.html"));
    }
}
