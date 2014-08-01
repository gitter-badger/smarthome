/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.io.upnp.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of the default OSGi bundle activator
 */
public final class UpnpServiceActivator implements BundleActivator {

    private static Logger logger = LoggerFactory.getLogger(UpnpServiceActivator.class);

    /**
     * Called whenever the OSGi framework starts our bundle
     */
    public void start(BundleContext context) throws Exception {
        logger.debug("Upnp service has been started.");
    }

    /**
     * Called whenever the OSGi framework stops our bundle
     */
    public void stop(BundleContext context) throws Exception {
        logger.debug("Upnp service has been stopped.");
    }
}