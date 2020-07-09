package com.bevis.logger.formatter;

import android.text.TextUtils;
import com.bevis.logger.LogMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

class LogMessageFormatter extends Formatter<LogMessage> {
    public static final String lineSeparator = System.getProperty("line.separator");

    private static final String FLAG_HEAD =      "╔═══════════════════════════════════════════════════════════════════════════════════════";
    private static final String FLAG_PART_HEAD = "║═══════════════════════════════════════════════════════════════════════════════════════";
    private static final String FLAG_PART_TAIL = "║═══════════════════════════════════════════════════════════════════════════════════════";
    private static final String FLAG_TAIL =      "╚═══════════════════════════════════════════════════════════════════════════════════════";
    private static final String FLAG_EDGE =      "║ ";

    private static final FormatFlag MAIN_MESSAGE_FLAG =
            new FormatFlag(
                    FLAG_HEAD,
                    FLAG_TAIL,
                    FLAG_PART_HEAD,
                    FLAG_PART_TAIL,
                    FLAG_EDGE);

    private static final FormatFlag SUB_MESSAGE_FLAG =MAIN_MESSAGE_FLAG;
//            new FormatFlag(
//                    null,
//                    null,
//                    null,
//                    null,
//                    null
//            );

    private static final class FormatFlag {
        String head;
        String tail;
        String partHead;
        String partTail;
        String edge;

        public FormatFlag(String head, String tail, String partHead, String partTail, String edge) {
            this.head = head;
            this.tail = tail;
            this.partHead = partHead;
            this.partTail = partTail;
            this.edge = edge;
        }

        void setParentPadding(int padding) {
            if(padding > 0) {
                tail = handlePadding(tail, padding);
                partHead = handlePadding(partHead, padding);
                partTail = handlePadding(partTail, padding);
                head = handlePadding(head, padding);


                if(TextUtils.isEmpty(head)) {
                    edge = "";
                }
            }
        }

        private String handlePadding(String flag, int padding) {
            String newFlag = flag;
            if(flag != null) {
                if(flag.length() >= padding) {
                    newFlag = flag.substring(0, flag.length() - padding);
                } else  {
                    newFlag = "";
                }
            }
            return newFlag;
        }
    }

    @Override
    boolean accept(Object obj) {
        return obj instanceof LogMessage;
    }

    @Override
    void onFormat(FormatBundle<LogMessage> node) {
        node.onResponse(formatLogMessage(node, node.obj));
    }

    private static String formatObject(FormatBundle node, Object object) {
        return formatObject(node, false,0, object);
    }

    private static String formatObject(FormatBundle node, boolean ignoreFirstLine, int padding, Object object) {
        final FormatResult formatResult = new FormatResult("");

        if(isJSONObject(object)) {
            formatResult.setOutput(object);
        } else {
            node.onFork(object, new FormatPipe.FormatCallback() {
                @Override
                public void onResponse(Object response) {
                    formatResult.setOutput(response);
                }
            });
        }
        String formatString = formatResult.getOutputString();
        if(formatString == null) {
            formatString = "";
        }

        if(padding > 0) {
            String[] lines = formatString.split(lineSeparator);
            StringBuilder builder = new StringBuilder();
            int lingCount = 0;
            int linePadding;
            for (String line : lines) {
                if(lingCount == 0 && ignoreFirstLine) {
                    linePadding = 0;
                }  else {
                    linePadding = padding;
                }
                int formatPadding = linePadding + line.length();
                builder.append(String.format("%" + (formatPadding == 0?"":formatPadding) + "s", line));
                if (lingCount < lines.length - 1) {
                    builder.append(lineSeparator);
                }
                lingCount ++;
            }
            return builder.toString();
        } else {
            return formatString;
        }
    }

    public static String formatLogMessage(FormatBundle node, LogMessage logMessage) {
        if(logMessage == null) {
            return "";
        }

        FormatFlag  formatFlag =   new FormatFlag(
                FLAG_HEAD,
                FLAG_TAIL,
                FLAG_PART_HEAD,
                FLAG_PART_TAIL,
                FLAG_EDGE);

        if(!logMessage.isMainMessage()) {
            formatFlag = new FormatFlag(
                    null,
                    null,
                    null,
                    null,
                    null);
            formatFlag.setParentPadding(logMessage.parentPadding);
        }

        List<StackTraceElement> stackTraces = logMessage.stackTraces == null?new ArrayList<StackTraceElement>():logMessage.stackTraces;
        StringBuilder sb = new StringBuilder();

        if(formatFlag.head != null) {
            sb.append("  ")
                    .append(lineSeparator)
                    .append(formatFlag.head)
                    .append(lineSeparator);
        }

        List<String> messageBodies = new ArrayList<>();

        if(logMessage.head != null) {
            messageBodies.add(formatObject(node, logMessage.head));
        }

        Thread thread = logMessage.thread;
        String threadInfo = "";

        if(thread != null) {
            threadInfo = new StringBuilder().append("Thread [ ")
                    .append("name = ")
                    .append(thread.getName())
                    .append("; ")
                    .append("id = ")
                    .append(thread.getId())
                    .append("; ")
                    .append("priority = ")
                    .append(thread.getPriority())
                    .append("; ")
                    .append("type = ")
                    .append(thread.getClass().getName())
                    .append(" ]").toString();
        }

        StringBuilder locationBuilder = new StringBuilder();

        if(logMessage.fullStackTrace) {
            locationBuilder.append("LOCATION :")
                    .append(lineSeparator)
                    .append(lineSeparator);
        }
        messageBodies.add(
                locationBuilder
                        .append(stackTraces.isEmpty()?"tack trace is null":(logMessage.fullStackTrace?formatObject(node, stackTraces): formatObject(node, stackTraces.get(0))))
                        .append(lineSeparator)
                        .append(lineSeparator)
                        .append(threadInfo)
                        .toString()
        );

        if(logMessage.body != null) {
            List<String> formatBodies = new ArrayList<>();
            int bodyCount = 0;

            if(logMessage.body.size() == 1) {
                formatBodies.add(formatObject(node, logMessage.body.get(0)));
            } else {
                for (LogMessage.FormativeMessage body : logMessage.body) {
                    bodyCount++;
                    formatBodies.add(new StringBuilder("BODY").append(logMessage.body.size() > 1 ? "_" + bodyCount + " :" : " :")
                            .append(lineSeparator)
                            .append(lineSeparator)
                            .append(formatObject(node, body)).toString());
                }
            }
            messageBodies.addAll(formatBodies);
        }

        if(logMessage.throwable != null) {

            messageBodies.add(new StringBuilder("THROWABLE :")
                    .append(lineSeparator)
                    .append(lineSeparator)
                    .append(logMessage.throwable.toString())
                    .append(lineSeparator)
                    .append(lineSeparator)
                    .append(Formatter.format(logMessage.throwable))
                    .toString());
        }

        if(logMessage.extra != null && !logMessage.extra.isEmpty()) {
            int maxPadding = 0;
            for(String key: logMessage.extra.keySet()){
                if(key != null && key.length() > maxPadding) {
                    maxPadding = key.length();
                }
            }

            int entryCount = 0;
            StringBuilder entryBuilder = new StringBuilder("EXTRA :")
                    .append(lineSeparator)
                    .append(lineSeparator);
            String equalFlag = " = ";
            for(Map.Entry<String, Object> entry: logMessage.extra.entrySet()) {
                entryCount ++;
                String key = entry.getKey();
                Object value = entry.getValue();

                int parentPadding = maxPadding + equalFlag.length();

                if(value instanceof LogMessage) {
                    ((LogMessage) value).parentPadding += ( parentPadding + (formatFlag.edge == null?0:formatFlag.edge.length()));
                }

                entryBuilder
                        .append(String.format("%-" + maxPadding + "s", key))
                        .append(equalFlag)
                        .append(formatObject(node, true, parentPadding, value));

                if(logMessage.extra.size() != entryCount) {
                    entryBuilder.append(lineSeparator).append(lineSeparator);
                }

            }
            messageBodies.add(entryBuilder.toString());
        }

        if(logMessage.tail != null) {
            messageBodies.add(formatObject(node, logMessage.tail));
        }

        int bodyCount = 0;
        for(String messageBody: messageBodies) {
            if(bodyCount == 0) {
                appendLogMessageBody(formatFlag, false, false, sb, messageBody);
            } else {
                appendLogMessageBody(formatFlag, true, false, sb, messageBody);
            }
            bodyCount ++;
        }
        if(formatFlag.tail != null) {
            sb.append(formatFlag.tail);
        }
        return sb.toString();
    }

    private static StringBuilder appendLogMessageBody(FormatFlag formatFlag, boolean headLine, boolean tailLine, StringBuilder builder, String... messages) {
        if(builder == null || messages == null || messages.length == 0){
            return builder;
        }
        return appendLogMessageBody(formatFlag, headLine, tailLine, builder, Arrays.asList(messages));
    }

    private static StringBuilder appendLogMessageBody(FormatFlag formatFlag, boolean headLine, boolean tailLine, StringBuilder builder, List<String> messages) {
        if(builder == null || messages == null || messages.isEmpty()){
            return builder;
        }
        for(String message: messages) {
            if (message == null) {
                continue;
            }
            if (headLine && formatFlag.partHead != null) {
                builder.append(formatFlag.partHead).append(lineSeparator);
            }
            String[] lines = message.split(lineSeparator);
            for (String line : lines) {
                builder.append(formatFlag.edge == null?"":formatFlag.edge).append(line).append(lineSeparator);
            }
            if (tailLine && formatFlag.partTail != null) {
                builder.append(formatFlag.partTail);
            }
        }
        return builder;
    }
}
