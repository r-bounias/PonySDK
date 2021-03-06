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

package com.ponysdk.core.ui.form.formfield;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.ponysdk.core.ui.basic.PDateBox;
import com.ponysdk.core.ui.basic.event.PValueChangeHandler;
import com.ponysdk.core.ui.form.dataconverter.DataConverter;
import com.ponysdk.core.ui.form.dataconverter.DateConverter;

public class DateBoxFormField extends AbstractFormField<Date, PDateBox> {

    public DateBoxFormField() {
        this(new PDateBox(), new DateConverter());
    }

    public DateBoxFormField(final String dateFormat) {
        this(new PDateBox(new SimpleDateFormat(dateFormat)), new DateConverter(new SimpleDateFormat(dateFormat)));
    }

    public DateBoxFormField(final SimpleDateFormat dateFormat) {
        this(new PDateBox(dateFormat), new DateConverter(dateFormat));
    }

    public DateBoxFormField(final PDateBox dateBox) {
        this(dateBox, new DateConverter(dateBox.getDateFormat()));
    }

    public DateBoxFormField(final PDateBox widget, final DataConverter<String, Date> dataConverter) {
        super(widget, dataConverter);
    }

    @Override
    public void addValueChangeHandler(final PValueChangeHandler<Date> handler) {
        if (handlers == null) widget.addValueChangeHandler(event -> fireValueChange(getValue()));
        super.addValueChangeHandler(handler);
    }

    @Override
    public void reset0() {
        widget.setValue(null);
    }

    @Override
    public Date getValue() {
        return widget.getValue();
    }

    @Override
    public void setValue(final Date value) {
        widget.setValue(value);
    }

    @Override
    protected String getStringValue() {
        return dataProvider.from(getValue());
    }

    @Override
    public void setEnabled(final boolean enabled) {
        widget.setEnabled(enabled);
    }

}
