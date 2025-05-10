package fr.ceri.amiibo


import android.annotation.SuppressLint
import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import fr.ceri.amiibo.GameActivity
import fr.ceri.amiibo.AmiiboQuestion
import kotlin.math.abs

class OnSwipeTouchListener(
    var context: Context? = null,
    var mainView: View? = null,
    private var gestureDetector: GestureDetector? = null
) : View.OnTouchListener {


    init {
        mainView?.setOnTouchListener(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        return gestureDetector?.onTouchEvent(p1!!)!!
    }

    class GestureListener(var context: Context, var gameActivity: GameActivity): GestureDetector.SimpleOnGestureListener() {

        companion object {
            val SWIPE_THRESHOLD = 100
            val SWIPE_VELOCITY_THRESHOLD = 100
        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var diffY = e2.y - (e1?.y ?: 0).toFloat()
            var diffX = e2.x - (e1?.x ?: 0).toFloat()
            var result = true
            if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0)
                    onSwipeRight()
                else
                    onSwipeLeft()
                result = true
            } else if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0)
                    onSwipeBottom()
                else
                    onSwipeTop()
                result = true
            }
            return result
        }

        private fun onSwipeRight() {
            gameActivity.handleSwipe("name")
        }

        private fun onSwipeLeft() {
            gameActivity.handleSwipe("gameSeries")
        }

        private fun onSwipeBottom() {
        }

        private fun onSwipeTop() {
        }
    }
}