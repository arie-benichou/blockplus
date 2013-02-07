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

package blockplus.adversity;

import interfaces.adversity.AdversityInterface;
import interfaces.adversity.SideInterface;

import java.util.Map;

import blockplus.context.Color;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public final class Adversity implements AdversityInterface<Color> {

    public final static class Builder {

        private final Map<SideInterface, Color> colorBySide = Maps.newHashMap();
        private final Map<Color, SideInterface> sideByColor = Maps.newHashMap();

        public Builder add(final SideInterface side, final Color color) {
            this.colorBySide.put(side, color);
            this.sideByColor.put(color, side);
            return this;
        }

        public Builder remove(final Color color) {
            final SideInterface side = this.sideByColor.get(color);
            this.colorBySide.remove(side);
            this.sideByColor.remove(color);
            return this;
        }

        public Adversity build() {
            return new Adversity(this.colorBySide, this.sideByColor);
        }
    }

    private final Map<SideInterface, Color> colorBySide;
    private final Map<Color, SideInterface> sideByColor;

    private Adversity(final Map<SideInterface, Color> colorBySide, final Map<Color, SideInterface> sideByColor) {
        this.colorBySide = new ImmutableMap.Builder<SideInterface, Color>().putAll(colorBySide).build();
        this.sideByColor = new ImmutableMap.Builder<Color, SideInterface>().putAll(sideByColor).build();
    }

    @Override
    public Color get(final SideInterface side) {
        return this.colorBySide.get(side);
    }

    @Override
    public Color getNext(final SideInterface side) {
        return this.get(side.next());
    }

    @Override
    public SideInterface getSide(final Color color) {
        return this.sideByColor.get(color);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(this.sideByColor).toString();
    }

}
