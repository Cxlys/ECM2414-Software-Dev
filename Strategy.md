### Knowledge
#### Minimum Requirements
- Card
- Player
- CardGame
- some form of Main

### Rules
#### Card Draw
Game has a set of $n$ players.
Each player is numbered $1$ to $n$. $n$ must be positive.

There are also $n$ decks of cards, again, each numbered 1 to $n$.

Each player will hold a hand of 4 cards. 
Each deck will also hold 4 cards.

Both these hands and the decks will be drawn from a central **deck** which contains $8n$ cards.

So, for a game of 4 players, there will be $8(4)$ cards $= 32$. 
**Assumption:** Each player holds a hand of 4 cards, and a deck of 4 cards.

Each **card** has a face value $v$, where $v$ is a non-negative integer.
The face value is defined using a "input pack", which has $8n$ rows.

#### The Game
If the game is not immediately won from the card draw, we continue.

The decks and players will form a ring topology. What this means is that each player has a deck to their 'left' and a deck to their 'right'.

Each round, the player picks a card from the top of the deck to their 'left', and discards a card to their 'right'.

This process continues until the first player declares that they have four cards of the same value, at which point that player has won and the game has ended.

#### The Strategy
Each player will typically prefer certain card numbers which reflect their index value; player1 will prefer 1s, player2 will prefer 2s, etc.

After drawing a 'left' card, a player will discard one of their cards.
The card they discard must display a value which is **not** of their preferred denomination.

Additionally, a player must not hold onto a non-preferred definition indefinitely, so you must implement our `Player` class to represent this.

#### The Solution
We must implement an executable class called CardGame, whose main method requests via command line:
1. The number of players in the game
2. The location of a **valid input pack**.

After reading in the input pack, the CardGame class should distribute the hands to the players, fill the decks and start the required threads for the players.

If the pack is invalid, the program should inform the user of this, and request a valid pack file.

If a player does not start with a winning hand, as a player processes their hand, each of their actions should be printed to an output file named after that particular player (i.e. `player1_output.txt`).

At the start of the game, the output file should note the initial card sequence.
```Output
player1 initial hand 1 1 2 3
```

Then, we continue to note the actions taken by the player.
```Output
player1 draws a 4 from deck 1
player1 discards a 3 to deck 2
player1 current hand is 1 1 2 4
```

Note that the game should **not start** without valid inputs, and each player should have **its own output file**.

At the end of the game, the last lines of the file should read either:
```
player1 wins
player1 exists
player1 final hand: 1 1 1 1
```
If player1 wins, or if another player wins:
```
player3 has informed player1 that player3 has won
player1 exists
player1 hand: 2 2 3 5
```

There should also be a message printed to the terminal window when a player wins, i.e. if the 4th player wins, `player4 wins`.

There should only be one player declaring it has won for any single game.
If the game is won instantly, the output files should still be written for all players.

In addition to player output files, there should be $n$ deck output files written at the end of the game. It should be named `deckn_output.txt` (replacing $n$ with the deck number) which should contain just one line of text detailing the contents of the deck at the **end of the game**.
```Output
deck2 contents: 1 3 3 7
```

The combination of a card draw and a discard should be treated as a single atomic action - should be done at the same time.

Therefore, at the end of the game every player should hold 4 cards.