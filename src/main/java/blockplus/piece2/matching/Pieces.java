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

package blockplus.piece2.matching;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableBiMap.Builder;
import com.google.common.collect.Multiset;

public class Pieces {

    public static void main(final String[] args) throws InterruptedException {

        final Builder<Integer, Integer> builder = new ImmutableBiMap.Builder<Integer, Integer>();

        int i = -1;
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    " ",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }

        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "O",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }

        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "O",
                    "O",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "O",
                    "O",
                    "O",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "OO",
                    " O",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }

        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "O",
                    "O",
                    "O",
                    "O",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "OO",
                    " O",
                    " O",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "OOO",
                    " O ",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "OO",
                    "OO",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "OO ",
                    " OO",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "OO",
                    " O",
                    " O",
                    " O",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "O",
                    "O",
                    "O",
                    "O",
                    "O",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "O ",
                    "OO",
                    " O",
                    " O",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "O ",
                    "OO",
                    "OO",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "OO",
                    " O",
                    "OO",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "O ",
                    "OO",
                    "O ",
                    "O ",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "OOO",
                    " O ",
                    " O ",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "OOO",
                    "  O",
                    "  O",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "OO ",
                    " OO",
                    "  O",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "O  ",
                    "OOO",
                    "  O",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    "O  ",
                    "OOO",
                    " O ",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }
        {
            System.out.print(++i + ")");
            final Piece p = Piece.from(new String[] {
                    " O ",
                    "OOO",
                    " O ",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(footPrint);
            builder.put(i, footPrint.hashCode());
            System.out.println(footPrint.hashCode());
            System.out.println();
        }

        final ImmutableBiMap<Integer, Integer> map = builder.build();
        Preconditions.checkState(map.size() == 22);

        {
            final Piece p = Piece.from(new String[] {
                    "O  ",
                    "OOO",
                    "O  ",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(map.inverse().get(footPrint.hashCode()));
        }
        {
            final Piece p = Piece.from(new String[] {
                    "  O",
                    "OOO",
                    "  O",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(map.inverse().get(footPrint.hashCode()));
        }
        {
            final Piece p = Piece.from(new String[] {
                    " O ",
                    " O ",
                    "OOO",
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(map.inverse().get(footPrint.hashCode()));
        }

        {
            final Piece p = Piece.from(new String[] {
                    "  O",
                    "OOO",
                    " O "
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(map.inverse().get(footPrint.hashCode()));
        }

        {
            final Piece p = Piece.from(new String[] {
                    " O ",
                    "OOO",
                    "  O"
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(map.inverse().get(footPrint.hashCode()));
        }

        {
            final Piece p = Piece.from(new String[] {
                    " O ",
                    "OOO",
                    "O  "
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(map.inverse().get(footPrint.hashCode()));
        }

        {
            final Piece p = Piece.from(new String[] {
                    " O ",
                    " OO",
                    "OO "
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(map.inverse().get(footPrint.hashCode()));
        }

        {
            final Piece p = Piece.from(new String[] {
                    " OO",
                    "OO ",
                    " O "
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(map.inverse().get(footPrint.hashCode()));
        }

        {
            final Piece p = Piece.from(new String[] {
                    "OO ",
                    " OO",
                    " O "
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(map.inverse().get(footPrint.hashCode()));
        }

        {
            final Piece p = Piece.from(new String[] {
                    " O ",
                    "OO ",
                    " OO"
            });
            final Multiset<String> footPrint = p.footPrint();
            System.out.println(map.inverse().get(footPrint.hashCode()));
        }

    }
}