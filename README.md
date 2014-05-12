mahjong
=======
Read Me
http://huan-kang.appspot.com/MahJong.html
Zhao, Pinji / Kang, Huan
MahJong is a popular game among East Asia. Typically speaking, it's a 4-player game.
However, because of the platform that we are working on, our MahJong has added in the following functionality:
1.Support 4-player mode
2.Support 2-player mode
3.Support AI mode (Two players)
1.	Introduction of the basic operations:
Pick Up: To fetch a tile from the tile at wall and put it into the tiles at hand.(This step will be skipped during the game, the computer
will automatically pick up a tile for the player when it comes to pick up turn)
Discard: To fetch a tile from the tile at hand and put it into the tiles used (visible to all players).
Before discarding, the player can arrange his/her tiles at hand at will.
Hu: In the most common case, to complete a certain combo of 14 tiles as follow.
  a1, a1, a1,   b3, b4, b5,   c4, c5, c6,   w0, w0, w0,   e0, e0 (a pair)
Gang: Having three same tiles at hand, when another player discards the fourth same one, you may AT ONCE fetch it and put the all four into the tiles declared (visible to all players). After Gang, you pick up a tile.
If a player can not gang, we will skip the round for the player and the machine will automatically refuseGang for the player
Peng: Having two same tiles at hand, when another player discards the third same one, you may AT ONCE fetch it and put the all three into the tiles declared (visible to all players). After Peng, you discard a tile.
If a player can not peng, we will skip the round for the player and the machine will automatically refusePeng for the player
Chi: Having certain two tiles at hand, when the player RIGHT BEFORE YOU discards a tile that make the three continuous (with the same Suit), you may AT ONCE fetch it and put the all three into the tiles declared (visible to all players). After Chi, you discard a tile.
If a player can not Chi, we will skip the round for the player and the machine will automatically refuseChi for the player
2.	How to win: Also means Hu before anyone else. When all the tiles you have, including tiles at hand and tiles declared, make up a certain combo, you win.
3.	Play with the game:
You will first assign as Player 1 and pick up a tile (now 14 tiles at hand). As you are the first player (no tiles used), you may only discard one tile. The discarded tile will be visible to all the players.
Then you may select Player 2 to see whether the tiles at hand can help complete any process of Hu/Gang/Peng/Chi. If there is the possibility, a button with “Yes” will appear and you may click it to process the last discarded tile. Otherwise, only your turn would be skipped since the only operation you are allowed is refuse, and the machine would automatically refuse for you.
Then you may select to Player 3 and 4 in order, since only when all high-priority processes are declined, should we allow the lower-priority processes. For example, only when all Hu is impossible or declined by the players, may we consider the possibility of Gang.
In a worst (and also common case), you will need to switch the player from 1 to 4 for 3 times, for Hu/Gang/Peng each, and click the “No” button. Then you may select Player 2 to consider a chance of Chi, since the player can only Chi a tile discarded by the player right before him/her. If no Chi processed, then Player 2 may pick up a tile as what Player 1 did at the beginning.
When any Gang/Peng/Chi completed, the combo will be visible to all the players. For example, when you have two “a1” and Peng the third “a1” discarded by another player, all the three “a1” will be visible to all the players, put next to your tiles at hand.
There are two ways to end the game: one player Hu or no more tiles at wall for picking up.
