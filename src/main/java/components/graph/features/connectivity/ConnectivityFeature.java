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

package components.graph.features.connectivity;

import components.graph.Graph;
import components.graph.features.FeatureInterface;


public final class ConnectivityFeature implements FeatureInterface {

    private final Graph<?> graph;

    private ConnectivityInterface<?> instance = null;

    public ConnectivityFeature(final Graph<?> graph) {
        this.graph = graph;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ConnectivityInterface<T> up() {
        if (this.instance == null) this.instance = Connectivity.from(this.graph);
        return (Connectivity<T>) this.instance;
    }

    @Override
    public <T> ConnectivityInterface<T> getInterface() {
        return this.up();
    }

}