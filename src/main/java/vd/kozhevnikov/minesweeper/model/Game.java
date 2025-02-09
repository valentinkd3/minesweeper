package vd.kozhevnikov.minesweeper.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

import static vd.kozhevnikov.minesweeper.model.Item.EXPLODED_MINE;
import static vd.kozhevnikov.minesweeper.model.Item.MINE;
import static vd.kozhevnikov.minesweeper.model.Item.NONE;

@Getter
public class Game {

    private final Cell[][] gameField;

    private final Set<Cell.Coordinate> checkedCoordinates;

    private final String[][] gameSnapshot;

    private final int height, width, minesCount;

    private boolean completed, isWin;

    public Game(final int height, final int width, final int minesCount) {
        this.width = width;
        this.height = height;
        this.minesCount = minesCount;
        this.gameField = new Cell[height][width];
        this.gameSnapshot = new String[height][width];
        this.checkedCoordinates = new HashSet<>();
        createGame();
    }

    public void makeTurn(Integer x, Integer y) {
        openCells(gameField[y][x]);
        isWin = determineWon();
        if (gameField[y][x].isMine() || isWin) {
            gameOver();
        }
    }

    private void createGame() {
        Set<Cell.Coordinate> mineCoordinates = initMineCoordinates();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell.Coordinate coordinate = new Cell.Coordinate(x, y);
                boolean isMine = mineCoordinates.contains(coordinate);
                gameField[y][x] = new Cell(coordinate, isMine);
                gameSnapshot[y][x] = NONE.getItem();
            }
        }
        countMineNeighbors();
    }

    private Set<Cell.Coordinate> initMineCoordinates() {
        List<Cell.Coordinate> allCells = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                allCells.add(new Cell.Coordinate(x, y));
            }
        }
        Collections.shuffle(allCells);
        return new HashSet<>(allCells.subList(0, minesCount));
    }

    private void countMineNeighbors() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = gameField[y][x];
                if (!cell.isMine()) {
                    int countMineNeighbors = (int) getCellNeighbors(cell).stream()
                            .filter(Cell::isMine)
                            .count();
                    cell.setCountMineNeighbors(countMineNeighbors);
                }
            }
        }
    }

    private List<Cell> getCellNeighbors(final Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        int cellX = cell.getCoordinate().x();
        int cellY = cell.getCoordinate().y();
        for (int y = cellY - 1; y <= cellY + 1; y++) {
            for (int x = cellX - 1; x <= cellX + 1; x++) {
                if (isValidCoordinate(x, y, cell)) {
                    neighbors.add(gameField[y][x]);
                }
            }
        }
        return neighbors;
    }

    private boolean isValidCoordinate(final int x, final int y, final Cell cell) {
        if (x < 0 || x >= width) return false;
        if (y < 0 || y >= height) return false;
        return !cell.equals(gameField[y][x]);
    }

    private void gameOver() {
        completed = true;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = gameField[y][x];
                checkedCoordinates.add(cell.getCoordinate());
                if (cell.isMine()) {
                    gameSnapshot[y][x] = isWin ? MINE.getItem() : EXPLODED_MINE.getItem();
                } else {
                    gameSnapshot[y][x] = String.valueOf(cell.getCountMineNeighbors());
                }
            }
        }
    }

    private void openCells(Cell cell) {
        if (!cell.isMine()) {
            checkedCoordinates.add(cell.getCoordinate());
            gameSnapshot[cell.getCoordinate().y()][cell.getCoordinate().x()] = String.valueOf(cell.getCountMineNeighbors());
            if (cell.getCountMineNeighbors() == 0) {
                getCellNeighbors(cell).stream()
                        .filter(neighbor -> !checkedCoordinates.contains(neighbor.getCoordinate()))
                        .forEach(this::openCells);
            }
        }
    }

    private boolean determineWon() {
        return checkedCoordinates.size() + minesCount == height * width;
    }
}
