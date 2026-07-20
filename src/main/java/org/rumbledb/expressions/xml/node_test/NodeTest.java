package org.rumbledb.expressions.xml.node_test;

import com.esotericsoftware.kryo.KryoSerializable;
import org.rumbledb.context.StaticContext;
import org.rumbledb.exceptions.ExceptionMetadata;

import java.io.Serializable;

public interface NodeTest extends Serializable, KryoSerializable {
    default void resolve(StaticContext context, ExceptionMetadata metadata) {
    }
}
