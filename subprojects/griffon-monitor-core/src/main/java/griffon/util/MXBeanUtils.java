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
package griffon.util;

import griffon.exceptions.GriffonException;

import javax.annotation.Nonnull;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import static griffon.util.GriffonNameUtils.requireNonBlank;
import static java.lang.management.ManagementFactory.getPlatformMBeanServer;
import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public final class MXBeanUtils {
    private MXBeanUtils() {

    }

    public static void register(@Nonnull Object mbean, @Nonnull ObjectName objectName, @Nonnull MBeanServer server) {
        requireNonNull(mbean, "Argument 'mbean', must not be null");
        requireNonNull(objectName, "Argument 'objectName', must not be null");
        requireNonNull(server, "Argument 'server', must not be null");
        try {
            server.registerMBean(mbean, objectName);
        } catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
            throw new GriffonException("Can't register mbean with name '" + objectName.getCanonicalName() + "' into " + server, e);
        }
    }

    public static void register(@Nonnull Object mbean, @Nonnull String objectName, @Nonnull MBeanServer server) {
        requireNonBlank(objectName, "Argument 'objectName', must not be blank");
        try {
            register(mbean, new ObjectName(objectName), server);
        } catch (MalformedObjectNameException e) {
            throw new GriffonException("Can't register mbean with name '" + objectName + "' into " + server, e);
        }
    }

    public static void registerIntoPlatformMBeanServer(@Nonnull Object mbean, @Nonnull ObjectName objectName) {
        register(mbean, objectName, getPlatformMBeanServer());
    }

    public static void registerIntoPlatformMBeanServer(@Nonnull Object mbean, @Nonnull String objectName) {
        register(mbean, objectName, getPlatformMBeanServer());
    }

    public static void unregisterFromPlatformMBeanServer(@Nonnull String objectName) {
        unregisterFromPlatformMBeanServer(objectName, getPlatformMBeanServer());
    }

    public static void unregisterFromPlatformMBeanServer(@Nonnull String objectName, @Nonnull MBeanServer server) {
        requireNonBlank(objectName, "Argument 'objectName', must not be blank");
        try {
            unregister(new ObjectName(objectName), server);
        } catch (MalformedObjectNameException e) {
            throw new GriffonException("Can't unregister mbean with name '" + objectName + "' into " + server, e);
        }
    }

    public static void unregister(@Nonnull ObjectName objectName, @Nonnull MBeanServer server) {
        requireNonNull(objectName, "Argument 'objectName', must not be null");
        requireNonNull(server, "Argument 'server', must not be null");
        try {
            if (server.isRegistered(objectName)) {
                server.unregisterMBean(objectName);
            }
        } catch (InstanceNotFoundException | MBeanRegistrationException e) {
            throw new GriffonException("Can't register mbean with name '" + objectName.getCanonicalName() + "' into " + server, e);
        }
    }
}
