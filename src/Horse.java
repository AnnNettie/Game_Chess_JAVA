public class Horse extends ChessPiece{

    public Horse(int colorInd){
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

        if( (Math.abs(line-toLine) == 2)&&(Math.abs(column-toColumn) == 1) )
            return true;
        else if( (Math.abs(line-toLine) == 1)&&(Math.abs(column-toColumn) == 2) )
            return true;
        else
            return false;
    }
    public String getSymbol(){
        return "H";
    }
}