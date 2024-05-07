import java.util.Scanner;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static ChessBoard buildBoard() {
        ChessBoard board = new ChessBoard(ChessParameters.WHITE);

        board.board[0][0] = new Rook(ChessParameters.WHITE);
        board.board[0][1] = new Horse(ChessParameters.WHITE);
        board.board[0][2] = new Bishop(ChessParameters.WHITE);
        board.board[0][3] = new Queen(ChessParameters.WHITE);
        board.board[0][4] = new King(ChessParameters.WHITE);
        board.setKingPosition(ChessParameters.WHITE, 0, 4);
        board.board[0][5] = new Bishop(ChessParameters.WHITE);
        board.board[0][6] = new Horse(ChessParameters.WHITE);
        board.board[0][7] = new Rook(ChessParameters.WHITE);
        board.board[1][0] = new Pawn(ChessParameters.WHITE);
        board.board[1][1] = new Pawn(ChessParameters.WHITE);
        board.board[1][2] = new Pawn(ChessParameters.WHITE);
        board.board[1][3] = new Pawn(ChessParameters.WHITE);
        board.board[1][4] = new Pawn(ChessParameters.WHITE);
        board.board[1][5] = new Pawn(ChessParameters.WHITE);
        board.board[1][6] = new Pawn(ChessParameters.WHITE);
        board.board[1][7] = new Pawn(ChessParameters.WHITE);

        board.board[7][0] = new Rook(ChessParameters.BLACK);
        board.board[7][1] = new Horse(ChessParameters.BLACK);
        board.board[7][2] = new Bishop(ChessParameters.BLACK);
        board.board[7][3] = new Queen(ChessParameters.BLACK);
        board.board[7][4] = new King(ChessParameters.BLACK);
        board.setKingPosition(ChessParameters.BLACK, 7, 4);
        board.board[7][5] = new Bishop(ChessParameters.BLACK);
        board.board[7][6] = new Horse(ChessParameters.BLACK);
        board.board[7][7] = new Rook(ChessParameters.BLACK);
        board.board[6][0] = new Pawn(ChessParameters.BLACK);
        board.board[6][1] = new Pawn(ChessParameters.BLACK);
        board.board[6][2] = new Pawn(ChessParameters.BLACK);
        board.board[6][3] = new Pawn(ChessParameters.BLACK);
        board.board[6][4] = new Pawn(ChessParameters.BLACK);
        board.board[6][5] = new Pawn(ChessParameters.BLACK);
        board.board[6][6] = new Pawn(ChessParameters.BLACK);
        board.board[6][7] = new Pawn(ChessParameters.BLACK);

        return board;
    }

    public static void main(String[] args) {
        ChessBoard board = buildBoard();
        Scanner scanner = new Scanner(System.in);
        System.out.println("""
               Чтобы проверить игру надо вводить команды:
               'exit' - для выхода
               'replay' - для перезапуска игры
               'castling1' или 'castling8' - для рокировки по соответствующей линии
               'move A 1 C 2' - для передвижения фигуры с позиции A1 на C2.""");
        board.printBoard();
        while (true) {
            String s = scanner.nextLine();
            if (s.equals("exit")) break;
            else if (s.equals("replay")) {
                System.out.println("Заново");
                board = buildBoard();
                board.printBoard();
            } else {
                if (s.equals("castling1")) {
                    if (board.castling1()) {
                        System.out.println("Рокировка удалась");
                        board.printBoard();
                    } else {
                        System.out.println("Рокировка не удалась");
                    }
                } else if (s.equals("castling8")) {
                    if (board.castling8()) {
                        System.out.println("Рокировка удалась");
                        board.printBoard();
                    } else {
                        System.out.println("Рокировка не удалась");
                    }
                } else if (s.contains("move")) {
                    String[] a = s.split(" ");
                    try {
                        int column = board.getLetterInd(a[1]);
                        int line = Integer.parseInt(a[2])-1;
                        int toColumn = board.getLetterInd(a[3]);
                        int toLine = Integer.parseInt(a[4])-1;

                        if (board.moveToPosition(line, column, toLine, toColumn)) {
                            System.out.println("Успешно передвинулись");
                            board.printBoard();
                        } else {
                            if(! board.checkGameOver())
                                System.out.println("Передвижение не удалось");
                            else
                                System.out.println("Игра завершена!");
                        }
                    } catch (Exception e) {
                        System.out.println("Вы что-то ввели не так, попробуйте ещё раз");
                    }
                }
            }
        }
    }
}