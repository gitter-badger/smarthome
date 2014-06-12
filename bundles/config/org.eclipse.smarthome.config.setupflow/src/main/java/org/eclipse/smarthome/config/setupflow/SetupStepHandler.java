/**
 * Copyright (c) 2014 openHAB UG (haftungsbeschraenkt) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.eclipse.smarthome.config.setupflow;


/**
 * @author Michael Grammling - Initial Contribution (Draft API)
 */
public interface SetupStepHandler {

    // Hint: Any allocated resources must be released after fireing the succeeding/error event
    void start(SetupFlowContext flowContext, SetupStepHandlerCallback callback);

    void abort(String contextId);
    // void abort(String contextId, SetupStepHandlerCallback callback);

}