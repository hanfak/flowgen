package com.hanfak.flowgen;

// TODO: Deletes text on arrow ie using label
public enum Theme {

    NONE("none"),
    AMIGA("amiga"),
    AWS_ORANGE("aws-orange"),
    BLACK_KNIGHT("black-knight"),//n
    BLUEGRAY("bluegray"),
    BLUEPRINT("blueprint"),
    CERULEAN("cerulean"),
    CERULEAN_OUTLINE("cerulean-outline"),
    CRT_AMBER("crt-amber"),
    CRT_GREEN("crt-green"),
    CYBORG("cyborg"), //n
    CYBORG_OUTLINE("cyborg-outline"), //n
    HACKER("hacker"), //n
    LIGHTGRAY("lightgray"),
    MARS("mars"),
    MATERIA("materia"),
    MATERIA_OUTLINE("materia-outline"),
    METAL("metal"),
    MIMEOGRAPH("mimeograph"),
    MINTY("minty"), //n
    PLAIN("plain"),
    REDDRESS_DARKBLUE("reddress-darkblue"),
    REDDRESS_DARKGREEN("reddress-darkgreen"),
    REDDRESS_DARKORANGE("reddress-darkorange"),
    REDDRESS_DARKRED("reddress-darkred"),
    REDDRESS_LIGHTBLUE("reddress-lightblue"),
    REDDRESS_LIGHTGREEN("reddress-lightgreen"),
    REDDRESS_LIGHTORANGE("reddress-lightorange"),
    REDDRESS_LIGHTRED("reddress-lightred"),
    SANDSTONE("sandstone"), //n
    SILVER("silver"),
    SKETCHY("sketchy"), //n
    SKETCHY_OUTLINE("sketchy-outline"),
    SPACELAB("spacelab"), //n
    SPACELAB_WHITE("spacelab-white"),//n
    SUPERHERO("superhero"), //n
    SUPERHERO_OUTLINE("superhero-outline"), //n
    TOY("toy"),
    UNITED("united"), //n
    VIBRANT("vibrant");

    private final String value;

    Theme(String value) {
        this.value = value;
    }

    String value() {
        return value;
    }
}








































