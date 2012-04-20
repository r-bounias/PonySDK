/*
 * Copyright (c) 2011 PonySDK
 *  Owners:
 *  Luciano Broussal  <luciano.broussal AT gmail.com>
 *	Mathieu Barbier   <mathieu.barbier AT gmail.com>
 *	Nicolas Ciaravola <nicolas.ciaravola.pro AT gmail.com>
 *  
 *  WebSite:
 *  http://code.google.com/p/pony-sdk/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.ponysdk.ui.server.basic.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ponysdk.ui.server.basic.PKeyCode;
import com.ponysdk.ui.terminal.instruction.Dictionnary.PROPERTY;

public abstract class PKeyPressFilterHandler extends JSONObject implements PKeyPressHandler {

    private final Logger log = LoggerFactory.getLogger(PKeyUpFilterHandler.class);

    public PKeyPressFilterHandler(final PKeyCode... keyCodes) {

        final List<String> codes = new ArrayList<String>();
        for (final PKeyCode code : keyCodes) {
            codes.add(code.getCodeToString());
        }

        try {
            put(PROPERTY.KEY_FILTER, new JSONArray(Arrays.asList(keyCodes)));
        } catch (final JSONException e) {
            log.error("Cannot update key codes : " + keyCodes, e);
        }
    }

}
