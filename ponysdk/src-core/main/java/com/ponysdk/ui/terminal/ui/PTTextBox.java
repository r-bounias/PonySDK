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

import com.google.gwt.user.client.ui.TextBox;
import com.ponysdk.ui.terminal.UIService;
import com.ponysdk.ui.terminal.instruction.PTInstruction;
import com.ponysdk.ui.terminal.instruction.Dictionnary.PROPERTY;

public class PTTextBox extends PTTextBoxBase {

    @Override
    public void create(final PTInstruction create, final UIService uiService) {
        init(create, uiService, new TextBox());
    }

    @Override
    public void update(final PTInstruction update, final UIService uiService) {
        if (update.containsKey(PROPERTY.TEXT)) {
            cast().setText(update.getString(PROPERTY.TEXT));
        } else if (update.containsKey(PROPERTY.VALUE)) {
            cast().setValue(update.getString(PROPERTY.VALUE));
        } else if (update.containsKey(PROPERTY.VISIBLE_LENGTH)) {
            cast().setVisibleLength(update.getInt(PROPERTY.VISIBLE_LENGTH));
        } else {
            super.update(update, uiService);
        }
    }

    @Override
    public TextBox cast() {
        return (TextBox) uiObject;
    }
}
