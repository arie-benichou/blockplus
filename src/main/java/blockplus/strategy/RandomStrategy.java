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

package blockplus.strategy;

import java.util.List;
import java.util.Random;

import blockplus.game.BlockplusGameContext;
import blockplus.move.Move;

public final class RandomStrategy implements StrategyInterface {

    private final Random random = new Random();

    @Override
    public Move chooseMove(final BlockplusGameContext gameContext) {
        final List<Move> options = gameContext.options();
        return options.get(this.random.nextInt(options.size()));
    }

    @Override
    public List<Move> sort(final BlockplusGameContext context, final List<Move> options) {
        return options;
    }
}