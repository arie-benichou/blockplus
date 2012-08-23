/*
 * Copyright 2012 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package blockplus.model.piece;

import static blockplus.model.piece.PieceComponent.*;
import static blockplus.model.piece.PieceData.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.direction.Direction;
import components.direction.DirectionInterface;
import components.position.PositionInterface;

// TODO !! Piece doit rester un symbole
// TODO !! Introduire ConcretePiece: une pièce translated et/ou rotated et/ou
// reflected

public class Piece implements PieceInterface {

    private final static boolean IS_FACTORY_CACHING = true;

    private static String asString(final int id, final PositionInterface referential, final int rotationOrdinal) {
        return Objects.toStringHelper(Piece.class)
                .add("id", id)
                .add("referential", referential)
                .add("rotation", rotationOrdinal)
                .toString();
    }

    private static int hashCode(final int id, final PositionInterface referential, final int rotationOrdinal) {
        return asString(id, referential, rotationOrdinal).hashCode();
    }

    public final static class Factory {

        private final boolean isCaching;

        private long cacheHit = 0;

        private final Map<Integer, Piece> cache = Maps.newConcurrentMap();

        public Factory(final boolean isCaching) {
            this.isCaching = isCaching;
        }

        public Factory() {
            this(IS_FACTORY_CACHING);
        }

        public Piece get(final int id, final PositionInterface referential, final int rotationOrdinal) {
            final int hashCode = Piece.hashCode(id, referential, rotationOrdinal);
            Piece instance = this.cache.get(hashCode);
            if (instance == null) {

                if (rotationOrdinal != 0) {
                    instance = this.get(id, referential, 0);
                }

                final Piece piece = Pieces.get(id);

                if (instance == null) {
                    final Set<PieceInterface> rotations = piece.get();
                    final Set<PieceInterface> translatedComponents = Sets.newLinkedHashSet();
                    for (final PieceInterface pieceInterface : rotations) {
                        translatedComponents.add(pieceInterface.translateTo(referential));
                    }
                    final Iterable<PieceInterface> components = Iterables.limit(
                            Iterables.skip(Iterables.cycle(translatedComponents), rotationOrdinal), rotations.size());
                    instance = new Piece(piece.getPieceData(), rotationOrdinal, Sets.newLinkedHashSet(components));
                }
                else {
                    final Iterable<PieceInterface> components = Iterables.limit(
                            Iterables.skip(Iterables.cycle(instance.get()), rotationOrdinal), instance.get().size());
                    instance = new Piece(piece.getPieceData(), rotationOrdinal, Sets.newLinkedHashSet(components));
                }

                if (this.isCaching) {
                    this.cache.put(hashCode, instance); // TODO ...
                }

            }
            else ++this.cacheHit;
            return instance;
        }

        public long cacheHits() {
            return this.cacheHit;
        }

        public int size() {
            return this.cache.size();
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this.getClass().getCanonicalName())
                    .add("size", this.size())
                    .add("cacheHits", this.cacheHits())
                    .toString();
        }

    }

    /*----------------------------------------8<----------------------------------------*/

    public final static Factory FACTORY = new Factory();

    /*----------------------------------------8<----------------------------------------*/

    @SuppressWarnings("all")
    public static Piece Piece(final PieceData pieceData) {
        final int rotationOrdinal = 0;
        final PieceInterface rotation0 = PieceComposite.from(pieceData.id(), pieceData.referential(), pieceData.positions());
        final PieceInterface rotation1 = rotation0.rotate();
        final PieceInterface rotation2 = rotation1.rotate();
        final PieceInterface rotation3 = rotation2.rotate();
        final Set<PieceInterface> components = Sets.newLinkedHashSet();
        components.add(rotation0);
        components.add(rotation1);
        components.add(rotation2);
        components.add(rotation3);
        final Piece piece = new Piece(pieceData, rotationOrdinal, components);
        FACTORY.cache.put(hashCode(pieceData.id(), pieceData.referential(), rotationOrdinal), piece);
        return piece;
    }

    @SuppressWarnings("all")
    public static Piece Piece(final int id) {
        return Piece(PieceData(id));
    }

    @SuppressWarnings("all")
    public static Piece Piece(final Piece piece) {
        final Set<PieceInterface> rotations = piece.get();
        final int rotationOrdinal = (piece.getRotationOrdinal() + 1) % rotations.size();
        return FACTORY.get(piece.getId(), piece.getReferential(), rotationOrdinal);
    }

    @SuppressWarnings("all")
    public static Piece Piece(final Piece piece, final DirectionInterface direction) {
        return FACTORY.get(piece.getId(), piece.getReferential().apply(direction), piece.getRotationOrdinal());
    }

    private final PieceData pieceData;
    private final int rotationOrdinal;
    private final Set<PieceInterface> components;

    public Piece(final PieceData pieceData, final int rotationOrdinal, final Iterable<PieceInterface> components) {
        this.pieceData = pieceData;
        this.rotationOrdinal = rotationOrdinal;
        this.components = ImmutableSet.copyOf(components);
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
    public Set<PositionInterface> getSelfPositions() {
        return this.iterator().next().getSelfPositions();
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
    public Set<PositionInterface> getLightPositions() {
        return this.iterator().next().getLightPositions();
    }

    @Override
    public Set<PositionInterface> getShadowPositions() {
        return this.iterator().next().getShadowPositions();
    }

    @Override
    public PieceInterface translateTo(final PositionInterface position) {
        final PositionInterface referential = this.iterator().next().getReferential();
        final DirectionInterface direction = Direction.from(referential, position);
        return Piece(this, direction);
    }

    @Override
    public PieceInterface translateBy(final DirectionInterface direction) {
        return Piece(this, direction);
    }

    @Override
    public PieceInterface rotate() {
        return Piece(this);
    }

    @Override
    public PieceInterface rotateAround(final PositionInterface referential) { // TODO à revoir
        return this.rotate().translateTo(PieceComponent(this.getReferential()).rotateAround(referential).getReferential());
    }

    @Override
    public String toString() {
        //return asString(this.getId(), this.getReferential(), this.getRotationOrdinal());
        return Pieces.valueOf(Pieces.PIECE_NAME_PATTERN + this.getId()).name();
    }

    @Override
    public int hashCode() {
        return this.getId();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof PieceInterface)) return false;
        final PieceInterface that = (PieceInterface) object;
        return this.getId() == that.getId();
    }

    @Override
    public PieceInterface reflectAlongVerticalAxis(final PositionInterface referential) {
        // cf Class TODO
        return null;
    }

    @Override
    public PieceInterface reflectAlongVerticalAxis() {
        // cf Class TODO
        return null;
    }

    @Override
    public PieceInterface reflectAlongHorizontalAxis(final PositionInterface referential) {
        // cf Class TODO
        return null;
    }

    @Override
    public PieceInterface reflectAlongHorizontalAxis() {
        // cf Class TODO
        return null;
    }

}