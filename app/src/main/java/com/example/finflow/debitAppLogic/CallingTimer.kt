import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.CountDownTimer
import android.widget.Toast

class CountdownManager(private val context: Context) {

    private var timer: CountDownTimer? = null

    companion object {
        const val TIMER_UPDATED = "com.example.finflow.countdown.TIMER_UPDATED"
        const val TIMER_EXTRA = "timer_extra"
    }

    private val countdownReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val secondsRemaining = intent?.getLongExtra(TIMER_EXTRA, 0) ?: 0
            // You can update the UI or perform any other actions here if needed
        }
    }

    fun startCountdown(timeInMillis: Long) {
        val countdownIntent = Intent(context, CountdownService::class.java)
        countdownIntent.putExtra("timeInMillis", timeInMillis)

        context.startService(countdownIntent)

        // Register the receiver to receive timer updates
        context.registerReceiver(countdownReceiver, IntentFilter(TIMER_UPDATED),null,null)
    }

    fun performActionsOnTimerEnd() {
        // Unregister the receiver when the actions are performed or when the activity is destroyed
        context.unregisterReceiver(countdownReceiver)

        // Perform actions on timer end
        Toast.makeText(context, "Timer is ended", Toast.LENGTH_SHORT).show()
        // TODO: Perform additional actions here
    }
}
