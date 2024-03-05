package org.rumbledb.context;

import java.io.Serializable;

import org.rumbledb.config.RumbleRuntimeConfiguration;
import org.rumbledb.exceptions.ExceptionMetadata;
import org.rumbledb.exceptions.OurBadException;
import org.rumbledb.expressions.ExecutionMode;
import org.rumbledb.types.SequenceType;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class RuntimeStaticContext implements Serializable, KryoSerializable {
    private static final long serialVersionUID = 1L;

    private RumbleRuntimeConfiguration configuration;
    private SequenceType staticType;
    private ExecutionMode executionMode;
    private ExceptionMetadata metadata;

    public RuntimeStaticContext() {
        this.configuration = null;
        this.staticType = null;
        this.executionMode = null;
        this.metadata = null;
    }

    public RuntimeStaticContext(
            RumbleRuntimeConfiguration configuration,
            SequenceType staticType,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        this.configuration = configuration;
        this.staticType = staticType;
        this.executionMode = executionMode;
        this.metadata = metadata;
    }

    public RuntimeStaticContext(
            RumbleRuntimeConfiguration configuration,
            ExecutionMode executionMode,
            ExceptionMetadata metadata
    ) {
        this.configuration = configuration;
        this.staticType = null;
        this.executionMode = executionMode;
        this.metadata = metadata;
    }

    public RumbleRuntimeConfiguration getConfiguration() {
        return this.configuration;
    }

    public SequenceType getStaticType() {
        if (this.staticType == null) {
            throw new OurBadException("Clauses do not have static types.");
        }
        return this.staticType;
    }

    public ExecutionMode getExecutionMode() {
        return this.executionMode;
    }

    public void setExecutionMode(ExecutionMode mode) {
        this.executionMode = mode;
    }

    public ExceptionMetadata getMetadata() {
        return this.metadata;
    }

	@Override
	public void write(Kryo kryo, Output output) {
		kryo.writeObject(output, this.configuration);
		kryo.writeObject(output, this.staticType);
		kryo.writeObject(output, this.executionMode);
		kryo.writeObject(output, this.metadata);
	}

	@Override
	public void read(Kryo kryo, Input input) {
		this.configuration = kryo.readObject(input, RumbleRuntimeConfiguration.class);
		this.staticType = kryo.readObject(input, SequenceType.class);
		this.executionMode = kryo.readObject(input, ExecutionMode.class);
		this.metadata = kryo.readObject(input, ExceptionMetadata.class);
	}

}
