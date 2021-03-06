/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.binding.hue.test

import static org.eclipse.smarthome.binding.hue.HueBindingConstants.THING_TYPE_BRIDGE;
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import static org.junit.matchers.JUnitMatchers.*

import java.util.Collections;

import org.eclipse.smarthome.binding.hue.HueBindingConstants;
import org.eclipse.smarthome.binding.hue.config.HueBridgeConfiguration
import org.eclipse.smarthome.binding.hue.internal.discovery.HueBridgeDiscoveryParticipant
import org.eclipse.smarthome.config.discovery.DiscoveryListener
import org.eclipse.smarthome.config.discovery.DiscoveryResult
import org.eclipse.smarthome.config.discovery.DiscoveryResultFlag
import org.eclipse.smarthome.config.discovery.DiscoveryService
import org.eclipse.smarthome.config.discovery.UpnpDiscoveryParticipant;
import org.eclipse.smarthome.core.thing.ThingUID
import org.eclipse.smarthome.test.AsyncResultWrapper
import org.eclipse.smarthome.test.OSGiTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.jupnp.model.types.DeviceType
import org.jupnp.model.types.UDN
import org.jupnp.model.meta.DeviceDetails
import org.jupnp.model.meta.ManufacturerDetails
import org.jupnp.model.meta.ModelDetails
import org.jupnp.model.meta.RemoteDevice
import org.jupnp.model.meta.RemoteDeviceIdentity
import org.osgi.service.device.Constants
import org.osgi.service.upnp.UPnPDevice

/**
 * Tests for {@link HueBridgeDiscoveryParticipant}.
 *
 * @author Kai Kreuzer - Initial contribution
 */
class HueBridgeDiscoveryParticipantOSGITest extends OSGiTest {

    UpnpDiscoveryParticipant discoveryParticipant

	RemoteDevice hueDevice
	RemoteDevice otherDevice
	
    @Before
    void setUp() {
        discoveryParticipant = getService(UpnpDiscoveryParticipant, HueBridgeDiscoveryParticipant)
        assertThat discoveryParticipant, is(notNullValue())
		
		hueDevice = new RemoteDevice(
			new RemoteDeviceIdentity(new UDN("123"), 60, new URL("http://hue"), null, null), 
			new DeviceType("namespace", "type"),
			new DeviceDetails(
				new URL("http://1.2.3.4/"),
				"Hue Bridge", 
				new ManufacturerDetails("Philips"), 
				new ModelDetails("Philips hue bridge"),
				"serial123",
				"upc",
				null))
		
		otherDevice = new RemoteDevice(
			new RemoteDeviceIdentity(new UDN("567"), 60, new URL("http://acme"), null, null),
			new DeviceType("namespace", "type"),
			new DeviceDetails(
				"Some Device",
				new ManufacturerDetails("Taiwan"),
				new ModelDetails("§\$%&/"),
				"serial567",
				"upc"))

    }

    @After
    void cleanUp() {
    }

    @Test
    void 'assert correct supported types'() {
		assertThat discoveryParticipant.supportedThingTypeUIDs.size(), is(1)
		assertThat discoveryParticipant.supportedThingTypeUIDs.first(), is(THING_TYPE_BRIDGE)
    }

    @Test
    void 'assert correct thing UID'() {
		assertThat discoveryParticipant.getThingUID(hueDevice), is(new ThingUID("hue:bridge:serial123"))
	}

	@Test
	void 'assert valid DiscoveryResult'() {
		discoveryParticipant.createResult(hueDevice).with {
			assertThat flag, is (DiscoveryResultFlag.NEW)
			assertThat thingUID, is(new ThingUID("hue:bridge:serial123"))
			assertThat thingTypeUID, is (HueBindingConstants.THING_TYPE_BRIDGE)
			assertThat bridgeUID, is(nullValue())
            assertThat properties.get(HueBridgeConfiguration.IP_ADDRESS), is("1.2.3.4")
            assertThat properties.get(HueBridgeConfiguration.SERIAL_NUMBER), is("serial123")
		}

	}
	
	@Test
	void 'assert no thing UID for unknown device'() {
		assertThat discoveryParticipant.getThingUID(otherDevice), is(nullValue())
	}

	@Test
	void 'assert no discovery result for unknown device'() {
		assertThat discoveryParticipant.createResult(otherDevice), is(nullValue())
	}
}
