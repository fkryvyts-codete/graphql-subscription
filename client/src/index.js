import React from "react";
import { render } from "react-dom";

import ApolloClient from "apollo-client";
import { InMemoryCache } from 'apollo-cache-inmemory';
import { ApolloProvider, Query, Subscription } from "react-apollo";
import gql from "graphql-tag";
import { split } from 'apollo-link';
import { HttpLink } from 'apollo-link-http';
import { WebSocketLink } from 'apollo-link-ws';
import { getMainDefinition } from 'apollo-utilities';

// Create an http link:
const httpLink = new HttpLink({
  uri: 'http://localhost:8081/graphql'
});

// Create a WebSocket link:
const wsLink = new WebSocketLink({
  uri: `ws://localhost:8081/subscriptions`,
  options: {
    reconnect: true
  }
});

// using the ability to split links, you can send data to each link
// depending on what kind of operation is being sent
const link = split(
  // split based on operation type
  ({ query }) => {
    const { kind, operation } = getMainDefinition(query);
    return kind === 'OperationDefinition' && operation === 'subscription';
  },
  wsLink,
  httpLink,
);

const client = new ApolloClient({
  link: link,
  cache: new InMemoryCache()
});

const QUOTES_SUBSCRIPTION = gql`
  subscription onRandomQuote {
    randomQuote{
      text
      author
    }
  }`;

const Quote = ({quote}) => (
  <div>
    <h3>{quote.text}</h3>
    <h4>{quote.author}</h4>
  </div>
);

const QuotesList = () => (
  <Subscription query={QUOTES_SUBSCRIPTION}>
    {({ data, loading }) => (
      (!loading) ? <Quote quote={data.randomQuote} /> : null
    )}
  </Subscription>
);

const App = () => (
  <ApolloProvider client={client}>
    <div>
      <h2>Famous quotes</h2>
      <QuotesList />
    </div>
  </ApolloProvider>
);

render(<App />, document.getElementById("root"));
