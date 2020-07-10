package ru.arsakhanov;

//определяем клетки для нашей сетки
public class Cell {

    //координаты
    public int i, j;
    //родительская клетка пути
    public Cell parent;
    //Эврестический подсчет данной клетки
    public int heuresticCost;
    //Суммарный подсчет
    public int finalCost; //G + H
    //где G(n) количество ходов от start до n
    //и H(n) эврестический подсчет, который подсчитывает минимальный путь от n до goal - конца
    public boolean solution;

    public Cell (int i, int j){
        this.i = i;
        this.j = j;
    }

    @Override
    public String toString() {
        return "[" + i +
                "][" + j +
                ']';
    }
}

