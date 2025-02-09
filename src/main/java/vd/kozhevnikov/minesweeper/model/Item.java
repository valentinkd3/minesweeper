package vd.kozhevnikov.minesweeper.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Item {

    NONE(" "),

    MINE("M"),

    EXPLODED_MINE("X");

    private final String item;
}
