package com.hanfak.flowgen;

// TODO: Deletes text on arrow ie using label
public enum Theme {
    CLASSIC("skin rose"),
    NONE("!theme none"),
    AMIGA("!theme amiga"),
    AWS_ORANGE("!theme aws-orange"),
    BLACK_KNIGHT("!theme black-knight"),//n
    BLUEGRAY("!theme bluegray"),
    BLUEPRINT("!theme blueprint"),
    CERULEAN("!theme cerulean"),
    CERULEAN_OUTLINE("!theme cerulean-outline"),
    CRT_AMBER("!theme crt-amber"),
    CRT_GREEN("!theme crt-green"),
    CYBORG("!theme cyborg"), //n
    CYBORG_OUTLINE("!theme cyborg-outline"), //n
    HACKER("!theme hacker"), //n
    LIGHTGRAY("!theme lightgray"),
    MARS("!theme mars"),
    MATERIA("!theme materia"),
    MATERIA_OUTLINE("!theme materia-outline"),
    METAL("!theme metal"),
    MIMEOGRAPH("!theme mimeograph"),
    MINTY("!theme minty"), //n
    PLAIN("!theme plain"),
    REDDRESS_DARKBLUE("!theme reddress-darkblue"),
    REDDRESS_DARKGREEN("!theme reddress-darkgreen"),
    REDDRESS_DARKORANGE("!theme reddress-darkorange"),
    REDDRESS_DARKRED("!theme reddress-darkred"),
    REDDRESS_LIGHTBLUE("!theme reddress-lightblue"),
    REDDRESS_LIGHTGREEN("!theme reddress-lightgreen"),
    REDDRESS_LIGHTORANGE("!theme reddress-lightorange"),
    REDDRESS_LIGHTRED("!theme reddress-lightred"),
    SANDSTONE("!theme sandstone"), //n
    SILVER("!theme silver"),
    SKETCHY("!theme sketchy"), //n
    SKETCHY_OUTLINE("!theme sketchy-outline"),
    SPACELAB("!theme spacelab"), //n
    SPACELAB_WHITE("!theme spacelab-white"),//n
    SUPERHERO("!theme superhero"), //n
    SUPERHERO_OUTLINE("!theme superhero-outline"), //n
    TOY("!theme toy"),
    UNITED("!theme united"), //n
    VIBRANT("!theme vibrant");

    private final String value;

    Theme(String value) {
        this.value = value;
    }

    String value() {
        return value;
    }
}








































