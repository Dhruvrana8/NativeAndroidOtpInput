package me.dhruv.otpinput;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
            // Extract and handle the OTP from the SMS message
            val otp = extractOtpFromSms(intent)
            // Fill the OTP in your UI elements
            fillOtpInEditText(otp)
        }
    }

    private fun extractOtpFromSms(intent: Intent): String {
        // Assume OTP is a 6-digit number for simplicity
        val smsBundle = intent.extras
        val messages = smsBundle?.get("pdus") as Array<*>
        val sb = StringBuilder()

        for (message in messages) {
            val smsMessage = SmsMessage.createFromPdu(message as ByteArray)
            sb.append(smsMessage.messageBody)
        }

        // Extract only the digits from the SMS body
        val otpDigits = sb.toString().replace("[^0-9]".toRegex(), "")

        // Assume OTP is a 6-digit number
        return otpDigits.substring(0, minOf(otpDigits.length, 6))
    }

    private fun fillOtpInEditText(otp: String) {
        // Implement logic to fill OTP in your UI elements
        // This might involve setting text in EditText fields
        // or triggering the OTP input process

    }
}
