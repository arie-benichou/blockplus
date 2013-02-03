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

package blockplus.color;

public final class Colors {

    public final static ColorInterface Black = NullColor.getInstance();

    public final static ColorInterface Blue = PrimeColors.Blue;
    public final static ColorInterface Yellow = PrimeColors.Yellow;
    public final static ColorInterface Red = PrimeColors.Red;
    public final static ColorInterface Green = PrimeColors.Green;

    public final static ColorInterface White =
                                               new Color.Builder()
                                                       .add(Blue)
                                                       .add(Yellow)
                                                       .add(Red)
                                                       .add(Green)
                                                       .build();

    private Colors() {}

}