/*
 * Copyright 2014-2017 the original author or authors.
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

import griffon.core.env.Metadata;
import griffon.core.threading.UIThreadManager;

import javax.annotation.Nonnull;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class UIThreadManagerMonitor extends AbstractMBeanRegistration implements UIThreadManagerMonitorMXBean {
    private UIThreadManager delegate;

    public UIThreadManagerMonitor(@Nonnull Metadata metadata, @Nonnull UIThreadManager delegate) {
        super(metadata);
        this.delegate = requireNonNull(delegate, "Argument 'delegate' must not be null");
    }

    @Override
    public String getManagerImplementationClass() {
        return delegate.getClass().getName();
    }

    @Override
    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        return new ObjectName("griffon.core:type=Manager,application=" + metadata.getApplicationName() + ",name=uithread");
    }

    @Override
    public void postDeregister() {
        delegate = null;
        super.postDeregister();
    }
}
