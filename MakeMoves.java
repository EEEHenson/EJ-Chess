public class MakeMoves 
{
    public static void makeMoveWrong(String move,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ) {
        //can not opperate on a single board since moves are not backwards compatable
        EP=0;
        if (Character.isDigit(move.charAt(3))) {
            int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
            int end=(Character.getNumericValue(move.charAt(2))*8)+(Character.getNumericValue(move.charAt(3)));
            if ((WK&(1L<<start))!=0) {//white castle move
                WK^=(1L<<start);
                WK^=(1L<<end);
                if (end>start) {//kingside
                    WR^=(1L<<63);
                    WR^=(1L<<61);
                    CWK=false;
                } else {//queenside
                    WR^=(1L<<56);
                    WR^=(1L<<59);
                    CWQ=false;
                }
            } else if ((BK&(1L<<start))!=0) {//black castle move
                WK^=(1L<<start);
                WK^=(1L<<end);
                if (end>start) {//kingside
                    WR^=(1L<<7);
                    WR^=(1L<<5);
                    CBK=false;
                } else {//queenside
                    WR^=1L;
                    WR^=(1L<<3);
                    CBQ=false;
                }
            } else {//'regular' move
                //clear destination:
                WP&=~(1L<<end);
                WN&=~(1L<<end);
                WB&=~(1L<<end);
                WR&=~(1L<<end);
                WQ&=~(1L<<end);
                WK&=~(1L<<end);
                //move piece:
                if ((WP&(1L<<start))!=0)
                {
                    WP^=(1L<<start);
                    WP^=(1L<<end);
                    if ((end-start)==16) {//pawn double push
                        EP=FileMasks8['0'-move.charAt(1)];
                    }
                }
                else if ((BP&(1L<<start))!=0)
                {
                    BP^=(1L<<start);
                    BP^=(1L<<end);
                    if ((start-end)==16) {//pawn double push
                        EP=FileMasks8['0'-move.charAt(1)];
                    }
                }
                else if ((WN&(1L<<start))!=0)
                {
                    WN^=(1L<<start);
                    WN^=(1L<<end);
                }
                else if ((BN&(1L<<start))!=0)
                {
                    BN^=(1L<<start);
                    BN^=(1L<<end);
                }
                else if ((WB&(1L<<start))!=0)
                {
                    WB^=(1L<<start);
                    WB^=(1L<<end);
                }
                else if ((BB&(1L<<start))!=0)
                {
                    BB^=(1L<<start);
                    BB^=(1L<<end);
                }
                else if ((WR&(1L<<start))!=0)
                {
                    WR^=(1L<<start);
                    WR^=(1L<<end);
                }
                else if ((BR&(1L<<start))!=0)
                {
                    BR^=(1L<<start);
                    BR^=(1L<<end);
                }
                else if ((WQ&(1L<<start))!=0)
                {
                    WQ^=(1L<<start);
                    WQ^=(1L<<end);
                }
                else if ((BQ&(1L<<start))!=0)
                {
                    BQ^=(1L<<start);
                    BQ^=(1L<<end);
                }
                else if ((WK&(1L<<start))!=0)
                {
                    WK^=(1L<<start);
                    WK^=(1L<<end);
                    CWK=false;
                    CWQ=false;
                }
                else if ((BK&(1L<<start))!=0)
                {
                    BK^=(1L<<start);
                    BK^=(1L<<end);
                    CBK=false;
                    CBQ=false;
                }
                else
                {
                    System.out.print("error: invalid move");
                }
            }
        } else if (move.charAt(3)=='P') {//pawn promotion
            //y1,y2,Promotion Type,"P"
            if (Character.isUpperCase(move.charAt(2)))//white piece promotion
            {
                WP^=(RankMasks8[6]&FileMasks8[move.charAt(0)-'0']);
                switch (move.charAt(2)) {
                case 'N': WN^=(RankMasks8[7]&FileMasks8[move.charAt(1)-'0']);
                    break;
                case 'B': WB^=(RankMasks8[7]&FileMasks8[move.charAt(1)-'0']);
                    break;
                case 'R': WR^=(RankMasks8[7]&FileMasks8[move.charAt(1)-'0']);
                    break;
                case 'Q': WQ^=(RankMasks8[7]&FileMasks8[move.charAt(1)-'0']);
                    break;
                }
            } else {//black piece promotion
                BP^=(RankMasks8[1]&FileMasks8[move.charAt(0)-'0']);
                switch (move.charAt(2)) {
                case 'n': BN^=(RankMasks8[0]&FileMasks8[move.charAt(1)-'0']);
                    break;
                case 'b': BB^=(RankMasks8[0]&FileMasks8[move.charAt(1)-'0']);
                    break;
                case 'r': BR^=(RankMasks8[0]&FileMasks8[move.charAt(1)-'0']);
                    break;
                case 'q': BQ^=(RankMasks8[0]&FileMasks8[move.charAt(1)-'0']);
                    break;
                }
            }
        } else if (move.charAt(3)=='E') {//en passant move
            if (move.charAt(2)=='w') {//white move
                //y1,y2,"BE"
                WP^=(RankMasks8[4]&FileMasks8['0'-move.charAt(0)]);//remove white pawn
                WP^=(RankMasks8[5]&FileMasks8['0'-move.charAt(1)]);//add white pawn
                BP^=(RankMasks8[4]&FileMasks8['0'-move.charAt(1)]);//remove black pawn)
            } else {//black move
                BP^=(RankMasks8[3]&FileMasks8['0'-move.charAt(0)]);//remove black pawn
                BP^=(RankMasks8[2]&FileMasks8['0'-move.charAt(1)]);//add black pawn
                WP^=(RankMasks8[3]&FileMasks8['0'-move.charAt(1)]);//remove white pawn)
            }
        } else {
            System.out.print("error:");
        }
    }
	public static long makeMove(long board, String move, char type) {
        if (Character.isDigit(move.charAt(3))) {//'regular' move
            int start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
            int end=(Character.getNumericValue(move.charAt(2))*8)+(Character.getNumericValue(move.charAt(3)));
            if (((board>>>start)&1)==1) {board&=~(1L<<start); board|=(1L<<end);} else {board&=~(1L<<end);}
        } else if (move.charAt(3)=='P') {//pawn promotion
            int start, end;
            if (Character.isUpperCase(move.charAt(2))) {
                start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[6]);
                end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[7]);
            } else {
                start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[1]);
                end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[0]);
            }
            if (type==move.charAt(2)) {board&=~(1L<<start); board|=(1L<<end);} else {board&=~(1L<<end);}
        } else if (move.charAt(3)=='E') {//en passant
            int start, end;
            if (Character.isUpperCase(move.charAt(2))) {
                start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[4]);
                end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[5]);
                board&=~(1L<<(FileMasks8[move.charAt(1)-'0']&RankMasks8[4]));
            } else {
                start=Long.numberOfTrailingZeros(FileMasks8[move.charAt(0)-'0']&RankMasks8[3]);
                end=Long.numberOfTrailingZeros(FileMasks8[move.charAt(1)-'0']&RankMasks8[2]);
                board&=~(1L<<(FileMasks8[move.charAt(1)-'0']&RankMasks8[3]));
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
                return FileMasks8[move.charAt(1)-'0'];
            }
        }
        return 0;
    }
    
}
