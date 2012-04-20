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

package com.ponysdk.ui.terminal.ui;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.ponysdk.ui.terminal.UIService;
import com.ponysdk.ui.terminal.instruction.Dictionnary.HANDLER;
import com.ponysdk.ui.terminal.instruction.Dictionnary.PROPERTY;
import com.ponysdk.ui.terminal.instruction.Dictionnary.TYPE;
import com.ponysdk.ui.terminal.instruction.PTInstruction;

public class PTTextBoxBase extends PTValueBoxBase<String> {

    @Override
    public void addHandler(final PTInstruction addHandler, final UIService uiService) {
        final String handler = addHandler.getString(HANDLER.KEY);

        if (HANDLER.STRING_VALUE_CHANGE_HANDLER.equals(handler)) {
            cast().addValueChangeHandler(new ValueChangeHandler<String>() {

                @Override
                public void onValueChange(final ValueChangeEvent<String> event) {
                    final PTInstruction eventInstruction = new PTInstruction();
                    eventInstruction.setObjectID(addHandler.getObjectID());
                    eventInstruction.put(TYPE.KEY, TYPE.EVENT);
                    eventInstruction.put(HANDLER.KEY, HANDLER.STRING_VALUE_CHANGE_HANDLER);
                    eventInstruction.put(PROPERTY.VALUE, event.getValue());
                    uiService.triggerEvent(eventInstruction);
                }
            });
        } else {
            super.addHandler(addHandler, uiService);
        }
    }

    @Override
    public void update(final PTInstruction update, final UIService uiService) {
        if (update.containsKey(PROPERTY.TEXT)) {
            cast().setText(update.getString(PROPERTY.TEXT));
        } else {
            super.update(update, uiService);
        }
    }

    @Override
    public TextBoxBase cast() {
        return (TextBoxBase) uiObject;
    }
}
