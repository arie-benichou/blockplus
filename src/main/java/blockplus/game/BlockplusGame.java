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

package blockplus.game;

import blockplus.move.Move;

@Deprecated
// useless now, use context
public class BlockplusGame {

    private final BlockplusGameContext initialContext;

    public BlockplusGameContext getInitialContext() {
        return this.initialContext;
    }

    public BlockplusGame(final BlockplusGameContext initialContext) {
        this.initialContext = initialContext;
    }

    public BlockplusGame() {
        this(BlockplusGameContext.DEFAULT);
    }

    public BlockplusGameContext start(final int max) {
        int i = 0;
        BlockplusGameContext newContext = this.getInitialContext();
        while (newContext.hasNext() && i != max) {
            final Move move = newContext.getMove();
            newContext = newContext.apply(move);
            newContext = newContext.next();
            ++i;
        }
        return newContext;
    }

    public BlockplusGameContext start() {
        return this.start(-1);
    }

    public BlockplusGameContext reset() {
        return this.getInitialContext();
    }

}