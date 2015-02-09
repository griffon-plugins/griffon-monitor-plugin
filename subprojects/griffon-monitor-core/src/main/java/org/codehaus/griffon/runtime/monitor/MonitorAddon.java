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
import griffon.plugins.monitor.ActionManagerMonitor;
import griffon.plugins.monitor.AddonManagerMonitor;
import griffon.plugins.monitor.ArtifactManagerMonitor;
import griffon.plugins.monitor.EnvironmentMonitor;
import griffon.plugins.monitor.MVCGroupManagerMonitor;
import griffon.plugins.monitor.MetadataMonitor;
import griffon.plugins.monitor.UIThreadManagerMonitor;
import griffon.plugins.monitor.WindowManagerMonitor;
import org.codehaus.griffon.runtime.core.addon.AbstractGriffonAddon;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Named;

import static griffon.util.MXBeanUtils.registerIntoPlatformMBeanServer;
import static griffon.util.MXBeanUtils.unregisterFromPlatformMBeanServer;

/**
 * @author Andres Almiray
 */
@Named("monitor")
public class MonitorAddon extends AbstractGriffonAddon {
    @Override
    public void init(final @Nonnull GriffonApplication application) {
        application.getEventRouter().addEventListener(ApplicationEvent.BOOTSTRAP_END.getName(), new CallableWithArgs<Void>() {
            @Nullable
            @Override
            public Void call(Object... args) {
                registerMBeans(application);
                return null;
            }
        });
    }

    private void registerMBeans(@Nonnull GriffonApplication application) {
        String applicationName = Metadata.getCurrent().getApplicationName();
        registerIntoPlatformMBeanServer(new EnvironmentMonitor(), "griffon.core:type=Environment,application=" + applicationName + ",name=griffon");
        registerIntoPlatformMBeanServer(new MetadataMonitor(Metadata.getCurrent()), "griffon.core:type=Environment,application=" + applicationName + ",name=metadata");
        registerIntoPlatformMBeanServer(new AddonManagerMonitor(application.getAddonManager()), "griffon.core:type=Manager,application=" + applicationName + ",name=addon");
        registerIntoPlatformMBeanServer(new ArtifactManagerMonitor(application.getArtifactManager()), "griffon.core:type=Manager,application=" + applicationName + ",name=artifact");
        registerIntoPlatformMBeanServer(new MVCGroupManagerMonitor(application), "griffon.core:type=Manager,application=" + applicationName + ",name=mvc");
        registerIntoPlatformMBeanServer(new WindowManagerMonitor(application.getWindowManager()), "griffon.core:type=Manager,application=" + applicationName + ",name=window");
        registerIntoPlatformMBeanServer(new ActionManagerMonitor(application.getActionManager()), "griffon.core:type=Manager,application=" + applicationName + ",name=action");
        registerIntoPlatformMBeanServer(new UIThreadManagerMonitor(application.getUIThreadManager()), "griffon.core:type=Manager,application=" + applicationName + ",name=thread");
    }

    @Override
    public void onShutdown(@Nonnull GriffonApplication application) {
        String applicationName = Metadata.getCurrent().getApplicationName();
        unregisterFromPlatformMBeanServer("griffon.core:type=Environment,application=" + applicationName + ",name=griffon");
        unregisterFromPlatformMBeanServer("griffon.core:type=Environment,application=" + applicationName + ",name=metadata");
        unregisterFromPlatformMBeanServer("griffon.core:type=Manager,application=" + applicationName + ",name=addon");
        unregisterFromPlatformMBeanServer("griffon.core:type=Manager,application=" + applicationName + ",name=artifact");
        unregisterFromPlatformMBeanServer("griffon.core:type=Manager,application=" + applicationName + ",name=mvc");
        unregisterFromPlatformMBeanServer("griffon.core:type=Manager,application=" + applicationName + ",name=window");
        unregisterFromPlatformMBeanServer("griffon.core:type=Manager,application=" + applicationName + ",name=action");
        unregisterFromPlatformMBeanServer("griffon.core:type=Manager,application=" + applicationName + ",name=thread");
    }
}
