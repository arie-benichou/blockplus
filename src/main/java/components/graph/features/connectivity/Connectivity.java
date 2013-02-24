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

final class Connectivity<T> implements ConnectivityInterface<T> {

    private final Graph<T> graph;

    private volatile boolean[][] data = null;

    private boolean isConnected;

    public static <T> Connectivity<T> from(final Graph<T> graph) {
        return new Connectivity<T>(graph);
    }

    private Connectivity(final Graph<T> graph) {
        this.graph = graph;
    }

    public Graph<T> getGraph() {
        return this.graph;
    }

    private int initialize(final boolean[][] data) {
        int potentiallyNotConnected = 0;
        final int n = this.graph.getOrder();
        for (int i = 0; i < n; ++i) {
            final T endPoint1 = this.getGraph().get(i);
            for (int j = 0; j < n; ++j) {
                final T endPoint2 = this.getGraph().get(j);
                if (i == j || this.getGraph().hasArc(endPoint1, endPoint2)) data[i][j] = true;
                else {
                    ++potentiallyNotConnected;
                    data[i][j] = false;
                }
            }
        }
        //this.debug(data);
        return potentiallyNotConnected;
    }

    private int computeData(final boolean[][] data, final int potentiallyNotConnected) {
        int notConnected = potentiallyNotConnected;
        final int n = this.graph.getOrder();
        for (int k = 0; k < n; ++k) {
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (data[i][j]) continue;
                    if (!data[i][k] || !data[k][j]) continue;
                    data[i][j] = true;
                    --notConnected;
                }
            }
        }
        //this.debug(data);
        return notConnected;
    }

    private boolean[][] getData() {
        boolean[][] value = this.data;
        if (value == null) {
            synchronized (this) {
                if ((value = this.data) == null) {
                    final int n = this.getGraph().getOrder();
                    final boolean[][] data = new boolean[n][n];
                    final int potentiallyNotConnected = this.initialize(data);
                    this.isConnected = potentiallyNotConnected == 0 ? true : this.computeData(data, potentiallyNotConnected) == 0;
                    this.data = value = data;
                }
            }
        }
        return value;
    }

    @Override
    public boolean isConnected() {
        this.getData();
        return this.isConnected;
    }

    @Override
    public boolean isConnected(final T endPoint1, final T endPoint2) {
        return this.getData()[this.getGraph().getOrdinal(endPoint1)][this.getGraph().getOrdinal(endPoint2)];
    }

    public void debug(final boolean[][] array) {
        final int n = this.getGraph().getOrder();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j)
                System.out.print((array[i][j] ? 1 : 0) + " ");
            System.out.println();
        }
        System.out.println();
    }

}