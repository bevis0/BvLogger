package com.bevis.logger.formatter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * 每个管道独立候选转换节点，如果需要移除其中的转换节点使用 {@link #removeAlternativeFormatter(Formatter)}
 */
public class FormatPipe {
    FormatContext context;
    FormatCallback responseCb;
    Set<FormatPipe> childPipes = new HashSet<>();
    private List<Formatter> alternativeFormatter = new LinkedList<>();

    public FormatPipe(FormatContext context, List<Formatter> allFormatter) {
        this.context = context;
        this.alternativeFormatter.addAll(allFormatter);
    }

    void callResponse(FormatCallback callback) {
        responseCb = callback;
    }

    void removeAlternativeFormatter(Formatter formatter) {
        alternativeFormatter.remove(formatter);
    }

    List<Formatter> allFormatter() {
        return new LinkedList<>(alternativeFormatter);
    }

    void onResponse(FormatBundle node) {
        if(node.output != null && responseCb != null) {
            responseCb.onResponse(node.output);
        }
    }

    public interface FormatCallback {
        void onResponse(Object response);
    }

    FormatPipe forkPipe() {
        FormatPipe childPipe = new FormatPipe(context, FormatBundle.FORMATTER);
        childPipes.add(childPipe);
        return childPipe;
    }
}
