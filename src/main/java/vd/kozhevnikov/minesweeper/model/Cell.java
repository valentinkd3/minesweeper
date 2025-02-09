package vd.kozhevnikov.minesweeper.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cell {

    private final Coordinate coordinate;

    private final boolean isMine;

    private int countMineNeighbors;

    public Cell(int x, int y, boolean isMine) {
        coordinate = new Coordinate(x, y);
        this.isMine = isMine;
    }

    public Cell(Coordinate coordinate, boolean isMine) {
        this.coordinate = coordinate;
        this.isMine = isMine;
    }

    public record Coordinate(int x, int y) {}
}
