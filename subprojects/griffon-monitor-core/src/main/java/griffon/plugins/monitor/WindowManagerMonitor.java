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

import griffon.core.view.WindowManager;

import javax.annotation.Nonnull;

/**
 * @author Andres Almiray
 */
public class WindowManagerMonitor implements WindowManagerMonitorMXBean {
    private final WindowManager delegate;

    public WindowManagerMonitor(@Nonnull WindowManager delegate) {
        this.delegate = delegate;
    }

    @Override
    public String getManagerImplementationClass() {
        return delegate.getClass().getName();
    }

    @Override
    public int getWindowCount() {
        return delegate.getWindows().size();
    }
}
