import java.util.*;

public class Game {
    static Scanner sc = new Scanner(System.in);
    GameSession gameSession;
    List<Long> twoPlayers = new ArrayList<>(2);
    public static Map<List<Long>, GameSession> session = new HashMap<>();
    public static String[][] gameBoardFirstPlayer;
    public static String[][] gameBoardSecondPlayer;

    public String createGameSession(Long firstUserId, Long secondUserId) {
        GameSession gameSession = new GameSession();
        twoPlayers.add(firstUserId);
        twoPlayers.add(secondUserId);
        twoPlayers = new ArrayList<>();
        // initialization
        session.put(twoPlayers, gameSession);
        generateBothGameBoards(gameSession.gameBoardFirstPlayer, gameSession.gameBoardSecondPlayer);
        return printGameBoard(gameSession.gameBoardFirstPlayer)
                + "\n"
                + printGameBoard(gameSession.gameBoardSecondPlayer);
    }

    public String game() {

        int player = 1;                     //
        String[][] gameBoard;
        while (true) {
            if (player == 1) {              //<
                System.out.println("First player's turn:");
                player = 2;
                gameBoard = gameBoardSecondPlayer;
            } else {
                System.out.println("Second player's turn:");
                player = 1;
                gameBoard = gameBoardFirstPlayer;
            }                               //>


            printGameBoard(gameBoard);
            int choice = sc.nextInt();
            getChoice(gameBoard, choice);
            System.out.println();
            printGameBoard(gameBoard);
            System.out.println();
        }
    }

    private static void generateBothGameBoards(String[][] gameBoardFirstPlayer, String[][] gameBoardSecondPlayer) {
        gameBoardFirstPlayer[0][0] = "S";
        gameBoardFirstPlayer[3][3] = "S";
        gameBoardFirstPlayer[0][4] = "S";
        gameBoardFirstPlayer[1][4] = "S";

        gameBoardSecondPlayer[4][1] = "B";
        gameBoardSecondPlayer[0][4] = "B";
        gameBoardSecondPlayer[2][2] = "B";
        gameBoardSecondPlayer[2][3] = "B";
    }

    private static void getChoice(String[][] gameBoard, int choice) {
        try {
            int x = choice / 10;
            int y = choice % 10;
            gameBoard[x][y] = "X";

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public static String printGameBoard(String[][] gameBoard) {
        StringBuilder str = new StringBuilder();
        for (String[] row : gameBoard) {
            for (String col : row) {
                str.append(col);
            }
            str.append("\n");
        }
        return str.toString();
    }
}
