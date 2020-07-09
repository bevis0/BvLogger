package com.bevis.logger;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;

class AsyncPrinterImpl implements Printer {
    private static final Handler LOG_HANDLER;

    static {
        HandlerThread thread = new HandlerThread(AsyncPrinterImpl.class.getName());
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {

            }
        });
        thread.start();
        LOG_HANDLER = new Handler(thread.getLooper());
    }

    private final Printer printer;
    public AsyncPrinterImpl(Printer printer) {
        this.printer = printer;
    }

    private static void doPrintAction(final Runnable runnable) {
        if(runnable == null) {
            return;
        }
        LOG_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void v(@NonNull final LogMessage message) {
        doPrintAction(new Runnable() {
            @Override
            public void run() {
                printer.v(message);
            }
        });
    }

    @Override
    public void d(@NonNull final LogMessage message) {
        doPrintAction(new Runnable() {
            @Override
            public void run() {
                printer.d(message);
            }
        });
    }

    @Override
    public void i(@NonNull final LogMessage message) {
        doPrintAction(new Runnable() {
            @Override
            public void run() {
                printer.i(message);
            }
        });
    }

    @Override
    public void w(@NonNull final LogMessage message) {
        doPrintAction(new Runnable() {
            @Override
            public void run() {
                printer.w(message);
            }
        });
    }

    @Override
    public void e(@NonNull final LogMessage message) {
        doPrintAction(new Runnable() {
            @Override
            public void run() {
                printer.e(message);
            }
        });
    }
}
