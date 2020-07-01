package io.micronaut.jms.bind;

import io.micronaut.core.convert.ArgumentConversionContext;
import io.micronaut.core.type.Argument;
import io.micronaut.jms.serdes.DefaultSerializerDeserializer;
import io.micronaut.jms.serdes.Deserializer;
import io.micronaut.messaging.annotation.Body;

import javax.jms.Message;
import java.util.Optional;

public class BodyArgumentBinder extends AbstractChainedArgumentBinder {

    private final Deserializer deserializer = new DefaultSerializerDeserializer();

    @Override
    public BindingResult<Object> bind(ArgumentConversionContext<Object> context, Message source) {
        return () -> Optional.of(deserializer.deserialize(source, context.getArgument().getType()));
    }

    @Override
    public boolean canBind(Argument<?> argument) {
        return argument.isDeclaredAnnotationPresent(Body.class);
    }
}