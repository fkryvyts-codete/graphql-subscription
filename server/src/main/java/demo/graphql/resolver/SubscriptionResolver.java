package demo.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import demo.dto.MailMessageDto;
import demo.graphql.publisher.MailMessagePublisher;

@Service
public class SubscriptionResolver implements GraphQLSubscriptionResolver {
    private final MailMessagePublisher mailMessagePublisher;

    public SubscriptionResolver(MailMessagePublisher mailMessagePublisher) {
        this.mailMessagePublisher = mailMessagePublisher;
    }

    Publisher<MailMessageDto> mailMessages() {
        return mailMessagePublisher.getPublisher();
    }

}
