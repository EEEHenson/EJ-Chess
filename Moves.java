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
     public static String WNMoves(long WN) 
  {
	String list = "";
	//Knight Moves
	long Knight_Moves=(WN>>15)&EMPTY&~FILE_H; //Knight One left two Forward
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
		Knight_Moves=(WN>>17)&EMPTY&~FILE_A; //Knight One right two Forward
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
  	Knight_Moves=(WN>>10)&EMPTY&~FILE_A; //Knight Two right one Forward
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(WN>>6)&EMPTY&~FILE_H; //Knight two left one Forward
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(WN<<10)&EMPTY&~FILE_H; //Knight two left one Backwards
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(WN<<17)&EMPTY&~FILE_H; //Knight one left two Backwards
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(WN<<15)&EMPTY&~FILE_A; //Knight one right two Backwards
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(WN<<6)&EMPTY&~FILE_A; //Knight two right one Backwards
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	//Knight Captures

	Knight_Moves=(WN>>15)&BLACK_PIECES&EMPTY&~FILE_H; //Knight One left two Forward
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
		Knight_Moves=(WN>>17)&BLACK_PIECES&EMPTY&~FILE_A; //Knight One right two Forward
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
  	Knight_Moves=(WN>>10)&BLACK_PIECES&EMPTY&~FILE_A; //Knight Two right one Forward
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(WN>>6)&BLACK_PIECES&EMPTY&~FILE_H; //Knight two left one Forward
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(WN<<10)&BLACK_PIECES&EMPTY&~FILE_H; //Knight two left one Backwards
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(WN<<17)&BLACK_PIECES&EMPTY&~FILE_H; //Knight one left two Backwards
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(WN<<15)&BLACK_PIECES&EMPTY&~FILE_A; //Knight one right two Backwards
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(WN<<6)&BLACK_PIECES&EMPTY&~FILE_A; //Knight two right one Backwards
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	return list;
  }
  public static String WKMoves(long WK) 
  {
	String list = "";

	long King_Moves=(WK>>8)&EMPTY; //King One Forward
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}

	King_Moves=(WK<<8)&EMPTY; //King One Backwards
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	King_Moves=(WK<<1)&EMPTY&~FILE_H; //King One Left
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	King_Moves=(WK>>1)&EMPTY&~FILE_A; //King One Right
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	King_Moves=(WK>>7)&EMPTY&~FILE_H; //King One Forward Diagonal Left
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	King_Moves=(WK>>9)&EMPTY&~FILE_A; //King One Forward Diagonal Right
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	King_Moves=(WK<<7)&EMPTY&~FILE_H; //King One Backwards Diagonal Left
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	King_Moves=(WK<<9)&EMPTY&~FILE_A; //King One Backwards Diagonal Right
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	//King Captures

	King_Moves=(WK>>8)&BLACK_PIECES&EMPTY; //King One Forward
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}

	King_Moves=(WK<<8)&BLACK_PIECES&EMPTY; //King One Backwards
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	King_Moves=(WK<<1)&BLACK_PIECES&EMPTY&~FILE_H; //King One Left
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	King_Moves=(WK>>1)&BLACK_PIECES&EMPTY&~FILE_A; //King One Right
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	King_Moves=(WK>>7)&BLACK_PIECES&EMPTY&~FILE_H; //King One Forward Diagonal Left
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	King_Moves=(WK>>9)&BLACK_PIECES&EMPTY&~FILE_A; //King One Forward Diagonal Right
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	King_Moves=(WK<<7)&BLACK_PIECES&EMPTY&~FILE_H; //King One Backwards Diagonal Left
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	King_Moves=(WK<<9)&BLACK_PIECES&EMPTY&~FILE_A; //King One Backwards Diagonal Right
	for (int i=0; i<64; i++) {
		if (((King_Moves>>i)&1)==1) 
		{
			list+="("+(i%8)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}

	return list;
 }
  
}
