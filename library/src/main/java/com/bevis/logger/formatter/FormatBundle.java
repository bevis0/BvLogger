package com.bevis.logger.formatter;

import java.util.LinkedList;
import java.util.List;

/**
 * 转换包裹类
 * 每个转换器构成一个转换节点{@link Formatter}, 转换一个对象如果需要进行递归转换（上级转换节点产物还是无法直接使用【格式化产物要求是 JSONObject
 * JSONArray 或 String 】，需要给其他节点处理的，执行 {@link #onNext(Object)})，则会形成转换管道{@link FormatPipe}，进行链式转换。
 * 中间产物通过 {@link #onFork(Object, FormatPipe.FormatCallback)} 进行处理，将分支转换管道的产物，归并到核心产物中。该转换结构参数 树状结构处理
 * @param <T> 需要转换的类型信息
 */
public class FormatBundle<T> {
    static final List<Formatter> FORMATTER = new LinkedList<>();
    static {
        FORMATTER.add(new NullFormatter());
        FORMATTER.add(new LogMessageFormatter());
        FORMATTER.add(new ArrayFormatter());
        FORMATTER.add(new BundleFormatter());
        FORMATTER.add(new ThrowableFormatter());
        FORMATTER.add(new StackTraceElementFormatter());
        FORMATTER.add(new StackTraceElementsFormatter());
        FORMATTER.add(new MapFormatter());
        FORMATTER.add(new MapEntryFormatter());
        FORMATTER.add(new JSONStringFormatter());
        FORMATTER.add(new JSONFormatter());
        FORMATTER.add(new FormativeMessageFormatter());
        FORMATTER.add(new IterableFormatter());
        FORMATTER.add(new ObjectFormatter());
    }

    private FormatContext context;
    public final T obj;
    private final FormatPipe pipe;
    Object output;

    public FormatBundle(T obj) {
        this((FormatBundle)null, obj);
    }

    private FormatBundle(FormatBundle parent,
                         T obj) {
        if(parent == null) {
            this.context = FormatContext.create();
            this.pipe = new FormatPipe(this.context, FORMATTER);
        } else {
            this.context = parent.context;
            this.pipe = parent.pipe;
        }
        this.obj = obj;
    }

    private FormatBundle(FormatPipe pipe,
                         T obj) {
        if(pipe == null) {
            this.context = FormatContext.create();
            this.pipe = new FormatPipe(this.context, FORMATTER);
        } else {
            this.context = pipe.context;
            this.pipe = pipe;
        }
        this.obj = obj;
    }

    FormatPipe getPipe() {
        return pipe;
    }

    FormatContext getContext(){
        return context;
    }

    private Formatter findFormatter(Object object) {
        Formatter targetFormatter = null;
        List<Formatter> allFormatter = getPipe().allFormatter();
        for(Formatter formatter: allFormatter) {
            if(formatter.accept(object)) {
                targetFormatter = formatter;
                break;
            }
        }
        return targetFormatter;
    }

    /**
     * 开始转换该包裹对象
     */
    void format(){
        Formatter targetFormatter = findFormatter(obj);
        targetFormatter.onFormat(this);
    }

    /**
     * 如果当前处理节点没办法处理，需要抛给其他节点处理
     * {@link #onResponse(Object)}  和 {@link #onNext(Object)} 必须调用其中一个
     */
    public FormatBundle onNext(Object obj){
        Formatter targetFormatter = findFormatter(obj);
        FormatBundle node = new FormatBundle(this, obj);
        targetFormatter.onFormat(node);
        return node;
    }

    /**
     * 转换器是1:1的转换，如果需要进行1:N的转换，并且最终产出1的情况：
     * 比如  List 对象， 需要单独对每个元素进行 转换，并将转换结果集成到 JSAONArray 中时，则需要使用该方法，将最终的处理结果
     * {@link FormatPipe.FormatCallback} 最终集成到主转换节点中。
     *
     * 使用该方法会启动一个新的转换管道{@link FormatPipe}，但是上下文属于同一个。
     */
    public FormatBundle onFork(Object obj, FormatPipe.FormatCallback formatCallback){
        if(formatCallback == null)  {
            return onNext(obj);
        } else {
            FormatPipe childPipe = pipe.forkPipe();
            childPipe.callResponse(formatCallback);
            Formatter targetFormatter = findFormatter(obj);
            FormatBundle childNode = new FormatBundle(childPipe, obj);
            targetFormatter.onFormat(childNode);
            return childNode;
        }
    }

    /**
     * 最终转换结果响应
     * 无论转换结果如何，都需要将最终结果进行汇报，否则抛给兜底装换器处理 {@link ObjectFormatter}
     * @param obj
     */
    public void onResponse(Object obj) {
        if(Formatter.isOriginalFormativeObject(obj)) {
            this.output = obj;
            if (pipe != null) {
                pipe.onResponse(this);
            }
        } else  {
            throw new RuntimeException("onResponse only accepts types JSONObject, JSONArray, and String");
        }
    }
}
