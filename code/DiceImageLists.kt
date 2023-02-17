package se.umu.cs.dv21sln.ou1_thirty

/**
 * This class handles all of the references to the dice images of the application Thirty.
 * The class contains of 3 lists, white dice list, grey dice list and red dice list.
 *
 * Copyright 2023 Simon Lindgren (dv21sln@cs.umu.se).
 * Usage requires the author's permission.
 *
 * @author Simon Lindgren
 * @since  2023-02-10
 *
 */

class DiceImageLists {

    /*White dice list*/
    val whiteDiceList = arrayListOf<Int>(
        R.drawable.white1,
        R.drawable.white2,
        R.drawable.white3,
        R.drawable.white4,
        R.drawable.white5,
        R.drawable.white6)

    /*Red dice list*/
    val redDiceList = arrayListOf<Int>(
        R.drawable.red1,
        R.drawable.red2,
        R.drawable.red3,
        R.drawable.red4,
        R.drawable.red5,
        R.drawable.red6)

    /*Grey dice list*/
    val greyDiceList = arrayListOf<Int>(
        R.drawable.grey1,
        R.drawable.grey2,
        R.drawable.grey3,
        R.drawable.grey4,
        R.drawable.grey5,
        R.drawable.grey6)
}