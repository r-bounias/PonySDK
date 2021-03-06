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

package com.ponysdk.core.terminal.ui;

import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import com.ponysdk.core.model.ClientToServerModel;
import com.ponysdk.core.model.HandlerModel;
import com.ponysdk.core.model.ServerToClientModel;
import com.ponysdk.core.terminal.UIBuilder;
import com.ponysdk.core.terminal.instruction.PTInstruction;
import com.ponysdk.core.terminal.model.BinaryModel;
import com.ponysdk.core.terminal.model.ReaderBuffer;

public class PTTabPanel extends PTWidget<TabPanel> {

    @Override
    protected TabPanel createUIObject() {
        return new TabPanel();
    }

    @Override
    public void add(final ReaderBuffer buffer, final PTObject ptObject) {
        final Widget w = asWidget(ptObject);

        final BinaryModel binaryModel = buffer.readBinaryModel();
        if (ServerToClientModel.TAB_TEXT.equals(binaryModel.getModel())) {
            final String value = binaryModel.getStringValue();
            final BinaryModel beforeIndexModel = buffer.readBinaryModel();
            if (ServerToClientModel.BEFORE_INDEX.equals(beforeIndexModel.getModel())) {
                uiObject.insert(w, value, beforeIndexModel.getIntValue());
            } else {
                buffer.rewind(beforeIndexModel);
                uiObject.add(w, value);
            }
        } else if (ServerToClientModel.TAB_WIDGET.equals(binaryModel.getModel())) {
            final PTWidget<?> ptWidget = (PTWidget<?>) uiBuilder.getPTObject(binaryModel.getIntValue());
            final BinaryModel beforeIndexModel = buffer.readBinaryModel();
            if (ServerToClientModel.BEFORE_INDEX.equals(beforeIndexModel.getModel())) {
                uiObject.insert(w, ptWidget.cast(), beforeIndexModel.getIntValue());
            } else {
                buffer.rewind(beforeIndexModel);
                uiObject.add(w, ptWidget.cast());
            }
        }

        if (uiObject.getWidgetCount() == 1) {
            uiObject.selectTab(0);
        }
    }

    @Override
    public void addHandler(final ReaderBuffer buffer, final HandlerModel handlerModel, final UIBuilder uiService) {
        if (HandlerModel.HANDLER_SELECTION.equals(handlerModel)) {
            uiObject.addSelectionHandler(new SelectionHandler<Integer>() {

                @Override
                public void onSelection(final SelectionEvent<Integer> event) {
                    final PTInstruction eventInstruction = new PTInstruction(getObjectID());
                    eventInstruction.put(ClientToServerModel.HANDLER_SELECTION, event.getSelectedItem());
                    uiService.sendDataToServer(uiObject, eventInstruction);
                }
            });
        }
        if (HandlerModel.HANDLER_BEFORE_SELECTION.equals(handlerModel)) {
            uiObject.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {

                @Override
                public void onBeforeSelection(final BeforeSelectionEvent<Integer> event) {
                    final PTInstruction eventInstruction = new PTInstruction(getObjectID());
                    eventInstruction.put(ClientToServerModel.HANDLER_BEFORE_SELECTION, event.getItem());
                    uiService.sendDataToServer(uiObject, eventInstruction);
                }
            });
        } else {
            super.addHandler(buffer, handlerModel, uiService);
        }
    }

    @Override
    public void remove(final ReaderBuffer buffer, final PTObject ptObject, final UIBuilder uiService) {
        uiObject.remove(asWidget(ptObject));
    }

    @Override
    public boolean update(final ReaderBuffer buffer, final BinaryModel binaryModel) {
        if (ServerToClientModel.SELECTED_INDEX.equals(binaryModel.getModel())) {
            uiObject.selectTab(binaryModel.getIntValue());
            return true;
        } else if (ServerToClientModel.ANIMATION.equals(binaryModel.getModel())) {
            uiObject.setAnimationEnabled(binaryModel.getBooleanValue());
            return true;
        } else {
            return super.update(buffer, binaryModel);
        }
    }

}
