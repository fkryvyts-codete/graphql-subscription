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

import 'bootstrap/dist/css/bootstrap.min.css';

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
  <div className="jumbotron">
    <div className="display-4">{quote.text}</div>
    <p>{quote.author}</p>
  </div>
);

const LoadingMessage = () => (
  <div className="alert alert-info" role="alert">
    <strong>Waiting for a new quote...</strong>
  </div>
);

const RandomQuote = ({minimumLength}) => (
  <Subscription subscription={QUOTES_SUBSCRIPTION} variables={{minimumLength}}>
    {({ data, loading }) => (
      loading ? <LoadingMessage /> : <Quote quote={data.randomQuote} />
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
        <div>
          <RandomQuote minimumLength={this.state.minimumLength} />
        </div>
        <label>
          Minimum quote length:
          <input className="form-control" type="number" min={0} value={this.state.minimumLength} onChange={this.handleChange} />
        </label>
      </div>
    );
  }
}

const App = () => (
  <ApolloProvider client={client}>
    <div className="container">
      <h3 className="text-muted">Famous quotes</h3>
      <FilterableRandomQuote />
    </div>
  </ApolloProvider>
);

render(<App />, document.getElementById("root"));

