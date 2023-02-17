package se.umu.cs.dv21sln.ou1_thirty

import android.os.Parcel
import android.os.Parcelable

/**
 * This class represent a die of the application Thirty. A die with a value, a boolean that
 * represent if the die is saved or not and a boolean that represent if the die have been used in
 * pairing.
 *
 * Copyright 2023 Simon Lindgren (dv21sln@cs.umu.se).
 * Usage requires the author's permission.
 *
 * @author Simon Lindgren
 * @since  2023-02-14
 *
 */

class Die(): Parcelable {

    /*The current value of the die*/
    var value = 1

    /*To keep track if teh die is saved or not*/
    var keep = false

    /*To keep track if the die have been used in pairing*/
    var paired = false

    /**
     * Constructor for the parcel.
     */
    constructor(parcel: Parcel) : this() {
        value = parcel.readInt()
        keep = parcel.readByte() != 0.toByte()
        paired = parcel.readByte() != 0.toByte()
    }

    /*-------------------------Parcel auto functions/methods--------------------------------------*/

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {

        parcel.writeInt(value)
        parcel.writeByte(if (keep) 1 else 0)
        parcel.writeByte(if (paired) 1 else 0)
    }

    companion object CREATOR : Parcelable.Creator<Die> {
        override fun createFromParcel(parcel: Parcel): Die {
            return Die(parcel)
        }

        override fun newArray(size: Int): Array<Die?> {
            return arrayOfNulls(size)
        }
    }
}