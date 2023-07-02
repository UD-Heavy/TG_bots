package logic;

import java.util.*;

public class ticTacToe {
    public static List<List> winList = new ArrayList<>();
    public static Map<Long, gameUser> userList = new HashMap<>();
     public static char[][] gameBoard;
    gameUser gameUser;
    Random rnd = new Random();

    public String userCheck(Long userId) {
        gameUser gameUserArea = new gameUser();
        userList.put(userId, gameUserArea);
        initialization();
        return printGameBoard(gameUserArea.gameBoard);
    }

    public String game(String choice, Long userId) {
        try {
            this.gameUser = userList.get(userId);
            this.gameBoard = gameUser.gameBoard;

            int choiceInt = Integer.parseInt(choice);

            String result = "";
            while (gameUser.allMoves.contains(choiceInt) || choiceInt > 9 || choiceInt < 1) {
                return "PLS enter another number from 1 to 9!\n" + printGameBoard(gameBoard); //
            }
            addBoth(gameUser.allMoves, gameUser.playerMoves, choiceInt);
            placePiece(gameBoard, choiceInt, "Player");

            result = checkWinner("Player", gameUser, userId);  //
            if (result.length() > 0) {
                return result + "\n" + printGameBoard(gameBoard) + "You are out of the game, use the /help command";
            }

            int cpuChoice = rnd.nextInt(9) + 1;
            while (gameUser.allMoves.contains(cpuChoice)) {
                cpuChoice = rnd.nextInt(9) + 1;
            }
            addBoth(gameUser.allMoves, gameUser.cpuMoves, cpuChoice);
            placePiece(gameBoard, cpuChoice, "Cpu");
            result = checkWinner("Cpu", gameUser, userId);
            if (result.length() > 0) {
                return result + printGameBoard(gameBoard) + "You are out of the game, use the /help command";
            }
            return printGameBoard(gameBoard);
        } catch (RuntimeException e) {
            return "Enter a number or use /exit to end the game!\n" + printGameBoard(gameBoard);
        }
    }

    public static String printGameBoard(char[][] printGameBoard) {
        StringBuilder str = new StringBuilder();
        for (char[] row : printGameBoard) {
            for (char col : row) {
                str.append(col);
            }
            str.append("\n");
        }
        return str.toString();
    }

    public static void initialization() {
        List<Integer> topRow = Arrays.asList(1, 2, 3);
        List<Integer> midRow = Arrays.asList(4, 5, 6);
        List<Integer> botRow = Arrays.asList(7, 8, 9);
        List<Integer> leftCol = Arrays.asList(1, 4, 7);
        List<Integer> midCol = Arrays.asList(2, 5, 8);
        List<Integer> rightCol = Arrays.asList(3, 6, 9);
        List<Integer> cross1 = Arrays.asList(1, 5, 9);
        List<Integer> cross2 = Arrays.asList(7, 5, 3);

        winList.add(topRow);
        winList.add(midRow);
        winList.add(botRow);
        winList.add(leftCol);
        winList.add(midCol);
        winList.add(rightCol);
        winList.add(cross1);
        winList.add(cross2);
    }

    public static String checkWinner(String user, gameUser gameUser, Long userID) {

        for (var list : winList) {
            if (gameUser.playerMoves.containsAll(list) && user.equals("Player")) {
                BotMain.gameFlag = 0;
                userList.remove(userID);
                return """
                        Congratulations!!!
                        You won!
                        """;
            } else if (gameUser.cpuMoves.containsAll(list) && user.equals("Cpu")) {
                BotMain.gameFlag = 0;
                userList.remove(userID);
                return "Unfortunately cpu won ;(\n";
            }
        }
        return "";
    }

    public static void addBoth(List<Integer> generalSet, List<Integer> privateSet, int value) {
        generalSet.add(value);
        privateSet.add(value);
    }

    public static void placePiece(char[][] gameBoard, int choice, String user) {
        char symbol = ' ';
        if (user.equals("Cpu")) {
            symbol = 'O';
        } else if (user.equals("Player")) {
            symbol = 'X';
        }
        switch (choice) {
            case 1 -> gameBoard[0][0] = symbol;
            case 2 -> gameBoard[0][2] = symbol;
            case 3 -> gameBoard[0][4] = symbol;
            case 4 -> gameBoard[2][0] = symbol;
            case 5 -> gameBoard[2][2] = symbol;
            case 6 -> gameBoard[2][4] = symbol;
            case 7 -> gameBoard[4][0] = symbol;
            case 8 -> gameBoard[4][2] = symbol;
            case 9 -> gameBoard[4][4] = symbol;
        }
    }

}