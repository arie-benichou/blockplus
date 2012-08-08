
package demo;

import blockplus.piece.PieceData;

public class PieceDataDemo {

    public static void main(final String[] args) {
        for (final PieceData data : PieceData.values()) {
            System.out.println("-----------------------------8<-----------------------------");
            System.out.println();
            System.out.println(data.name() + ": ");
            System.out.println();
            System.out.println(data);
            System.out.println();
        }
        System.out.println("-----------------------------8<-----------------------------");
    }

}