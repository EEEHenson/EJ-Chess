package elor;


public class Main {
	static long WP=0L,WN=0L,WB=0L,WR=0L,WQ=0L,WK=0L,BP=0L,BN=0L,BB=0L,BR=0L,BQ=0L,BK=0L,EP=0L;
	static boolean CWK=true,CWQ=true,CBK=true,CBQ=true,WhiteToMove=true;//true=castle is possible
	
    static int searchDepth=5,moveCounter;
    static int MATE_SCORE=5000, NULL_INT=Integer.MIN_VALUE;;
    //public static String startpos = "2bqkbn1/Pppppppp/8/8/8/8/8/RNBQKBNR w KQkq - 0 1";
    //public static String startpos = "2bqkbn1/8/8/8/8/8/p6p/1NBQKBN1 w KQkq - 0 1";
    public static String startpos = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

	public static void main(String[] args) {
	    	BitBoards.importFEN(startpos);
	        //UCI.inputPrint();
	        long startTime=System.currentTimeMillis();
	        System.out.println(Search.pvSearch(-1000,1000,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ,WhiteToMove,0));
	        long endTime=System.currentTimeMillis();
	        System.out.println("That took "+(endTime-startTime)+" milliseconds");
	        UCI.uciCommunication();
	        
	}
}
