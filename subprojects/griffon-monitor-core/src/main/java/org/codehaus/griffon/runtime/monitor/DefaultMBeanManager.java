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

import griffon.annotations.core.Nonnull;
import griffon.core.GriffonApplication;
import griffon.plugins.monitor.MBeanManager;

import javax.management.MBeanRegistration;
import javax.management.ObjectName;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static griffon.util.GriffonNameUtils.requireNonBlank;
import static griffon.util.monitor.MXBeanUtils.registerIntoPlatformMBeanServer;
import static griffon.util.monitor.MXBeanUtils.unregisterFromPlatformMBeanServer;
import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class DefaultMBeanManager implements MBeanManager {
    private static final String ERROR_BEAN_NULL = "Argument 'bean' must not be null";
    private static final String ERROR_OBJECT_NAME_NULL = "Argument 'objectName' must not be null";
    private static final String ERROR_OBJECT_NAME_BLANK = "Argument 'objectName' must not be blank";

    private final Object lock = new Object[0];
    private final Map<String, ObjectName> objectNames = new ConcurrentHashMap<>();
    // @GuardedBy("lock")
    private boolean enabled;

    @Override
    public boolean isEnabled() {
        synchronized (lock) {
            return enabled;
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        synchronized (lock) {
            this.enabled = enabled;
        }
    }

    @Nonnull
    @Override
    public ObjectName registerMBean(@Nonnull MBeanRegistration bean) {
        return registerMBean(bean, true);
    }

    @Nonnull
    @Override
    public ObjectName registerMBean(@Nonnull MBeanRegistration bean, boolean unregisterOnShutdown) {
        requireNonNull(bean, ERROR_BEAN_NULL);
        ObjectName objectName = registerIntoPlatformMBeanServer(bean);
        if (unregisterOnShutdown) {
            objectNames.put(objectName.getCanonicalName(), objectName);
        }
        return objectName;
    }

    @Nonnull
    @Override
    public ObjectName registerMBean(@Nonnull Object bean, @Nonnull String objectName) {
        return registerMBean(bean, objectName, true);
    }

    @Nonnull
    @Override
    public ObjectName registerMBean(@Nonnull Object bean, @Nonnull String objectName, boolean unregisterOnShutdown) {
        requireNonNull(bean, ERROR_BEAN_NULL);
        requireNonBlank(objectName, ERROR_OBJECT_NAME_BLANK);
        ObjectName name = registerIntoPlatformMBeanServer(bean, objectName);
        if (unregisterOnShutdown) {
            objectNames.put(name.getCanonicalName(), name);
        }
        return name;
    }

    @Nonnull
    @Override
    public ObjectName registerMBean(@Nonnull Object bean, @Nonnull ObjectName objectName) {
        return registerMBean(bean, objectName, true);
    }

    @Nonnull
    @Override
    public ObjectName registerMBean(@Nonnull Object bean, @Nonnull ObjectName objectName, boolean unregisterOnShutdown) {
        requireNonNull(bean, ERROR_BEAN_NULL);
        requireNonNull(objectName, ERROR_OBJECT_NAME_NULL);
        registerIntoPlatformMBeanServer(bean, objectName);
        if (unregisterOnShutdown) {
            objectNames.put(objectName.getCanonicalName(), objectName);
        }
        return objectName;
    }

    @Override
    public void unregisterMBean(@Nonnull String objectName) {
        requireNonBlank(objectName, ERROR_OBJECT_NAME_BLANK);
        unregisterFromPlatformMBeanServer(objectName);
        objectNames.remove(objectName);
    }

    @Override
    public void unregisterMBean(@Nonnull ObjectName objectName) {
        requireNonNull(objectName, ERROR_OBJECT_NAME_NULL);
        unregisterFromPlatformMBeanServer(objectName);
        objectNames.remove(objectName.getCanonicalName());
    }

    @Override
    public boolean canShutdown(@Nonnull GriffonApplication application) {
        return true;
    }

    @Override
    public void onShutdown(@Nonnull GriffonApplication application) {
        synchronized (objectNames) {
            for (String objectName : objectNames.keySet()) {
                unregisterFromPlatformMBeanServer(objectName);
            }
        }
    }
}
