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
import griffon.core.env.Metadata;
import griffon.core.storage.ObjectStorage;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public abstract class AbstractObjectStorageMonitor<T> extends AbstractMBeanRegistration implements ObjectStorageMonitorMXBean {
    protected ObjectStorage<T> delegate;

    public AbstractObjectStorageMonitor(@Nonnull Metadata metadata, @Nonnull ObjectStorage<T> delegate) {
        super(metadata);
        this.delegate = requireNonNull(delegate, "Argument 'delegate' must not be null");
    }

    @Override
    public String getStorageImplementationClass() {
        return delegate.getClass().getName();
    }

    @Override
    public int getObjectCount() {
        return delegate.getKeys().length;
    }

    @Override
    public String[] getKeys() {
        return delegate.getKeys();
    }

    @Override
    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        return new ObjectName("griffon.core:type=Storage,application=" + metadata.getApplicationName() + ",name=" + getStorageName());
    }

    protected abstract String getStorageName();

    @Override
    public void postDeregister() {
        delegate = null;
        super.postDeregister();
    }
}
