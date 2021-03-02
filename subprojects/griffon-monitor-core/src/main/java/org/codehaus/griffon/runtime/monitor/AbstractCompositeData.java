/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2017-2021 The author and/or original authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.griffon.runtime.monitor;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import java.util.Collection;

/**
 * @author Andres Almiray
 */
public abstract class AbstractCompositeData implements CompositeData {
    protected CompositeDataSupport cds;

    @Override
    public CompositeType getCompositeType() {
        return cds.getCompositeType();
    }

    @Override
    public Object get(String key) {
        return cds.get(key);
    }

    @Override
    public Object[] getAll(String[] keys) {
        return cds.getAll(keys);
    }

    @Override
    public boolean containsKey(String key) {
        return cds.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return cds.containsValue(value);
    }

    @Override
    public Collection<?> values() {
        return cds.values();
    }
}
