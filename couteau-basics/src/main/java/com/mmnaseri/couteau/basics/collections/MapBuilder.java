/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 AgileApes, Ltd.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.mmnaseri.couteau.basics.collections;

import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (14/6/16 AD, 16:09)
 */
public final class MapBuilder {

    private MapBuilder() {}

    public static <K> MapKeyHolder<K> map(Class<K> keyType) {
        return new MapKeyHolder<K>();
    }

    public static class MapKeyHolder<K> {

        public <V> MapCreator<K, V> to(Class<V> valueType) {
            return new MapCreator<K, V>();
        }

    }

    public static class MapCreator<K, V> {

        private List<K> keys = new ArrayList<K>();

        public MapCreator() {
            this.keys = new ArrayList<K>();
        }

        public MapCreator(Collection<K> keys) {
            this.keys = new ArrayList<K>(keys);
        }
        
        public MapCreator<K, V> forKeys(K... keys) {
            return forKeys(Arrays.asList(keys));
        }

        public MapCreator<K, V> forKeys(Collection<K> keys) {
            return new MapCreator<K, V>(keys);
        }
        
        public Map<K, V> andValues(V... values) {
            return andValues(Arrays.asList(values));
        }

        public Map<K, V> andValues(Collection<V> values) {
            final HashMap<K, V> map = new HashMap<K, V>();
            final Iterator<V> iterator = values.iterator();
            for (int i = 0; i < Math.min(values.size(), keys.size()); i ++) {
                map.put(keys.get(i), iterator.next());
            }
            return map;
        }

    }
}
