package me.dhruv.otpinput;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import android.content.IntentFilter
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var otpDigit1: EditText
    private lateinit var otpDigit2: EditText
    private lateinit var otpDigit3: EditText
    private lateinit var otpDigit4: EditText

    private lateinit var smsReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        otpDigit1 = findViewById(R.id.otpDigit1)
        otpDigit2 = findViewById(R.id.otpDigit2)
        otpDigit3 = findViewById(R.id.otpDigit3)
        otpDigit4 = findViewById(R.id.otpDigit4)

        setEditTextListeners()

        // Initialize SMS receiver
        smsReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
                    val extras = intent.extras
                    val smsMessage = extras?.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    Log.d("MainActivity", "SMS retrieval started successfully smsMessage ${smsMessage}")

                    // Extract the OTP from the SMS message and fill the EditTexts
                    handleOtpReceived(smsMessage)
                }
            }
        }

        // Register SMS receiver
        registerReceiver(smsReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))

        // Start SMS retrieval
        startSmsRetrieval()
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

    private fun startSmsRetrieval() {
        val client = SmsRetriever.getClient(this)
        val task = client.startSmsRetriever()

        task.addOnSuccessListener {
            // SMS retrieval has started successfully
            Log.d("MainActivity", "SMS retrieval started successfully")
        }

        task.addOnFailureListener {
            // SMS retrieval failed
            Log.e("MainActivity", "SMS retrieval failed")
        }
    }

    private fun handleOtpReceived(smsMessage: String) {
        val otp = extractOtp(smsMessage)
        Log.d("MainActivity", "Received OTP: $otp")
        fillOtpInEditText(otp)
    }

    private fun extractOtp(smsMessage: String): String {
        Log.d("MainActivity", "Received SMS: $smsMessage")

        // Implement your logic to extract the OTP from the SMS message
        // This may involve parsing the message or using a regular expression
        val pattern = Regex("\\b(\\d{4})\\b")  // Adjust to match the length of your OTP
        val matchResult = pattern.find(smsMessage)
        return matchResult?.groupValues?.get(1) ?: ""
    }

    private fun fillOtpInEditText(otp: String) {
        otpDigit1.setText(otp.getOrNull(0).toString())
        otpDigit2.setText(otp.getOrNull(1).toString())
        otpDigit3.setText(otp.getOrNull(2).toString())
        otpDigit4.setText(otp.getOrNull(3).toString())
    }


    private fun findPreviousView(currentView: EditText): EditText? {
        return when (currentView) {
            otpDigit2 -> otpDigit1
            otpDigit3 -> otpDigit2
            otpDigit4 -> otpDigit3
            else -> null
        }
    }

    override fun onDestroy() {
        // Unregister SMS receiver
        unregisterReceiver(smsReceiver)
        super.onDestroy()
    }
}

