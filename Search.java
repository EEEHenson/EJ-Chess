package elor;

public class Search {
	String moves;
	
    public static int getFirstLegalMove(String moves,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ,boolean WhiteToMove) {
        for (int i=0;i<moves.length();i+=4) {
        	String move = moves.substring(i,i+4);
            long WPt=Moves.makeMove(WP, move, 'P'), WNt=Moves.makeMove(WN, move, 'N'),
                    WBt=Moves.makeMove(WB, move, 'B'), WRt=Moves.makeMove(WR, move, 'R'),
                    WQt=Moves.makeMove(WQ, move, 'Q'), WKt=Moves.makeMove(WK, move, 'K'),
                    BPt=Moves.makeMove(BP, move, 'p'), BNt=Moves.makeMove(BN, move, 'n'),
                    BBt=Moves.makeMove(BB, move, 'b'), BRt=Moves.makeMove(BR, move, 'r'),
                    BQt=Moves.makeMove(BQ, move, 'q'), BKt=Moves.makeMove(BK, move, 'k');
            WRt=Moves.makeMoveCastle(WRt, WK|BK, move, 'R');
            BRt=Moves.makeMoveCastle(BRt, WK|BK, move, 'r');
            if (((WKt&Moves.dangerWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) ||
                    ((BKt&Moves.dangerBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
            	
            	return i;
            }
        }
        return -1;
    }
}
