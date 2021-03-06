/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.discovery.internal.console;

import java.util.Collections;
import java.util.List;

import org.eclipse.smarthome.config.discovery.DiscoveryServiceRegistry;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.io.console.Console;
import org.eclipse.smarthome.io.console.extensions.ConsoleCommandExtension;

import com.google.common.collect.Lists;

/**
 * {@link DiscoveryConsoleCommandExtension} provides console commands for thing discovery.
 * 
 * @author Kai Kreuzer - Initial contribution
 */
public class DiscoveryConsoleCommandExtension implements ConsoleCommandExtension {

    private DiscoveryServiceRegistry discoveryServiceRegistry;

    private final static String COMMAND_DISCOVERY = "discovery";

    private final static List<String> SUPPORTED_COMMANDS = Lists.newArrayList(COMMAND_DISCOVERY);

    @Override
    public boolean canHandle(String[] args) {
        String firstArgument = args[0];
        return SUPPORTED_COMMANDS.contains(firstArgument);
    }

    @Override
    public void execute(String[] args, Console console) {
        String command = args[0];
        switch (command) {
        case COMMAND_DISCOVERY:
            if(args.length > 1) {
                String subCommand = args[1];
                switch (subCommand) {
                case "start":
                    if (args.length > 2) {
                    	String arg2 = args[2];
                    	if(arg2.contains(":")) {
                    		ThingTypeUID thingTypeUID = new ThingTypeUID(arg2);
                    		runDiscoveryForThingType(console, thingTypeUID);
                    	} else {
                    		runDiscoveryForBinding(console, arg2);
                    	}
                    } else {
						console.println("Specify thing type id or binding id to discover: discovery "
								+ "start <thingTypeUID|bindingID> (e.g. \"hue:bridge\" or \"hue\")");
                    }
                    return;
                default:
                    break;
                }
            } else {
            	console.println(getUsages().get(0));
            }
            return;
        default:
            return;
        }
    }

    private void runDiscoveryForThingType(Console console, ThingTypeUID thingTypeUID) {
		discoveryServiceRegistry.startScan(thingTypeUID, null);
	}
    
    private void runDiscoveryForBinding(Console console, String bindingId) {
		discoveryServiceRegistry.startScan(bindingId, null);
	}

	public List<String> getUsages() {
        return Collections.singletonList("discovery start <thingTypeUID|bindingID> - runs a discovery on a given thing type or binding");
    }

	protected void setDiscoveryServiceRegistry(DiscoveryServiceRegistry discoveryServiceRegistry) {
        this.discoveryServiceRegistry = discoveryServiceRegistry;
    }

    protected void unsetDiscoveryServiceRegistry(DiscoveryServiceRegistry discoveryServiceRegistry) {
        this.discoveryServiceRegistry = null;
    }

}