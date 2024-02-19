public class Moves {
	// Used to detect when a piece is at the boards edge
	static long FILE_A= 72340172838076673L;
	static long FILE_H= -9187201950435737472L;
	
	// Used to detect when a pawn has reached the promotion rank 
	static long RANK_8 =    255L;
	static long RANK_1 =    72057594037927936L;

	static long BLACK_PIECES;// Used to store the position of all black pieces
	static long WHITE_PIECES;// Used to store the position of all white pieces
	static long EMPTY;

  public static String allWhiteMoves(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK) {
	BLACK_PIECES = BP|BN|BB|BR|BQ;// no black king to prevent illegal captures
	EMPTY=~(WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK); // Stores the locations where there are no pieces
	String list=WPmoves(WP);
	return list;
	}

  
  public static String WPmoves(long WP) {
	String list=""; // returns the inital position and possible position in terms of x1 y1 and x2 y2
	
  long PAWN_MOVES=(WP>>8)&~RANK_8&EMPTY;// move forward
	for (int i=0; i<64; i++) {
		if (((PAWN_MOVES>>i)&1)==1) {
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	
  PAWN_MOVES=(WP>>7)&BLACK_PIECES&~RANK_8&~FILE_A;// capture right
	for (int i=0; i<64; i++) {
		if (((PAWN_MOVES>>i)&1)==1) {
			list+=""+(i%8-1)+(i/8+1)+(i%8)+(i/8)+" ";
		}
	}
	
    PAWN_MOVES=(WP>>9)&BLACK_PIECES&~RANK_8&~FILE_H; // capture left
	for (int i=0; i<64; i++) {
		if (((PAWN_MOVES>>i)&1)==1) {
		list+=""+(i%8+1)+(i/8-1)+(i%8)+(i/8);
		}
	}
	// need to add double move, en passent, and promotion
	return list;
  }
  
}
