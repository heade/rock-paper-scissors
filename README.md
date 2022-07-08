## Rock, Paper, Scissors

> Task: Write a program that plays 10 iterations of Rock, Paper, Scissors
(https://en.wikipedia.org/wiki/Rock-paper-scissors).
There should be two modes of play, one fair, where both players should play always randomly,
another unfair, where one player should always play randomly, the other should always choose
rock.
The mode should be asked at the beginning of the iterations through command line.
It should show each player's play and the outcome of every game (iteration), and at the end of
the iterations, how many games each player has won and how many were a draw. This output
should go to console or a file. This should be asked through command line as well.

Before running the application, if you need to, you can modify the configuration file to specify for example these parameters
```
#output mode: log, file
app.outputMode=file
#game mode: fair, unfair, online
app.gameMode=online

#base dir for the results
app.resultFilePath=rock-paper-scissors/results

#player names
app.firstPlayerName=Alex
app.secondPlayerName=Bob

#second player url
app.game.url=localhost:8085
```
or override them using the command line
```
mvn spring-boot:run -Dspring-boot.run.arguments="--app.outputMode=log --app.gameMode=fair"
```
## Bonus
Run the game-client application (spring-boot application) and provide 'online' game mode

## Testing
* game-client: rest controller covered with @WebFluxTest
* rock-paper-scissors: fileResultService and strategyProvider are covered with unit tests
    * MockWebServer added to mock http responses