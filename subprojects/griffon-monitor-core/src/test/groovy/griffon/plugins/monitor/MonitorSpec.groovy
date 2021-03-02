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
package griffon.plugins.monitor

import griffon.test.core.GriffonUnitRule
import org.junit.Rule
import spock.lang.Specification

import javax.management.MBeanServer
import javax.management.MBeanServerConnection
import javax.management.ObjectName
import javax.management.remote.JMXConnector
import javax.management.remote.JMXConnectorFactory
import javax.management.remote.JMXConnectorServer
import javax.management.remote.JMXConnectorServerFactory
import javax.management.remote.JMXServiceURL

import static java.lang.management.ManagementFactory.getPlatformMBeanServer

class MonitorSpec extends Specification {
    static {
        System.setProperty('org.slf4j.simpleLogger.defaultLogLevel', 'trace')
    }

    @Rule
    public final GriffonUnitRule griffon = new GriffonUnitRule()

    void 'MBeans were exported successfully'() {
        given:
        MBeanServer mbs = getPlatformMBeanServer()
        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://")
        JMXConnectorServer cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs)
        cs.start()

        JMXConnector cc = null

        when:
        JMXServiceURL addr = cs.getAddress()
        cc = JMXConnectorFactory.connect(addr)
        MBeanServerConnection mbsc = cc.getMBeanServerConnection()
        ObjectName objectName = new ObjectName("griffon.core:type=Environment,application=monitor,name=griffon")

        then:
        mbsc.getMBeanInfo(objectName)

        cleanup:
        cc?.close()
        cs.stop()
    }
}
