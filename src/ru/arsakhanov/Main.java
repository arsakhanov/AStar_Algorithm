package ru.arsakhanov;

public class Main {

    public static void main(String[] args) {
        AStar aStar = new AStar(5, 5, 0, 0, 3, 2,
                new int[][]{
                        {0, 4}, {2, 2}, {3, 1}, {3, 3}, {2, 1}, {2, 3}
                }
        );
        aStar.display();
        aStar.process(); //Apply A* algorithm
        aStar.displayScores(); //Display Scores on grid
        aStar.displaySolution(); // Display Solution Path
    }
}
