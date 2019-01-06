package demo.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;
import demo.dto.MailMessageDto;

@Service
public class QueryResolver implements GraphQLQueryResolver {

    public MailMessageDto hello() {
        return new MailMessageDto("Hello world");
    }
}