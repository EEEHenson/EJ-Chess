public class MakeMoves 
{
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
