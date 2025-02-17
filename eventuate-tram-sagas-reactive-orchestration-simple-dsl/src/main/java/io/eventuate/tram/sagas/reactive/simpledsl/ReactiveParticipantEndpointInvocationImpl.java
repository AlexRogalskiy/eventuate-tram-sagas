package io.eventuate.tram.sagas.reactive.simpledsl;

import io.eventuate.tram.commands.common.Command;
import io.eventuate.tram.commands.common.CommandReplyOutcome;
import io.eventuate.tram.commands.common.ReplyMessageHeaders;
import io.eventuate.tram.commands.consumer.CommandWithDestination;
import io.eventuate.tram.messaging.common.Message;
import io.eventuate.tram.sagas.simpledsl.CommandEndpoint;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public class ReactiveParticipantEndpointInvocationImpl<Data, C extends Command> extends AbstractReactiveParticipantInvocation<Data> {


  private final CommandEndpoint<C> commandEndpoint;
  private final Function<Data, Publisher<C>> commandProvider;

  public ReactiveParticipantEndpointInvocationImpl(Optional<Predicate<Data>> invocablePredicate,
                                                   CommandEndpoint<C> commandEndpoint,
                                                   Function<Data, Publisher<C>> commandProvider) {
    super(invocablePredicate);
    this.commandEndpoint = commandEndpoint;
    this.commandProvider = commandProvider;
  }

  @Override
  public boolean isSuccessfulReply(Message message) {
    return CommandReplyOutcome.SUCCESS.name().equals(message.getRequiredHeader(ReplyMessageHeaders.REPLY_OUTCOME));
  }

  @Override
  public Publisher<CommandWithDestination> makeCommandToSend(Data data) {
    return Mono
            .from(commandProvider.apply(data))
            .map(cmd -> new CommandWithDestination(commandEndpoint.getCommandChannel(), null, cmd));
  }
}
