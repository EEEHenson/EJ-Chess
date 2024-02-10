import java.util.*;

public class BitBoards {
	public static void initGame() {
		String board[][] = {
				{"r", "n", "b", "q", "k", "b", "n", "r"},
				{"p", "p", "p", "p", "p", "p", "p", "p"},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{" ", " ", " ", " ", " ", " ", " ", " "},
				{"P", "P", "P", "P", "P", "P", "P", "P"},
				{"R", "N", "B", "Q", "K", "B", "N", "R"},
		};
		long WP=0L, WR=0L, WN=0L, WB=0L, WQ=0L, WK=0L, BP=0L, BR=0L, BN=0L, BB=0L, BQ=0L, BK=0L;
		arrayToBitboards(board, WP, WR, WN, WB, WQ, WK, BP, BR, BN, BB, BQ, BK);
	}
	
	public static void arrayToBitboards(String[][] board, long WP, long WR, long WN, long WB, long WQ, long WK, long BP, long BR, long BN, long BB, long BQ, long BK){
		String binary;
		for (int i=0; i<64; i++) {
			binary="0000000000000000000000000000000000000000000000000000000000000000";
			binary=binary.substring(i+1)+"1"+binary.substring(0, i);
			switch (board[i/8][i%8]) {
				case "P": WP+=convertStringToBitboard(binary);
					break;
				case "p": BP+=convertStringToBitboard(binary);
					break;
				case "R": WR+=convertStringToBitboard(binary);
					break;
				case "r": BR+=convertStringToBitboard(binary);
					break;
				case "N": WN+=convertStringToBitboard(binary);
					break;
				case "n": BN+=convertStringToBitboard(binary);
					break;
				case "B": WB+=convertStringToBitboard(binary);
					break;
				case "b": BB+=convertStringToBitboard(binary);
					break;
				case "Q": WQ+=convertStringToBitboard(binary);
					break;
				case "q": BQ+=convertStringToBitboard(binary);
					break;
				case "K": WK+=convertStringToBitboard(binary);
					break;
				case "k": BK+=convertStringToBitboard(binary);
					break;
				
			}
		}
		
		drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
		System.out.println("white rook value = " + WR);
		System.out.println("White knight value = " + WN);
		System.out.println("white bishop value = " + WB);
		System.out.println("White queen value = " + WQ);
		System.out.println("white king value = " + WK);
		System.out.println("White pawn value = " + WP);
		System.out.println("black rook value = " + BR);
		System.out.println("Black knight value = " + BN);
		System.out.println("black bishop value = " + BB);
		System.out.println("Black queen value = " + BQ);
		System.out.println("black king value = " + BK);
		System.out.println("Black pawn value = " + BP);
		
		BP = Moves.bPawnMove(BP);
		WP = Moves.wPawnMove(WP);
		System.out.println("Black pawn value = " + BP);
		System.out.println("White pawn value = " + WP);
		drawArray(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK);
		
	}
	
    public static long convertStringToBitboard(String binary) {
        if (binary.charAt(0)=='0') {
            return Long.parseLong(binary, 2);
        } else {
            return Long.parseLong("1"+binary.substring(2), 2)*2;
        }
    }
	
	public static void drawArray(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK) {
        String chessBoard[][]=new String[8][8];
        for (int i=0;i<64;i++) {
            chessBoard[i/8][i%8]=" ";
        }
        for (int i=0;i<64;i++) {
            if (((WP>>i)&1)==1) {
            	chessBoard[i/8][i%8]="P";}
            if (((WN>>i)&1)==1) {
            	chessBoard[i/8][i%8]="N";}
            if (((WB>>i)&1)==1) {
            	chessBoard[i/8][i%8]="B";}
            if (((WR>>i)&1)==1) {
            	chessBoard[i/8][i%8]="R";}
            if (((WQ>>i)&1)==1) {
            	chessBoard[i/8][i%8]="Q";}
            if (((WK>>i)&1)==1) {
            	chessBoard[i/8][i%8]="K";}
            if (((BP>>i)&1)==1) {
            	chessBoard[i/8][i%8]="p";}
            if (((BN>>i)&1)==1) {
            	chessBoard[i/8][i%8]="n";}
            if (((BB>>i)&1)==1) {
            	chessBoard[i/8][i%8]="b";}
            if (((BR>>i)&1)==1) {
            	chessBoard[i/8][i%8]="r";}
            if (((BQ>>i)&1)==1) {
            	chessBoard[i/8][i%8]="q";}
            if (((BK>>i)&1)==1) {
            	chessBoard[i/8][i%8]="k";}
        }
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }

}
