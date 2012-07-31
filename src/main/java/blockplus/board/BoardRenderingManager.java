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

package blockplus.board;

import com.google.common.base.Preconditions;

public class BoardRenderingManager {

    public String render(final char[] charArray) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Board.HEIGHT; ++i) {
            for (int j = 0; j < Board.WIDTH; ++j) {
                char c = charArray[Board.WIDTH * i + j];
                if (c == '1') c = '.';
                else if (c == '2') c = 'o'; // TODO ! à virer...
                else c = 'x'; // TODO ! à virer...
                sb.append(c);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public String render(final Board board) {
        Preconditions.checkArgument(board != null);
        return this.render(board.toCharArray());
    }

}