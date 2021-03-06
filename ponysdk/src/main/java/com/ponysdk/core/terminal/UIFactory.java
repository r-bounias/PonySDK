/*
 * Copyright (c) 2011 PonySDK
 *  Owners:
 *  Luciano Broussal  <luciano.broussal AT gmail.com>
 *  Mathieu Barbier   <mathieu.barbier AT gmail.com>
 *  Nicolas Ciaravola <nicolas.ciaravola.pro AT gmail.com>
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

package com.ponysdk.core.terminal;

import com.google.gwt.user.client.Window;
import com.ponysdk.core.model.WidgetType;
import com.ponysdk.core.terminal.ui.*;
import com.ponysdk.core.terminal.ui.PTSuggestBox.PTMultiWordSuggestOracle;

class UIFactory {

    PTObject newUIObject(final WidgetType widgetType) {
        switch (widgetType) {
            case ABSOLUTE_PANEL:
                return new PTAbsolutePanel();
            case ADDON:
                return new PTAddOn();
            case ADDON_COMPOSITE:
                return new PTAddOnComposite();
            case ANCHOR:
                return new PTAnchor();
            case BUTTON:
                return new PTButton();
            case CHECKBOX:
                return new PTCheckBox();
            case COMPOSITE:
                Window.alert("UIFactory: Client implementation not found, type : " + widgetType);
                break;
            case DATEBOX:
                return new PTDateBox();
            case DATEPICKER:
                return new PTDatePicker();
            case DECORATED_POPUP_PANEL:
                return new PTDecoratedPopupPanel();
            case DECORATOR_PANEL:
                return new PTDecoratorPanel();
            case DIALOG_BOX:
                return new PTDialogBox();
            case DISCLOSURE_PANEL:
                return new PTDisclosurePanel();
            case DOCK_LAYOUT_PANEL:
                return new PTDockLayoutPanel();
            case ELEMENT:
                return new PTElement();
            case FILE_UPLOAD:
                return new PTFileUpload();
            case FLEX_TABLE:
                return new PTFlexTable();
            case FLOW_PANEL:
                return new PTFlowPanel();
            case FOCUS_PANEL:
                return new PTFocusPanel();
            case GRID:
                return new PTGrid();
            case HEADER_PANEL:
                return new PTHeaderPanel();
            case HORIZONTAL_PANEL:
                return new PTHorizontalPanel();
            case HTML:
                return new PTHTML();
            case IMAGE:
                return new PTImage();
            case LABEL:
                return new PTLabel();
            case LAYOUT_PANEL:
                return new PTLayoutPanel();
            case LISTBOX:
                return new PTListBox();
            case MENU_BAR:
                return new PTMenuBar();
            case MENU_ITEM:
                return new PTMenuItem();
            case MENU_ITEM_SEPARATOR:
                return new PTMenuItemSeparator();
            case MULTIWORD_SUGGEST_ORACLE:
                return new PTMultiWordSuggestOracle();
            case PASSWORD_TEXTBOX:
                return new PTPasswordTextBox();
            case POPUP_PANEL:
                return new PTPopupPanel();
            case PUSH_BUTTON:
                return new PTPushButton();
            case RADIO_BUTTON:
                return new PTRadioButton();
            case RICH_TEXT_AREA:
                return new PTRichTextArea();
            case RICH_TEXT_TOOLBAR:
                return new PTRichTextToolbar();
            case ROOT_LAYOUT_PANEL:
                return new PTRootLayoutPanel();
            case ROOT_PANEL:
                return new PTRootPanel();
            case SCRIPT:
                return new PTScript();
            case SCROLL_PANEL:
                return new PTScrollPanel();
            case SIMPLE_LAYOUT_PANEL:
                return new PTSimpleLayoutPanel();
            case SIMPLE_PANEL:
                return new PTSimplePanel();
            case SPLIT_LAYOUT_PANEL:
                return new PTSplitLayoutPanel();
            case STACKLAYOUT_PANEL:
                return new PTStackLayoutPanel();
            case SUGGESTBOX:
                return new PTSuggestBox();
            case TAB_LAYOUT_PANEL:
                return new PTTabLayoutPanel();
            case TAB_PANEL:
                return new PTTabPanel();
            case TEXTBOX:
                return new PTTextBox();
            case TEXT_AREA:
                return new PTTextArea();
            case TREE:
                return new PTTree();
            case TREE_ITEM:
                return new PTTreeItem();
            case VERTICAL_PANEL:
                return new PTVerticalPanel();
            case WINDOW:
                return new PTWindow();
            default:
                Window.alert("UIFactory: Client implementation not found, type : " + widgetType);
                break;
        }

        return null;
    }
}
