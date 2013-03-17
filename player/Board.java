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
	
	public boolean isValidMove(Move m, int player)
	{
		if (m == null)
			return false;
		if (m.moveKind == Move.QUIT)
			return true;
		if (player != BLACK && player != WHITE)
			return false;
		if (m.x1 < 0 || m.x1 > 7 || m.y1 < 0 || m.y2 > 7 || m.x2 < 0 || m.x2 > 7 || m.y1 < 0 || m.y1 > 7 || m.y2 < 0 || m.y2 > 7)
			return false;
		if ((m.x1 == 0 || m.x1 == 7) && (m.y1 == 0 || m.y1 == 7))
			return false;
		if (m.x2 == m.x1 && m.y2 == m.y1)
			return false;
		if (board[m.y1][m.x1] != EMPTY)
			return false;
		if (player == WHITE)
		{
			for (int i = 1; i < 7; i++)
			{
				if ((m.x1 == i && m.y1 == 7) || (m.x1 == i && m.y1 == 0))
					return false;
			}
		}
		else
		{
			for (int i = 1; i < 7; i++)
			{
				if ((m.x1 == 0 && m.y1 == i) || (m.x1 == 7 && m.y1 == i))
					return false;
			}
		}
		DList n = neighbors(m.x1,m.y1, player);
		if (n.length() > 1)
			return false;
		else if (n.length() < 1)
			return true;
		else
		{
			DList n1 = new DList();
			try {
				n1 = neighbors(( (Coordinate) n.front().item()).getX(), ((Coordinate) n.front().item()).getY(), player);
			} catch (InvalidNodeException e) {

			}
			if (n1.length() > 1)
				return false;
		}
		if (m.moveKind == Move.ADD)
		{
			if (player == BLACK)
			{
				if (blackpieces > 10)
					return false;
			}
			else 
			{
				if (whitepieces > 10)
					return false;
			}
		}
		if (m.moveKind == Move.STEP)
		{
			if (getPiece(m.x2,m.y2) != player)
				return false;
			if (player == BLACK && blackpieces < 10)
				return false;
			if (player == WHITE && whitepieces < 10)
				return false;
		}
		return true;
	}
	
	 private DList neighbors (int i, int j, int player)
	  {
		 DList neighbors = new DList();
		 for (int a = -1; a <= 1; a++)
			 for (int b = -1; b <=1; b++)
			 {
				 if ((a != 0 || b != 0) && (j+ a >= 0) && (j + a <=7) && (i + b >= 0) && (i + a <= 7))
				 {
					 if (board[j+a][i+b] == player)
						 neighbors.insertBack(new Coordinate(i,j)); 
				 }
			 }
		 return neighbors;
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
		else
			return false;
	}
	
	public int getAddsRemaining(int player)
	{
		if (player == BLACK)
			return 10-blackpieces;
		else
			return 10-whitepieces;
	}
	
	public DList generateAllPossibleMoves(int player)
	{
		DList allMoves = new DList();
		if (getAddsRemaining(player) > 0)
		{
			for (int i = 0; i < 8; i++)
			{
				for (int j = 0; j < 8; j++)
				{
					Move m = new Move(i,j);
					if (isValidMove(m, player))
					{
						allMoves.insertBack(m);
					}
						
				}
			}
		}
		else
		{
			DList startPositions = new DList();
			for (int i = 0; i < 8; i++)
			{
				for (int j = 0; j < 8; j++)
				{
					if (board[j][i] == player)
						startPositions.insertBack(new Coordinate(i, j));
				}
			}
			for (int k = 0; k < 8; k++)
			{
				for (int l = 0; l < 8; l++)
				{
					try 
					{
						Move m = new Move (k,l,((Coordinate) startPositions.front().item()).getX(), ((Coordinate) startPositions.front().item()).getY());
						if (isValidMove(m, player))
						{
							allMoves.insertBack(m);
						}
					} 
					catch (InvalidNodeException e) 
					{
					}
				}
			}
		}
		return allMoves;
	}
}
/*

-> private boolean isValidMove(Move something, int player) // checks if the move is legal for player
-> public DList generateAllPossibleMoves(int player); // generates all the possible moves for player. 
//Returns a DList of Move objects. If there are no possible moves for player, the method returns an empty DList*/