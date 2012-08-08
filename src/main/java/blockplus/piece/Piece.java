
package blockplus.piece;

import java.util.Iterator;
import java.util.Set;

import blockplus.direction.Direction;
import blockplus.direction.DirectionInterface;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;

public class Piece implements PieceInterface {

    private final PieceData pieceData;
    private final int rotationOrdinal;

    private final Set<PieceInterface> components;

    public Piece(final Piece piece) {
        this.pieceData = piece.getPieceData();
        final Set<PieceInterface> rotations = piece.get();
        this.rotationOrdinal = (piece.getRotationOrdinal() + 1) % rotations.size();
        this.components = ImmutableSet.copyOf(Iterables.limit(Iterables.skip(Iterables.cycle(rotations), 1), rotations.size()));
    }

    public Piece(final PieceData pieceData) {
        this.pieceData = pieceData;
        this.rotationOrdinal = 0;
        final PieceInterface rotation0 = PieceComposite.from(pieceData.id(), pieceData.referential(), pieceData.positions());
        final PieceInterface rotation1 = rotation0.rotate();
        final PieceInterface rotation2 = rotation1.rotate();
        final PieceInterface rotation3 = rotation2.rotate();
        final Set<PieceInterface> distinctRotations = Sets.newLinkedHashSet();
        distinctRotations.add(rotation0);
        distinctRotations.add(rotation1);
        distinctRotations.add(rotation2);
        distinctRotations.add(rotation3);
        this.components = ImmutableSet.copyOf(distinctRotations);
    }

    public Piece(final Piece piece, final DirectionInterface direction) {
        this.pieceData = piece.getPieceData();
        this.rotationOrdinal = piece.getRotationOrdinal();
        final Set<PieceInterface> rotations = Sets.newLinkedHashSet();
        for (final PieceInterface rotation : piece)
            rotations.add(rotation.translateBy(direction));
        this.components = ImmutableSet.copyOf(rotations);
    }

    public PieceData getPieceData() {
        return this.pieceData;
    }

    public int getRotationOrdinal() {
        return this.rotationOrdinal;
    }

    @Override
    public Set<PieceInterface> get() { // TODO Iterable<PI>
        return this.components;
    }

    @Override
    public Iterator<PieceInterface> iterator() {
        return this.get().iterator();
    }

    @Override
    public int getId() {
        return this.pieceData.id();
    }

    @Override
    public PositionInterface getReferential() {
        return this.iterator().next().getReferential();
    }

    @Override
    public Set<PositionInterface> getPositions() {
        return this.iterator().next().getPositions();
    }

    @Override
    public Set<PositionInterface> getCorners() {
        return this.iterator().next().getCorners();
    }

    @Override
    public Set<PositionInterface> getSides() {
        return this.iterator().next().getSides();
    }

    @Override
    public Set<PositionInterface> getPotentialPositions() {
        return this.iterator().next().getPotentialPositions();
    }

    @Override
    public PieceInterface translateTo(final PositionInterface position) {
        final PositionInterface referential = this.iterator().next().getReferential();
        final DirectionInterface direction = Direction.from(referential, position);
        return new Piece(this, direction);
    }

    @Override
    public PieceInterface translateBy(final DirectionInterface direction) {
        return new Piece(this, direction);
    }

    @Override
    public PieceInterface rotate() {
        return new Piece(this);
    }

    @Override
    public PieceInterface rotateAround(final PositionInterface referential) {
        return null; // TODO
    }

    @Override
    public String toString() {
        return "" + this.rotationOrdinal;
    }

    public static void main(final String[] args) {

        final Piece rotated0 = new Piece(PieceData.get(2));

        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println(rotated0);
        for (final PieceInterface pieceInterface : rotated0)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();
        final PieceInterface rotated1 = rotated0.rotate();
        System.out.println(rotated1);
        for (final PieceInterface pieceInterface : rotated1)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();
        final PieceInterface rotated2 = rotated1.rotate();
        System.out.println(rotated2);
        for (final PieceInterface pieceInterface : rotated2)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();
        final PieceInterface rotated3 = rotated2.rotate();
        System.out.println(rotated3);
        for (final PieceInterface pieceInterface : rotated3)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println();
        final PieceInterface rotated4 = rotated3.rotate();
        System.out.println(rotated4);
        for (final PieceInterface pieceInterface : rotated4)
            System.out.println(pieceInterface);
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println();

        final PieceInterface translateTo = rotated4.translateTo(Position.from(5, 5));
        System.out.println(translateTo.get());

    }
}