package elor;

public class Moves {
	
	// Used to detect when a piece is at the boards edge
	static long FILE_A= 72340172838076673L;
	static long FILE_H= -9187201950435737472L;
	static long FILE_AB= 217020518514230019L;
	static long FILE_GH= -4557430888798830400L;
	
	// Used to detect when a pawn has reached the promotion rank 
	static long RANK_8 =    255L;
	static long RANK_1=     -72057594037927936L;
	static long RANK_4=     1095216660480L;
	static long RANK_5 =    4278190080L;
	
	// Used to store the position of all black pieces
	static long BLACK_PIECES;
	// Used to store the position of all white pieces
	static long WHITE_PIECES;
	
	static long CASTLE_ROOKS[]={63,56,7,0};
	
	static long EMPTY;
	static long OCCUPIED;
	static long NOT_MY_PIECES;
	static long MY_PIECES;
	
	static long RankMasks[] =/*from rank1 to rank8*/
	    {
	        0xFFL, 0xFF00L, 0xFF0000L, 0xFF000000L, 0xFF00000000L, 0xFF0000000000L, 0xFF000000000000L, 0xFF00000000000000L
	    };
	static long FileMasks[] =
	    {
	        0x101010101010101L, 0x202020202020202L, 0x404040404040404L, 0x808080808080808L,
	        0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
	    };
	static long DiagonalMasks[] =
	    {
		0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L,
		0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L,
		0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L
	    };
	static long AntiDiagonalMasks[] =
	    {
		0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L, 0x80402010080402L,
		0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L,
		0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L
	    };
	
	static long HorizontalAndVertical(int s) {
        long binaryS=1L<<s;
        long possibilitiesHorizontal = (OCCUPIED - 2 * binaryS) ^ Long.reverse(Long.reverse(OCCUPIED) - 2 * Long.reverse(binaryS));
        long possibilitiesVertical = ((OCCUPIED&FileMasks[s % 8]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&FileMasks[s % 8]) - (2 * Long.reverse(binaryS)));
        return (possibilitiesHorizontal&RankMasks[s / 8]) | (possibilitiesVertical&FileMasks[s % 8]);
    }
    static long DiagonalAndADiagonal(int s) {
        long binaryS=1L<<s;
        long possibilitiesDiagonal = ((OCCUPIED&DiagonalMasks[(s / 8) + (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&DiagonalMasks[(s / 8) + (s % 8)]) - (2 * Long.reverse(binaryS)));
        long possibilitiesAntiDiagonal = ((OCCUPIED&AntiDiagonalMasks[(s / 8) + 7 - (s % 8)]) - (2 * binaryS)) ^ Long.reverse(Long.reverse(OCCUPIED&AntiDiagonalMasks[(s / 8) + 7 - (s % 8)]) - (2 * Long.reverse(binaryS)));
        return (possibilitiesDiagonal&DiagonalMasks[(s / 8) + (s % 8)]) | (possibilitiesAntiDiagonal&AntiDiagonalMasks[(s / 8) + 7 - (s % 8)]);
    }
	
	public static String allWhiteMoves(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean CWK,boolean CWQ,boolean CBK,boolean CBQ) {
		BLACK_PIECES=BP|BN|BB|BR|BQ;
		NOT_MY_PIECES=~(WP|WN|WB|WR|WQ|WK|BK);// black king to prevent illegal captures
		MY_PIECES=WP|WN|WB|WR|WQ; //
        OCCUPIED=WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK;// stores all occupied locations
		EMPTY=~OCCUPIED; // finds all empty squares
		long danger = dangerWhite(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
		String list=wPawnMoves(WP, BP, EP)+
				knightMoves(WN)+
				rookMoves(OCCUPIED, WR)+
				queenMoves(OCCUPIED, WQ)+
				bishopMoves(OCCUPIED, WB)+
				kingMoves(WK, danger);
		return list;
	}
	
	public static String allBlackMoves(long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean CWK,boolean CWQ,boolean CBK,boolean CBQ) {
		WHITE_PIECES = WP|WN|WB|WR|WQ;// no white king to prevent illegal captures
		NOT_MY_PIECES=~(BP|BN|BB|BR|BQ|WK|BK);// black king to prevent illegal captures
		MY_PIECES=BP|BN|BB|BR|BQ; //
        OCCUPIED=WP|WN|WB|WR|WQ|WK|BP|BN|BB|BR|BQ|BK;// stores all occupied locations
		EMPTY=~OCCUPIED; // finds all empty squares
		long danger = dangerBlack(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
		String list=bPawnMoves(BP,WP,EP)+
				knightMoves(BN)+
				rookMoves(OCCUPIED,BR)+
				queenMoves(OCCUPIED, BQ)+
				bishopMoves(OCCUPIED, BB)+
				kingMoves(BK, danger);;
		return list;
	}
	
	//[x] generates all possible moves for the white pawn
	public static String wPawnMoves(long WP, long BP, long EP) {
		String list="";
		long PAWN_MOVES=(WP>>8)&~RANK_8&EMPTY;// move forward
		for (int i=0; i<64; i++) {
			if (((PAWN_MOVES>>i)&1)==1) {
				list+=""+(i%8)+(i/8+1)+(i%8)+(i/8);
			}
		}
		PAWN_MOVES=(WP>>7)&BLACK_PIECES&~RANK_8&~FILE_A;// capture right
		for (int i=0; i<64; i++) {
			if (((PAWN_MOVES>>i)&1)==1) {
				list+=""+(i%8-1)+(i/8+1)+(i%8)+(i/8);
			}
		}
		PAWN_MOVES=(WP>>9)&BLACK_PIECES&~RANK_8&~FILE_H; // capture left
		for (int i=0; i<64; i++) {
			if (((PAWN_MOVES>>i)&1)==1) {
			list+=""+(i%8+1)+(i/8+1)+(i%8)+(i/8);
			}
		}
		
		//Double move
		PAWN_MOVES=(WP>>16)&EMPTY&(EMPTY>>8)&RANK_4;//move 2 forward
        long possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8)+(index/8+2)+(index%8)+(index/8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
		
		//Pawn Promotion
		PAWN_MOVES=(WP>>7)&BLACK_PIECES&RANK_8&~FILE_A;//pawn promotion by capture right
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+"QP"+(index%8-1)+(index%8)+"RP"+(index%8-1)+(index%8)+"BP"+(index%8-1)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>9)&NOT_MY_PIECES&OCCUPIED&RANK_8&~FILE_H;//pawn promotion by capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+"QP"+(index%8+1)+(index%8)+"RP"+(index%8+1)+(index%8)+"BP"+(index%8+1)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(WP>>8)&EMPTY&RANK_8;//pawn promotion by move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8)+(index%8)+"QP"+(index%8)+(index%8)+"RP"+(index%8)+(index%8)+"BP"+(index%8)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        
      //en passant right
        possibility = (WP << 1)&BP&RANK_5&~FILE_A&EP;//shows piece to remove, not the destination
        if (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+"WE";
        }
        //en passant left
        possibility = (WP >> 1)&BP&RANK_5&~FILE_H&EP;//shows piece to remove, not the destination
        if (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+"WE";
        }
        
        return list;
	}

	
	//[x] generates all possible moves for the black pawn
	public static String bPawnMoves(long BP, long WP, long EP) {
		String list="";
		long PAWN_MOVES=(BP<<8)&~RANK_1&EMPTY;// move forward
		for (int i=0; i<64; i++) {
			if (((PAWN_MOVES>>i)&1)==1) {
				list+=""+(i%8)+(i/8-1)+(i%8)+(i/8);
			}
		}
		PAWN_MOVES=(BP<<7)&WHITE_PIECES&~RANK_1&~FILE_H;// capture right
		for (int i=0; i<64; i++) {
			if (((PAWN_MOVES>>i)&1)==1) {
				list+=""+(i%8+1)+(i/8-1)+(i%8)+(i/8);
			}
		}
		PAWN_MOVES=(BP<<9)&WHITE_PIECES&~RANK_1&~FILE_A; // capture left
		for (int i=0; i<64; i++) {
			if (((PAWN_MOVES>>i)&1)==1) {
			list+=""+(i%8-1)+(i/8-1)+(i%8)+(i/8);
			}
		}
		
		//Double move
		PAWN_MOVES=(BP<<16)&EMPTY&(EMPTY<<8)&RANK_5;//move 2 forward
		long possibility=PAWN_MOVES&~(PAWN_MOVES-1);
		while (possibility != 0)
		{
		        int index=Long.numberOfTrailingZeros(possibility);
		        list+=""+(index%8)+(index/8+2)+(index%8)+(index/8);
		        PAWN_MOVES&=~possibility;
		        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
		}
				
				//Pawn Promotion
		PAWN_MOVES=(BP<<7)&NOT_MY_PIECES&OCCUPIED&RANK_1&~FILE_H;//pawn promotion by capture right
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+"qP"+(index%8+1)+(index%8)+"rP"+(index%8+1)+(index%8)+"bP"+(index%8+1)+(index%8)+"nP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
		
        PAWN_MOVES=(BP<<9)&NOT_MY_PIECES&OCCUPIED&RANK_1&~FILE_A;//pawn promotion by capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+"qP"+(index%8-1)+(index%8)+"rP"+(index%8-1)+(index%8)+"bP"+(index%8-1)+(index%8)+"nP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
		
		PAWN_MOVES=(BP<<8)&EMPTY&RANK_1;//pawn promotion by move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8)+(index%8)+"qP"+(index%8)+(index%8)+"rP"+(index%8)+(index%8)+"bP"+(index%8)+(index%8)+"nP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
		        
		      //en passant right
		possibility = (BP << 1)&WP&RANK_4&~FILE_H&EP;//shows piece to remove, not the destination
		if (possibility != 0)
		{
		    int index=Long.numberOfTrailingZeros(possibility);
		    list+=""+(index%8-1)+(index%8)+"BE";
		}
		//en passant left
		possibility = (BP << 1)&WP&RANK_4&~FILE_A&EP;//shows piece to remove, not the destination
		if (possibility != 0)
		{
		    int index=Long.numberOfTrailingZeros(possibility);
		    list+=""+(index%8+1)+(index%8)+"BE";
		}
		return list;
	}
	
	//[*] generates all possible moves for the Knight
	public static String knightMoves(long N) 
	  {
		String list = "";
		//Knight Moves
		long Knight_Moves=(N>>15)&NOT_MY_PIECES&~FILE_A; //Knight One left two Forward
		for (int i=0; i<64; i++) {
			if (((Knight_Moves>>i)&1)==1) {
				list+=""+(i%8-1)+(i/8+2)+(i%8)+(i/8);
			}
		}
		Knight_Moves=(N>>17)&NOT_MY_PIECES&~FILE_H; //Knight One right two Forward
		for (int i=0; i<64; i++) {
			if (((Knight_Moves>>i)&1)==1) {
				list+=""+(i%8+1)+(i/8+2)+(i%8)+(i/8);
			}
		}
	  	Knight_Moves=(N>>10)&NOT_MY_PIECES&~FILE_GH; //Knight Two right one Forward
		for (int i=0; i<64; i++) {
			if (((Knight_Moves>>i)&1)==1) {
				list+=""+(i%8+2)+(i/8+1)+(i%8)+(i/8);
			}
		}
		Knight_Moves=(N>>6)&NOT_MY_PIECES&~FILE_AB; //Knight two left one Forward
		for (int i=0; i<64; i++) {
			if (((Knight_Moves>>i)&1)==1) {
				list+=""+(i%8-2)+(i/8+1)+(i%8)+(i/8);
			}
		}
		Knight_Moves=(N<<10)&NOT_MY_PIECES&~FILE_AB; //Knight two left one Backwards
		for (int i=0; i<64; i++) {
			if (((Knight_Moves>>i)&1)==1) {
				list+=""+(i%8-2)+(i/8-1)+(i%8)+(i/8);
			}
		}
		Knight_Moves=(N<<17)&NOT_MY_PIECES&~FILE_A; //Knight one left two Backwards
		for (int i=0; i<64; i++) {
			if (((Knight_Moves>>i)&1)==1) {
				list+=""+(i%8-1)+(i/8-2)+(i%8)+(i/8);
			}
		}
		Knight_Moves=(N<<15)&NOT_MY_PIECES&~FILE_H; //Knight one right two Backwards
		for (int i=0; i<64; i++) {
			if (((Knight_Moves>>i)&1)==1) {
				list+=""+(i%8+1)+(i/8-2)+(i%8)+(i/8);
			}
		}
		Knight_Moves=(N<<6)&NOT_MY_PIECES&~FILE_GH; //Knight two right one Backwards
		for (int i=0; i<64; i++) {
			if (((Knight_Moves>>i)&1)==1) {
				list+=""+(i%8+2)+(i/8-1)+(i%8)+(i/8);
			}
		}
		return list;
	  }

	//[*] generates all possible moves for the Rook
	public static String rookMoves(long OCCUPIED, long R) {
		String list="";
        long i=R&~(R-1);
        long possibility;
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            possibility=HorizontalAndVertical(iLocation)&NOT_MY_PIECES;
            long j=possibility&~(possibility-1);
            while (j != 0)
            {
                int index=Long.numberOfTrailingZeros(j);
                list+=""+(iLocation%8)+(iLocation/8)+(index%8)+(index/8);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            R&=~i;
            i=R&~(R-1);
        }
        return list;
	}

	//[*] generates all possible moves for the Bishop
	public static String bishopMoves(long OCCUPIED, long B) {
        String list="";
        long i=B&~(B-1);
        long possibility;
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            possibility=DiagonalAndADiagonal(iLocation)&NOT_MY_PIECES;
            long j=possibility&~(possibility-1);
            while (j != 0)
            {
                int index=Long.numberOfTrailingZeros(j);
                list+=""+(iLocation%8)+(iLocation/8)+(index%8)+(index/8);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            B&=~i;
            i=B&~(B-1);
        }
        return list;
	}
	
	//[*] generates all possible moves for the Queen
	public static String queenMoves(long OCCUPIED,long Q) {
        String list="";
        long i=Q&~(Q-1);
        long possibility;
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            possibility=(HorizontalAndVertical(iLocation)|DiagonalAndADiagonal(iLocation))&NOT_MY_PIECES;
            long j=possibility&~(possibility-1);
            while (j != 0)
            {
                int index=Long.numberOfTrailingZeros(j);
                list+=""+(iLocation%8)+(iLocation/8)+(index%8)+(index/8);
                possibility&=~j;
                j=possibility&~(possibility-1);
            }
            Q&=~i;
            i=Q&~(Q-1);
        }
        return list;
    }
	
	//[x] generates all possible moves for the King
	public static String kingMoves(long K, long danger) {
		String list = "";

		long King_Moves=(K>>8)&NOT_MY_PIECES&~danger; //King One Forward
		for (int i=0; i<64; i++) {
			if (((King_Moves>>i)&1)==1) {
				list+=""+(i%8)+(i/8+1)+(i%8)+(i/8);
			}
		}

		King_Moves=(K<<8)&NOT_MY_PIECES&~danger; //King One Backwards
		for (int i=0; i<64; i++) {
			if (((King_Moves>>i)&1)==1) {
				list+=""+(i%8)+(i/8-1)+(i%8)+(i/8);
			}
		}
		King_Moves=(K<<1)&NOT_MY_PIECES&~FILE_A&~danger; //King One Left
		for (int i=0; i<64; i++) {
			if (((King_Moves>>i)&1)==1) {
				list+=""+(i%8-1)+(i/8)+(i%8)+(i/8);
			}
		}
		King_Moves=(K>>1)&NOT_MY_PIECES&~FILE_H&~danger; //King One Right
		for (int i=0; i<64; i++) {
			if (((King_Moves>>i)&1)==1) {
				list+=""+(i%8+1)+(i/8)+(i%8)+(i/8);
			}
		}
		King_Moves=(K>>7)&NOT_MY_PIECES&~FILE_A&~danger; //King One Forward Diagonal Left
		for (int i=0; i<64; i++) {
			if (((King_Moves>>i)&1)==1) {
				list+=""+(i%8-1)+(i/8+1)+(i%8)+(i/8);
			}
		}
		King_Moves=(K>>9)&NOT_MY_PIECES&~FILE_H&~danger; //King One Forward Diagonal Right
		for (int i=0; i<64; i++) {
			if (((King_Moves>>i)&1)==1) {
				list+=""+(i%8+1)+(i/8+1)+(i%8)+(i/8);
			}
		}
		King_Moves=(K<<7)&NOT_MY_PIECES&~FILE_H&~danger; //King One Backwards Diagonal Left
		for (int i=0; i<64; i++) {
			if (((King_Moves>>i)&1)==1) {
				list+=""+(i%8+1)+(i/8-1)+(i%8)+(i/8);
			}
		}
		King_Moves=(K<<9)&NOT_MY_PIECES&~FILE_A&~danger; //King One Backwards Diagonal Right
		for (int i=0; i<64; i++) {
			if (((King_Moves>>i)&1)==1) {
				list+=""+(i%8-1)+(i/8-1)+(i%8)+(i/8);
			}
		}
		return list;
	 }
	
	// castling
	public static String castleWhite(long WR, boolean CWK, boolean CWQ) {
		String list="";
		if (CWK&&(((1l<<CASTLE_ROOKS[0])&WR)!=0))
		{
			list+="7476";
		}
		if (CWQ&&(((1l<<CASTLE_ROOKS[0])&WR)!=0))
		{
			list+="7472";
		}
		return list;
	}
	
	public static String castleBlack(long BR, boolean CBK, boolean CBQ) {
		String list="";
		if (CBK&&(((1l<<CASTLE_ROOKS[0])&BR)!=0))
		{
			list+="0406";
		}
		if (CBQ&&(((1l<<CASTLE_ROOKS[0])&BR)!=0))
		{
			list+="0402";
		}
		return list;
	}
	
	// squares under attack
	public static long dangerWhite(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK) {
		long danger;
		long i;
		long possibility;
		// Black Pawn
		danger=((BP<<7)&~FILE_A);
		danger|=((BP<<9)&~FILE_H);
		// Knight
		danger |= BN >>15&~FILE_A;
		danger |= BN >>17&~FILE_H;
		danger |= BN >>10&~FILE_GH;
		danger |= BN >>6&~FILE_AB;
		danger |= BN <<6&~FILE_GH;
		danger |= BN <<10&~FILE_AB;
		danger |= BN <<17&~FILE_A;
		danger |= BN <<15&~FILE_H;
		// Diagonal Attacks (Bishop and Queen)
		long QB=BQ|BB;
		i=QB&~(QB-1);
		while (i!=0) {
			int iLocation=Long.numberOfTrailingZeros(i);
			possibility = DiagonalAndADiagonal(iLocation);
			danger |= possibility;
			QB&=~i;
			i=QB&~(QB-1);	
		}
		// Horizontal and Vertical moves (Rook and Queen)
		long QR=BQ|BR;
		i=QR&~(QR-1);
		while (i!=0) {
			int iLocation=Long.numberOfTrailingZeros(i);
			possibility = HorizontalAndVertical(iLocation);
			danger |= possibility;
			QR&=~i;
			i=QR&~(QR-1);
		}
		// king
		danger |= BK>>8;
		danger |= BK<<8;
		danger |= BK<<1&~FILE_A;
		danger |= BK>>1&~FILE_H;
		danger |= BK>>7&~FILE_A;
		danger |= BK>>9&~FILE_H;
		danger |= BK<<7&~FILE_H;
		danger |= BK<<9&~FILE_A;
		return danger;
	}
	public static long dangerBlack(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK) {
		long danger;
		long i;
		long possibility;
		// Black Pawn
		danger=((WP>>7)&~FILE_A);
		danger|=((WP>>9)&~FILE_H);
		// Knight
		danger |= WN >>15&~FILE_A;
		danger |= WN >>17&~FILE_H;
		danger |= WN >>10&~FILE_GH;
		danger |= WN >>6&~FILE_AB;
		danger |= WN <<6&~FILE_GH;
		danger |= WN <<10&~FILE_AB;
		danger |= WN <<17&~FILE_A;
		danger |= WN <<15&~FILE_H;
		// Diagonal Attacks (Bishop and Queen)
		long QB=WQ|WB;
		i=QB&~(QB-1);
		while (i!=0) {
			int iLocation=Long.numberOfTrailingZeros(i);
			possibility = DiagonalAndADiagonal(iLocation);
			danger |= possibility;
			QB&=~i;
			i=QB&~(QB-1);	
		}
		// Horizontal and Vertical moves (Rook and Queen)
		long QR=WQ|WR;
		i=QR&~(QR-1);
		while (i!=0) {
			int iLocation=Long.numberOfTrailingZeros(i);
			possibility = HorizontalAndVertical(iLocation);
			danger |= possibility;
			QR&=~i;
			i=QR&~(QR-1);
		}
		// king
		danger |= WK>>8;
		danger |= WK<<8;
		danger |= WK<<1&~FILE_A;
		danger |= WK>>1&~FILE_H;
		danger |= WK>>7&~FILE_A;
		danger |= WK>>9&~FILE_H;
		danger |= WK<<7&~FILE_H;
		danger |= WK<<9&~FILE_A;
		return danger;
	}
	
	public static long makeMove(long board, String move, char type) {
        if (Character.isDigit(move.charAt(3))) {//'regular' move
            int start=(Character.getNumericValue(move.charAt(0)))+(Character.getNumericValue(move.charAt(1))*8);
            int end=(Character.getNumericValue(move.charAt(2)))+(Character.getNumericValue(move.charAt(3))*8);
            if (((board>>>start)&1)==1) {board&=~(1L<<start); board|=(1L<<end);} else {board&=~(1L<<end);}
        } else if (move.charAt(3)=='P') {//pawn promotion
            int start, end;
            if (Character.isUpperCase(move.charAt(2))) {
                start=Long.numberOfTrailingZeros(FileMasks[move.charAt(0)-'0']&RankMasks[1]);
                end=Long.numberOfTrailingZeros(FileMasks[move.charAt(1)-'0']&RankMasks[0]);
            } else {
                start=Long.numberOfTrailingZeros(FileMasks[move.charAt(0)-'0']&RankMasks[6]);
                end=Long.numberOfTrailingZeros(FileMasks[move.charAt(1)-'0']&RankMasks[7]);
            }
            if (type==move.charAt(2)) {board|=(1L<<end);} else {board&=~(1L<<start); board&=~(1L<<end);}
        } else if (move.charAt(3)=='E') {//en passant
            int start, end;
            if (Character.isUpperCase(move.charAt(2))) {
                start=Long.numberOfTrailingZeros(FileMasks[move.charAt(0)-'0']&RankMasks[4]);
                end=Long.numberOfTrailingZeros(FileMasks[move.charAt(1)-'0']&RankMasks[5]);
                board&=~(1L<<(FileMasks[move.charAt(1)-'0']&RankMasks[4]));
            } else {
                start=Long.numberOfTrailingZeros(FileMasks[move.charAt(0)-'0']&RankMasks[3]);
                end=Long.numberOfTrailingZeros(FileMasks[move.charAt(1)-'0']&RankMasks[2]);
                board&=~(1L<<(FileMasks[move.charAt(1)-'0']&RankMasks[3]));
            }
            if (((board>>>start)&1)==1) {board&=~(1L<<start); board|=(1L<<end);}
        } else {
            System.out.print("ERROR: Invalid move");
        }
        return board;
    }
    public static long makeMoveEP(long board,String move) {
        if (Character.isDigit(move.charAt(3))) {
            int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
            if ((Math.abs(move.charAt(0)-move.charAt(2))==2)&&(((board>>>start)&1)==1)) {//pawn double push
                return FileMasks[move.charAt(1)-'0'];
            }
        }
        return 0;
    }
    
    public static long makeMoveCastle(long rookBoard, long kingBoard, String move, char type) {
        int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
        if ((((kingBoard>>>start)&1)==1)&&(("0402".equals(move))||("0406".equals(move))||("7472".equals(move))||("7476".equals(move)))) {//'regular' move
            if (type=='R') {
                switch (move) {
                    case "7472": rookBoard&=~(1L<<CASTLE_ROOKS[1]); rookBoard|=(1L<<(CASTLE_ROOKS[1]+3));
                        break;
                    case "7476": rookBoard&=~(1L<<CASTLE_ROOKS[0]); rookBoard|=(1L<<(CASTLE_ROOKS[0]-2));
                        break;
                }
            } else {
                switch (move) {
                    case "0402": rookBoard&=~(1L<<CASTLE_ROOKS[3]); rookBoard|=(1L<<(CASTLE_ROOKS[3]+3));
                        break;
                    case "0406": rookBoard&=~(1L<<CASTLE_ROOKS[2]); rookBoard|=(1L<<(CASTLE_ROOKS[2]-2));
                        break;
                }
            }
        }
        return rookBoard;
    }
	
}
