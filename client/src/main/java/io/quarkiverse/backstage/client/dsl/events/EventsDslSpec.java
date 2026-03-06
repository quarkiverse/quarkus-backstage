package io.quarkiverse.backstage.client.dsl.events;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import io.quarkiverse.backstage.client.model.ScaffolderEvent;
import io.sundr.dsl.annotations.All;
import io.sundr.dsl.annotations.Any;
import io.sundr.dsl.annotations.Dsl;
import io.sundr.dsl.annotations.EntryPoint;
import io.sundr.dsl.annotations.InterfaceName;
import io.sundr.dsl.annotations.MethodName;
import io.sundr.dsl.annotations.None;
import io.sundr.dsl.annotations.Terminal;

@Dsl
@InterfaceName("EventsDsl")
public interface EventsDslSpec {

    @EntryPoint
    void events();

    @Any(methods = { "events" })
    void forTask(String id);

    @InterfaceName("WaitingUntilPredicateInterface")
    @MethodName("waitingUntil")
    @All(methods = { "events", "forTask" })
    @None(methods = { "waitingUntilCompletion" })
    void waitingUntil(Predicate<ScaffolderEvent> predicate);

    @InterfaceName("WaitingUntilPredicateInterface")
    @MethodName("waitingUntil")
    @All(methods = { "events", "forTask" })
    @None(methods = { "waitingUntilCompletion" })
    void waitingUntil(Predicate<ScaffolderEvent> predicate, long amount, TimeUnit unit);

    @MethodName("waitingUntilCompletion")
    @All(methods = { "events", "forTask" })
    @None(methods = { "waitingUntil" })
    void waitingUntilCompletion();

    @Terminal
    @MethodName("get")
    @All(methods = { "events" })
    @Any(methods = { "forTask", "waitingUntil", "waitingUntilCompletion" })
    List<ScaffolderEvent> getEvents();
}

interface Usage {
    default void usage(EventsDsl client) {
        List<ScaffolderEvent> events = client.events().forTask("my-task-id").get();
        events = client.events().forTask("my-task-id").waitingUntilCompletion().get();
        events = client.events().forTask("my-task-id").waitingUntil(e -> "completion".equalsIgnoreCase(e.getType())).get();
        events = client.events().forTask("my-task-id")
                .waitingUntil(e -> "completion".equalsIgnoreCase(e.getType()), 5L, TimeUnit.MINUTES).get();
    }
}
