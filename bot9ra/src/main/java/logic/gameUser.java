package logic;

import java.util.ArrayList;
import java.util.List;

public class gameUser {
    List<Integer> playerMoves = new ArrayList<>();
    List<Integer> cpuMoves = new ArrayList<>();
    List<Integer> allMoves = new ArrayList<>();
    char[][] gameBoard = {{' ', '|', ' ', '|', ' '},
            {'-', '+', '-', '+', '-'},
            {' ', '|', ' ', '|', ' '},
            {'-', '+', '-', '+', '-'},
            {' ', '|', ' ', '|', ' '}};
}
