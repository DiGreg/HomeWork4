package ru.geekbrains.java1.KN;

import java.util.Random;
import java.util.Scanner;

public class MainClass {
    public static final int field_Size = 5; //5 Размер игрового поля (Было 3)
    public static final char cellEmpty = '*';
    public static final char cell_X = 'X';
    public static final char cell_O = 'O';
    public static final int cellsToWin = 4; //4 Число клеток для победы (Было 3)
    public static Scanner scan = new Scanner(System.in);
    public static Random random = new Random();
    public static char[][] field = new char[field_Size][field_Size]; //массив для игрового поля
    public static int xAI = -1, yAI = -1; //координаты для искуственного интеллекта


    public static void main(String[] args) {
        System.out.println("Игра КРЕСТИКИ-НОЛИКИ");
        initField();
        printField();
        while(true) {
            humanTurn();
            printField();
            if (checkWin(cell_X)) {
                System.out.println("Ты ВЫИГРАЛ, ЧЕЛОВЕК!!!");
                break;
            }
            if (isFieldFull()) {
                System.out.println("А вот и НИЧЬЯ!");
                break;
            }
            compTurn();
            printField();
            if (checkWin(cell_O)) {
                System.out.println("ВЫИГРАЛ Искусственный Интеллект!о_О");
                break;
            }
            if (isFieldFull()) {
                System.out.println("А вот и НИЧЬЯ!");
                break;
            }
        }
    }

    public static void initField (){
        for (int i = 0; i < field_Size; i++) {
            for (int j = 0; j < field_Size; j++) {
                field[i][j] = cellEmpty;
            }
        }
    }

    public static void printField (){
        for (int i = 0; i <= field_Size; i++) {
            System.out.printf("%2d", i);
        }
        System.out.println();
        for (int i = 0; i < field_Size; i++){
            System.out.printf("%2d",(i+1));
            for (int j = 0; j < field_Size ; j++) {
                System.out.printf("%2c", field[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void humanTurn(){
        int x = 0, y = 0;
        do {
            System.out.println("Сделайте свой ход - введите через пробел координаты X и Y:");
            x = scan.nextInt() - 1;
            y = scan.nextInt() - 1;
            scan.nextLine();
            if (!isRightCell(x, y)) System.out.println("Нужны корректные координаты пустой ячейки!");
        } while(!isRightCell(x, y));
        field[y][x] = cell_X;
    }

    public static void compTurn(){
        int x, y;
        do {
            if (isRightCell(xAI, yAI)) {
                x = xAI;
                y = yAI;
                xAI = yAI = -1; //сбрасываю координаты ИИ
            } else { //иначе случайный метод
                x = random.nextInt(field_Size);
                y = random.nextInt(field_Size);
            }
        } while(!isRightCell(x, y));
        field[y][x] = cell_O;
        System.out.println("Компьютер сделал сделал ход в точку х=" + (x + 1) + ", y=" + (y + 1));
    }

    //Метод проверки ячейки на возможность сделать ход
    public static boolean isRightCell(int x, int y){
        boolean a = true;
        if (x > field_Size || y > field_Size || x < 0 || y < 0) {//проверка координат
            a = false;
            return a;
        } else if (field[y][x] != cellEmpty) {//проверка занята ли клетка символом Х или О
            a = false;
        }
        return a;
    }

    public static boolean checkWin (char symbol) {
        //проверка "прямых"
        int counter1;
        int counter2;
        for (int i = 0; i < field_Size; i++) {
            counter1 = 0;
            counter2 = 0;
            for (int j = 0; j < field_Size; j++) {
                if (field[i][j] == symbol) {//проверка горизонталей (строк - Y фикс.)
                    counter1++;
                    if (counter1 == cellsToWin) return true;
                    //координаты для искуственного интеллекта - для блокировки ходов игрока
                    else if (counter1 > 1) {
                        yAI = i;
                        if ((j + 1) == field_Size) xAI = j - counter1;
                        else xAI = j + 1;
                    }
                } else counter1--; //Домыслить,т.к. не работает при заполнении с конца строки наперед! //-это для исключения варианта, когда в ряду из 5 элементов есть 4 своих (неподряд) и 1 чужой элемент

                if (field[j][i] == symbol) {//проверка вертикалей (столбцов - X фикс.)
                    counter2++;
                    if (counter2 == cellsToWin) return true;
                    //координаты для ИИ - для блокировки игрока
                    else if (counter2 > 1) {
                        xAI = i;
                        if ((j + 1) == field_Size) yAI = j - counter2;
                        else yAI = j + 1;
                    }
                } else counter2--; //Домыслить,т.к. не работает при заполнении с низу столбца вверх!
            }
        }
        //проверка диагоналей
        counter1 = 0;
        counter2 = 0;
        for (int i = 0, c = field_Size - 1; i < field_Size; i++, c--){
            if (field[i][i] == symbol) {
                counter1++;
                if (counter1 == cellsToWin) return true;
                //координаты для ИИ - для блокировки игрока
                if (counter1 > 1) {
                    if ((i + 1) == field_Size) yAI = xAI = i - counter1;
                    else yAI = xAI = i + 1;
                }
            }
            else counter1 = 0;

            if (field[i][c] == symbol) {
                counter2++;
                if (counter2 == cellsToWin) return true;
                //координаты для ИИ - для блокировки игрока
                if (counter2 > 1) {
                    if ((i + 1) == field_Size) {
                        yAI = i - counter2;
                        xAI = c + counter2;
                    }
                    else {
                        yAI = i + 1;
                        xAI = c - 1;
                    }
                }
            }
            else counter2 = 0;
        }
        return false;
    }

    public static boolean isFieldFull() {//проверка поля на заполненность
        for (int i = 0; i < field_Size; i++) {
            for (int j = 0; j < field_Size; j++) {
                if (field[i][j] == '*') return false;
            }
        }
        return true;
    }
}
