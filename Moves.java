public class Moves {

  public static long possibleMovesWP(long WP) {
    return WP >>> 8;
  }

  public static long possibleMovesBP(long BP) {
    reurn BP << 8;
  }
}
