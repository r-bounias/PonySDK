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

package com.ponysdk.core.ui.form.formfield;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.ponysdk.core.tools.ListenerCollection;
import com.ponysdk.core.ui.basic.HasPValue;
import com.ponysdk.core.ui.basic.IsPWidget;
import com.ponysdk.core.ui.basic.PWidget;
import com.ponysdk.core.ui.basic.event.PValueChangeEvent;
import com.ponysdk.core.ui.basic.event.PValueChangeHandler;
import com.ponysdk.core.ui.form.dataconverter.DataConverter;
import com.ponysdk.core.ui.form.validator.FieldValidator;
import com.ponysdk.core.ui.form.validator.ValidationResult;

/**
 * A field of a {@link com.ponysdk.core.ui.form.Form} that can be validated or
 * reset
 */
public abstract class AbstractFormField<T, W extends IsPWidget> implements FormField, HasPValue<T> {

    protected final W widget;
    private final Set<FormFieldListener> listeners = new HashSet<>();
    protected DataConverter<String, T> dataProvider;
    private FieldValidator validator;
    protected ListenerCollection<PValueChangeHandler<T>> handlers;

    public AbstractFormField(final W widget, final DataConverter<String, T> dataProvider) {
        this.widget = widget;
        this.dataProvider = dataProvider;
    }

    @Override
    public ValidationResult isValid() {
        ValidationResult result;

        if (validator == null) result = ValidationResult.newOKValidationResult();
        else result = validator.isValid(getStringValue());

        fireAfterValidation(result);

        return result;
    }

    @Override
    public void setValidator(final FieldValidator validator) {
        this.validator = validator;
    }

    @Override
    public void addFormFieldListener(final FormFieldListener listener) {
        listeners.add(listener);
    }

    @Override
    public void reset() {
        reset0();
        fireAfterReset();
    }

    private void fireAfterReset() {
        for (final FormFieldListener listener : listeners) {
            listener.afterReset(this);
        }
    }

    private void fireAfterValidation(final ValidationResult result) {
        for (final FormFieldListener listener : listeners) {
            listener.afterValidation(this, result);
        }
    }

    @Override
    public PWidget asWidget() {
        return widget.asWidget();
    }

    public W getWidget() {
        return widget;
    }

    public void setDataProvider(final DataConverter<String, T> dataProvider) {
        this.dataProvider = dataProvider;
    }

    protected abstract String getStringValue();

    protected abstract void reset0();

    @Override
    public void addValueChangeHandler(final PValueChangeHandler<T> handler) {
        if (handlers == null) handlers = new ListenerCollection<>();
        handlers.add(handler);
    }

    @Override
    public boolean removeValueChangeHandler(final PValueChangeHandler<T> handler) {
        return handlers != null ? handlers.remove(handler) : false;
    }

    @Override
    public Collection<PValueChangeHandler<T>> getValueChangeHandlers() {
        return Collections.unmodifiableCollection(handlers);
    }

    protected void fireValueChange(final T value) {
        if (handlers == null) return;
        handlers.forEach(handler -> handler.onValueChange(new PValueChangeEvent<>(this, value)));
    }

    @Override
    public HasPValue<T> asHasPValue() {
        return this;
    }

}
