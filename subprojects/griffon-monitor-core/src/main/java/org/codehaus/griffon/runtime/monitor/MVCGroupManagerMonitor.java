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

import griffon.core.ApplicationEvent;
import griffon.core.CallableWithArgs;
import griffon.core.GriffonApplication;
import griffon.core.env.Metadata;
import griffon.core.mvc.MVCGroup;
import griffon.core.mvc.MVCGroupManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ObjectName;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Objects.requireNonNull;

/**
 * @author Andres Almiray
 */
public class MVCGroupManagerMonitor extends AbstractMBeanRegistration implements MVCGroupManagerMonitorMXBean {
    private MVCGroupManager delegate;
    private final NotificationBroadcasterSupport nbs = new NotificationBroadcasterSupport();
    private final AtomicLong sequenceNumber = new AtomicLong(0L);

    public MVCGroupManagerMonitor(@Nonnull Metadata metadata, @Nonnull GriffonApplication application) {
        super(metadata);
        requireNonNull(application, "Argument 'application' must not be null");
        this.delegate = application.getMvcGroupManager();
        application.getEventRouter().addEventListener(ApplicationEvent.CREATE_MVC_GROUP.getName(), new CallableWithArgs<Void>() {
            @Nullable
            @Override
            public Void call(Object... args) {
                fireNotification(ApplicationEvent.CREATE_MVC_GROUP.getName(), (MVCGroup) args[0]);
                return null;
            }
        });
        application.getEventRouter().addEventListener(ApplicationEvent.DESTROY_MVC_GROUP.getName(), new CallableWithArgs<Void>() {
            @Nullable
            @Override
            public Void call(Object... args) {
                fireNotification(ApplicationEvent.DESTROY_MVC_GROUP.getName(), (MVCGroup) args[0]);
                return null;
            }
        });
    }

    private void fireNotification(@Nonnull String event, @Nonnull MVCGroup group) {
        nbs.sendNotification(new Notification(
            event,
            this,
            sequenceNumber.getAndIncrement(),
            System.nanoTime(),
            "mvcType=" + group.getMvcType() + ", mvcId=" + group.getMvcId()
        ));
    }

    @Override
    public String getManagerImplementationClass() {
        return delegate.getClass().getName();
    }

    @Override
    public int getMVCGroupCount() {
        return delegate.getGroups().size();
    }

    @Override
    public int getMVCGroupConfigurationCount() {
        return delegate.getConfigurations().size();
    }

    @Override
    public int getModelCount() {
        return delegate.getModels().size();
    }

    @Override
    public int getViewCount() {
        return delegate.getViews().size();
    }

    @Override
    public int getControllerCount() {
        return delegate.getControllers().size();
    }

    @Override
    public MVCGroupInfo[] getMVCGroupDetails() {
        Collection<MVCGroup> groups = new ArrayList<>(delegate.getGroups().values());
        MVCGroupInfo[] data = new MVCGroupInfo[groups.size()];
        int i = 0;
        for (MVCGroup group : groups) {
            data[i++] = new MVCGroupInfo(group.getMvcType(), group.getMvcId());
        }
        return data;
    }

    @Override
    public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception {
        return new ObjectName("griffon.core:type=Manager,application=" + metadata.getApplicationName() + ",name=mvc");
    }

    @Override
    public void postDeregister() {
        delegate = null;
        super.postDeregister();
    }
}
