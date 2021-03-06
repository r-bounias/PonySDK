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

package com.ponysdk.sample.client.page;

import com.ponysdk.core.ui.basic.PDisclosurePanel;
import com.ponysdk.core.ui.basic.PLabel;
import com.ponysdk.core.ui.basic.PTextBox;
import com.ponysdk.core.ui.basic.PVerticalPanel;
import com.ponysdk.core.ui.basic.PWidget;

public class DisclosurePanelPageActivity extends SamplePageActivity {

    public DisclosurePanelPageActivity() {
        super("Disclosure Panel", "Panels");
    }

    @Override
    protected void onFirstShowPage() {
        super.onFirstShowPage();

        PVerticalPanel panel = new PVerticalPanel();

        final PDisclosurePanel disclosurePanel = new PDisclosurePanel("View details");
        disclosurePanel.setContent(getDisclosurePanelContent());
        disclosurePanel.setWidth("200px");

        panel.setSpacing(10);
        panel.add(disclosurePanel);

        examplePanel.setWidget(panel);
    }

    private PWidget getDisclosurePanelContent() {
        final PVerticalPanel verticalPanel = new PVerticalPanel();
        verticalPanel.add(new PLabel("First Name: "));
        verticalPanel.add(new PTextBox());
        verticalPanel.add(new PLabel("Last Name: "));
        verticalPanel.add(new PTextBox());
        return verticalPanel;
    }

}
