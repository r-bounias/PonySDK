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

package com.ponysdk.core.ui.basic;

import java.util.Objects;

import com.ponysdk.core.model.ServerToClientModel;
import com.ponysdk.core.model.WidgetType;

/**
 * A mutually-exclusive selection radio button widget. Fires {@link com.ponysdk.core.ui.basic.event.PClickEvent}s when the radio
 * button is clicked, and {@link com.ponysdk.core.ui.basic.event.PValueChangeEvent}s when the button becomes checked. Note, however,
 * that browser limitations prevent PValueChangeEvents from being sent when the radio button is
 * cleared as a side effect of another in the group being clicked.
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.gwt-RadioButton</dt>
 * <dd>the outer element</dd>
 * </dl>
 */
public class PRadioButton extends PCheckBox {

    private String name;

    public PRadioButton(final String name, final String label) {
        this(label);
        setName(name);
    }

    public PRadioButton(final String label) {
        super(label);
    }

    @Override
    protected WidgetType getWidgetType() {
        return WidgetType.RADIO_BUTTON;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        if (Objects.equals(this.name, name)) return;
        this.name = name;
        saveUpdate(writer -> writer.writeModel(ServerToClientModel.NAME, name));
    }

}
