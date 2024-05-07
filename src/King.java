public class King extends ChessPiece{
    public King(int colorInd){
        super(colorInd);
    }

    public int getColor(){
        return colorInd;
    }

    public boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn){
        if( (line == toLine)&&(column == toColumn) )
            return false;

        if( !chessBoard.checkPos(toLine) || !chessBoard.checkPos(toColumn) )
            return false;

        if(  ( (Math.abs(line-toLine) == 1) || (Math.abs(line-toLine) == 0)  ) && ( (Math.abs(column-toColumn) == 1) || (Math.abs(column-toColumn) == 0)  )  )
            return true;
        else return false;
    }

    public int isUnderAttack(ChessBoard board, int line, int column){
       int attacksNumb = 0;
       //перебираем в циклах все поля
       for(int l = 0; l < ChessBoard.CHSBRDSIZE; l++){
            for(int c = 0; c < ChessBoard.CHSBRDSIZE; c++){
                //если поле не пустое
                if(! board.checkEmptyField(l,c)) {
                    //если цвет фигуры на поле отличается от цвета короля
                    if (board.board[l][c].getColor() != colorInd){
                        //пешка - исключение, так как ходит одним способом, а бьет другим. Нужно дополнительно проверять идет ли она наискосок
                        if(board.board[l][c].getSymbol().equals("P")){
                            if( (board.board[l][c].getColor() == ChessParameters.WHITE) && (line - l == 1) && (Math.abs(c - column) == 1) )
                                attacksNumb++;
                            else if( (board.board[l][c].getColor() == ChessParameters.BLACK) && (l - line == 1) && (Math.abs(c - column) == 1) )
                                attacksNumb++;
                        }
                        //другие фигуры, кроме пешки как ходят, так и бьют, так что проверяем могут ли они ходить на указанное поле
                        else {
                            if (board.board[l][c].canMoveToPosition(board, l, c, line, column)) {
                                attacksNumb++;
                            }
                        }
                    }
                }
            }
        }

       //возвращаем количество фигур, которые атакуют указанное поле
        return attacksNumb;
    }
    public String getSymbol(){
        return "K";
    }
}