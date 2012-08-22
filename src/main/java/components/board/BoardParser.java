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

package components.board;

import static blockplus.model.board.State.*;
import static components.position.Position.*;

import java.util.Map;

import blockplus.model.board.State;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import components.board.rendering.StringRendering;
import components.position.PositionInterface;

public final class BoardParser<T extends Symbol> {

    private final Map<Character, T> symbolByCharacter;

    private final T initial;

    public T getInitial() {
        return this.initial;
    }

    private final T undefined;

    public T getUndefined() {
        return this.undefined;
    }

    public static <T extends Symbol> BoardParser<T> from(final Map<Character, T> symbolByCharacter, final T initial, final T undefined) {
        return new BoardParser<T>(symbolByCharacter, initial, undefined);
    }

    public BoardParser(final Map<Character, T> symbolByCharacter, final T initial, final T undefined) {
        this.symbolByCharacter = symbolByCharacter;
        this.initial = initial;
        this.undefined = undefined;
    }

    public T getSymbol(final Character character) {
        final T symbol = this.symbolByCharacter.get(character);
        if (symbol != null) return symbol;
        return this.getUndefined();
    }

    public BoardInterface<T> parse(final String[][] data) {
        final int rows = data.length;
        final int columns = data[0][0].length();
        final Map<PositionInterface, T> mutations = Maps.newHashMap();
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < columns; ++j) {
                final char character = data[i][0].charAt(j);
                final T symbol = this.getSymbol(character);
                if (!symbol.equals(this.getInitial())) mutations.put(Position(i, j), symbol);
            }
        return Board.from(rows, columns, this.getInitial(), this.getUndefined(), mutations);
    }

    public BoardInterface<T> parse(final int[][] data) {
        final int rows = data.length;
        final int columns = data[0].length;
        final Map<PositionInterface, T> mutations = Maps.newHashMap();
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < columns; ++j) {
                final int digit = data[i][j];
                final char character = Character.forDigit(digit, 10);
                final T symbol = this.getSymbol(character);
                if (!symbol.equals(this.getInitial())) mutations.put(Position(i, j), symbol);
            }
        return Board.from(rows, columns, this.getInitial(), this.getUndefined(), mutations);
    }

    // TODO ! unit tests
    public static void main(final String[] args) {
        final int[][] data = {
                //0  1  2  3  4  5  6 
                { 3, 3, 3, 3, 3, 3, 3 },// 0
                { 3, 2, 2, 2, 2, 2, 3 },// 1
                { 3, 2, 1, 1, 1, 2, 3 },// 2
                { 3, 2, 1, 0, 1, 2, 3 },// 3
                { 3, 2, 1, 1, 1, 2, 3 },// 4
                { 3, 2, 2, 2, 2, 2, 3 },// 5
                { 3, 3, 3, 3, 3, 3, 3 } // 6
        };
        final Map<Character, State> symbolByCharacter = ImmutableMap.of('0', None, '1', Self, '2', Light, '3', Shadow, '?', Other);
        final BoardParser<State> boardParser = BoardParser.from(symbolByCharacter, State.None, State.Other);
        final BoardInterface<State> board = boardParser.parse(data);
        final Map<State, Character> characterBySymbol = ImmutableMap.of(None, '0', Self, '1', Light, '2', Shadow, '3', Other, '?');
        final StringRendering boardConsoleView = new StringRendering(characterBySymbol);
        System.out.println(boardConsoleView.apply(board));
    }

}