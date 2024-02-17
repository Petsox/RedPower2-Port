package com.eloraam.redpower.nei;

import java.util.*;

public class ComboGenerator
{
    public static List<LinkedList<Integer>> generate(final int sum) {
        if (sum < 2) {
            return null;
        }
        final List<LinkedList<Integer>> combos = new ArrayList<LinkedList<Integer>>();
        if (sum == 2) {
            combos.add(new LinkedList<Integer>(Arrays.asList(1, 1)));
            return combos;
        }
        for (int base = 1; base <= sum / 2; ++base) {
            final List<LinkedList<Integer>> subcombos = generate(sum - base);
        Label_0081:
            for (final LinkedList<Integer> combo : subcombos) {
                for (final Integer i : combo) {
                    if (i < base) {
                        continue Label_0081;
                    }
                }
                combo.addFirst(base);
                combos.add(combo);
            }
            combos.add(new LinkedList<Integer>(Arrays.asList(base, sum - base)));
        }
        return combos;
    }
    
    public static void print(final List<LinkedList<Integer>> combos) {
        System.out.println("Combinations summing to: " + sum(combos.get(0)));
        for (final LinkedList<Integer> combo : combos) {
            final StringBuilder line = new StringBuilder();
            boolean comma = false;
            for (final Integer i : combo) {
                if (!comma) {
                    comma = true;
                }
                else {
                    line.append(',');
                }
                line.append(i);
            }
            System.out.println(line);
        }
    }
    
    public static List<LinkedList<Integer>> removeNotContaining(final List<LinkedList<Integer>> combos, final int required) {
        final Iterator<LinkedList<Integer>> iterator = combos.iterator();
    Label_0007:
        while (iterator.hasNext()) {
            final LinkedList<Integer> combo = iterator.next();
            for (final Integer i : combo) {
                if (i == required) {
                    continue Label_0007;
                }
            }
            iterator.remove();
        }
        return combos;
    }
    
    private static int sum(final LinkedList<Integer> combo) {
        int sum = 0;
        for (final Integer i : combo) {
            sum += i;
        }
        return sum;
    }
}
