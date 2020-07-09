package com.bevis.logger;

import java.util.List;
import java.util.Map;

public class LogMessage {
    public String tag;
    public List<String> labels;
    public FormativeMessage head;
    public List<FormativeMessage> body;
    public FormativeMessage tail;
    public Map<String, Object> extra;
    public List<StackTraceElement> stackTraces;
    public Throwable throwable;
    public Thread thread;
    public boolean fullStackTrace;
    private boolean isMainMessage = true;
    public int parentPadding = 0;

    public static class FormativeMessage {
        public final String message;
        public final Object[] args;
        public final Object[] extras;

        public FormativeMessage(String message, Object[] args) {
            this(message, args, null);
        }

        public FormativeMessage(String message, Object[] args, Object[] extras) {
            this.message = message;
            this.args = args;
            this.extras = extras;
        }
    }

    void detach() {
        isMainMessage = false;
    }

    public boolean isMainMessage() {
        return isMainMessage;
    }
}
