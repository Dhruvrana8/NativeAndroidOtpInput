package me.dhruv.otpinput;

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import me.dhruv.otpinput.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val otpDigit1: EditText = findViewById(R.id.otpDigit1)
        val otpDigit2: EditText = findViewById(R.id.otpDigit2)
        val otpDigit3: EditText = findViewById(R.id.otpDigit3)
        val otpDigit4: EditText = findViewById(R.id.otpDigit4)
        val submitBtn: Button = findViewById(R.id.submitBtn)

        submitBtn.setOnClickListener {
            val otpInput =
                otpDigit1.text.toString() +
                        otpDigit2.text.toString() +
                        otpDigit3.text.toString() +
                        otpDigit4.text.toString()

            // Validate OTP length (4 digits)
            if (otpInput.length == 4) {
                // Process the OTP (You can add your logic here)
                showToast("OTP submitted: $otpInput")
            } else {
                showToast("Invalid OTP length. Please enter a 4-digit OTP.")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
