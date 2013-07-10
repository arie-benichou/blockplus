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

package components.graph.features.degree;

import com.google.common.base.Predicate;

public final class Integers {

    public static class Predicates {

        private final static Predicate<Integer> IS_EVEN = new IsEven();

        private final static Predicate<Integer> IS_ODD = com.google.common.base.Predicates.not(IS_EVEN);

        private final static class IsEven implements Predicate<Integer> {

            @Override
            public boolean apply(final Integer integer) {
                return integer % 2 == 0;
            }
        }

        private final static class Is implements Predicate<Integer> {

            private final Integer integer;

            public Is(final int integer) {
                this.integer = integer;
            }

            @Override
            public boolean apply(final Integer integer) {
                return this.integer.equals(integer);

            }
        }

        public static Predicate<Integer> isEven() {
            return IS_EVEN;
        }

        public static Predicate<Integer> isOdd() {
            return IS_ODD;
        }

        public static Predicate<Integer> is(final int integer) {
            return new Is(integer);
        }

        public static Predicate<Integer> isNot(final int integer) {
            return com.google.common.base.Predicates.not(new Is(integer));
        }

    }

}