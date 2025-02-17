package io.eventuate.tram.sagas.reactive.simpledsl;

import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.simpledsl.StepOutcome;
import org.reactivestreams.Publisher;

import java.util.Optional;
import java.util.function.BiFunction;

public interface ReactiveSagaStep<Data> {
  boolean isSuccessfulReply(boolean compensating, Message message);

  Optional<BiFunction<Data, Object, Publisher<?>>> getReplyHandler(Message message, boolean compensating);

  Publisher<StepOutcome>  makeStepOutcome(Data data, boolean compensating);

  boolean hasAction(Data data);

  boolean hasCompensation(Data data);
}
