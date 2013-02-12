/*
 * Copyright 2012-2013 Arie Benichou
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

package blockplus.piece;

import java.util.Set;

import com.google.common.base.Supplier;
import components.direction.DirectionInterface;
import components.position.PositionInterface;

// TODO extract TransformableInterface
// TODO make distinction beetween an instance of PieceType and an instance of
// Transformable
public interface PieceInterface extends Iterable<PieceInterface>, Supplier<Set<PieceInterface>> {

    int getId(); // cf TODO

    PositionInterface getReferential();

    Set<PositionInterface> getSelfPositions();

    Set<PositionInterface> getSides();

    Set<PositionInterface> getShadowPositions();

    Set<PositionInterface> getCorners();

    Set<PositionInterface> getLightPositions();

    PieceInterface translateTo(PositionInterface position);

    PieceInterface translateBy(DirectionInterface direction);

    PieceInterface rotate();

    PieceInterface rotateAround(PositionInterface referential);

    PieceInterface reflectAlongVerticalAxis(PositionInterface referential);

    PieceInterface reflectAlongVerticalAxis();

    PieceInterface reflectAlongHorizontalAxis(PositionInterface referential);

    PieceInterface reflectAlongHorizontalAxis();

}