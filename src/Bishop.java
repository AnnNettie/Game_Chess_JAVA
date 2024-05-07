public class Bishop extends ChessPiece{
    public Bishop(int colorInd){
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

        if(Math.abs(line-toLine) == Math.abs(column-toColumn)){
            int xdiff = (toColumn-column)/Math.abs(toColumn-column);
            int ydiff = (toLine-line)/Math.abs(toLine-line);
            int i = column;
            int j = line;
            int k = 1;
            while(k < ChessBoard.CHSBRDSIZE){
                i = i+xdiff;
                j = j+ydiff;

                if( (i == toColumn)&&(j == toLine) )
                    return true;
                else {
                    if (!chessBoard.checkEmptyField(j, i))
                        return false;
                }
                k++;
            }
            return false;
        }
        else return false;
    }
    public String getSymbol(){
        return "B";
    }
}
