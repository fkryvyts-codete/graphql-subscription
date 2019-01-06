package demo.graphql.publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import demo.dto.MailMessageDto;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class MailMessagePublisher {
    private static final Logger logger = LoggerFactory.getLogger(MailMessagePublisher.class);
    private static final Random rand = new Random();
    private static final List<String> messages = Arrays.asList("Cheers!", "Message", "Another message", "Another one!");

    private final Flowable<MailMessageDto> publisher;

    public MailMessagePublisher() {
        Observable<MailMessageDto> mailMessageObservable = Observable.create(emitter -> {
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(newMessageTick(emitter), 0, 2, TimeUnit.SECONDS);
        });

        ConnectableObservable<MailMessageDto> connectableObservable = mailMessageObservable.share().publish();
        connectableObservable.connect();

        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    private Runnable newMessageTick(ObservableEmitter<MailMessageDto> emitter) {
        return () -> {
            String message = messages.get(rand.nextInt(messages.size()));
            try {
                emitter.onNext(new MailMessageDto(message));
            } catch (RuntimeException e) {
                logger.error("Cannot send MailMessage", e);
            }
        };
    }

    public Flowable<MailMessageDto> getPublisher() {
        return publisher;
    }

}
