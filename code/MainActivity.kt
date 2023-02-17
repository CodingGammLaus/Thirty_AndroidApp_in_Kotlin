package se.umu.cs.dv21sln.ou1_thirty

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import se.umu.cs.dv21sln.ou1_thirty.databinding.MainScreenBinding

/**
 * This class handles the main (game) screen of the application Thirty.
 *
 * GAME INFO: Thirty is a dice game where you roll six dice in rounds.
 * Then you get to reroll the dice you want twice, so total 3 throws/rolls per round.
 * After the three throws, the player will pair dice together to earn score.
 * The paring depends on which of the game mode is currently playing.
 * Each game mode can only be played once at each game, with a total of 10 different game modes.
 * A game consists of 10 rounds.
 *
 * CONTROLLER: Throw the dice with the throw button.
 * Click on the die to save/unsave it (red = saved, white = unsaved).
 * Pair button will be enabled when the dice have been thrown 3 times, then pair the diced to the
 * current game mode ex game mode 4, pair die with value 4, or dice with value of 4 combined.
 * Can only pair one at the time, ex if there is three 4's, you can only select one 4 at the time
 * and hit pair button.
 * The ? button explain the current game mode info.
 *
 * GAME EXAMPLE: After a round we end up with the dice (6, 4, 1, 5, 3, 4) and currently playing
 * game mode 4, we can pair a total of three 4's, by paring 4, then 4, then 3 and 1. We end up
 * with 12 points that round.
 *
 * Copyright 2023 Simon Lindgren (dv21sln@cs.umu.se).
 * Usage requires the author's permission.
 *
 * @author Simon Lindgren
 * @since  2023-02-14
 *
 */


class MainActivity : AppCompatActivity() {

    /*Model of the application, handles all the data*/
    private var model = Model()

    /*image list array*/
    private var imageList = arrayListOf<ImageView>()

    /*View binding*/
    private lateinit var binding: MainScreenBinding

    /*Adapter (array of strings) to the menu*/
    private lateinit var adapterItem: ArrayAdapter<String>

    /*Booleans to keep track of menu and pair button states*/
    private var menuEnabled = true
    private var pairEnabled = false

    /**
     * The on create function (Android)
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = MainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setThrowText()
        setStatText()
        connectImages()
        createDropMenu()
        throwButtonInit()
        pairButtonInit()
        infoButtonInit()

        /*Saved state if ex phone rotate*/
        if(savedInstanceState != null) {
            intiSavedState(savedInstanceState)
        }
    }

    /**
     * Init app with saved state values.
     * @param savedInstanceState The saved state
     */
    private fun intiSavedState(savedInstanceState: Bundle) {

        super.onRestoreInstanceState(savedInstanceState)

        model = savedInstanceState.getParcelable("model")!!
        model.diceCup = savedInstanceState.getParcelable("dice_array")!!
        model.gameList = savedInstanceState.getParcelableArrayList("game_list")!!

        binding.textStats.text = savedInstanceState.getString("stat_text")
        binding.textThrows.text = savedInstanceState.getString("throw_text")

        menuEnabled = savedInstanceState.getBoolean("menu_enabled")
        pairEnabled = savedInstanceState.getBoolean("pair_enabled")

        changeDiceImages()
        setStates()
    }

    /**
     * Create the drop down menu.
     */
    private fun createDropMenu() {

        adapterItem = ArrayAdapter(this, R.layout.list_game, model.menuItemList)
        binding.dropDownMenu.setAdapter(adapterItem)

        binding.dropDownMenu.setOnItemClickListener { _, _, pos, _ ->

            model.setGame(pos)
        }
    }

    /**
     * Connect images to Dice.
     */
    private fun connectImages() {

        imageList.add(binding.image1)
        imageList.add(binding.image2)
        imageList.add(binding.image3)
        imageList.add(binding.image4)
        imageList.add(binding.image5)
        imageList.add(binding.image6)
    }

    /**
     * Throw button initialization
     */
    private fun throwButtonInit() {

        binding.throwButton.setOnClickListener() {

            model.throwsLeft--
            setThrowText()

            model.diceCup.throwUnSavedDice()
            changeDiceImages()
        }
    }

    /**
     * Pair button initialization
     */
    private fun pairButtonInit() {

        disablePairButton()

        binding.pairButton.setOnClickListener() {

            /*If game not played*/
            if(!model.gameList[model.currentGameListIndex].played) {

                /*Pair dice (Game 4 - 12)*/
                if(model.currentGameId > 3) {

                    pairDice(it)
                }

                /*Auto points (Game Low)*/
                else {

                    autoPair()
                }
            }

            /*If game already played*/
            else {

                val snack = Snackbar.make(it, "Game already played, select another game", Snackbar.LENGTH_SHORT)
                snack.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                snack.show()
            }
        }
    }

    /**
     * Info button initialization
     */
    private fun infoButtonInit() {

        binding.infoButton.setOnClickListener() {

            val builder = AlertDialog.Builder(this)

            builder.setTitle("Game info")
            builder.setMessage(model.gameList[model.currentGameListIndex].gameInfo)
            builder.setPositiveButton("OK") {
                    _, _ -> closeContextMenu()
            }

            builder.create()
            builder.show()
        }
    }

    /**
     * Die image click initialization
     */
    private fun imageClickInit(i: Int) {

        imageList[i].setOnClickListener() {

            if(!model.diceCup.diceArray[i].keep) {

                model.diceCup.diceArray[i].keep = true
                imageList[i].setImageResource(model.diceCup.getRedDiceColor(i))
            }

            else {

                model.diceCup.diceArray[i].keep = false
                imageList[i].setImageResource(model.diceCup.getWhiteDiceColor(i))
            }
        }
    }

    /**
     * Set the correct image to each dice.
     */
    private fun changeDiceImages() {

        for(i in 0..5) {

            /*Set grey dice*/
            if(model.diceCup.diceArray[i].paired || model.throwsLeft == 3) {

                val value = model.diceCup.getGreyDiceColor(i)
                imageList[i].setImageResource(value)
                imageList[i].isEnabled = false
            }

            /*Set die grey*/
            else if(model.diceCup.diceArray[i].paired) {

                imageList[i].setImageResource(model.diceCup.getGreyDiceColor(i))
                imageList[i].isEnabled = false
            }

            /*Set die white*/
            else if(!model.diceCup.diceArray[i].keep) {

                imageList[i].setImageResource(model.diceCup.getWhiteDiceColor(i))
                imageList[i].isEnabled = true
            }


            /*Set die red*/
            else {

                imageList[i].setImageResource(model.diceCup.getRedDiceColor(i))
                imageList[i].isEnabled = true
            }

            imageClickInit(i)
        }

        checkState()
    }

    /**
     * Sets saved states to the menu and the pair button.
     */
    private fun setStates() {

        binding.menu.isEnabled = menuEnabled

        /*Set played on the already played modes*/
        for(i in 0..9) {

            if(model.gameList[i].played) {

                if(i == 0) {
                    model.menuItemList[i] = "Low (Played)"
                }

                else {
                    var k = i + 3
                    model.menuItemList[i] = "$k (Played)"
                }
            }
        }

        if(!pairEnabled) {

            disablePairButton()
        }
    }

    /**
     * Check current round state
     */
    private fun checkState() {

        /*Last round check*/
        if(model.checkIfLastRound()) {

            enablePairButton()
            binding.throwButton.setBackgroundColor(resources.getColor(R.color.light_green));

            result()
        }

        /*New round check*/
        else if(model.throwsLeft <= 0) {

            enablePairButton()

            /*Change throw button state*/
            binding.throwButton.setText(R.string.next_button)
            binding.throwButton.setBackgroundColor(resources.getColor(R.color.light_green));

            binding.throwButton.setOnClickListener() {

                if(!model.gameList[model.currentGameListIndex].played) {

                    newRound()
                }

                else {

                    val snack = Snackbar.make(it, "Game already played, select another game", Snackbar.LENGTH_SHORT)
                    snack.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    snack.show()
                }
            }
        }

        if(model.throwsLeft <= 0) {

            for(i in 0..5) {

                if(!model.diceCup.diceArray[i].paired) {

                    imageList[i].setImageResource(model.diceCup.getWhiteDiceColor(i))
                    model.diceCup.diceArray[i].keep = false
                }
            }
        }
    }

    /**
     * Auto pairing for the game Low
     */
    private fun autoPair() {

        model.autoCountPoints(model.diceCup.diceArray)
        setStatText()

        for(i in 0..5) {

            imageList[i].isEnabled = false

            if(model.diceCup.diceArray[i].keep) {

                imageList[i].setImageResource(model.diceCup.getGreyDiceColor(i))
                model.diceCup.diceArray[i].paired = true
            }
        }

        binding.menu.isEnabled = false
        disablePairButton()
    }

    /**
     * Pair dice to get point
     */
    private fun pairDice(it: View) {

        model.countPoint(model.diceCup.diceArray)

        /*Correct pairing*/
        if(model.roundScore == model.currentGameId) {

            model.addToTotalScore(model.roundScore)

            setStatText()

            for(i in 0..5) {

                if(model.diceCup.diceArray[i].keep) {

                    imageList[i].isEnabled = false
                    imageList[i].setImageResource(model.diceCup.getGreyDiceColor(i))
                    model.diceCup.diceArray[i].keep = false
                    model.diceCup.diceArray[i].paired = true
                }
            }

            binding.menu.isEnabled = false

            /*Add points to the score list and total score*/
            model.setScoreToScoreList(model.currentGameListIndex, model.roundScore)
        }

        /*Incorrect pairing*/
        else {

            val snack = Snackbar.make(it, "Invalid pairing", Snackbar.LENGTH_SHORT)
            snack.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            snack.show()

            for(i in 0..5) {

                if(model.diceCup.diceArray[i].keep) {

                    imageList[i].setImageResource(model.diceCup.getWhiteDiceColor(i))
                    model.diceCup.diceArray[i].keep = false
                }
            }
        }
    }

    /**
     * Sets up for a new round.
     */
    private fun newRound() {

        binding.throwButton.setBackgroundColor(resources.getColor(R.color.blue));
        binding.menu.isEnabled = true
        binding.throwButton.setText(R.string.throw_button)
        model.newRound()

        disablePairButton()
        setStatText()
        setThrowText()

        for(i in 0..5) {

            imageList[i].isEnabled = false
            imageList[i].setImageResource(model.diceCup.diceImageList.greyDiceList[i])
            model.resetDiceCup(i)
        }

        throwButtonInit()
    }

    /**
     * View the result
     */
    private fun result() {

        binding.throwButton.setText(R.string.result_button)

        binding.throwButton.setOnClickListener() {

            askResult()
        }
    }

    /**
     * Pop-up dialog to ask if they want to move on to the result screen.
     */
    private fun askResult() {

        val builder = AlertDialog.Builder(this)

        builder.setTitle("See Result")
        builder.setMessage("Do you want to move on to the result screen?")
        builder.setPositiveButton("YES") {_, _ ->

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("scoreList", model.scoreList)
            startActivity(intent)
            finish()
        }

        builder.setNegativeButton("NO") {_, _ ->
            closeContextMenu()
        }

        builder.create()
        builder.show()
    }

    /**
     * Set the amount o throws left text.
     */
    private fun setThrowText() {

        binding.textThrows.text = "Throws left: " + model.throwsLeft
    }

    /**
     * Set the score and round text.
     */
    private fun setStatText() {

        binding.textStats.text = "Score: " + model.totalScore +  " | Round: " + model.round
    }

    /**
     * Disable the pair button.
     */
    private fun disablePairButton() {

        binding.pairButton.isEnabled = false
        binding.pairButton.setBackgroundColor(resources.getColor(R.color.grey));
    }

    /**
     * Enable the pair button.
     */
    private fun enablePairButton() {

        binding.pairButton.isEnabled = true
        binding.pairButton.setBackgroundColor(resources.getColor(R.color.blue));
    }

    /**
     * Save current state.
     */
    override fun onSaveInstanceState(outState: Bundle) {

        super.onSaveInstanceState(outState)

        outState.putString("stat_text", binding.textStats.text.toString())
        outState.putString("throw_text", binding.textThrows.text.toString())
        outState.putParcelable("model", model)
        outState.putParcelable("dice_array", model.diceCup)
        outState.putParcelableArrayList("game_list", model.gameList)

        menuEnabled = binding.menu.isEnabled
        pairEnabled = binding.pairButton.isEnabled

        outState.putBoolean("menu_enabled", binding.menu.isEnabled)
        outState.putBoolean("pair_enabled", binding.pairButton.isEnabled)
    }

    /**
     * Only to get the menu list on eg rotation.
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {

        createDropMenu()
    }
}