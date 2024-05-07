abstract class ChessPiece {
    protected int colorInd;
    protected boolean check;
    public ChessPiece(int colorInd){
        this.colorInd = colorInd;
        this.check = true;
    }

    abstract public int getColor();
    abstract public boolean canMoveToPosition(ChessBoard chessBoard, int line, int column, int toLine, int toColumn);
    abstract public String getSymbol();
}