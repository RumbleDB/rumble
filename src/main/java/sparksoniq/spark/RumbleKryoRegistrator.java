/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0.
 */

package sparksoniq.spark;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import org.apache.spark.serializer.KryoRegistrator;
import org.rumbledb.exceptions.SourcePosition;
import org.rumbledb.exceptions.SourceRange;

/** Registers serializers for immutable Java record values carried by runtime plans. */
public final class RumbleKryoRegistrator implements KryoRegistrator {

    @Override
    public void registerClasses(Kryo kryo) {
        kryo.register(SourcePosition.class, new JavaSerializer());
        kryo.register(SourceRange.class, new JavaSerializer());
    }
}
