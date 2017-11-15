# binance_arbitrage

The program uses Binance's websocket API to look for triangle arbitrage opportunities between BTC, ETH, and BNB. 

It has been tested on Java 1.8 SE on MacOS. 

To run the executable .jar included in the repo simply type ```java -jar bnbbtceth.jar``` after unzipping

To build the project with Apache Maven (3.5.0):

- unzip and navigate to the folder before typing ```mvn package```
- navigate to /binance_arbitrage_master/target/
- run Arbitrage-0.0.1-SNAPSHOT-jar-with-dependencies.jar as an executable .jar

You will know the program is working if it starts printing currency-swap information after a few moments.

To turn off "losing yield" statements, comment out Utils.java line 121 in your favorite Java IDE and build it again
