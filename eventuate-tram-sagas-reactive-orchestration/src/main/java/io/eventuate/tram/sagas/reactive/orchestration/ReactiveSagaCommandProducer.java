package io.eventuate.tram.sagas.reactive.orchestration;

import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.reactive.commands.producer.ReactiveCommandProducer;
import io.eventuate.tram.sagas.common.SagaCommandHeaders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReactiveSagaCommandProducer {

  private ReactiveCommandProducer commandProducer;

  public ReactiveSagaCommandProducer(ReactiveCommandProducer commandProducer) {
    this.commandProducer = commandProducer;
  }

  public Mono<String> sendCommands(String sagaType, String sagaId, List<CommandWithDestination> commands, String sagaReplyChannel) {
    return Flux
            .fromIterable(commands)
            .flatMap(command -> {
              Map<String, String> headers = new HashMap<>(command.getExtraHeaders());
              headers.put(SagaCommandHeaders.SAGA_TYPE, sagaType);
              headers.put(SagaCommandHeaders.SAGA_ID, sagaId);
              return commandProducer.send(command.getDestinationChannel(), command.getResource(), command.getCommand(), sagaReplyChannel, headers);
            })
            .next();
  }
}
