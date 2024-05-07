public class ChessBoard {
    protected static final int CHSBRDSIZE = 8;
    private final String[] chessLetters = {"A","B","C","D","E","F","G","H"};
    public ChessPiece[][] board = new ChessPiece[CHSBRDSIZE][CHSBRDSIZE]; // creating a field for game

    //списки побитых фигур
    protected String[] beatenFigures;

    //позиции королей
    private final int[][] kingPositions = new int[2][2];

    private boolean isGameOver;

    private int nowPlayer;
    public ChessBoard(int nowPlayer) {
        this.nowPlayer = nowPlayer;
        this.beatenFigures = new String[2];
        this.beatenFigures[ChessParameters.WHITE] = "";
        this.beatenFigures[ChessParameters.BLACK] = "";
        setKingPosition(ChessParameters.WHITE, 0, 4);
        setKingPosition(ChessParameters.BLACK, 7, 4);
        this.isGameOver = false;
    }

    public void setKingPosition(int color, int line, int column){
        kingPositions[color][ChessParameters.LINEPOS] = line;
        kingPositions[color][ChessParameters.COLUMNPOS] = column;
    }

    public boolean moveToPosition(int startLine, int startColumn, int endLine, int endColumn) {
        //если игра завершена - не ходим
        if(checkGameOver())
            return false;

        //проверяем выход стартового поля за границы доски
       if ( ! (checkPos(startLine) && checkPos(startColumn)) ) {
           System.out.println("Выбранная фигура за границами доски!");
           return false;
       }

       //Есть ли фигура на стартовом поле
       if(checkEmptyField(startLine, startColumn)){
           System.out.println("На указанном поле нет фигуры!");
           return false;
       }

       // Соответствует ли цвет фигуры на стартовом тому, чей сйчас ход
       if ( ! (nowPlayer == board[startLine][startColumn].getColor()) ) {
           System.out.printf("Сейчас ход %s\n", ChessParameters.figColors[nowPlayer] );
           return false;
       }

       // Может ли данная фигура ходить на конечное поле
       if (! board[startLine][startColumn].canMoveToPosition(this, startLine, startColumn, endLine, endColumn))
           return false;

       //Состояние конечного поля - не пустое
       if(! checkEmptyField(endLine, endColumn) ){
           // Проверка можно ли фигуру на конечном поле побить. Если фигура того же цвета - то нельзя.
           if(checkHitPossible(nowPlayer, endLine, endColumn)) {
               //если на конечном поле фигура вражеского цвета, то вдруг она - король
               if( board[endLine][endColumn].getSymbol().equals("K") ){
                   System.out.printf("Короля не убивают! Мат кролю. Победа %s!\n", ChessParameters.figColors[nowPlayer] );
                   setGameOver(true);
                   return false;
               }
               else {
                   //если на конечном поле фигура вражеского цвета, и не король, то она будет побита, но если ходит король, то он, после хода, может оказаться под боем, тогда ходить нельзя
                   if( checkKingUnderAttack(startLine, startColumn, endLine, endColumn) )
                       return false;

                   // если с предыдущими условиями все в порядке, то выдаем надпись, что одна фигура побила другую и записываем побитую в список выведенных из игры фигур
                   System.out.printf("Побита %s %s!\n", board[endLine][endColumn].getSymbol(), ChessParameters.figColors[board[endLine][endColumn].getColor()] );
                   beatenFigures[nowPlayer] += String.format( " %s%s", board[endLine][endColumn].getSymbol(), getColorLetter(endLine, endColumn) );
               }
           }
           else return false;
       }
       //Состояние конечного поля - пустое. В случае, если ходит король, то он может на этом поле быть под боем, тогда ходить нельзя.
       else{
           if( checkKingUnderAttack(startLine, startColumn, endLine, endColumn) )
               return false;
       }

       // перемещение фигуры на конечное поле. Если на нем стоит вражеская фигура, то она перезаписывается и стирается
       board[endLine][endColumn] = board[startLine][startColumn]; // if piece can move, we moved a piece
       board[startLine][startColumn] = null; // set null to previous cell

       // Если ходил король, то мы меняем его координаты (координаты короля нужны, чтобы проверять под боем ли он находится после каждого хода).
       // И меняем переменную check - для рокировки, означающую, что ход был.
       if(board[endLine][endColumn].getSymbol().equals("K")) {
           setKingPosition(nowPlayer, endLine, endColumn);
           if(board[endLine][endColumn].check)
               board[endLine][endColumn].check = false;
       }
       //Если ходила ладья, то меняем переменную check - для рокировки, означающую, что ход был.
       else if (board[endLine][endColumn].getSymbol().equals("R")) {
           if(board[endLine][endColumn].check)
               board[endLine][endColumn].check = false;
       }
       //Если ходила пешка, то нужно проверить дошла ли она до последнего поля. Если дошла - то превращается в ферзя.
        else if(board[endLine][endColumn].getSymbol().equals("P")){
           if( (nowPlayer == ChessParameters.WHITE)&&(endLine == 7) ) {
               board[endLine][endColumn] = new Queen(ChessParameters.WHITE);
               System.out.println("Пешка становится ферзём!");
           }
           else  if( (nowPlayer == ChessParameters.BLACK)&&(endLine == 0) ) {
               board[endLine][endColumn] = new Queen(ChessParameters.BLACK);
               System.out.println("Пешка становится ферзём!");
           }
       }

        //Теперь проверяем находится ли под боем вражеский король. Определяем его цвет и поле
       int enemyKingCol = nowPlayer == ChessParameters.WHITE ? ChessParameters.BLACK : ChessParameters.WHITE;

       int enemyKingLine = kingPositions[enemyKingCol][ChessParameters.LINEPOS];
       int enemykingColumn = kingPositions[enemyKingCol][ChessParameters.COLUMNPOS];

       // Метод isUnderAttack возвращает количество фигур другого цвета, которые угрожают указанному полю. На случай 2-ного шаха.
       int shahNum = ((King) board[enemyKingLine][enemykingColumn]).isUnderAttack(this, enemyKingLine, enemykingColumn);

       //Если количество угрожающих полю фигур больше 0, то выводим надпись шах королю.
       if(shahNum > 0) {
           System.out.println((shahNum > 1 ? String.format("%d-ной ", shahNum) : "") + String.format("Шах королю %s", ChessParameters.figColors[enemyKingCol]));
       }

       //переключаем игрока и возвращаем true - удачный ход
       nowPlayer = enemyKingCol;
       return true;
    }
    public boolean castling1() {
        if (nowPlayer == ChessParameters.WHITE) {
            if (board[0][0] == null || board[0][4] == null) return false;
            if (board[0][0].getSymbol().equals("R") && board[0][4].getSymbol().equals("K") && // check that King and Rook
                    board[0][1] == null && board[0][2] == null && board[0][3] == null) {              // never moved
                if (board[0][0].getColor() == ChessParameters.WHITE && board[0][4].getColor() == ChessParameters.WHITE &&
                        board[0][0].check && board[0][4].check &&
                        new King(ChessParameters.WHITE).isUnderAttack(this, 0, 2) == 0) { // check that position not in under attack
                    board[0][4] = null;
                    board[0][2] = new King(ChessParameters.WHITE);   // move King
                    board[0][2].check = false;
                    board[0][0] = null;
                    board[0][3] = new Rook(ChessParameters.WHITE);   // move Rook
                    board[0][3].check = false;
                    nowPlayer = ChessParameters.BLACK;  // next turn
                    return true;
                } else return false;
            } else return false;
        } else {
            if (board[7][0] == null || board[7][4] == null) return false;
            if (board[7][0].getSymbol().equals("R") && board[7][4].getSymbol().equals("K") && // check that King and Rook
                    board[7][1] == null && board[7][2] == null && board[7][3] == null) {              // never moved
                if (board[7][0].getColor() == ChessParameters.BLACK && board[7][4].getColor() == ChessParameters.BLACK &&
                        board[7][0].check && board[7][4].check &&
                        new King(ChessParameters.BLACK).isUnderAttack(this, 7, 2) == 0) { // check that position not in under attack
                    board[7][4] = null;
                    board[7][2] = new King(ChessParameters.BLACK);   // move King
                    board[7][2].check = false;
                    board[7][0] = null;
                    board[7][3] = new Rook(ChessParameters.BLACK);   // move Rook
                    board[7][3].check = false;
                    nowPlayer = ChessParameters.WHITE;  // next turn
                    return true;
                } else return false;
            } else return false;
        }
    }

    public boolean castling8() {
        if (nowPlayer == ChessParameters.WHITE) {
            if (board[0][7] == null || board[0][4] == null) return false;
            if (board[0][7].getSymbol().equals("R") && board[0][4].getSymbol().equals("K") && // check that King and Rook
                    board[0][6] == null && board[0][5] == null) {              // never moved
                if (board[0][7].getColor() == ChessParameters.WHITE && board[0][4].getColor() == ChessParameters.WHITE &&
                        board[0][7].check && board[0][4].check &&
                        new King(ChessParameters.WHITE).isUnderAttack(this, 0, 6) == 0) { // check that position not in under attack
                    board[0][4] = null;
                    board[0][6] = new King(ChessParameters.WHITE);   // move King
                    board[0][6].check = false;
                    board[0][7] = null;
                    board[0][5] = new Rook(ChessParameters.WHITE);   // move Rook
                    board[0][5].check = false;
                    nowPlayer = ChessParameters.BLACK;  // next turn
                    return true;
                } else return false;
            } else return false;
        } else {
            if (board[7][7] == null || board[7][4] == null) return false;
            if (board[7][7].getSymbol().equals("R") && board[7][4].getSymbol().equals("K") && // check that King and Rook
                    board[7][6] == null && board[7][5] == null) {              // never moved
                if (board[7][7].getColor() == ChessParameters.BLACK && board[7][4].getColor() == ChessParameters.BLACK &&
                        board[7][7].check && board[7][4].check &&
                        new King(ChessParameters.BLACK).isUnderAttack(this, 7, 6) == 0) { // check that position not in under attack
                    board[7][4] = null;
                    board[7][6] = new King(ChessParameters.BLACK);   // move King
                    board[7][6].check = false;
                    board[7][7] = null;
                    board[7][5] = new Rook(ChessParameters.BLACK);   // move Rook
                    board[7][5].check = false;
                    nowPlayer = ChessParameters.WHITE;  // next turn
                    return true;
                } else return false;
            } else return false;
        }
    }

    public void printBoard() {  //print board in console
        if(! checkGameOver())
            System.out.printf("\nTurn %s\n", ChessParameters.figColors[nowPlayer]);
        System.out.printf("Player 2(%s)%s %s\n", ChessParameters.figColors[ChessParameters.BLACK], beatenFigures[ChessParameters.BLACK].isEmpty() ? "" : ". Побитые фигуры:", beatenFigures[ChessParameters.BLACK] );
        printLetters();
        printLine();
        for (int i = CHSBRDSIZE-1; i >= 0; i--) {
            System.out.print((i+1) + "|\t");
            for (int j = 0; j < CHSBRDSIZE; j++) {
                if (board[(i)][j] == null) {
                    System.out.print(".." + "\t");
                } else {
                    System.out.printf( "%s%s\t", board[i][j].getSymbol(), getColorLetter(i,j) );
                }
            }
            System.out.print("|"+(i+1) + "\t");
            System.out.println();
        }
        printLine();
        printLetters();
        System.out.printf("Player 1(%s)%s %s\n", ChessParameters.figColors[ChessParameters.WHITE], beatenFigures[ChessParameters.WHITE].isEmpty() ? "" : ". Побитые фигуры:", beatenFigures[ChessParameters.WHITE] );
    }
   public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public boolean checkGameOver(){
        return isGameOver;
    }
    private void printLetters(){
        for(int i = 0; i < CHSBRDSIZE; i++) {
            System.out.printf("\t%s",chessLetters[i]);
        }
        System.out.println();
    }

    private void printLine(){
        for(int i = 0; i < CHSBRDSIZE; i++) {
            System.out.print("\t──");
        }
        System.out.println();
    }

    public int getLetterInd(String let){
        for(int i = 0; i < chessLetters.length; i++){
            if(chessLetters[i].equals(let))
               return i;
        }
        return -1;
    }

    public String getColorLetter(int i, int j){
        if(board[i][j] == null)
            return "";
        else {
            int colrInd = board[i][j].getColor();
            if( (colrInd < 0)||(colrInd >= ChessParameters.figColors.length))
                return "";

            return ChessParameters.figColors[colrInd].substring(0, 1).toLowerCase();
        }
    }

    public boolean checkPos(int pos) {
        return pos >= 0 && pos < CHSBRDSIZE;
    }

    public boolean checkHitPossible(int figcol, int Line, int Column){
        if( checkEmptyField(Line, Column) )
            return false;

        int enemyfigcol = board[Line][Column].getColor();

        return (figcol != enemyfigcol);
    }
    public boolean checkEmptyField(int Line, int Column){
        return board[Line][Column] == null;
    }

    public boolean checkKingUnderAttack(int startLine, int startColumn, int endLine, int endColumn){
        if( board[startLine][startColumn].getSymbol().equals("K") ){
            int attackNum = ((King) board[startLine][startColumn]).isUnderAttack(this, endLine, endColumn);
            if(attackNum > 0){
                System.out.printf("Король %s после такого хода будет под боем!\n", ChessParameters.figColors[nowPlayer] );
                return true;
            }
        }
        return false;
    }
}