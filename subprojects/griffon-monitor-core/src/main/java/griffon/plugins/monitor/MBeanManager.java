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
package griffon.plugins.monitor;

import griffon.core.ShutdownHandler;

import javax.annotation.Nonnull;
import javax.management.MBeanRegistration;
import javax.management.ObjectName;

/**
 * @author Andres Almiray
 * @since 1.1.0
 */
public interface MBeanManager extends ShutdownHandler {
    boolean isEnabled();

    void setEnabled(boolean enabled);

    @Nonnull
    ObjectName registerMBean(@Nonnull MBeanRegistration bean);

    @Nonnull
    ObjectName registerMBean(@Nonnull MBeanRegistration bean, boolean unregisterOnShutdown);

    @Nonnull
    ObjectName registerMBean(@Nonnull Object bean, @Nonnull String objectName);

    @Nonnull
    ObjectName registerMBean(@Nonnull Object bean, @Nonnull String objectName, boolean unregisterOnShutdown);

    @Nonnull
    ObjectName registerMBean(@Nonnull Object bean, @Nonnull ObjectName objectName);

    @Nonnull
    ObjectName registerMBean(@Nonnull Object bean, @Nonnull ObjectName objectName, boolean unregisterOnShutdown);

    void unregisterMBean(@Nonnull String objectName);

    void unregisterMBean(@Nonnull ObjectName objectName);
}
