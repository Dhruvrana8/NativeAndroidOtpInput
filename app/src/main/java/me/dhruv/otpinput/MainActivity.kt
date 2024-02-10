package me.dhruv.otpinput;

import android.os.Bundle
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var otpDigit1: EditText
    private lateinit var otpDigit2: EditText
    private lateinit var otpDigit3: EditText
    private lateinit var otpDigit4: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        otpDigit1 = findViewById(R.id.otpDigit1)
        otpDigit2 = findViewById(R.id.otpDigit2)
        otpDigit3 = findViewById(R.id.otpDigit3)
        otpDigit4 = findViewById(R.id.otpDigit4)

        setEditTextListeners()
    }

    private fun setEditTextListeners() {
        setOnTextChangeListener(otpDigit1, otpDigit2)
        setOnTextChangeListener(otpDigit2, otpDigit3)
        setOnTextChangeListener(otpDigit3, otpDigit4)
        setOnTextChangeListener(otpDigit4, null)
    }

    private fun setOnTextChangeListener(currentView: EditText, nextView: EditText?) {
        currentView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do nothing
            }

            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Do nothing
            }

            override fun afterTextChanged(editable: Editable?) {
                if (editable != null && editable.isNotEmpty()) {
                    nextView?.requestFocus()
                }
            }
        })

        currentView.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                if (currentView.text.isEmpty()) {
                    val previousView = findPreviousView(currentView)
                    previousView?.requestFocus()
                    return@OnKeyListener true
                }
            }
            false
        })
    }

    private fun findPreviousView(currentView: EditText): EditText? {
        return when (currentView) {
            otpDigit2 -> otpDigit1
            otpDigit3 -> otpDigit2
            otpDigit4 -> otpDigit3
            else -> null
        }
    }
}
