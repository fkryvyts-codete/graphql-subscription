package demo.graphql.publisher;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.observables.ConnectableObservable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import demo.dto.QuoteDto;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class QuotePublisher {
    private static final Logger logger = LoggerFactory.getLogger(QuotePublisher.class);
    private static final Random rand = new Random();
    private static final List<QuoteDto> messages = Arrays.asList(
            new QuoteDto("Boldness be my friend", "William Shakespeare"),
            new QuoteDto("If it matters to you, you’ll find a way", "Charlie Gilkey"),
            new QuoteDto("If you’re going through hell, keep going", "Winston Churchill"),
            new QuoteDto("Persistence guarantees that results are inevitable", "Paramahansa Yogananda"),
            new QuoteDto("Go forth on your path, as it exists only through your walking", "Augustine of Hippo"),
            new QuoteDto("It does not matter how slowly you go as long as you do not stop", "Confucius"),
            new QuoteDto("The two most important days in your life are the day you are born and they day you find out why", "Mark Twain"),
            new QuoteDto("Impossible is for the unwilling", "John Keats")
    );

    private final Flowable<QuoteDto> publisher;

    public QuotePublisher() {
        Observable<QuoteDto> mailMessageObservable = Observable.create(emitter -> {
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(newMessageTick(emitter), 0, 2, TimeUnit.SECONDS);
        });

        ConnectableObservable<QuoteDto> connectableObservable = mailMessageObservable.share().publish();
        connectableObservable.connect();

        publisher = connectableObservable.toFlowable(BackpressureStrategy.BUFFER);
    }

    private Runnable newMessageTick(ObservableEmitter<QuoteDto> emitter) {
        return () -> {
            QuoteDto message = messages.get(rand.nextInt(messages.size()));
            try {
                emitter.onNext(message);
            } catch (RuntimeException e) {
                logger.error("Cannot send MailMessage", e);
            }
        };
    }

    public Flowable<QuoteDto> getPublisher() {
        return publisher;
    }

}
