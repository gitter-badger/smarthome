/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow.internal;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.smarthome.config.core.ConfigDescription;
import org.eclipse.smarthome.config.core.ConfigDescriptionParameter;
import org.eclipse.smarthome.config.core.ConfigDescriptionRegistry;
import org.eclipse.smarthome.config.setupflow.BaseSetupStepHandler;
import org.eclipse.smarthome.config.setupflow.SetupFlowContext;
import org.eclipse.smarthome.config.setupflow.SetupStepHandlerCallback;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

/**
 * 
 * The {@link GenericConfigurationStepHandler} handles configuration steps which are generated by the {@link SetupFlowManagerImpl}.
 * It double-checks the existence of all required parameters.
 * 
 * @author Oliver Libutzki - Initial contribution
 *
 */
public class GenericConfigurationStepHandler extends BaseSetupStepHandler {

	private final static Logger logger = LoggerFactory
			.getLogger(GenericConfigurationStepHandler.class);

	private ConfigDescriptionRegistryTracker configDescriptionRegistryTracker;

	public GenericConfigurationStepHandler(BundleContext bundleContext) {
		super(bundleContext);
		this.configDescriptionRegistryTracker = new ConfigDescriptionRegistryTracker(
				bundleContext);
		configDescriptionRegistryTracker.open();
	}

	@Override
	protected void startInternal(final SetupFlowContext setupFlowContext,
			final SetupStepHandlerCallback callback) {

		final String thingTypeId = setupFlowContext.getThingTypeId();
		Task genericStepTask = new AbstractTask(thingTypeId + ":GenericConfigurationStep") {

			@Override
			public void executeTask(TaskExecutionState taskExecutionState) {

				if (taskExecutionState.isAborted()) {
					logger.debug("The task execution has been aborted.");
					return;
				}
				// Check properties
				try {
					ConfigDescriptionRegistry configDescriptionRegistry = configDescriptionRegistryTracker
							.getService();
					if (configDescriptionRegistry != null) {

						ConfigDescription configDescription = configDescriptionRegistry
								.getConfigDescription(URI.create(thingTypeId));
						if (configDescription == null) {
							logger.error(
									"No configuration description can be found for thing type {}",
									thingTypeId);
							callback.sendErrorOccurredEvent(setupFlowContext,
									EventFactory.ERROR_CODE_CONFIGURATION_DESCRIPTION_MISSING,
									"No configuration description can be found for thing type "
											+ thingTypeId);
							return;
						}
						Iterable<ConfigDescriptionParameter> unsetParameters = getUnsetParameters(
								setupFlowContext, configDescription);

						Iterator<ConfigDescriptionParameter> unsetParametersIterator = unsetParameters
								.iterator();
						if (unsetParametersIterator.hasNext()) {
							StringBuilder errorMessage = new StringBuilder(
									"The following required parameter(s) have not been set: ");
							while (unsetParametersIterator.hasNext()) {
							    ConfigDescriptionParameter unsetParameter = (ConfigDescriptionParameter) unsetParametersIterator
										.next();
                                errorMessage.append(unsetParameter.getName());
								if (unsetParametersIterator.hasNext()) {
									errorMessage.append(", ");
								}

							}
							errorMessage.append(".");
							callback.sendErrorOccurredEvent(setupFlowContext,
									EventFactory.ERROR_CODE_REQUIRED_PARAMETER_MISSING, errorMessage.toString());
							return;
						}

					}

					// Everything is fine
                    // setupFlowContext.setStepId(null);
					callback.sendStepSucceededEvent(setupFlowContext);
				} finally {
					configDescriptionRegistryTracker.close();
				}
			}

            private Iterable<ConfigDescriptionParameter> getUnsetParameters(
					final SetupFlowContext setupFlowContext,
					ConfigDescription configDescription) {
				final Map<String, Object> contextProperties = setupFlowContext
						.getProperties();
                Iterable<ConfigDescriptionParameter> unsetParameters = Iterables.filter(
						configDescription.getParameters(),
                        new Predicate<ConfigDescriptionParameter>() {
							@Override
                            public boolean apply(ConfigDescriptionParameter parameter) {
								String parameterName = parameter
										.getName();
								if (!parameter.isRequired()) {
									return false;
								}
								if (!contextProperties
										.containsKey(parameterName)) {
									return true;
								}
								Object value = contextProperties
										.get(parameterName);
								if (value == null) {
									return true;
								}
								if (value instanceof String) {
									return ((String) value).isEmpty();
								}
								return false;
							}
						});
				return unsetParameters;
			}

		};
		logger.debug("Starting generic step for thing type {}.", thingTypeId);
		runTask(genericStepTask, setupFlowContext);

	}

}
