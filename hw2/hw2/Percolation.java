package hw2;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.util.ArrayList;


import java.util.Arrays;

public class Percolation {
    private boolean[][] grids;
    private int rowIndex;
    private int colIndex;
    private int vTop;
    private int vBot;
    private WeightedQuickUnionUF WQUF;
    private int openNumber;
    private ArrayList<Integer> bottom = new ArrayList<>();

    private int xyTo1D(int row, int col) {
        return row * colIndex + col;
    }

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        openNumber = 0;
        rowIndex = N;
        colIndex = N;
        vTop = N * N;
        vBot = N * N + 1;
        WQUF = new WeightedQuickUnionUF(rowIndex * colIndex + 2);
        grids = new boolean[N][N];
        //for (int i = 0; i < N; i++) {
        // Arrays.fill(grids[i], false);
        //}
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
        }
        if (row - 1 >= 0) {
            if (isOpen(row - 1, col)) {
                WQUF.union(upSide, currentSide);
            }
        }
        if (row + 1 < rowIndex) {
            if (isOpen(row + 1, col)) {
                WQUF.union(currentSide, downSide);
            }
        }
        if (row == rowIndex - 1) {
            bottom.add(currentSide);
        }
        if (col - 1 >= 0) {
            if (isOpen(row, col - 1)) {
                WQUF.union(currentSide, leftSide);
            }
        }
        if (col + 1 < colIndex) {
            if (isOpen(row, col + 1)) {
                WQUF.union(currentSide, rightSide);
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
        return WQUF.connected(currentItem, vTop);
    }

    public int numberOfOpenSites() {
        return openNumber;
    }

    public boolean percolates() {
        for (Integer i : bottom) {
            WQUF.union(i, vBot);
        }
        return WQUF.connected(vTop, vBot);
    }
}
