public class Queen extends ChessPiece{
    public Queen(int colorInd){
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

        return ( canRookMove(chessBoard, line, column, toLine, toColumn) || canBishopMove(chessBoard, line, column, toLine, toColumn) );
    }

    public boolean canRookMove(ChessBoard chessBoard, int line, int column, int toLine, int toColumn){
        if( line == toLine ){
            int xdiff = (toColumn-column)/Math.abs(toColumn-column);
            int i = column;
            int k = 1;
            while(k < ChessBoard.CHSBRDSIZE){
                i = i+xdiff;
                if(i == toColumn)
                    return true;
                else {
                    if (!chessBoard.checkEmptyField(line, i))
                        return false;
                }
                k++;
            }
            return false;
        }
        else if(column == toColumn){
            int ydiff = (toLine-line)/Math.abs(toLine-line);
            int i = line;
            int k = 1;
            while(k < ChessBoard.CHSBRDSIZE){
                i = i+ydiff;
                if(i == toLine)
                    return true;
                else {
                    if (!chessBoard.checkEmptyField(i, column))
                        return false;
                }
                k++;
            }
            return false;
        }
        else return false;
    }

    public boolean canBishopMove(ChessBoard chessBoard, int line, int column, int toLine, int toColumn){
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
        return "Q";
    }
}