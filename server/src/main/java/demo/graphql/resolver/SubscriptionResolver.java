package demo.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import demo.dto.QuoteDto;
import demo.graphql.publisher.QuotePublisher;

@Service
public class SubscriptionResolver implements GraphQLSubscriptionResolver {
    private final QuotePublisher quotePublisher;

    public SubscriptionResolver(QuotePublisher quotePublisher) {
        this.quotePublisher = quotePublisher;
    }

    Publisher<QuoteDto> randomQuote(Integer minimumLength) {
        return quotePublisher.getPublisher(minimumLength == null ? 0 : minimumLength);
    }

}
