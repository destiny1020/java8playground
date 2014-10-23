package perf;

import java.awt.Point;
import java.util.ArrayList;

public class Main {

	static ArrayList<Point> queenSpots = new ArrayList<Point>();

	public static void main(String[] args) {
		ArrayList<Point> chessboard = new ArrayList<Point>();
		for (int i = 1; i <= 8; i++) {
			for (int j = 1; j <= 8; j++) {
				chessboard.add(new Point(i, j));
			}
		}
		placeQueens(chessboard, 8);
	}

	public static boolean placeQueens(ArrayList<Point> chessboard, int queens) {
		if (queens == 0) {
			for (Point p : queenSpots) {
				System.out.print("(" + p.x + ", " + p.y + ");");
			}
			System.out.println();
			return true;
		}
		for (int i = 1; i <= 8; i++) {
			if (chessboard.contains(new Point(queens, i))) {
				ArrayList<Point> newBoard = addQueen(chessboard, new Point(
						queens, i));
				placeQueens(newBoard, queens - 1);
				queenSpots.remove(new Point(queens, i));
			}
		}
		return false;
	}

	public static ArrayList<Point> addQueen(ArrayList<Point> chessboard, Point p) {
		ArrayList<Point> newBoard = new ArrayList<Point>();
		queenSpots.add(p);
		for (Point q : chessboard) {
			if (q.x != p.x && q.y != p.y && (q.x - p.y) != (p.x - q.y)
					&& (q.x - q.y) != (p.x - p.y)) {
				newBoard.add(q);
			}
		}
		return newBoard;
	}
}
