package elor;

import java.util.*;

public class BitBoards {
	public static void importFEN(String fenString) {
	Main.WP=0; Main.WN=0; Main.WB=0;
        Main.WR=0; Main.WQ=0; Main.WK=0;
        Main.BP=0; Main.BN=0; Main.BB=0;
        Main.BR=0; Main.BQ=0; Main.BK=0;
        Main.CWK=false; Main.CWQ=false;
        Main.CBK=false; Main.CBQ=false;
	int charIndex = 0;
	int boardIndex = 0;
	while (fenString.charAt(charIndex) != ' ')
	{
		switch (fenString.charAt(charIndex++))
		{
		case 'P': Main.WP |= (1L << boardIndex++);
			break;
		case 'p': Main.BP |= (1L << boardIndex++);
			break;
		case 'N': Main.WN |= (1L << boardIndex++);
			break;
		case 'n': Main.BN |= (1L << boardIndex++);
			break;
		case 'B': Main.WB |= (1L << boardIndex++);
			break;
		case 'b': Main.BB |= (1L << boardIndex++);
			break;
		case 'R': Main.WR |= (1L << boardIndex++);
			break;
		case 'r': Main.BR |= (1L << boardIndex++);
			break;
		case 'Q': Main.WQ |= (1L << boardIndex++);
			break;
		case 'q': Main.BQ |= (1L << boardIndex++);
			break;
		case 'K': Main.WK |= (1L << boardIndex++);
			break;
		case 'k': Main.BK |= (1L << boardIndex++);
			break;
		case '/':
			break;
		case '1': boardIndex++;
			break;
		case '2': boardIndex += 2;
			break;
		case '3': boardIndex += 3;
			break;
		case '4': boardIndex += 4;
			break;
		case '5': boardIndex += 5;
			break;
		case '6': boardIndex += 6;
			break;
		case '7': boardIndex += 7;
			break;
		case '8': boardIndex += 8;
			break;
		default:
			break;
		}
	}
	Main.WhiteToMove = (fenString.charAt(++charIndex) == 'w');
	charIndex += 2;
	while (fenString.charAt(charIndex) != ' ')
	{
		switch (fenString.charAt(charIndex++))
		{
		case '-':
			break;
		case 'K': Main.CWK = true;
			break;
		case 'Q': Main.CWQ = true;
			break;
		case 'k': Main.CBK = true;
			break;
		case 'q': Main.CBQ = true;
			break;
		default:
			break;
		}
	}
	if (fenString.charAt(++charIndex) != '-')
	{
		Main.EP = Moves.FileMasks[fenString.charAt(charIndex++) - 'a'];
	}
    }
    public static void arrayToBitboards(String[][] chessBoard,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK) {
        String Binary;
        for (int i=0;i<64;i++) {
            Binary="0000000000000000000000000000000000000000000000000000000000000000";
            Binary=Binary.substring(i+1)+"1"+Binary.substring(0, i);
            switch (chessBoard[i/8][i%8]) {
                case "P": WP+=convertStringToBitboard(Binary);
                    break;
                case "N": WN+=convertStringToBitboard(Binary);
                    break;
                case "B": WB+=convertStringToBitboard(Binary);
                    break;
                case "R": WR+=convertStringToBitboard(Binary);
                    break;
                case "Q": WQ+=convertStringToBitboard(Binary);
                    break;
                case "K": WK+=convertStringToBitboard(Binary);
                    break;
                case "p": BP+=convertStringToBitboard(Binary);
                    break;
                case "n": BN+=convertStringToBitboard(Binary);
                    break;
                case "b": BB+=convertStringToBitboard(Binary);
                    break;
                case "r": BR+=convertStringToBitboard(Binary);
                    break;
                case "q": BQ+=convertStringToBitboard(Binary);
                    break;
                case "k": BK+=convertStringToBitboard(Binary);
                    break;
            }
        }
        drawArray(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
        Main.WP=WP; Main.WN=WN; Main.WB=WB;
        Main.WR=WR; Main.WQ=WQ; Main.WK=WK;
        Main.BP=BP; Main.BN=BN; Main.BB=BB;
        Main.BR=BR; Main.BQ=BQ; Main.BK=BK;
    }
    public static long convertStringToBitboard(String Binary) {
        if (Binary.charAt(0)=='0') {//not going to be a negative number
            return Long.parseLong(Binary, 2);
        } else {
            return Long.parseLong("1"+Binary.substring(2), 2)*2;
        }
    }
    public static void drawArray(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK) {
        String chessBoard[][]=new String[8][8];
        for (int i=0;i<64;i++) {
            chessBoard[i/8][i%8]=" ";
        }
        for (int i=0;i<64;i++) {
            if (((WP>>i)&1)==1) {chessBoard[i/8][i%8]="P";}
            if (((WN>>i)&1)==1) {chessBoard[i/8][i%8]="N";}
            if (((WB>>i)&1)==1) {chessBoard[i/8][i%8]="B";}
            if (((WR>>i)&1)==1) {chessBoard[i/8][i%8]="R";}
            if (((WQ>>i)&1)==1) {chessBoard[i/8][i%8]="Q";}
            if (((WK>>i)&1)==1) {chessBoard[i/8][i%8]="K";}
            if (((BP>>i)&1)==1) {chessBoard[i/8][i%8]="p";}
            if (((BN>>i)&1)==1) {chessBoard[i/8][i%8]="n";}
            if (((BB>>i)&1)==1) {chessBoard[i/8][i%8]="b";}
            if (((BR>>i)&1)==1) {chessBoard[i/8][i%8]="r";}
            if (((BQ>>i)&1)==1) {chessBoard[i/8][i%8]="q";}
            if (((BK>>i)&1)==1) {chessBoard[i/8][i%8]="k";}
        }
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }

}
