package com.hanfak.flowgen;

public class Note implements Action {

    private final String value;
    private boolean left;
    private boolean floating;

    private Note(String value) {
        this.value = value;
    }

    public static Note note(String value) {
        return new Note(value);
    }

    public Note left() {
        this.left = true;
        return this;
    }

    public Note right() {
        if (this.left) {
            this.left = false;
        }
        return this;
    }

    public Note floating() {
        this.floating = true;
        return this;
    }

    @Override
    public String build() {
        if (floating && left) {
            String DEFAULT_FLOATING_LEFT_TEMPLATE = "note floating left%n%s%nend note%n";
            return DEFAULT_FLOATING_LEFT_TEMPLATE.formatted(value);
        }

        if (left) {
            String LEFT_NOTE_TEMPLATE = "note left%n%s%nend note%n";
            return LEFT_NOTE_TEMPLATE.formatted(value);
        }

        if (floating) {
            String DEFAULT_FLOATING_TEMPLATE = "note floating right%n%s%nend note%n";
            return DEFAULT_FLOATING_TEMPLATE.formatted(value);
        }

        String DEFAULT_RIGHT_TEMPLATE = "note right%n%s%nend note%n";
        return DEFAULT_RIGHT_TEMPLATE.formatted(value);
    }
}
