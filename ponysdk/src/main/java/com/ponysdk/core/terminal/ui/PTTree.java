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

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.ponysdk.core.model.ClientToServerModel;
import com.ponysdk.core.model.HandlerModel;
import com.ponysdk.core.model.ServerToClientModel;
import com.ponysdk.core.terminal.UIBuilder;
import com.ponysdk.core.terminal.instruction.PTInstruction;
import com.ponysdk.core.terminal.model.BinaryModel;
import com.ponysdk.core.terminal.model.ReaderBuffer;

public class PTTree extends PTWidget<Tree> {

    @Override
    protected Tree createUIObject() {
        return new Tree();
    }

    @Override
    public void addHandler(final ReaderBuffer buffer, final HandlerModel handlerModel, final UIBuilder uiService) {
        if (HandlerModel.HANDLER_SELECTION.equals(handlerModel)) {
            uiObject.addSelectionHandler(new SelectionHandler<TreeItem>() {

                @Override
                public void onSelection(final SelectionEvent<TreeItem> event) {
                    final PTObject ptObject = uiService.getPTObject(event.getSelectedItem());
                    final PTInstruction eventInstruction = new PTInstruction(getObjectID());
                    eventInstruction.put(ClientToServerModel.HANDLER_SELECTION, ptObject.getObjectID());
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
        if (ServerToClientModel.ANIMATION.equals(binaryModel.getModel())) {
            uiObject.setAnimationEnabled(binaryModel.getBooleanValue());
            return true;
        } else {
            return super.update(buffer, binaryModel);
        }
    }
}
