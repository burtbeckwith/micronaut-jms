package io.micronaut.jms.bind;

import io.micronaut.core.bind.ArgumentBinder;
import io.micronaut.core.bind.ArgumentBinderRegistry;
import io.micronaut.core.type.Argument;

import javax.jms.Message;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class JMSArgumentBinderRegistry implements ArgumentBinderRegistry<Message> {

    private final List<AbstractChainedArgumentBinder> argumentBinderChain = new LinkedList<>();
    private final AbstractChainedArgumentBinder defaultArgumentBinder = new DefaultArgumentBinder();

    public JMSArgumentBinderRegistry() {
        argumentBinderChain.add(new HeaderArgumentBinder());
    }

    /***
     *
     * Adds an {@link AbstractChainedArgumentBinder} to the chain. If no argument binder is found then the
     *      default argument binder (an {@link DefaultArgumentBinder}) is used
     *
     * @param argumentBinder
     */
    public void addArgumentBinder(AbstractChainedArgumentBinder argumentBinder) {
        argumentBinderChain.add(argumentBinder);
    }

    @Override
    public <T> Optional<ArgumentBinder<T, Message>> findArgumentBinder(Argument<T> argument, Message source) {
        return Optional.of((ArgumentBinder<T, Message>) argumentBinderChain.stream()
                .filter(binder -> binder.canBind(argument))
                .findFirst()
                .orElse(defaultArgumentBinder));
    }
}
