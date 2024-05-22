package com.example.savecoco

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity(), GameTask {

    private lateinit var rootLayout: LinearLayout
    private lateinit var startBtn: Button
    private lateinit var mGameView: GameView
    private lateinit var scoreTextView: TextView
    private lateinit var highestScoreTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var exitBtn: ImageButton
    private lateinit var root1: LinearLayout
    private lateinit var pandaImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("com.example.savecoco.PREFERENCES", Context.MODE_PRIVATE)

        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        scoreTextView = findViewById(R.id.score)
        highestScoreTextView = findViewById(R.id.highestScore)
        exitBtn = findViewById(R.id.exitBtn)
        root1 = findViewById(R.id.root1)
        pandaImageView = findViewById(R.id.panda)

        val highestScore = sharedPreferences.getInt("highestScore", 0)
        val currentScore = sharedPreferences.getInt("currentScore", 0)

        highestScoreTextView.text = "Highest Score: $highestScore"
        scoreTextView.text = "Current Score: $currentScore"

        mGameView = GameView(this, this)

        startBtn.setOnClickListener {
            mGameView = GameView(this@MainActivity, this@MainActivity)
            mGameView.setBackgroundResource(R.drawable.bg)
            rootLayout.addView(mGameView)
            root1.visibility = View.GONE // Hide root1 layout
            pandaImageView.visibility = View.GONE // Hide panda image view
            startBtn.visibility = View.GONE
            scoreTextView.visibility = View.GONE
            highestScoreTextView.visibility = View.GONE
            exitBtn.visibility = View.GONE
        }

        exitBtn.setOnClickListener {
            finish()
        }

    }

    override fun closeGame(mScore: Int) {
        val highestScore = sharedPreferences.getInt("highestScore", 0)
        if (mScore > highestScore) {
            sharedPreferences.edit().putInt("highestScore", mScore).apply()
            highestScoreTextView.text = "Highest Score: $mScore"
        }
        sharedPreferences.edit().putInt("currentScore", mScore).apply()
        scoreTextView.text = "Current Score: $mScore"

        rootLayout.removeView(mGameView)
        root1.visibility = View.VISIBLE
        pandaImageView.visibility = View.VISIBLE
        startBtn.visibility = View.VISIBLE
        scoreTextView.visibility = View.VISIBLE
        highestScoreTextView.visibility = View.VISIBLE
        exitBtn.visibility = View.VISIBLE
    }
}