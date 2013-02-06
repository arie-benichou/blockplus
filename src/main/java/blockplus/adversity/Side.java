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

import com.google.common.base.Objects;

public class Side implements SideInterface {

    private final Integer id;

    public Side(final Integer id) {
        this.id = id;
    }

    @Override
    public Integer get() {
        return this.id;
    }

    @Override
    public int hashCode() {
        return this.get();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof SideInterface)) return false;
        final SideInterface that = (SideInterface) object;
        return this.get().equals(that.get());
    }

    @Override
    public Side next() {
        return new Side((this.get() + 1) % 4); // TODO caching factory
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).addValue(this.get()).toString();
    }

}
