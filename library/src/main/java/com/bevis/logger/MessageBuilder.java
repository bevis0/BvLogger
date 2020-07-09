package com.bevis.logger;

import android.text.TextUtils;

import java.util.LinkedHashMap;
import java.util.LinkedList;

public class MessageBuilder<T extends MessageBuilder> {
    protected LogMessage log;

    public MessageBuilder(LogMessage log) {
        this.log = log;
    }

    public T addLabel(String... labels) {
        if(this.log.labels == null) {
            this.log.labels = new LinkedList<>();
        }
        if(labels != null) {
            for(String label: labels) {
                if(!TextUtils.isEmpty(label)) {
                    this.log.labels.add(label);
                }
            }
        }
        return (T) this;
    }

    protected boolean enable() {
        return true;
    }


    public T setHead(String head, Object... args) {
        if(head != null && enable()) {
            log.head = new LogMessage.FormativeMessage(head, args);
        }
        return (T) this;
    }

    public T addBody(String body, Object... args) {
        if(body != null && enable()) {
            if (log.body == null) {
                log.body = new LinkedList<>();
            }
            log.body.add(new LogMessage.FormativeMessage(body, args));
        }
        return (T) this;
    }

    public T addExtra(String key, Object value) {
        if(key != null && value != null && enable()) {
            if(log.extra == null) {
                log.extra = new LinkedHashMap<>();
            }
            log.extra.put(key, value);
        }
        return (T) this;
    }

    public T addExtra(String key, String value, Object... args) {
        if(key != null && value != null && enable()) {
            if(log.extra == null) {
                log.extra = new LinkedHashMap<>();
            }
            log.extra.put(key, new LogMessage.FormativeMessage(value, args));
        }
        return (T) this;
    }

    public T setThrowable(Throwable throwable) {
        if(throwable != null && enable()) {
            log.throwable = throwable;
        }
        return (T) this;
    }

    public T fullStackTrace() {
        if(enable()) {
            log.fullStackTrace = true;
        }
        return (T) this;
    }

    public T setTail(String tail, Object... args) {
        if(tail != null && enable()) {
            log.tail = new LogMessage.FormativeMessage(tail, args);
        }
        return (T) this;
    }
}
