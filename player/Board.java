package player;
import list.*;


public class Board {
	public static final int EMPTY = 2;
	public static final int BLACK = 0;
	public static final int WHITE = 1;
	public static final int MAXPIECES = 10;
	private int[][] board;
	private int blackpieces, whitepieces;
	
	public Board(int color)
	{
		board = new int[8][8];
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
			{
				board[i][j] = EMPTY;
			}
		blackpieces = 0;
		whitepieces = 0;
	}
	
	public int getPiece(int x, int y)
	{
		return board[y][x];
	}
	
	private boolean isValidMove(Move m, int player)
	{
		
		return true;
	}
	
	public boolean makeMove (Move m, int player)
	{
		if (m.moveKind == Move.ADD)
		{
			board[m.y1][m.x1] = player;
			if (player == BLACK)
				blackpieces++;
			else
				whitepieces++;
			return true;
		}
		else if (m.moveKind == Move.STEP)
		{
			board[m.y1][m.x1] = player;
			board[m.y2][m.x2] = EMPTY;
			return true;
		}
		else
			return false;
	}
	
	public boolean undoMove (Move m, int player)
	{
		if (m.moveKind == Move.ADD)
		{
			board[m.y1][m.x1] = EMPTY;
			if (player == BLACK)
				blackpieces--;
			else
				whitepieces--;
			return true;
		}
		else if (m.moveKind == Move.STEP)
		{
			board[m.y1][m.x1] = board[m.y2][m.x2];
			board[m.y2][m.x2] = EMPTY;
			return true;
		}
		return false;
	}
	
	public int getAddsRemaning(int player)
	{
		if (player == BLACK)
			return blackpieces;
		else
			return whitepieces;
	}
	
	public DList generateAllPossibleMoves(int player)
	{
		
	}
}
/*

-> private boolean isValidMove(Move something, int player) // checks if the move is legal for player
-> public DList generateAllPossibleMoves(int player); // generates all the possible moves for player. 
//Returns a DList of Move objects. If there are no possible moves for player, the method returns an empty DList*/