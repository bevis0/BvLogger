package com.bevis.logger;

public final class Logger {

    private String mTag;
    private Printer mPrinter;
    private int mMaxLevel = LogLevel.ALL;
    private Class mStackOffset;

    public Logger() {
        this(null);
    }

    public Logger(Printer printer) {
        mPrinter = printer == null? new PrinterImpl():printer;
    }

    public Logger setStackOffset(Class stackOffset) {
        this.mStackOffset = stackOffset;
        return this;
    }

    public Logger setMaxLevel(@LogLevel int level) {
        mMaxLevel = level;
        return this;
    }

    public Logger setTag(String tag) {
        mTag= tag;
        return this;
    }

    public LogBuilder v() {
        return v(mTag);
    }

    public LogBuilder d() {
        return d(mTag);
    }

    public LogBuilder i() {
        return i(mTag);
    }

    public LogBuilder w() {
        return w(mTag);
    }

    public LogBuilder e() {
        return e(mTag);
    }


    public LogBuilder v(String tag) {
        return new LogBuilder(tag, LogLevel.VERBOSE, mPrinter, mStackOffset).setMaxLevel(mMaxLevel);
    }

    public LogBuilder d(String tag) {
        return new LogBuilder(tag, LogLevel.DEBUG, mPrinter, mStackOffset).setMaxLevel(mMaxLevel);
    }

    public LogBuilder i(String tag) {
        return new LogBuilder(tag, LogLevel.INFO, mPrinter, mStackOffset).setMaxLevel(mMaxLevel);
    }

    public LogBuilder w(String tag) {
        return new LogBuilder(tag, LogLevel.WARN, mPrinter, mStackOffset).setMaxLevel(mMaxLevel);
    }

    public LogBuilder e(String tag) {
        return new LogBuilder(tag, LogLevel.ERROR, mPrinter, mStackOffset).setMaxLevel(mMaxLevel);
    }


    // 静态工具
    private static final Logger INSTANCE = new Logger();

    public static void tag(String tag) {
        INSTANCE.setTag(tag);
    }

    public static void maxLevel(int level) {
        INSTANCE.setMaxLevel(level);
    }

    public static LogBuilder verbose() {
        return INSTANCE.v().setStackOffset(null);
    }

    public static LogBuilder debug() {
        return INSTANCE.d().setStackOffset(null);
    }

    public static LogBuilder info() {
        return INSTANCE.i().setStackOffset(null);
    }

    public static LogBuilder warn() {
        return INSTANCE.w().setStackOffset(null);
    }

    public static LogBuilder error() {
        return INSTANCE.e().setStackOffset(null);
    }

    public static void verbose(String msg, Object... args) {
        INSTANCE.v().setStackOffset(Logger.class).addBody(msg, args).print();
    }

    public static void debug(String msg, Object... args) {
        INSTANCE.d().setStackOffset(Logger.class).addBody(msg, args).print();
    }

    public static void info(String msg, Object... args) {
        INSTANCE.i().setStackOffset(Logger.class).addBody(msg, args).print();
    }

    public static void warn(String msg, Object... args) {
        INSTANCE.w().setStackOffset(Logger.class).addBody(msg, args).print();
    }

    public static void warn(Throwable throwable) {
        INSTANCE.w().setStackOffset(Logger.class).setThrowable(throwable).print();
    }

    public static void warn(Throwable throwable, String msg, Object... args) {
        INSTANCE.w().setStackOffset(Logger.class).addBody(msg, args).setThrowable(throwable).print();
    }

    public static void error(String msg, Object... args) {
        INSTANCE.e().setStackOffset(Logger.class).addBody(msg, args).print();
    }

    public static void error(Throwable throwable) {
        INSTANCE.e().setStackOffset(Logger.class).setThrowable(throwable).print();
    }

    public static void error(Throwable throwable, String msg, Object... args) {
        INSTANCE.e().setStackOffset(Logger.class).addBody(msg, args).setThrowable(throwable).print();
    }

}
