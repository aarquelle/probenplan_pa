/*
 * Copyright (c) 2025, Aaron Prott
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.aarquelle.probenplan_pa.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SortedUniqueList<T extends Comparable<T>> implements Iterable<T> {
    private final List<T> list = new ArrayList<>();

    public void clear() {
        list.clear();
    }

    public T get(int index) {
        return list.get(index);
    }

    public int indexOf(T t) {
        return list.indexOf(t);
    }

    //TODO Effizienter gestalten, das ist nur (hoffentlich) korrekter Platzhalter
    public void add(T t) {
        if (!list.contains(t)) {
            list.add(t);
            Collections.sort(list);
        }
    }

    public void remove(T t) {
        list.remove(t);
    }

    public void sort() {
        Collections.sort(list);
    }

    public Stream<T> stream() {
        return list.stream();
    }

    public int size() {
        return list.size();
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return list.iterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        list.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return list.spliterator();
    }

    public Set<T> toSet() {
        return new HashSet<>(list);
    }

    public List<T> toList() {
        return List.copyOf(list);
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public T getFirst() {
        return list.getFirst();
    }

    public T getLast() {
        return list.getLast();
    }
}
