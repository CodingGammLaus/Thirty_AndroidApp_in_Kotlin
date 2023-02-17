package se.umu.cs.dv21sln.ou1_thirty

import android.os.Parcel
import android.os.Parcelable

/**
 * This class handles the model of the application Thirty. All the game data management.
 * Keep tracks of which round, the amount of throws left, the total score, the score of each
 * game mode, the current game mode and the dice cup (Array och die).
 *
 * Copyright 2023 Simon Lindgren (dv21sln@cs.umu.se).
 * Usage requires the author's permission.
 *
 * @author Simon Lindgren
 * @since  2023-02-14
 *
 */

class Model() : Parcelable{

    /*Setting to the game (set the amount of rounds and throws per round)*/
    private val amountOfRounds = 10  //Max 10
    private val totalThrows = 3

    /*Properties*/
    var diceCup = DiceHandler()
    val menuItemList = arrayListOf<String>("Low", "4", "5", "6", "7", "8", "9", "10", "11", "12")
    var scoreList: IntArray = intArrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)

    var totalScore = 0
    var throwsLeft = totalThrows
    var round = 1
    var roundScore = 0

    var currentGameId = 0
    var currentGameListIndex = 0
    var gameList = arrayListOf<GameMode>(
        GameMode(3, "Low: All dice with value of 3 or less score points"),
        GameMode(4, "4: All combinations of one or more dice that add up to 4 score points"),
        GameMode(5, "5: All combinations of one or more dice that add up to 5 score points"),
        GameMode(6, "6: All combinations of one or more dice that add up to 6 score points"),
        GameMode(7, "7: All combinations of one or more dice that add up to 7 score points"),
        GameMode(8, "8: All combinations of one or more dice that add up to 8 score points"),
        GameMode(9, "9: All combinations of one or more dice that add up to 9 score points"),
        GameMode(10, "10: All combinations of one or more dice that add up to 10 score points"),
        GameMode(11, "11: All combinations of one or more dice that add up to 11 score points"),
        GameMode(12, "12: All combinations of one or more dice that add up to 12 score points")
    )

    /**
     * Constructor for the parcel.
     * @param parcel
     */
    constructor(parcel: Parcel) : this() {

        scoreList = parcel.createIntArray()!!
        totalScore = parcel.readInt()
        throwsLeft = parcel.readInt()
        round = parcel.readInt()
        roundScore = parcel.readInt()
        currentGameId = parcel.readInt()
        currentGameListIndex = parcel.readInt()
    }

    /**
     * Set the game mode
     * @param pos The position of the game to be set.
     */
    fun setGame(pos: Int) {

        currentGameListIndex = pos
        currentGameId = gameList[pos].gameId
    }

    /**
     * Count the score of the saved (marked) dice.
     * @param diceList The dice list.
     */
    fun countPoint(diceList: ArrayList<Die>){

        roundScore = 0

        for(dice in diceList) {

            if(dice.keep) {

                roundScore += dice.value
            }
        }
    }

    /**
     * Count the score of the dice automatically (only works for the game mode Low).
     * @param diceList The dice list.
     */
    fun autoCountPoints(diceList: ArrayList<Die>) {

        roundScore = 0

        for(dice in diceList) {

            if(dice.value < 4) {

                roundScore += dice.value
                dice.keep = true
            }
        }

        totalScore += roundScore
        scoreList[0] += roundScore
    }

    /**
     * Add score to a game mode.
     * @param index The index of the game mode.
     * @param score The score to be added.
     */
    fun setScoreToScoreList(index: Int, score: Int) {

        scoreList[index] += score
    }

    /**
     * Add score to the total score.
     * @param score The score to be added.
     */
    fun addToTotalScore(score: Int) {

        totalScore += score
    }

    /**
     * New round adding one to the round property, reset the throws left counter and
     * set current game to played.
     */
    fun newRound() {

        round++
        throwsLeft = totalThrows
        gameList[currentGameListIndex].played = true
        menuItemList[currentGameListIndex] = menuItemList[currentGameListIndex] + " (Played)"
    }

    /**
     * Check if it is the last round.
     * @return True if it the last round.
     */
    fun checkIfLastRound(): Boolean {

        return round >= amountOfRounds && throwsLeft <= 0
    }

    /**
     * Reset the die.
     * @param i The die to be reset.
     */
    fun resetDiceCup(i: Int) {

        diceCup.diceArray[i].keep = false
        diceCup.diceArray[i].paired = false
        diceCup.diceArray[i].value = (i + 1)
    }

    /*-------------------------Parcel auto functions/methods--------------------------------------*/

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeIntArray(scoreList)
        parcel.writeInt(totalScore)
        parcel.writeInt(throwsLeft)
        parcel.writeInt(round)
        parcel.writeInt(roundScore)
        parcel.writeInt(currentGameId)
        parcel.writeInt(currentGameListIndex)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Model> {

        override fun createFromParcel(parcel: Parcel): Model {
            return Model(parcel)
        }

        override fun newArray(size: Int): Array<Model?> {
            return arrayOfNulls(size)
        }
    }
}