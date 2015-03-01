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

import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import java.beans.ConstructorProperties;

/**
 * @author Andres Almiray
 */
public class GriffonAddonInfo extends AbstractCompositeData {
    private final String name;

    public static final CompositeType COMPOSITE_TYPE;
    private static final String[] ATTRIBUTE_NAMES = {"name"};

    static {
        try {
            COMPOSITE_TYPE = new CompositeType(
                GriffonAddonInfo.class.getName(),
                GriffonAddonInfo.class.getName(),
                ATTRIBUTE_NAMES,
                ATTRIBUTE_NAMES,
                new OpenType[]{SimpleType.STRING}
            );
        } catch (OpenDataException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @ConstructorProperties({"name"})
    public GriffonAddonInfo(String name) {
        this.name = name;
        try {
            cds = new CompositeDataSupport(
                COMPOSITE_TYPE,
                ATTRIBUTE_NAMES,
                new Object[]{name}
            );
        } catch (OpenDataException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public String getName() {
        return name;
    }
}
