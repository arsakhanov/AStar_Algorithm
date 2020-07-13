package ru.arsakhanov;

/**
 * Класс описывает работу алгоритма А*
 */

import java.util.Comparator;
import java.util.PriorityQueue;


public class AStar {

    //количество для диагонального и вертикального/горизонтального перехода
    public static final int DIAGONAL_COST = 14;
    public static final int V_H_COST = 10;
    //Клетки для нашей сетки
    public Cell[][] grid;
    //Мы сделаем приоритетную очередь для открытой клетки
    //Open Cells: the set of nodes to ne evaluated
    //we put cells with lowest cost in first
    private PriorityQueue<Cell> openCells;
    //Closed Cells: the set of nodes already evaluated
    private boolean[][] closedCells;
    //Start cell
    private int startI, startJ;
    //End Cell
    private int endI, endJ;

    public AStar(int width, int height, int si, int sj, int ei, int ej, int[][] blocks) {
        grid = new Cell[width][height];
        closedCells = new boolean[width][height];
        openCells = new PriorityQueue<>(Comparator.comparingInt((Cell c) -> c.finalCost));

        startCell(si, sj);
        endCell(ei, ej);

        //init heuristic and cells
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell(i, j);
                grid[i][j].heuresticCost = Math.abs(i - endI) + Math.abs(j - endJ);
                grid[i][j].solution = false;
            }
        }

        grid[startI][startJ].finalCost = 0;

        //we put the blocks in the grid
        for (int[] block : blocks) {
            addBlockOnCell(block[0], block[1]);
        }
    }

    /**
     * добавление блока в сетку
     * @param i в параметры передается индекс массива
     * @param j в параметры передается индекс массива
     */
    public void addBlockOnCell(int i, int j) {
        grid[i][j] = null;
    }

    /**
     * начальная точка пути
     * @param i в параметр передается индекс массива
     * @param j в параметр передается индекс массива
     */
    public void startCell(int i, int j) {
        startI = i;
        startJ = j;
    }

    /**
     * конечная точка пути
     * @param i в параметр передается индекс массива
     * @param j в параметр передается индекс массива
     */
    public void endCell(int i, int j) {
        endI = i;
        endJ = j;
    }

    /**
     *
     * @param current
     * @param t
     * @param cost
     */
    public void updateCostIfNeeded(Cell current, Cell t, int cost) {
        if (t == null || closedCells[t.i][t.j])
            return;

        int tFinalCost = t.heuresticCost + cost;
        boolean isOpen = openCells.contains(t);

        if (!isOpen || tFinalCost < t.finalCost) {
            t.finalCost = tFinalCost;
            t.parent = current;

            if (!isOpen) {
                openCells.add(t);
            }
        }
    }

    /**
     * Метод процесса поиска путь для А* алгоритма
     *
     */
    public void process() {
        //add the start location to open list
        openCells.add(grid[startI][startJ]);
        Cell current;

        while (true) {
            current = openCells.poll();

            if (current == null)
                break;

            closedCells[current.i][current.j] = true;

            if (current.equals(grid[endI][endJ]))
                return;

            Cell t;

            if (current.i - 1 >= 0) {
                t = grid[current.i - 1][current.j];
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST);

                if (current.j - 1 >= 0) {
                    t = grid[current.i - 1][current.j - 1];
                    updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
                }


                if (current.j + 1 < grid[0].length) {
                    t = grid[current.i - 1][current.j + 1];
                    updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
                }
            }

            if (current.j - 1 >= 0) {
                t = grid[current.i][current.j - 1];
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
            }

            if (current.j + 1 < grid[0].length) {
                t = grid[current.i][current.j + 1];
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST);
            }

            if (current.i + 1 < grid.length) {
                t = grid[current.i + 1][current.j];
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST);


                if (current.j - 1 >= 0) {
                    t = grid[current.i + 1][current.j - 1];
                    updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
                }

                if (current.j + 1 < grid[0].length - 1) {
                    t = grid[current.i + 1][current.j + 1];
                    updateCostIfNeeded(current, t, current.finalCost + DIAGONAL_COST);
                }
            }
        }

    }

    /**
     * метод, который выводит етку на экран
     * SO - начальная точка
     * DE - конечная точка
     * BL - это блок, через который нельзя проложить путь
     */
    public void display() {
        System.out.println("Grid: ");

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i == startI && j == startJ)
                    System.out.print("SO  "); //Source cell
                else if (i == endI && j == endJ)
                    System.out.print("DE  "); // Destination cell
                else if (grid[i][j] != null)
                    System.out.printf("%-3d ", 0);
                else System.out.print("BL  "); //Block cell
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * выводит на экран "стоимость" шага, которую нужно потратить, чтобы перейти от одной клетки
     * к другой и в конечном итоге достигнуть конца пути
     */
    public void displayScores() {
        System.out.println("\n Scores for cell:");

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != null)
                    System.out.printf("%-3d ", grid[i][j].finalCost);
                else System.out.print("BL  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * выводит на экран Path - самый короткий путь от конца пути до его начала
     * метод еще выводит конечный результат сетки
     * X - означает, что путь должен быть проделан через данную клетку
     */
    public void displaySolution() {
        if (closedCells[endI][endJ]) {
            //we track back the path
            System.out.println("Path:");
            Cell current = grid[endI][endJ];
            System.out.println(current);
            grid[current.i][current.j].solution = true;

            while (current.parent != null) {
                System.out.println(" -> " + current.parent);
                grid[current.parent.i][current.parent.j].solution = true;
                current = current.parent;
            }

            System.out.println("\n");

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (i == startI && j == startJ)
                        System.out.print("SO  "); //Source cell
                    else if (i == endI && j == endJ)
                        System.out.print("DE  "); // Destination cell
                    else if (grid[i][j] != null)
                        System.out.printf("%-3s ", grid[i][j].solution ? "X" : "0");
                    else System.out.print("BL  "); //Block cell
                }
                System.out.println();
            }
            System.out.println();
        } else System.out.println("No possible path");
    }
}



