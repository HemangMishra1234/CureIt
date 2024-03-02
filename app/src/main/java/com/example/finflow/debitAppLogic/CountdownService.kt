import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder

class CountdownService : Service() {

    private var timer: CountDownTimer? = null

    companion object {
        const val TIMER_UPDATED = "com.example.finflow.countdown.TIMER_UPDATED"
        const val TIMER_EXTRA = "timer_extra"
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize your timer here
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val timeInMillis = intent?.getLongExtra("timeInMillis", 0) ?: 0
        startCountdown(timeInMillis)
        return START_NOT_STICKY
    }

    private fun startCountdown(timeInMillis: Long) {
        timer = object : CountDownTimer(timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                sendTimerUpdate(secondsRemaining)
            }

            override fun onFinish() {
                sendTimerUpdate(0)
                stopSelf()
            }
        }
        timer?.start()
    }

    private fun sendTimerUpdate(secondsRemaining: Long) {
        val intent = Intent(TIMER_UPDATED)
        intent.putExtra(TIMER_EXTRA, secondsRemaining)
        sendBroadcast(intent)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
