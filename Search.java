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
    
    public static int zWSearch(int beta,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ,boolean WhiteToMove,int depth) {//fail-hard zero window search, returns either beta-1 or beta
        int score = Integer.MIN_VALUE;
        //alpha == beta - 1
        //this is either a cut- or all-node
        String moves;
        if (WhiteToMove) {
            moves=Moves.allWhiteMoves(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        } else {
            moves=Moves.allBlackMoves(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        }
        
        
        if (depth == Main.searchDepth)
        {
            score = Rating.evaluate(moves,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ,WhiteToMove);
            return score;
        }
        
        //sortMoves();
        for (int i=0;i<moves.length();i+=4) {
            String move = moves.substring(i, i+4);
        	long WPt=Moves.makeMove(WP, move, 'P'), WNt=Moves.makeMove(WN, move, 'N'),
                    WBt=Moves.makeMove(WB, move, 'B'), WRt=Moves.makeMove(WR, move, 'R'),
                    WQt=Moves.makeMove(WQ, move, 'Q'), WKt=Moves.makeMove(WK, move, 'K'),
                    BPt=Moves.makeMove(BP, move, 'p'), BNt=Moves.makeMove(BN, move, 'n'),
                    BBt=Moves.makeMove(BB, move, 'b'), BRt=Moves.makeMove(BR, move, 'r'),
                    BQt=Moves.makeMove(BQ, move, 'q'), BKt=Moves.makeMove(BK, move, 'k'),
                    EPt=Moves.makeMoveEP(WP|BP,move);
            WRt=Moves.makeMoveCastle(WRt, WK|BK, move, 'R');
            BRt=Moves.makeMoveCastle(BRt, WK|BK, move, 'r');
            boolean CWKt=CWK,CWQt=CWQ,CBKt=CBK,CBQt=CBQ;
            if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
                //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
            	int start=(Character.getNumericValue(moves.charAt(i))*8)+(Character.getNumericValue(moves.charAt(i+1)));                if (((1L<<start)&WK)!=0) {CWKt=false; CWQt=false;}
                else if (((1L<<start)&BK)!=0) {CBKt=false; CBQt=false;}
                else if (((1L<<start)&WR&(1L<<63))!=0) {CWKt=false;}
                else if (((1L<<start)&WR&(1L<<56))!=0) {CWQt=false;}
                else if (((1L<<start)&BR&(1L<<7))!=0) {CBKt=false;}
                else if (((1L<<start)&BR&1L)!=0) {CBQt=false;}
            }
            if (((WKt&Moves.dangerWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) ||
                    ((BKt&Moves.dangerBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
                return score = i;
            }
            if (score >= beta)
            {
                return score;//fail-hard beta-cutoff
            }
        }
        return beta - 1;//fail-hard, return alpha
    }
    
    public static int pvSearch(int alpha,int beta,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ,boolean WhiteToMove,int depth) {//using fail soft with negamax
        int bestScore;
        int bestMoveIndex = -1;
        
        String moves;
        if (WhiteToMove) {
            moves=Moves.allWhiteMoves(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        } else {
            moves=Moves.allBlackMoves(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        }
            //System.out.println(moves);
        if (depth == Main.searchDepth)
        {
            bestScore = Rating.evaluate(moves,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ,WhiteToMove);
            return bestScore;
        }
        
        //sortMoves();
        int firstLegalMove = getFirstLegalMove(moves,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ,WhiteToMove);
        if (firstLegalMove == -1)
        {
            return WhiteToMove ? Main.MATE_SCORE : -Main.MATE_SCORE;
        }
        String move = moves.substring(firstLegalMove,firstLegalMove+4);
        long WPt=Moves.makeMove(WP, move, 'P'), WNt=Moves.makeMove(WN, move, 'N'),
                WBt=Moves.makeMove(WB, move, 'B'), WRt=Moves.makeMove(WR, move, 'R'),
                WQt=Moves.makeMove(WQ, move, 'Q'), WKt=Moves.makeMove(WK, move, 'K'),
                BPt=Moves.makeMove(BP, move, 'p'), BNt=Moves.makeMove(BN, move, 'n'),
                BBt=Moves.makeMove(BB, move, 'b'), BRt=Moves.makeMove(BR, move, 'r'),
                BQt=Moves.makeMove(BQ, move, 'q'), BKt=Moves.makeMove(BK, move, 'k'),
                EPt=Moves.makeMoveEP(WP|BP,move);
        WRt=Moves.makeMoveCastle(WRt, WK|BK, move, 'R');
        BRt=Moves.makeMoveCastle(BRt, WK|BK, move, 'r');
        boolean CWKt=CWK,CWQt=CWQ,CBKt=CBK,CBQt=CBQ;
        if (Character.isDigit(moves.charAt(firstLegalMove+3))) {//'regular' move
            //AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
        	int start=(Character.getNumericValue(moves.charAt(firstLegalMove))*8)+(Character.getNumericValue(moves.charAt(firstLegalMove))+1);
        	if (((1L<<start)&WK)!=0) {CWKt=false; CWQt=false;}
            else if (((1L<<start)&BK)!=0) {CBKt=false; CBQt=false;}
            else if (((1L<<start)&WR&(1L<<63))!=0) {CWKt=false;}
            else if (((1L<<start)&WR&(1L<<56))!=0) {CWQt=false;}
            else if (((1L<<start)&BR&(1L<<7))!=0) {CBKt=false;}
            else if (((1L<<start)&BR&1L)!=0) {CBQt=false;}
        }
        bestScore = -pvSearch(-beta,-alpha,WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,CWKt,CWQt,CBKt,CBQt,!WhiteToMove,depth+1);
        Main.moveCounter++;
        if (Math.abs(bestScore) == Main.MATE_SCORE)
        {
            return bestScore;
        }
        if (bestScore > alpha)
        {
            if (bestScore >= beta)
            {
                //This is a refutation move
                //It is not a PV move
                //However, it will usually cause a cutoff so it can
                //be considered a best move if no other move is found
                return bestScore;
            }
            alpha = bestScore;
        }
        bestMoveIndex = firstLegalMove;
        for (int i=firstLegalMove+4;i<moves.length();i+=4) {
            int score;
            Main.moveCounter++;
            //legal, non-castle move
            move = moves.substring(i, i+4);
            WPt=Moves.makeMove(WP, move, 'P');
            WNt=Moves.makeMove(WN, move, 'N');
            WBt=Moves.makeMove(WB, move, 'B');
            WRt=Moves.makeMove(WR, move, 'R');
            WQt=Moves.makeMove(WQ, move, 'Q');
            WKt=Moves.makeMove(WK, move, 'K');
            BPt=Moves.makeMove(BP, move, 'p');
            BNt=Moves.makeMove(BN, move, 'n');
            BBt=Moves.makeMove(BB, move, 'b');
            BRt=Moves.makeMove(BR, move, 'r');
            BQt=Moves.makeMove(BQ, move, 'q');
            BKt=Moves.makeMove(BK, move, 'k');
            EPt=Moves.makeMoveEP(WP|BP,move);
            WRt=Moves.makeMoveCastle(WRt, WK|BK, move, 'R');
            BRt=Moves.makeMoveCastle(BRt, WK|BK, move, 'r');
            CWKt=CWK;
            CWQt=CWQ;
            CBKt=CBK;
            CBQt=CBQ;
            if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
 //AAAAAAAAAAAAAAAAAAAAAAAAAAA
            	int start=(Character.getNumericValue(moves.charAt(i))*8)+(Character.getNumericValue(moves.charAt(i+1)));
                if (((1L<<start)&WK)!=0) {CWKt=false; CWQt=false;}
                else if (((1L<<start)&BK)!=0) {CBKt=false; CBQt=false;}
                else if (((1L<<start)&WR&(1L<<63))!=0) {CWKt=false;}
                else if (((1L<<start)&WR&(1L<<56))!=0) {CWQt=false;}
                else if (((1L<<start)&BR&(1L<<7))!=0) {CBKt=false;}
                else if (((1L<<start)&BR&1L)!=0) {CBQt=false;}
            }
            score = -zWSearch(-alpha,WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,CWKt,CWQt,CBKt,CBQt,!WhiteToMove,depth+1);
            if ((score > alpha) && (score < beta))
            {
                //research with window [alpha;beta]
                bestScore = -pvSearch(-beta,-alpha,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ,!WhiteToMove,depth+1);
                if (score>alpha)
                {
                    bestMoveIndex = i;
                    alpha = score;
                }
            }
            if ((score != Main.NULL_INT) && (score > bestScore))
            {
                if (score >= beta)
                {
                    return score;
                }
                bestScore = score;
                if (Math.abs(bestScore) == Main.MATE_SCORE)
                {
                    return bestScore;
                }
            }
        }
        //System.out.println(moves);
        return bestScore;
    }
}
