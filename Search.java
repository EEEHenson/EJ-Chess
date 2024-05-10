package elor;

public class Search {
	String moves;
	
    public static int getFirstLegalMove(String moves,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ,boolean WhiteToMove) {
        for (int i=0;i<moves.length();i+=4) {
        	String move = moves.substring(i,i+4);
            long WPt=MakeMoves.makeMove(WP, move, 'P'), WNt=Moves.makeMove(WN, move, 'N'),
                    WBt=MakeMoves.makeMove(WB, move, 'B'), WRt=MakeMoves.makeMove(WR, move, 'R'),
                    WQt=MakeMoves.makeMove(WQ, move, 'Q'), WKt=MakeMoves.makeMove(WK, move, 'K'),
                    BPt=MakeMoves.makeMove(BP, move, 'p'), BNt=MakeMoves.makeMove(BN, move, 'n'),
                    BBt=MakeMoves.makeMove(BB, move, 'b'), BRt=MakeMoves.makeMove(BR, move, 'r'),
                    BQt=MakeMoves.makeMove(BQ, move, 'q'), BKt=MakeMoves.makeMove(BK, move, 'k');
            WRt=MakeMoves.makeMoveCastle(WRt, WK|BK, move, 'R');
            BRt=MakeMoves.makeMoveCastle(BRt, WK|BK, move, 'r');
            if (((WKt&MakeMoves.dangerWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) ||
                    ((BKt&MakeMoves.dangerBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
            	
            	return i;
            }
        }
        return -1;
    }
}
