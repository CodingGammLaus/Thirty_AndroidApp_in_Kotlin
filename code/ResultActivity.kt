package se.umu.cs.dv21sln.ou1_thirty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import se.umu.cs.dv21sln.ou1_thirty.databinding.ResultScreenBinding

/**
 * This class handles the result screen of the application Thirty. Here you can see the result
 * of each game mode and a total score it a table. At the bottom of the screen it's a button
 * "Play again" that leads to the "main screen" (game screen) and resets the game.
 *
 * Copyright 2023 Simon Lindgren (dv21sln@cs.umu.se).
 * Usage requires the author's permission.
 *
 * @author Simon Lindgren
 * @since  2023-02-10
 *
 */


class ResultActivity : AppCompatActivity() {

    /*View binding*/
    private lateinit var binding: ResultScreenBinding

    /**
     * The onCreate function.
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ResultScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayAgainButton()
        setScore()
    }

    /**
     * Play again button initialization (resets the game).
     */
    private fun initPlayAgainButton() {

        binding.playAgainButton.setOnClickListener() {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /**
     * Set the score to each game mode in the table.
     */
    private fun setScore() {

        val scoreList: IntArray? = intent.getIntArrayExtra("scoreList")
        var totalScore = 0

        if (scoreList != null) {

            binding.scoreLow.text = scoreList[0].toString()
            binding.score4.text = scoreList[1].toString()
            binding.score5.text = scoreList[2].toString()
            binding.score6.text = scoreList[3].toString()
            binding.score7.text = scoreList[4].toString()
            binding.score8.text = scoreList[5].toString()
            binding.score9.text = scoreList[6].toString()
            binding.score10.text = scoreList[7].toString()
            binding.score11.text = scoreList[8].toString()
            binding.score12.text = scoreList[9].toString()

            for(i in 0..9) {

                totalScore += scoreList[i]
            }

            binding.scoreTotal.text = totalScore.toString()
        }
    }

    /**
     * When back button on phone is used, reset the game.
     */
    override fun onBackPressed() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}