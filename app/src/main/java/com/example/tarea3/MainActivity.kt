package com.example.tarea3

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.GestureDetector
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {

    private lateinit var gestureDetector: GestureDetector
    private lateinit var editText: EditText
    private lateinit var textView: TextView
    private lateinit var textView2: TextView
    private var swipeEnabled = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logo = findViewById<ImageView>(R.id.logoitla)
        logo.setImageResource(R.drawable.logoitla)
        editText = findViewById(R.id.editText)
        textView2 = findViewById(R.id.textView2)
        textView = findViewById(R.id.textView3)

        gestureDetector = GestureDetector(this, SwipeGestureListener())

        val rootView = findViewById<ConstraintLayout>(R.id.rootView)
        rootView.setOnTouchListener { _, event ->
            if (swipeEnabled) { // Verificamos si el swipe está habilitado antes de procesar el evento
                gestureDetector.onTouchEvent(event)
            } else {
                false
            }
        }

        textView2.setOnTouchListener { _, event ->
            swipeEnabled = true // Habilitamos el swipe cuando se toca el textView2
            gestureDetector.onTouchEvent(event)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    private inner class SwipeGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onFling(
            downEvent: MotionEvent,
            moveEvent: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val swipeThreshold = 100
            val swipeVelocityThreshold = 100

            val diffY = moveEvent.y - downEvent.y
            val diffX = moveEvent.x - downEvent.x

            val lastLinearLayout = findViewById<LinearLayout>(R.id.lastLinearLayout)
            val lastLinearLayoutLocation = IntArray(2)
            lastLinearLayout.getLocationOnScreen(lastLinearLayoutLocation)
            val lastLinearLayoutY = lastLinearLayoutLocation[1]

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > swipeThreshold && Math.abs(velocityX) > swipeVelocityThreshold) {
                    if (diffX > 0 && downEvent.y >= lastLinearLayoutY) {
                        // Swipe de izquierda a derecha en el último LinearLayout (Salir de la app)
                        finish()
                    } else if (diffX < 0 && downEvent.y >= lastLinearLayoutY) {
                        // Swipe de derecha a izquierda en el último LinearLayout (Limpiar contenido)
                        editText.text.clear()
                        textView.text = ""
                    }
                    return true
                }
            } else {
                if (Math.abs(diffY) > swipeThreshold && Math.abs(velocityY) > swipeVelocityThreshold) {
                    if (swipeEnabled) {
                        if (diffY < 0) {
                            // Swipe de arriba hacia abajo (Cambiar color de fondo)
                            changeBgColor()
                        } else {
                            // Swipe de abajo hacia arriba (Cambiar color de fondo)
                            changeBackgroundColor()
                        }
                        swipeEnabled = false
                    }
                    return true
                }
            }

            return super.onFling(downEvent, moveEvent, velocityX, velocityY)
        }
    }

    private fun changeBackgroundColor() {
        val colors = intArrayOf(Color.BLACK, Color.GRAY, Color.WHITE)
        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
        val rootView = findViewById<ConstraintLayout>(R.id.rootView)
        rootView.background = gradientDrawable
    }

    private fun changeBgColor() {
        val colors = intArrayOf(Color.WHITE, Color.GRAY, Color.BLACK)
        val gradientDrawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors)
        val rootView = findViewById<ConstraintLayout>(R.id.rootView)
        rootView.background = gradientDrawable
    }

    @SuppressLint("SetTextI18n")
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            val amount = editText.text.toString().toIntOrNull()
            if (amount != null) {
                val hundredBills = amount / 100
                val fiftyBills = (amount % 100) / 50
                val twentyBills = ((amount % 100) % 50) / 20
                val tenCoins = ((((amount % 100) % 50) % 20) % 5) / 10
                val fiveCoins = (((amount % 100) % 50) % 20) / 5
                val cents = ((((amount % 100) % 50) % 20) % 5) % 10

                textView.text = "Billetes de 100: $hundredBills\n" +
                        "Billetes de 50: $fiftyBills\n" +
                        "Billetes de 20: $twentyBills\n" +
                        "Monedas de 10: $tenCoins\n" +
                        "Monedas de 5: $fiveCoins\n" +
                        "Centavos: $cents"
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
