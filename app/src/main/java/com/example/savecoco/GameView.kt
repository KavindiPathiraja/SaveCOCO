package com.example.savecoco

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View

class GameView(var c :Context, var gameTask: GameTask): View(c) {

    private var myPaint: Paint? = null
    private var speed = 1;
    private var time = 0
    private var score = 0
    private var myPosition = 0
    private var otherBombs = ArrayList<HashMap<String,Any>>() // Store bombs in different lanes

    var viewWidth = 0
    var viewHeight = 0
    init{
        myPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if(time % 700 < 10 +speed){
            val map = HashMap<String,Any>()
            map["lane"] = (0..2).random()    //lane change
            map["startTime"] = time
            otherBombs.add(map)
        }

        time = time + 10 + speed
        val pandaWidth = (viewWidth / 5) * 2  // Adjust panda width
        val pandaHeight = pandaWidth + 20      // Adjust panda height
        val pandaPadding = 25                  // Adjust panda padding
        val bombWidth = viewWidth / 5
        val bombHeight = bombWidth + 10
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.panda2,null)

        d.setBounds(
            myPosition * viewWidth / 3 + viewWidth / 15 + pandaPadding,
            viewHeight - 2 - pandaHeight,
            myPosition * viewWidth / 3 + viewWidth / 15 + pandaWidth - pandaPadding,
            viewHeight - 2
        )
        d.draw(canvas!!)
        myPaint!!.color = Color.GREEN
        var highScore = 0

        // Loop through bombs
        for(i in otherBombs.indices){
            try{
                val bombX = otherBombs[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                var bombY = time - otherBombs[i]["startTime"] as Int
                val d2 = resources.getDrawable(R.drawable.spike1,null)

                d2.setBounds(
                    bombX + 25 , bombY - bombHeight , bombX + bombWidth - 25, bombY
                )
                d2.draw(canvas)
                // Check collision with panda
                if(otherBombs[i]["lane"] as Int == myPosition){
                    if(bombY > viewHeight -2 - bombHeight
                        &&bombY < viewHeight - 2){
                        gameTask.closeGame(score)
                    }
                }
                // Remove bomb if it goes off-screen
                if(bombY > viewHeight + bombHeight){
                    otherBombs.removeAt(i)
                    score++                           //increase score
                    speed = 1 + Math.abs(score / 10) //speed increase
                    if(score > highScore){
                        highScore = score
                    }
                }
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }

        // Set color and size for score and speed
        myPaint!!.color = Color.parseColor("#333333") // Dark color
        myPaint!!.textSize = 54f // Increase size

        canvas.drawText("Score : $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed : $speed", 380f, 80f, myPaint!!)
        invalidate()



    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN ->{
                val x1 = event.x
                // Move panda left or right based on touch position
                if(x1 < viewWidth/2){
                    if(myPosition>0){
                        myPosition--
                    }
                }
                if(x1 > viewWidth/2){
                    if(myPosition<2){
                        myPosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP ->{}
        }
        return true
    }
}