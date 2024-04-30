package elor;

import java.util.*;
public class UCI {
    static String ENGINENAME="EJ-Chess";
    public static void uciCommunication() {
        while (true)
        {
            Scanner input = new Scanner(System.in);
            String inputString=input.nextLine();
            if ("uci".equals(inputString))
            {
                inputUCI();
            }
            else if (inputString.startsWith("setoption"))
            {
                inputSetOption(inputString);
            }
            else if ("isready".equals(inputString))
            {
                inputIsReady();
            }
            else if ("ucinewgame".equals(inputString))
            {
                inputUCINewGame();
            }
            else if (inputString.startsWith("position"))
            {
                inputPosition(inputString);
            }
            else if (inputString.startsWith("go"))
            {
                inputGo();
            }
            else if ("print".equals(inputString))
            {
                inputPrint();
            }
            else if ("quit".equals(inputString))
            {
            	inputQuit();
            }
        }
    }
    public static void inputUCI() {
        System.out.println("id name "+ENGINENAME);
        System.out.println("id author Ethan Henson and Jodie Phipps");
        //options go here
        System.out.println("uciok");
    }
    public static void inputSetOption(String inputString) {
        //set options
    }
    public static void inputIsReady() {
         System.out.println("readyok");
    }
    public static void inputUCINewGame() {
        //add code here
    }
    public static void inputPosition(String input) {
        input=input.substring(9).concat(" ");
        if (input.contains("startpos ")) {
            input=input.substring(9);
            BitBoards.importFEN(Main.startpos);
        }
        else if (input.contains("fen")) {
            input=input.substring(4);
            BitBoards.importFEN(input);
        }
        if (input.contains("moves")) {
            input=input.substring(input.indexOf("moves")+6);
            //make each of the moves
            while (input.length()>0)
            {
            	String moves;
            	if (Main.WhiteToMove) {
            		moves=Moves.allWhiteMoves(Main.WP,Main.WN,Main.WB,Main.WR,Main.WQ,Main.WK,Main.BP,Main.BN,Main.BB,Main.BR,Main.BQ,Main.BK,Main.EP,Main.CWK,Main.CWQ,Main.CBK,Main.CBQ);
            	}
            	else {
            		moves=Moves.allBlackMoves(Main.WP,Main.WN,Main.WB,Main.WR,Main.WQ,Main.WK,Main.BP,Main.BN,Main.BB,Main.BR,Main.BQ,Main.BK,Main.EP,Main.CWK,Main.CWQ,Main.CBK,Main.CBQ);
            	}
            	algebraToMove(input,moves);
            	input=input.substring(input.indexOf(' ')+1);
            }
        }
    }
    public static void inputGo() {
        String move;
        if (Main.WhiteToMove) {
            move=Moves.allWhiteMoves(Main.WP,Main.WN,Main.WB,Main.WR,Main.WQ,Main.WK,Main.BP,Main.BN,Main.BB,Main.BR,Main.BQ,Main.BK,Main.EP,Main.CWK,Main.CWQ,Main.CBK,Main.CBQ);
            
        } else {
            move=Moves.allBlackMoves(Main.WP,Main.WN,Main.WB,Main.WR,Main.WQ,Main.WK,Main.BP,Main.BN,Main.BB,Main.BR,Main.BQ,Main.BK,Main.EP,Main.CWK,Main.CWQ,Main.CBK,Main.CBQ);
            
        }
        int index=Search.getFirstLegalMove(move,Main.WP,Main.WN,Main.WB,Main.WR,Main.WQ,Main.WK,Main.BP,Main.BN,Main.BB,Main.BR,Main.BQ,Main.BK,Main.EP,Main.CWK,Main.CWQ,Main.CBK,Main.CBQ,Main.WhiteToMove);;
        
        System.out.println("bestmove "+moveToAlgebra(move.substring(index,index+4)));
    }
    
    public static String moveToAlgebra(String move) {
        String append="";
        int start=0,end=0;
        if (Character.isDigit(move.charAt(3))) {//'regular' move
            start=(Character.getNumericValue(move.charAt(1))*8)+(Character.getNumericValue(move.charAt(0)));
            end=(Character.getNumericValue(move.charAt(3))*8)+(Character.getNumericValue(move.charAt(2)));
        } else if (move.charAt(3)=='P') {//pawn promotion
            if (Character.isUpperCase(move.charAt(2))) {
                start=Long.numberOfTrailingZeros(Moves.FileMasks[move.charAt(0)-'0']&Moves.RankMasks[1]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks[move.charAt(1)-'0']&Moves.RankMasks[0]);
            } else {
                start=Long.numberOfTrailingZeros(Moves.FileMasks[move.charAt(0)-'0']&Moves.RankMasks[6]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks[move.charAt(1)-'0']&Moves.RankMasks[7]);
            }
            append=""+Character.toLowerCase(move.charAt(2));
        } else if (move.charAt(3)=='E') {//en passant
            if (move.charAt(2)=='W') {
                start=Long.numberOfTrailingZeros(Moves.FileMasks[move.charAt(0)-'0']&Moves.RankMasks[3]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks[move.charAt(1)-'0']&Moves.RankMasks[2]);
            } else {
                start=Long.numberOfTrailingZeros(Moves.FileMasks[move.charAt(0)-'0']&Moves.RankMasks[4]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks[move.charAt(1)-'0']&Moves.RankMasks[5]);
            }
        }
        String returnMove="";
        returnMove+=(char)('a'+(start%8));
        returnMove+=(char)('8'-(start/8));
        returnMove+=(char)('a'+(end%8));
        returnMove+=(char)('8'-(end/8));
        returnMove+=append;
        return returnMove;
    }
    public static void algebraToMove(String input,String moves) {
        int start=0,end=0;
        int from=(input.charAt(0)-'a')+(8*('8'-input.charAt(1)));
        int to=(input.charAt(2)-'a')+(8*('8'-input.charAt(3)));
        for (int i=0;i<moves.length();i+=4) {
            if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
            	start=(Character.getNumericValue(moves.charAt(i+1))*8)+(Character.getNumericValue(moves.charAt(i+0)));
                end=(Character.getNumericValue(moves.charAt(i+3))*8)+(Character.getNumericValue(moves.charAt(i+2)));
            } 
            else if (moves.charAt(i+3)=='P') {//pawn promotion
                if (Character.isUpperCase(moves.charAt(i+2))) {
                    start=Long.numberOfTrailingZeros(Moves.FileMasks[moves.charAt(i+0)-'0']&Moves.RankMasks[1]);
                    end=Long.numberOfTrailingZeros(Moves.FileMasks[moves.charAt(i+1)-'0']&Moves.RankMasks[0]);
                } else {
                    start=Long.numberOfTrailingZeros(Moves.FileMasks[moves.charAt(i+0)-'0']&Moves.RankMasks[6]);
                    end=Long.numberOfTrailingZeros(Moves.FileMasks[moves.charAt(i+1)-'0']&Moves.RankMasks[7]);
                }
            } 
            if ((start==from) && (end==to)) {
                if ((input.charAt(4)==' ') || (Character.toUpperCase(input.charAt(4))==Character.toUpperCase(moves.charAt(i+2)))) {
                    if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
                        start=(Character.getNumericValue(moves.charAt(i))*8)+(Character.getNumericValue(moves.charAt(i+1)));
                        if (((1L<<start)&Main.WK)!=0) {Main.CWK=false; Main.CWQ=false;}
                        else if (((1L<<start)&Main.BK)!=0) {Main.CBK=false; Main.CBQ=false;}
                        else if (((1L<<start)&Main.WR&(1L<<63))!=0) {Main.CWK=false;}
                        else if (((1L<<start)&Main.WR&(1L<<56))!=0) {Main.CWQ=false;}
                        else if (((1L<<start)&Main.BR&(1L<<7))!=0) {Main.CBK=false;}
                        else if (((1L<<start)&Main.BR&1L)!=0) {Main.CBQ=false;}
                    }
                    
                    Main.EP=Moves.makeMoveEP(Main.WP|Main.BP,moves.substring(i,i+4));
                    Main.WR=Moves.makeMoveCastle(Main.WR, Main.WK|Main.BK, moves.substring(i,i+4), 'R');
                    Main.BR=Moves.makeMoveCastle(Main.BR, Main.WK|Main.BK, moves.substring(i,i+4), 'r');
                    Main.WP=Moves.makeMove(Main.WP, moves.substring(i,i+4), 'P');
                    Main.WN=Moves.makeMove(Main.WN, moves.substring(i,i+4), 'N');
                    Main.WB=Moves.makeMove(Main.WB, moves.substring(i,i+4), 'B');
                    Main.WR=Moves.makeMove(Main.WR, moves.substring(i,i+4), 'R');
                    Main.WQ=Moves.makeMove(Main.WQ, moves.substring(i,i+4), 'Q');
                    Main.WK=Moves.makeMove(Main.WK, moves.substring(i,i+4), 'K');
                    Main.BP=Moves.makeMove(Main.BP, moves.substring(i,i+4), 'p');
                    Main.BN=Moves.makeMove(Main.BN, moves.substring(i,i+4), 'n');
                    Main.BB=Moves.makeMove(Main.BB, moves.substring(i,i+4), 'b');
                    Main.BR=Moves.makeMove(Main.BR, moves.substring(i,i+4), 'r');
                    Main.BQ=Moves.makeMove(Main.BQ, moves.substring(i,i+4), 'q');
                    Main.BK=Moves.makeMove(Main.BK, moves.substring(i,i+4), 'k');
                    Main.WhiteToMove=!Main.WhiteToMove;
                    break;
                }
            }
        }
    }
    
    public static void inputQuit() {
        System.exit(0);
    }
    
    public static void inputPrint() {
        BitBoards.drawArray(Main.WP,Main.WN,Main.WB,Main.WR,Main.WQ,Main.WK,Main.BP,Main.BN,Main.BB,Main.BR,Main.BQ,Main.BK);
    }
}
