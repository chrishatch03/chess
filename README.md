# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
[View Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=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-bnLhqTa5Q4UaYCAA)
