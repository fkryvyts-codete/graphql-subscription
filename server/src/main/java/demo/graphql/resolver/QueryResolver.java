package demo.graphql.resolver;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Service;
import demo.dto.QuoteDto;

@Service
public class QueryResolver implements GraphQLQueryResolver {

    public QuoteDto hello() {
        return new QuoteDto("Hello world", "Me");
    }
}