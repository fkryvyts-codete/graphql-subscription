import React from "react";
import { render } from "react-dom";

import ApolloClient from "apollo-client";
import { InMemoryCache } from 'apollo-cache-inmemory';
import { ApolloProvider, Subscription } from "react-apollo";
import gql from "graphql-tag";
import { split } from 'apollo-link';
import { HttpLink } from 'apollo-link-http';
import { WebSocketLink } from 'apollo-link-ws';
import { getMainDefinition } from 'apollo-utilities';

const httpLink = new HttpLink({
  uri: 'http://localhost:8081/graphql'
});

const wsLink = new WebSocketLink({
  uri: `ws://localhost:8081/subscriptions`,
  options: {
    reconnect: true
  }
});

const link = split(
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
  subscription onRandomQuote($minimumLength: Int!) {
    randomQuote(minimumLength: $minimumLength){
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

const RandomQuote = ({minimumLength}) => (
  <Subscription query={QUOTES_SUBSCRIPTION} variables={{minimumLength}}>
    {({ data, loading }) => (
      (!loading) ? <Quote quote={data.randomQuote} /> : null
    )}
  </Subscription>
);

class FilterableRandomQuote extends React.Component {
  constructor(props) {
    super(props);
    this.state = {minimumLength: 0};
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(event) {
    this.setState({minimumLength: parseInt(event.target.value || 0, 10)});
  }

  render() {
    return (
      <div>
        <label>
          Minimum length:
          <input type="number" min={0} value={this.state.minimumLength} onChange={this.handleChange} />
        </label>
        <div>
          <RandomQuote minimumLength={this.state.minimumLength} />
        </div>
      </div>
    );
  }
}

const App = () => (
  <ApolloProvider client={client}>
    <div>
      <h2>Famous quotes</h2>
      <FilterableRandomQuote />
    </div>
  </ApolloProvider>
);

render(<App />, document.getElementById("root"));

