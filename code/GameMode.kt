package se.umu.cs.dv21sln.ou1_thirty

import android.os.Parcel
import android.os.Parcelable

/**
 * This class represent a game mode of the application Thirty.
 * Each game consists of a gameId, that represent which game, gameInfo, that represent the
 * information about the game and a played boolean, that represent if the game has been played or not.
 *
 * Copyright 2023 Simon Lindgren (dv21sln@cs.umu.se).
 * Usage requires the author's permission.
 *
 * @author Simon Lindgren
 * @since  2023-02-10
 *
 */

class GameMode(val gameId: Int, val gameInfo: String): Parcelable {

    /*Boolean if played*/
    var played = false

    /**
     * Constructor for the parcel.
     */
    constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readString().toString()) {
        played = parcel.readByte() != 0.toByte()
    }

    /*-------------------------Parcel auto functions/methods--------------------------------------*/
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(gameId)
        parcel.writeString(gameInfo)
        parcel.writeByte(if (played) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GameMode> {
        override fun createFromParcel(parcel: Parcel): GameMode {
            return GameMode(parcel)
        }

        override fun newArray(size: Int): Array<GameMode?> {
            return arrayOfNulls(size)
        }
    }
}