package com.bevis.logger;

import com.bevis.logger.formatter.FormatContext;

import java.util.*;

public class LogBuilder extends MessageBuilder<LogBuilder> {
    private int maxLevel = 0;
    private final int level;
    private final Printer printer;
    private Class stackOffset;
    private StackTraceElement[]  stackTraceElements;

    public LogBuilder(String tag,
                      int level,
                      Printer printer,
                        Class stackOffset) {
        super(new LogMessage());
        this.level = level;
        this.log.tag = tag;
        if(FormatContext.global().getSetting().isAsyncOutput()) {
            this.printer = new AsyncPrinterImpl(printer);
        } else {
            this.printer = printer;
        }
        this.stackOffset = stackOffset == null?this.getClass():stackOffset;
    }

//    public LogBuilder addLabel(String... labels) {
//        if(this.log.labels == null) {
//            this.log.labels = new LinkedList<>();
//        }
//        if(labels != null) {
//            for(String label: labels) {
//                if(!TextUtils.isEmpty(label)) {
//                    this.log.labels.add(label);
//                }
//            }
//        }
//        return this;
//    }

    public LogBuilder setTag(String tag) {
        this.log.tag = tag;
        return this;
    }

    /**
     * 调整输出等级
     * @param maxLevel
     * @return
     */
    public LogBuilder setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
        return this;
    }

    /**
     * 调整等级为任意输出
     * @return
     */
    public LogBuilder debug() {
        setMaxLevel(LogLevel.VERBOSE);
        return this;
    }

    public LogBuilder setHead(String head, Object... args) {
        if(head != null && acceptLevel()) {
            log.head = new LogMessage.FormativeMessage(head, args);
        }
        return this;
    }

    public LogBuilder addBody(String body, Object... args) {
        if(body != null && acceptLevel()) {
            if (log.body == null) {
                log.body = new LinkedList<>();
            }
            log.body.add(new LogMessage.FormativeMessage(body, args));
        }
        return this;
    }

    public LogBuilder addExtra(String key, Object value) {
        if(key != null && value != null && acceptLevel()) {
            if(log.extra == null) {
                log.extra = new LinkedHashMap<>();
            }
            log.extra.put(key, value);
        }
        return this;
    }

    public LogBuilder addExtra(String key, String value, Object... args) {
        if(key != null && value != null && acceptLevel()) {
            if(log.extra == null) {
                log.extra = new LinkedHashMap<>();
            }
            log.extra.put(key, new LogMessage.FormativeMessage(value, args));
        }
        return this;
    }

    public LogBuilder setThrowable(Throwable throwable) {
        if(throwable != null && acceptLevel()) {
            log.throwable = throwable;
        }
        return this;
    }

    public LogBuilder fullStackTrace() {
        if(acceptLevel()) {
            log.fullStackTrace = true;
        }
        return this;
    }

    public LogBuilder setTail(String tail, Object... args) {
        if(tail != null && acceptLevel()) {
            log.tail = new LogMessage.FormativeMessage(tail, args);
        }
        return this;
    }

    public LogBuilder setStackOffset(Class stackOffset) {
        this.stackOffset = stackOffset;
        return this;
    }

    public LogBuilder setStackTrace(StackTraceElement[] stackTrace) {
        this.stackTraceElements = stackTrace;
        return this;
    }

    public LogBuilder resetStackOffset() {
        this.stackOffset = this.getClass();
        return this;
    }

    public LogMessage close() {
        LogMessage oldMessage = this.log;
        oldMessage.stackTraces = getStackTrace(stackOffset, stackTraceElements);
        oldMessage.thread = Thread.currentThread();
        this.log = new LogMessage();
        oldMessage.detach();
        return oldMessage;
    }

    private boolean acceptLevel() {
        return level <= maxLevel;
    }

    @Override
    protected boolean enable() {
        return level <= maxLevel;
    }

    public void print() {
        if(enable()) {
            log.stackTraces = getStackTrace(stackOffset, stackTraceElements);
            log.thread = Thread.currentThread();
            switch (level) {
                case LogLevel.VERBOSE:
                    printer.v(log);
                    break;
                case LogLevel.DEBUG:
                    printer.d(log);
                    break;
                case LogLevel.INFO:
                    printer.i(log);
                    break;
                case LogLevel.WARN:
                    printer.w(log);
                    break;
                case LogLevel.ERROR:
                    printer.e(log);
                    break;
            }
        }
    }

    private static List<StackTraceElement> getStackTrace(Class<?> offsetClass, StackTraceElement[] userSet) {
        StackTraceElement[] traceElements = (userSet == null || userSet.length == 0)?Thread.currentThread().getStackTrace():userSet;
        // 偏移位
        String targetOffsetName = offsetClass == null? LogBuilder.class.getName():offsetClass.getName();
        StackTraceElement nextTraceElement;
        int targetOffset = -1;
        int nextOffset = -1;

        List<StackTraceElement> linkStackTraces = new ArrayList<>();
        while (++nextOffset < traceElements.length && (nextTraceElement = traceElements[nextOffset]) != null) {
            if(nextTraceElement.getClassName().equals(targetOffsetName)) {
                targetOffset = nextOffset;
            } else if(targetOffset >= 0) {
                linkStackTraces.add(nextTraceElement);
            }
        }
        return linkStackTraces;
    }
}
