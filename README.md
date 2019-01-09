# graphql-subscription
Example project, that demonstrates usage of graphql subscriptions

This project consists of 2 folders:

`server` - contains server-related code. 
It is normal maven project that can be run from within  IntelliJ. 
If you do not have InteliJ installed you can use following bash command:  ```./mvnw spring-boot:run``` inside `server` folder.

You need to have Java at least at version 10 to be able to run server application.
In case if you have multiple java distributions installed, make sure that `JAVA_HOME` variable points towards proper distribution.
This can be done by checking output of ```echo $JAVA_HOME``` command, which should print something like this: ```/usr/lib/jvm/java-10-oracle``` 

 
`client` - client-related code. Execute ```npm install``` inside this folder to install all necessary dependencies. After that run  ```npm start``` client app.
Note, that you need to have both `nodejs` and `npm` [installed](https://nodejs.org/en/download/package-manager/) to do this.
Recommended version for nodejs is `v8.9.1` or newer. For npm it is `5.5.1` or newer.

