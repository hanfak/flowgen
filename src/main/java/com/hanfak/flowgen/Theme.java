package com.hanfak.flowgen;

public enum Theme {
    CLASSIC("skin rose"),
    DEBUG("skin debug"),
    PLANT_UML("skin plantuml"),
    PLANT_UML_TWO("skin plantuml2"),
    NONE("!theme none"),
    AMIGA("!theme amiga"),
    AWS_ORANGE("!theme aws-orange"),
    BLUEGRAY("!theme bluegray"),
    BLUEPRINT("!theme blueprint"),
    CERULEAN("!theme cerulean"),
    CERULEAN_OUTLINE("!theme cerulean-outline"),
    CRT_AMBER("!theme crt-amber"),
    CRT_GREEN("!theme crt-green"),
    LIGHTGRAY("!theme lightgray"),
    MARS("!theme mars"),
    MATERIA("!theme materia"),
    MATERIA_OUTLINE("!theme materia-outline"),
    METAL("!theme metal"),
    MIMEOGRAPH("!theme mimeograph"),
    PLAIN("!theme plain"),
    REDDRESS_DARKBLUE("!theme reddress-darkblue"),
    REDDRESS_DARKGREEN("!theme reddress-darkgreen"),
    REDDRESS_DARKORANGE("!theme reddress-darkorange"),
    REDDRESS_DARKRED("!theme reddress-darkred"),
    REDDRESS_LIGHTBLUE("!theme reddress-lightblue"),
    REDDRESS_LIGHTGREEN("!theme reddress-lightgreen"),
    REDDRESS_LIGHTORANGE("!theme reddress-lightorange"),
    REDDRESS_LIGHTRED("!theme reddress-lightred"),
    SILVER("!theme silver"),
    SKETCHY_OUTLINE("!theme sketchy-outline"),
    TOY("!theme toy"),
    VIBRANT("!theme vibrant"),

    // These themes hide the labels on arrows
    BLACK_KNIGHT("!theme black-knight"),
    CYBORG("!theme cyborg"),
    CYBORG_OUTLINE("!theme cyborg-outline"),
    HACKER("!theme hacker"),
    MINTY("!theme minty"),
    SANDSTONE("!theme sandstone"),
    SKETCHY("!theme sketchy"),
    SPACELAB("!theme spacelab"),
    SPACELAB_WHITE("!theme spacelab-white"),
    SUPERHERO_OUTLINE("!theme superhero-outline"),
    SUPERHERO("!theme superhero"),
    UNITED("!theme united");

    private final String value;

    Theme(String value) {
        this.value = value;
    }

    String value() {
        return value;
    }
}








































