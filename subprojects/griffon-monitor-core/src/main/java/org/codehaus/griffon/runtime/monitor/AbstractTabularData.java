/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.griffon.runtime.monitor;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;
import java.util.Collection;
import java.util.Set;

/**
 * @author Andres Almiray
 */
public class AbstractTabularData implements TabularData {
    protected TabularDataSupport tbs;

    @Override
    public TabularType getTabularType() {
        return tbs.getTabularType();
    }

    @Override
    public Object[] calculateIndex(CompositeData value) {
        return tbs.calculateIndex(value);
    }

    @Override
    public int size() {
        return tbs.size();
    }

    @Override
    public boolean isEmpty() {
        return tbs.isEmpty();
    }

    @Override
    public boolean containsKey(Object[] key) {
        return tbs.containsKey(key);
    }

    @Override
    public boolean containsValue(CompositeData value) {
        return tbs.containsValue(value);
    }

    @Override
    public CompositeData get(Object[] key) {
        return tbs.get(key);
    }

    @Override
    public void put(CompositeData value) {
        tbs.put(value);
    }

    @Override
    public CompositeData remove(Object[] key) {
        return tbs.remove(key);
    }

    @Override
    public void putAll(CompositeData[] values) {
        tbs.putAll(values);
    }

    @Override
    public void clear() {
        tbs.clear();
    }

    @Override
    public Set<?> keySet() {
        return tbs.keySet();
    }

    @Override
    public Collection<?> values() {
        return tbs.values();
    }
}
