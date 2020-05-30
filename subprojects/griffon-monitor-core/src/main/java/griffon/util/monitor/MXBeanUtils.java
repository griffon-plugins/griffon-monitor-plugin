/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2017-2020 The author and/or original authors.
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
package griffon.util.monitor;

import griffon.annotations.core.Nonnull;
import griffon.exceptions.GriffonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistration;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import static griffon.util.GriffonNameUtils.requireNonBlank;
import static java.lang.management.ManagementFactory.getPlatformMBeanServer;
import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public final class MXBeanUtils {
    private static final Logger LOG = LoggerFactory.getLogger(MXBeanUtils.class);
    private static final String ERROR_SERVER_NULL = "Argument 'server', must not be null";
    private static final String ERROR_OBJECT_NAME_BLANK = "Argument 'objectName', must not be blank";
    private static final String ERROR_OBJECT_NAME_NULL = "Argument 'objectName', must not be null";

    private MXBeanUtils() {

    }

    public static ObjectName register(@Nonnull MBeanRegistration mbean, @Nonnull MBeanServer server) {
        requireNonNull(mbean, "Argument 'mbean', must not be null");
        requireNonNull(server, ERROR_SERVER_NULL);
        try {
            ObjectInstance objectInstance = server.registerMBean(mbean, null);
            LOG.debug("Registered bean {} with {} at server {}", mbean, objectInstance.getObjectName(), server);
            return objectInstance.getObjectName();
        } catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
            throw new GriffonException("Can't register mbean " + mbean + " into " + server, e);
        }
    }

    public static ObjectName register(@Nonnull Object mbean, @Nonnull ObjectName objectName, @Nonnull MBeanServer server) {
        requireNonNull(mbean, "Argument 'mbean', must not be null");
        requireNonNull(objectName, ERROR_OBJECT_NAME_NULL);
        requireNonNull(server, ERROR_SERVER_NULL);
        try {
            ObjectInstance objectInstance = server.registerMBean(mbean, objectName);
            LOG.debug("Registered bean {} with {} at server {}", mbean, objectInstance.getObjectName(), server);
            return objectInstance.getObjectName();
        } catch (InstanceAlreadyExistsException | MBeanRegistrationException | NotCompliantMBeanException e) {
            throw new GriffonException("Can't register mbean with name '" + objectName.getCanonicalName() + "' into " + server, e);
        }
    }

    public static ObjectName register(@Nonnull Object mbean, @Nonnull String objectName, @Nonnull MBeanServer server) {
        requireNonBlank(objectName, ERROR_OBJECT_NAME_BLANK);
        try {
            return register(mbean, new ObjectName(objectName), server);
        } catch (MalformedObjectNameException e) {
            throw new GriffonException("Can't register mbean with name '" + objectName + "' into " + server, e);
        }
    }

    public static ObjectName registerIntoPlatformMBeanServer(@Nonnull MBeanRegistration mbean) {
        return register(mbean, getPlatformMBeanServer());
    }

    public static ObjectName registerIntoPlatformMBeanServer(@Nonnull Object mbean, @Nonnull ObjectName objectName) {
        return register(mbean, objectName, getPlatformMBeanServer());
    }

    public static ObjectName registerIntoPlatformMBeanServer(@Nonnull Object mbean, @Nonnull String objectName) {
        return register(mbean, objectName, getPlatformMBeanServer());
    }

    public static ObjectName unregisterFromPlatformMBeanServer(@Nonnull String objectName) {
        return unregister(objectName, getPlatformMBeanServer());
    }

    public static ObjectName unregisterFromPlatformMBeanServer(@Nonnull ObjectName objectName) {
        return unregister(objectName, getPlatformMBeanServer());
    }

    public static ObjectName unregister(@Nonnull String objectName, @Nonnull MBeanServer server) {
        requireNonBlank(objectName, ERROR_OBJECT_NAME_BLANK);
        try {
            return unregister(new ObjectName(objectName), server);
        } catch (MalformedObjectNameException e) {
            throw new GriffonException("Can't unregister mbean with name '" + objectName + "' from " + server, e);
        }
    }

    public static ObjectName unregister(@Nonnull ObjectName objectName, @Nonnull MBeanServer server) {
        requireNonNull(objectName, ERROR_OBJECT_NAME_NULL);
        requireNonNull(server, ERROR_SERVER_NULL);
        try {
            if (server.isRegistered(objectName)) {
                server.unregisterMBean(objectName);
                LOG.debug("Unregistered {} from server {}", objectName, server);
            }
            return objectName;
        } catch (Exception e) {
            throw new GriffonException("Can't unregister mbean with name '" + objectName.getCanonicalName() + "' from " + server, e);
        }
    }
}
