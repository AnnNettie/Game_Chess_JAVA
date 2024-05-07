public class Pawn extends ChessPiece{

    public Pawn(int colorInd){
        super(colorInd);
    }

    public int getColor(){
        return colorInd;
    }

    public boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn){
        if( (line == toLine)&&(column == toColumn) )
            return false;

        if( !chessBoard.checkPos(toLine) || !chessBoard.checkPos(toColumn) ) {
            System.out.println("Ход за границы поля невозможен!");
            return false;
        }

        if(colorInd == ChessParameters.WHITE) {
            if ((line == 1) && (toLine - line == 2) && (column == toColumn)) {
                return (chessBoard.checkEmptyField(line + 1, column) && chessBoard.checkEmptyField(line + 2, column));
            } else if ((toLine - line == 1) && (column == toColumn)) {
                return chessBoard.checkEmptyField(line + 1, column);
            } else if ((toLine - line == 1) && (Math.abs(column - toColumn) == 1)) {
                return ! chessBoard.checkEmptyField(toLine, toColumn);
            }
            else return false;
        } else {
            if( (line == 6)&&(line-toLine == 2)&&(column == toColumn) ){
                return ( chessBoard.checkEmptyField(line-1, column)&&chessBoard.checkEmptyField(line-2, column) );
            }
            else if((line-toLine == 1)&&(column == toColumn) ){
                return chessBoard.checkEmptyField(line-1, column);
            }
            else if( (line-toLine == 1)&&(Math.abs(column - toColumn) == 1 ) ){
                return ! chessBoard.checkEmptyField(toLine, toColumn);
            }
            else return false;
        }
    }
    public String getSymbol(){
        return "P";
    }
}