package src;
import ChessBoard.ChessBoard;
public class Main
{
    public static void main(String[] args)
    {
        ChessBoard Game=new ChessBoard();
        Game.CreatePieces();
        Game.DisplayOnBoard();
    }
}