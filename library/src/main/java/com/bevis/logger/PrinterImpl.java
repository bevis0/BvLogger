package com.bevis.logger;

import android.util.Log;
import android.util.SparseArray;
import com.bevis.logger.formatter.Formatter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class PrinterImpl implements Printer {
    private final String lineSeparator = System.getProperty("line.separator");
    private final long logMaxLength = 2000;
    private final SparseArray<PrintAction> printActions = new SparseArray<>();

    @Override
    public void v(LogMessage message) {
        PrintAction printAction;
        synchronized (printActions) {
            printAction = printActions.get(LogLevel.VERBOSE);
            if(printAction == null) {
                printAction = new PrintAction() {

                    @Override
                    public void print(String tag, String message) {
                        Log.v(tag, message);
                    }
                };
                printActions.put(LogLevel.VERBOSE, printAction);
            }
        }
        print(message, printAction);
    }

    @Override
    public void d(LogMessage message) {
        PrintAction printAction;
        synchronized (printActions) {
            printAction = printActions.get(LogLevel.DEBUG);
            if(printAction == null) {
                printAction = new PrintAction() {

                    @Override
                    public void print(String tag, String message) {
                        Log.d(tag, message);
                    }
                };
                printActions.put(LogLevel.DEBUG, printAction);
            }
        }
        print(message, printAction);
    }

    @Override
    public void i(LogMessage message) {
        PrintAction printAction;
        synchronized (printActions) {
            printAction = printActions.get(LogLevel.INFO);
            if(printAction == null) {
                printAction = new PrintAction() {

                    @Override
                    public void print(String tag, String message) {
                        Log.i(tag, message);
                    }
                };
                printActions.put(LogLevel.INFO, printAction);
            }
        }
        print(message, printAction);
    }

    @Override
    public void w(LogMessage message) {
        PrintAction printAction;
        synchronized (printActions) {
            printAction = printActions.get(LogLevel.WARN);
            if(printAction == null) {
                printAction = new PrintAction() {

                    @Override
                    public void print(String tag, String message) {
                        Log.w(tag, message);
                    }
                };
                printActions.put(LogLevel.WARN, printAction);
            }
        }
        print(message, printAction);
    }

    @Override
    public void e(LogMessage message) {
        PrintAction printAction;
        synchronized (printActions) {
            printAction = printActions.get(LogLevel.ERROR);
            if(printAction == null) {
                printAction = new PrintAction() {

                    @Override
                    public void print(String tag, String message) {
                        Log.e(tag, message);
                    }
                };
                printActions.put(LogLevel.ERROR, printAction);
            }
        }
        print(message, printAction);
    }

    private interface PrintAction {
        void print(String tag, String message);
    }

    private void print(LogMessage message, PrintAction printAction) {
        if(message != null) {
            String logMessage = Formatter.format(message);//Formatter.formatLogMessage(message);
            long maxPartSize = logMaxLength;
            if(logMessage != null) {
                if(logMessage.length() <= maxPartSize) {
                    printAction.print(message.tag, logMessage);
                } else {
                    List<String> logMsgParts = splitLog(logMessage, maxPartSize);
                    if(logMsgParts != null && !logMsgParts.isEmpty()) {
                        for(String logMsgPart:logMsgParts) {
                            printAction.print(message.tag, logMsgPart);
                        }
                    }
                }
            }
        }
    }

    public List<String> splitLog(String msg, long maxPartSize) {
        // 分割行数
        List<String> lines = null;
        String[] splitLines = msg.split(lineSeparator);
        if(splitLines != null) {
            lines = Arrays.asList(splitLines);
        }
        List<String> logParts = new ArrayList<>();
        if(lines != null) {
            StringBuilder partBuilder = null;
            int lineCount = 0;
            for (String line : lines) {
                lineCount ++;
                if (line == null) {
                    line = "";
                }
                if (partBuilder == null) {
                    partBuilder = new StringBuilder();
                }
                // 如果即将加的内容 大于最大内容，则直接结算该日志
                if (partBuilder.length() + line.length() > maxPartSize) {
                    String logPart = partBuilder.toString();
                    if(logPart.length() > 0) {
                        logPart = logPart.subSequence(0, logPart.length() - lineSeparator.length()).toString();
                    }
                    if(logParts.isEmpty()) {
                        logParts.add(logPart);
                    } else {
                        logParts.add("   \n" + logPart);
                    }
                    partBuilder = new StringBuilder();
                }
                partBuilder.append(line).append(lineSeparator);

                if (partBuilder.length() >= maxPartSize || lineCount == lines.size()) {
                    String logPart = partBuilder.toString();
                    if(logPart.length() > 0) {
                        logPart = logPart.subSequence(0, logPart.length() - lineSeparator.length()).toString();
                    }
                    if(logParts.isEmpty()) {
                        logParts.add(logPart);
                    } else {
                        logParts.add("   \n" + logPart);
                    }
                    partBuilder = null;
                }
            }
        }
        return logParts;
    }
}

