package com.hanfak.flowgen;

public enum Theme {

    NONE("none"),
    AMIGA("amiga"),
    AWS_ORANGE("aws-orange"),
    BLACK_KNIGHT("black-knight"),
    BLUEGRAY("bluegray"),
    BLUEPRINT("blueprint"),
    CERULEAN("cerulean"),
    CERULEAN_OUTLINE("cerulean-outline"),
    CRT_AMBER("crt-amber"),
    CRT_GREEN("crt-green"),
    CYBORG("cyborg"),
    CYBORG_OUTLINE("cyborg-outline"),
    HACKER("hacker"),
    LIGHTGRAY("lightgray"),
    MARS("mars"),
    MATERIA("materia"),
    MATERIA_OUTLINE("materia-outline"),
    METAL("metal"),
    MIMEOGRAPH("mimeograph"),
    MINTY("minty"),
    PLAIN("plain"),
    REDDRESS_DARKBLUE("reddress-darkblue"),
    REDDRESS_DARKGREEN("reddress-darkgreen"),
    REDDRESS_DARKORANGE("reddress-darkorange"),
    REDDRESS_DARKRED("reddress-darkred"),
    REDDRESS_LIGHTBLUE("reddress-lightblue"),
    REDDRESS_LIGHTGREEN("reddress-lightgreen"),
    REDDRESS_LIGHTORANGE("reddress-lightorange"),
    REDDRESS_LIGHTRED("reddress-lightred"),
    SANDSTONE("sandstone"),
    SILVER("silver"),
    SKETCHY("sketchy"),
    SKETCHY_OUTLINE("sketchy-outline"),
    SPACELAB("spacelab"),
    SPACELAB_WHITE("spacelab-white"),
    SUPERHERO("superhero"),
    SUPERHERO_OUTLINE("superhero-outline"),
    TOY("toy"),
    UNITED("united"),
    VIBRANT("vibrant");

    private final String value;

    Theme(String value) {
        this.value = value;
    }

    String value() {
        return value;
    }
}








































