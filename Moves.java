public class Moves {
	// Used to detect when a piece is at the boards edge
	static long FILE_A= 72340172838076673L;
	static long FILE_H= -9187201950435737472L;
	static long FILE_AB= 217020518514230019L;
	static long FILE_GH= -4557430888798830400L;
	
	// Used to detect when a pawn has reached the promotion rank 
	static long RANK_8 =    255L;
	static long RANK_1 =    72057594037927936L;

	static long EMPTY;
	static long OCCUPIED;
	static long NOT_MY_PIECES;
	static long MY_PIECES;
	
	static long RankMasks8[] =/*from rank1 to rank8*/
	    {
	        0xFFL, 0xFF00L, 0xFF0000L, 0xFF000000L, 0xFF00000000L, 0xFF0000000000L, 0xFF000000000000L, 0xFF00000000000000L
	    };
	    static long FileMasks8[] =/*from fileA to FileH*/
	    {
	        0x101010101010101L, 0x202020202020202L, 0x404040404040404L, 0x808080808080808L,
	        0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
	    };
	    static long DiagonalMasks8[] =/*from top left to bottom right*/
	    {
		0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L,
		0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L,
		0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L
	    };
	    static long AntiDiagonalMasks8[] =/*from top right to bottom left*/
	    {
		0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L, 0x80402010080402L,
		0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L,
		0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L
	    };
	
	static long HAndVMoves(int s) {
        long binaryS=1L<<s;
        long possibilitiesHorizontal = (OCCUPIED - 2 * binaryS) ^ Long.reverse(Long.reverse(OCCUPIED) - 2 * Long.reverse(binaryS));
        long possibilitiesVertical = ((OCCUPIED&FileMasks8[s % 8]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&FileMasks8[s % 8]) - (2 * Long.reverse(binaryS)));
        return (possibilitiesHorizontal&RankMasks8[s / 8]) | (possibilitiesVertical&FileMasks8[s % 8]);
    }
    static long DAndAntiDMoves(int s) {
        long binaryS=1L<<s;
        long possibilitiesDiagonal = ((OCCUPIED&DiagonalMasks8[(s / 8) + (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&DiagonalMasks8[(s / 8) + (s % 8)]) - (2 * Long.reverse(binaryS)));
        long possibilitiesAntiDiagonal = ((OCCUPIED&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]) - (2 * Long.reverse(binaryS)));
        return (possibilitiesDiagonal&DiagonalMasks8[(s / 8) + (s % 8)]) | (possibilitiesAntiDiagonal&AntiDiagonalMasks8[(s / 8) + 7 - (s % 8)]);
    }

  public static String allWhiteMoves(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK) {
	NOT_MY_PIECES = BP|BN|BB|BR|BQ;// no black king to prevent illegal captures
	MY_PIECES = WP|WN|WB|WR|WQ;
	OCCUPIED=WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK // Stores the location of all pieces
	EMPTY=~OCCUPIED; // Stores the locations where there are no pieces
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
public static String KnightMoves(long N) 
  {
	String list = "";
	//Knight Moves
	long Knight_Moves=(N>>15)&NOT_MY_PIECES&~FILE_A; //Knight One left two Forward
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8-1)+(i/8+2)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(N>>17)&NOT_MY_PIECES&~FILE_H; //Knight One right two Forward
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8+1)+(i/8+2)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(N>>10)&NOT_MY_PIECES&~FILE_GH; //Knight Two right one Forward
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8+2)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(N>>6)&NOT_MY_PIECES&~FILE_AB; //Knight two left one Forward
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8-2)+(i/8+1)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(N<<10)&NOT_MY_PIECES&~FILE_AB; //Knight two left one Backwards
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8-2)+(i/8-1)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(N<<17)&NOT_MY_PIECES&~FILE_A; //Knight one left two Backwards
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8-1)+(i/8-2)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(N<<15)&NOT_MY_PIECES&~FILE_H; //Knight one right two Backwards
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8+1)+(i/8-2)+","+(i%8)+(i/8)+"),";
		}
	}
	Knight_Moves=(N<<6)&NOT_MY_PIECES&~FILE_GH; //Knight two right one Backwards
	for (int i=0; i<64; i++) {
		if (((Knight_Moves>>i)&1)==1) 
		{
			list+="("+(i%8+2)+(i/8-1)+","+(i%8)+(i/8)+"),";
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
