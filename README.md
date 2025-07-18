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
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBADgG2ATwKZQFB2FMBLEPbAOzBgGcoA3LHfQksgCwBMFNtcCjhSKqQtLg15kWwCBnFhgAI2DlUMFrIwZUpKMnLZCxAOYwADADoAnGv1QIAVzgwoqfXnJgowfBGIYY8JGlgAWgA+CmoALhgAbQAFAHkAZQAVAF0YAHobRSgAHWIAbwAiLPRiYABbVELwwsKAGhhC7HJyAHdoFmrahsLUcuA8BC7CgF8fMKoYEJhWdkiikqgyyvqmhTaO1b6BobHfWaDQ8gFIx2dXdAAKM4AlVABHAEpx45Ap0PEISP1UMABVbKXZaoZ6+T7vZSySIAMzwxBYMEW42ACDIixgqAAHi4wORxmCJFNpq9Ios7iAOvj+G9AtMDpEZABrDQAUSg1kwvn2bCCxIiMCZrPZ0CplEmtNCiBQ6EiABYjABmPJFcrkfRddAc0nZYEC4DM4ijcaoBCKGDEVCtRHZKngiXUyLEGwIBCigQQz6RYAsFgAq5k1AUqAsUFcu0fKEwb0IpFc6keiRen1JCAGy6QA0AEQ8wFDBIgHsj0YFqY0bppdJ5pycdzxcYORKO-IWOoqVTqhWANjATBTBqNcbFjb80qgkQATEYjMriq2Vh2uz2+xoBxj4ep1xgrLZ7AgIM5vL4pQEIWLIrFEqkMooWngvDPFsDVs0NsHV0PK3MYC3Sm3n+t2jfPYZh5U8ThgPcD0uSC4TuJ4XndaZPRgH5-kBYFQ3DSEYThGMbXzYcSWtdByUpXxXgTL4o2TUtiHTWjsxkTDCSQosfRLA0EIrUJ6Qg-dYNQOsQPYQjm1nX9507bte1o99qGHY8ZRgSdpwKcSlj-BdpOXQ0xg0FhLGsOw+P0WwwHGRTDgmc9MxZAAZFkkhZNJ0hvcg72IPJFxkzjyPkz8xyjbTaPGBtiXAyCzOg-czLg0MKKQxMUN+HTLm8nTmILVicPhDiywI+0iIzDRSODLjKMiFgTVS9LaMywtq3KCAqCUYrD3je1ePyYCwr5Kh5mAj9pksicp2VPTN23YyEBxFC2yEyzT35KIAHFHJc-Q2y84LfImCFeNq3awqOCKcRW+brgeeLEI+JLUJqnaNHq7KYFhXK2vGO1wpABlaNKgzyJu5QkuhX4QCYc7KnIS5nojL0XTmqHPpY77z02yoAEliGhCATDxlJys6qtEcE2tQtAwqxPRwTqiiQAyAhSQb-OG-wlJUmdqfIWmGYmgytyM+wQEcDwlGpizWass9oniZINq2spHs8tTqYAOT-Go5PFHjicOjQGiKVW-x6imTp+81LUh1BLvgwGaVuqj7totLFdh7DXtwvL2q+03foNf7CftpMWEty5qaxnHXZUIOSeRrLUfN1oMczQmAsiIXUBFsnuREyn+u-Qow8zaoLUTzMmfFFnR1G1SDbbJPi8tJPecMncYAAKwgOEY6PCWlrz2I-ivdJqe2pdaJnCk9ygVZC81-adcV-XCkn6AZ7rovjZzn32874gQ8cG2OsD5KwAeseDUjyM3oRD6Cvjtr-dtiqT5DwvL5yhExbv7ew+xyRfBRGQHQEAyDQkcKgcsz87DSFQK-Ouf88zAyynDREcAYHdy5AlVO35gLZ15E2PO3VRTM0lBLau41jSmlavqfKmDELYMFMQNkHIqS9QIQyGhTDhSckwSQkcAQ5SKhnKqdUNRNTQEiMAsgjDVz6Q3HzKagsEAZ1gCocWo4+42Xso5ZyGQ1F+S1sJQK5Mt4OhgCAZROAACCLoYYByQWnSxUA7EFRQa0IgSgUSuifkTL8RC8GiUIeXBSZDlJjQKM3DAQA