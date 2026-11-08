package com.ajay.upivoice

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.speech.tts.TextToSpeech
import java.util.Locale
import java.util.regex.Pattern

class PaymentNotificationService : NotificationListenerService(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var ready = false
    private var lastMessage = ""
    private var lastAt = 0L

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(applicationContext, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("hi", "IN"))
            ready = result != TextToSpeech.LANG_MISSING_DATA &&
                    result != TextToSpeech.LANG_NOT_SUPPORTED
        }
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val extras = sbn.notification.extras ?: return
        val title = extras.getCharSequence("android.title")?.toString().orEmpty()
        val text = extras.getCharSequence("android.text")?.toString().orEmpty()
        val big = extras.getCharSequence("android.bigText")?.toString().orEmpty()
        val body = listOf(title, text, big).filter { it.isNotBlank() }.distinct().joinToString(" ")
        if (body.isBlank()) return

        // Only announce notifications that look like incoming-payment alerts.
        val lower = body.lowercase(Locale.ROOT)
        val paymentWords = listOf("received", "credited", "payment received", " पैसे मिले",
            "प्राप्त", "जमा", "credited to", "money received", "has paid you")
        if (paymentWords.none { lower.contains(it) }) return

        val amount = extractAmount(body) ?: return
        val appName = try {
            packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(sbn.packageName, 0)
            ).toString()
        } catch (_: Exception) { sbn.packageName }

        val now = System.currentTimeMillis()
        val fingerprint = "${sbn.packageName}|$amount|$body"
        if (fingerprint == lastMessage && now - lastAt < 15_000) return
        lastMessage = fingerprint
        lastAt = now

        if (ready) {
            val announcement = "$amount रुपये $appName पर प्राप्त हुए।"
            tts?.speak(announcement, TextToSpeech.QUEUE_ADD, null, "upi_${now}")
        }
    }

    private fun extractAmount(text: String): String? {
        val patterns = listOf(
            Pattern.compile("(?:₹|Rs\\.?\\s*)([0-9,]+(?:\\.[0-9]{1,2})?)", Pattern.CASE_INSENSITIVE),
            Pattern.compile("([0-9,]+(?:\\.[0-9]{1,2})?)\\s*(?:रुपये|rupees)", Pattern.CASE_INSENSITIVE)
        )
        for (p in patterns) {
            val m = p.matcher(text)
            if (m.find()) return m.group(1)?.replace(",", "")
        }
        return null
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        super.onDestroy()
    }
}
