public class Moves {
  static long FILE_A=72340172838076673L;
  static long FILE_H=-9187201950435737472L;

  public static long possibleMovesWP(long WP) {
    return WP >>> 8;
  }

  public static long possibleMovesBP(long BP) {
    return BP << 8;
  }
}
