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

package com.ponysdk.core.ui.eventbus;

import com.ponysdk.core.ui.eventbus.Event.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SimpleEventBus implements EventBus {

    private static final Logger log = LoggerFactory.getLogger(SimpleEventBus.class);

    private final Map<Type<?>, Map<Object, Set<?>>> map = new HashMap<>();

    private final Set<BroadcastEventHandler> broadcastHandlerManager = new HashSet<>();
    private final Queue<Event<? extends EventHandler>> eventQueue = new LinkedList<>();
    private final List<HandlerContext<? extends EventHandler>> pendingHandlerRegistration = new ArrayList<>();
    private boolean firing = false;

    @Override
    public <H extends EventHandler> HandlerRegistration addHandler(final Type<H> type, final H handler) {
        if (type == null) {
            throw new NullPointerException("Cannot add a handler with a null type");
        }
        if (handler == null) {
            throw new NullPointerException("Cannot add a null handler");
        }

        return doAdd(type, null, handler);
    }

    @Override
    public <H extends EventHandler> void removeHandler(final Type<H> type, final H handler) {
        doRemove(type, null, handler);
    }

    @Override
    public <H extends EventHandler> void removeHandlerFromSource(final Type<H> type, final Object source, final H handler) {
        doRemove(type, source, handler);
    }

    @Override
    public void removeHandler(final BroadcastEventHandler handler) {
        broadcastHandlerManager.remove(handler);
    }

    @Override
    public <H extends EventHandler> HandlerRegistration addHandlerToSource(final Type<H> type, final Object source, final H handler) {
        if (type == null) {
            throw new NullPointerException("Cannot add a handler with a null type");
        }
        if (source == null) {
            throw new NullPointerException("Cannot add a handler with a null source");
        }
        if (handler == null) {
            throw new NullPointerException("Cannot add a null handler");
        }

        return doAdd(type, source, handler);
    }

    @Override
    public void fireEvent(final Event<?> event) {
        if (event == null) {
            throw new NullPointerException("Cannot fire null eventbus");
        }
        doFire(event, null);
    }

    @Override
    public void addHandler(final BroadcastEventHandler handler) {
        broadcastHandlerManager.add(handler);
    }

    @Override
    public void fireEventFromSource(final Event<? extends EventHandler> event, final Object source) {
        if (event == null) {
            throw new NullPointerException("Cannot fire null eventbus");
        }
        if (source == null) {
            throw new NullPointerException("Cannot fire from a null source");
        }
        doFire(event, source);
    }

    protected <H extends EventHandler> void doRemove(final Type<H> type, final Object source, final H handler) {
        if (!firing) {
            doRemoveNow(type, source, handler);
        } else {
            defferedRemove(type, source, handler);
        }
    }

    private void doRemoveNow(final Type<? extends EventHandler> type, final Object source, final EventHandler handler) {
        final Map<Object, Set<?>> sourceMap = map.get(type);
        if (sourceMap == null) {
            return;
        }

        final Set<?> handlers = sourceMap.get(source);
        if (handlers == null) {
            return;
        }

        final boolean removed = handlers.remove(handler);
        assert removed : "redundant remove call";
        if (removed && handlers.isEmpty()) {
            prune(type, source);
        }
    }

    private <H extends EventHandler> void defferedRemove(final Type<H> type, final Object source, final H handler) {
        final HandlerContext<H> context = new HandlerContext<>();
        context.type = type;
        context.source = source;
        context.handler = handler;
        context.add = false;

        final boolean removed = pendingHandlerRegistration.remove(context);

        if (!removed) pendingHandlerRegistration.add(context);
    }

    private <H extends EventHandler> HandlerRegistration doAdd(final Type<H> type, final Object source, final H handler) {
        if (!firing) {
            doAddNow(type, source, handler);
        } else {
            defferedAdd(type, source, handler);
        }

        return () -> doRemove(type, source, handler);
    }

    private <H extends EventHandler> void defferedAdd(final Type<H> type, final Object source, final H handler) {
        final HandlerContext<H> context = new HandlerContext<>();
        context.type = type;
        context.source = source;
        context.handler = handler;
        context.add = true;

        pendingHandlerRegistration.add(context);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void doAddNow(final Type type, final Object source, final Object handler) {
        ensureHandlerSet(type, source).add(handler);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void doFire(final Event<? extends EventHandler> event, final Object source) {
        if (source != null) event.setSource(source);

        eventQueue.add(event);

        if (firing) return;

        firing = true;
        Set<Throwable> causes = null;

        try {

            Event e;

            while ((e = eventQueue.poll()) != null) {

                final Collection<? extends EventHandler> handlers = getDispatchSet(e.getAssociatedType(), e.getSource());

                for (EventHandler handler1 : handlers) {
                    try {
                        if (log.isDebugEnabled()) log.debug("dispatch eventbus #" + e);
                        e.dispatch(handler1);
                    } catch (final Throwable t) {
                        log.error("Cannot process fired eventbus #" + e.getAssociatedType(), t);
                        if (causes == null) {
                            causes = new HashSet<>();
                        }
                        causes.add(t);
                    }
                }

                for (final BroadcastEventHandler handler : broadcastHandlerManager) {
                    if (log.isDebugEnabled()) log.debug("broadcast eventbus #" + e);
                    handler.onEvent(e);
                }

            }

            for (final HandlerContext<? extends EventHandler> context : pendingHandlerRegistration) {
                if (context.add)
                    doAddNow(context.type, context.source, context.handler);
                else
                    doRemoveNow(context.type, context.source, context.handler);
            }

            pendingHandlerRegistration.clear();

            if (causes != null) throw new UmbrellaException(causes);
        } finally {
            firing = false;
        }
    }

    private <H extends EventHandler> Set<H> ensureHandlerSet(final Type<H> type, final Object source) {
        Map<Object, Set<?>> sourceMap = map.get(type);
        if (sourceMap == null) {
            sourceMap = new HashMap<>();
            map.put(type, sourceMap);
        }

        // safe, we control the puts.
        @SuppressWarnings("unchecked")
        Set<H> handlers = (Set<H>) sourceMap.get(source);
        if (handlers == null) {
            handlers = new LinkedHashSet<>();
            sourceMap.put(source, handlers);
        }

        return handlers;
    }

    private <H extends EventHandler> Collection<H> getDispatchSet(final Type<H> type, final Object source) {
        final Collection<H> directHandlers = getHandlers(type, source);
        if (source == null) {
            return directHandlers;
        }

        final Collection<H> globalHandlers = getHandlers(type, null);

        final Set<H> rtn = new LinkedHashSet<>(directHandlers);
        rtn.addAll(globalHandlers);
        return rtn;
    }

    @Override
    public <H extends EventHandler> Collection<H> getHandlers(final Type<H> type, final Object source) {
        final Map<Object, Set<?>> sourceMap = map.get(type);
        if (sourceMap == null) {
            return Collections.emptySet();
        }

        // safe, we control the puts.
        @SuppressWarnings("unchecked")
        final Set<H> handlers = (Set<H>) sourceMap.get(source);
        if (handlers == null) {
            return Collections.emptySet();
        }

        return new HashSet<>(handlers);
    }

    private void prune(final Type<?> type, final Object source) {
        final Map<Object, Set<?>> sourceMap = map.get(type);

        final Set<?> pruned = sourceMap.remove(source);

        assert pruned != null : "Can't prune what wasn't there";
        assert pruned.isEmpty() : "Pruned unempty list!";

        if (sourceMap.isEmpty()) {
            map.remove(type);
        }
    }

    static class HandlerContext<H> {

        boolean add;

        Type<H> type;
        Object source;
        H handler;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HandlerContext<?> that = (HandlerContext<?>) o;
            return add == that.add &&
                    Objects.equals(type, that.type) &&
                    Objects.equals(source, that.source) &&
                    Objects.equals(handler, that.handler);
        }

        @Override
        public int hashCode() {
            return Objects.hash(add, type, source, handler);
        }
    }

}