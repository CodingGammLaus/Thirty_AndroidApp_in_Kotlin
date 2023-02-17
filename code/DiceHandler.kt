package se.umu.cs.dv21sln.ou1_thirty

import android.os.Parcel
import android.os.Parcelable

/**
 * This class represent the dice handler of the application Thirty. Contains array of 6 Die,
 * array of all die images, functions to get the corresponding image to the die and throw
 * functions for random throw all the dice or just the unsaved once.
 *
 * Copyright 2023 Simon Lindgren (dv21sln@cs.umu.se).
 * Usage requires the author's permission.
 *
 * @author Simon Lindgren
 * @since  2023-02-14
 *
 */

class DiceHandler(): Parcelable{

    /*The die array*/
    val diceArray = arrayListOf<Die>(
        Die(),
        Die(),
        Die(),
        Die(),
        Die(),
        Die()
    )

    /*The dice image list (white, red and grey)*/
    val diceImageList = DiceImageLists()

    /**
     * Constructor for the parcel.
     * @param parcel
     */
    constructor(parcel: Parcel) : this() {

        diceArray[0] = parcel.readParcelable(Die::class.java.classLoader)!!
        diceArray[1] = parcel.readParcelable(Die::class.java.classLoader)!!
        diceArray[2] = parcel.readParcelable(Die::class.java.classLoader)!!
        diceArray[3] = parcel.readParcelable(Die::class.java.classLoader)!!
        diceArray[4] = parcel.readParcelable(Die::class.java.classLoader)!!
        diceArray[5] = parcel.readParcelable(Die::class.java.classLoader)!!
    }

    /**
     * Get the white die image from the die value.
     * @param index The die value.
     * @return The number of the white image.
     */
    fun getWhiteDiceColor(index: Int): Int {

        return diceImageList.whiteDiceList[diceArray[index].value - 1]
    }

    /**
     * Get the red die image from the die value
     * @param index The die value
     * @return The number of the red image
     */
    fun getRedDiceColor(index: Int): Int {

        return diceImageList.redDiceList[diceArray[index].value - 1]
    }

    /**
     * Get the grey die image from the die value.
     * @param index The die value.
     * @return The number of the grey image.
     */
    fun getGreyDiceColor(index: Int): Int {

        return diceImageList.greyDiceList[diceArray[index].value - 1]
    }

    /**
     * Throw all the dice.
     */
    fun throwAllDice() {

        for(dice in diceArray) {

            dice.value = (1..6).random()
        }
    }

    /**
     * Throw the unsaved dice.
     */
    fun throwUnSavedDice() {

        for(dice in diceArray) {

            if(!dice.keep) {

                dice.value = (1..6).random()
            }
        }
    }

    /*-------------------------Parcel auto functions/methods--------------------------------------*/

    override fun writeToParcel(parcel: Parcel, flags: Int) {

        parcel.writeParcelable(diceArray[0], 0)
        parcel.writeParcelable(diceArray[1], 0)
        parcel.writeParcelable(diceArray[2], 0)
        parcel.writeParcelable(diceArray[3], 0)
        parcel.writeParcelable(diceArray[4], 0)
        parcel.writeParcelable(diceArray[5], 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DiceHandler> {
        override fun createFromParcel(parcel: Parcel): DiceHandler {
            return DiceHandler(parcel)
        }

        override fun newArray(size: Int): Array<DiceHandler?> {
            return arrayOfNulls(size)
        }
    }
}