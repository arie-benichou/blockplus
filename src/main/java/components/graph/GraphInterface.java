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

package components.graph;

import java.util.List;
import java.util.Set;

import components.graph.arc.ArcInterface;

public interface GraphInterface<T> extends Iterable<T> {

    final static boolean USER_MODE = true;
    final static boolean SUPERVISER_MODE = false;

    //

    int getOrder();

    boolean hasEndPoint(final T endPoint);

    Integer getOrdinal(final T endPoint);

    T get(final int ordinal);

    List<T> getConnectedEndPoints(final T endPoint);

    //

    boolean hasArc(final T endPoint1, final T endPoint2);

    List<ArcInterface<T>> getArcsFrom(final T endPoint);

    Set<ArcInterface<T>> getSetOfArcs();

    ArcInterface<T> getArc(final T endPoint1, final T endPoint2);

}