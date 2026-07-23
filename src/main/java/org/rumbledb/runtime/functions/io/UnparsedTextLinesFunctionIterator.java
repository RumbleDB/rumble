package org.rumbledb.runtime.functions.io;

import org.apache.spark.api.java.JavaRDD;
import org.rumbledb.api.Item;
import org.rumbledb.context.DynamicContext;
import org.rumbledb.context.RuntimeStaticContext;
import org.rumbledb.items.parsing.StringToStringItemMapper;
import org.rumbledb.runtime.RDDRuntimeIterator;
import org.rumbledb.runtime.RuntimeIterator;

import sparksoniq.spark.SparkSessionManager;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UnparsedTextLinesFunctionIterator extends RDDRuntimeIterator {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final Pattern LINE_SPLIT_PATTERN = Pattern.compile("\r\n|\r|\n");

    public UnparsedTextLinesFunctionIterator(
            List<RuntimeIterator> arguments,
            RuntimeStaticContext staticContext
    ) {
        super(arguments, staticContext);
    }

    @Override
    public JavaRDD<Item> getRDDAux(DynamicContext context) {
        RuntimeIterator hrefIterator = this.getChild(0);
        Item hrefItem = hrefIterator.materializeFirstItemOrNull(context);
        if (hrefItem == null) {
            return SparkSessionManager.getInstance()
                .getJavaSparkContext()
                .emptyRDD();
        }
        String encoding = null;
        if (this.getNumberOfChildren() == 2) {
            Item encodingItem = this.getChild(1).materializeFirstItemOrNull(context);
            encoding = encodingItem.getStringValue();
        }

        String text = UnparsedTextReader.read(
            this.staticContext.getStaticURI(),
            hrefItem.getStringValue(),
            encoding,
            context.getRumbleRuntimeConfiguration(),
            getConfiguration().getXmlVersion(),
            getMetadata()
        );

        String[] split = LINE_SPLIT_PATTERN.split(text, -1);
        List<String> lines = new ArrayList<>(split.length);
        int last = split.length - 1;
        for (int i = 0; i < split.length; i++) {
            if (i == last && split[i].isEmpty()) {
                continue;
            }
            lines.add(split[i]);
        }

        return SparkSessionManager.getInstance()
            .getJavaSparkContext()
            .parallelize(lines)
            .mapPartitions(new StringToStringItemMapper());
    }
}
