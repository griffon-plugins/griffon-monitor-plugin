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
import griffon.core.env.Metadata;
import griffon.core.events.BootstrapEndEvent;
import griffon.plugins.monitor.MBeanManager;
import org.codehaus.griffon.runtime.core.addon.AbstractGriffonAddon;

import javax.application.event.EventHandler;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author Andres Almiray
 */
@Named("monitor")
public class MonitorAddon extends AbstractGriffonAddon {
    @Inject
    private Metadata metadata;

    @Inject
    private MBeanManager mbeanmanager;

    @Override
    public void init(final @Nonnull GriffonApplication application) {
        application.getEventRouter().subscribe(this);
        application.addShutdownHandler(mbeanmanager);
    }

    @EventHandler
    public void handleBootstrapEndEven(@Nonnull BootstrapEndEvent event) {
        GriffonApplication application = event.getApplication();
        mbeanmanager.registerMBean(new EnvironmentMonitor(metadata));
        mbeanmanager.registerMBean(new MetadataMonitor(metadata));
        mbeanmanager.registerMBean(new AddonManagerMonitor(metadata, application.getAddonManager()));
        mbeanmanager.registerMBean(new ArtifactManagerMonitor(metadata, application.getArtifactManager()));
        mbeanmanager.registerMBean(new MVCGroupManagerMonitor(metadata, application));
        mbeanmanager.registerMBean(new WindowManagerMonitor(metadata, application.getWindowManager()));
        mbeanmanager.registerMBean(new ActionManagerMonitor(metadata, application.getActionManager()));
        mbeanmanager.registerMBean(new UIThreadManagerMonitor(metadata, application.getUIThreadManager()));
    }
}
