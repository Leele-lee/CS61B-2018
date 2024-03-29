package hw2;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.ArrayList;
import java.util.Arrays;

public class Percolation {
    private final boolean[][] grids;
    private final int rowIndex;
    private final int colIndex;
    private final int vTop;
    private final int vBot;
    private WeightedQuickUnionUF WQUF;
    private WeightedQuickUnionUF WQUFNOBOT;
    private int openNumber;
    private boolean percolationed;
    private ArrayList<Integer> bottom = new ArrayList<>();

    private int xyTo1D(int row, int col) {
        return row * colIndex + col;
    }

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        percolationed = false;
        openNumber = 0;
        rowIndex = N;
        colIndex = N;
        vTop = N * N;
        vBot = N * N + 1;
        WQUF = new WeightedQuickUnionUF(rowIndex * colIndex + 2);
        WQUFNOBOT = new WeightedQuickUnionUF(rowIndex * colIndex + 1);
        grids = new boolean[N][N];
    }

    public void open(int row, int col) {
        if (checkIndexOut(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        if (!isOpen(row, col)) {
            openNumber += 1;
            grids[row][col] = true;
            int currentSide = xyTo1D(row, col);
            checkUnion(row, col);
        }
    }
    private boolean checkIndexOut(int row, int col) {
        return (row >= rowIndex || row < 0 || col < 0 || col >= colIndex);
    }

    private void checkUnion(int row, int col) {
        int currentSide = xyTo1D(row, col);
        int upSide = xyTo1D(row - 1, col);
        int downSide = xyTo1D(row + 1, col);
        int leftSide = xyTo1D(row, col - 1);
        int rightSide = xyTo1D(row, col + 1);
        if (row == 0) {
            WQUF.union(currentSide, vTop);
            WQUFNOBOT.union(currentSide, vTop);
        }
        if (row - 1 >= 0) {
            if (isOpen(row - 1, col)) {
                WQUF.union(upSide, currentSide);
                WQUFNOBOT.union(upSide, currentSide);
            }
        }
        if (row + 1 < rowIndex) {
            if (isOpen(row + 1, col)) {
                WQUF.union(currentSide, downSide);
                WQUFNOBOT.union(currentSide, downSide);
            }
        }
        if (row == rowIndex - 1) {
            //bottom.add(currentSide);
            WQUF.union(currentSide, vBot);
        }
        if (col - 1 >= 0) {
            if (isOpen(row, col - 1)) {
                WQUF.union(currentSide, leftSide);
                WQUFNOBOT.union(currentSide, leftSide);
            }
        }
        if (col + 1 < colIndex) {
            if (isOpen(row, col + 1)) {
                WQUF.union(currentSide, rightSide);
                WQUFNOBOT.union(currentSide, rightSide);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        if (checkIndexOut(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        return grids[row][col];
    }

    public boolean isFull(int row, int col) {
        if (checkIndexOut(row, col)) {
            throw new IndexOutOfBoundsException();
        }
        int currentItem = xyTo1D(row, col);
        return WQUFNOBOT.connected(currentItem, vTop);
    }

    public int numberOfOpenSites() {
        return openNumber;
    }

    public boolean percolates() {
        if (percolationed == false) {
            percolationed = WQUF.connected(vTop, vBot);
            return percolationed;
        }
        return percolationed;
    }
    public static void main(String[] args) { // test client (optional)
    }
}
