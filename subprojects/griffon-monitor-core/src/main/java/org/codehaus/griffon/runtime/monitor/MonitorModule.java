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

import griffon.core.addon.GriffonAddon;
import griffon.core.injection.Module;
import griffon.plugins.monitor.MBeanManager;
import org.codehaus.griffon.runtime.core.injection.AbstractModule;
import org.kordamp.jipsy.ServiceProviderFor;

import javax.inject.Named;

/**
 * @author Andres Almiray
 */
@Named("monitor")
@ServiceProviderFor(Module.class)
public class MonitorModule extends AbstractModule {
    @Override
    protected void doConfigure() {
        // tag::bindings[]
        bind(MBeanManager.class)
            .to(DefaultMBeanManager.class)
            .asSingleton();

        bind(GriffonAddon.class)
            .to(MonitorAddon.class)
            .asSingleton();
        // end::bindings[]
    }
}
