package com.schedulewizard
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.Toast

class AudioFocusReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
            // Handle audio becoming noisy (e.g., headphones unplugged)
            Toast.makeText(context, "Audio became noisy", Toast.LENGTH_SHORT).show()
            // Pause or stop playback as needed
        } else if (action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
            // Handle audio focus changes (e.g., another app taking focus)
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            when (audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)) {
                AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> {
                    // Audio focus gained, resume playback
                    Toast.makeText(context, "Audio focus gained", Toast.LENGTH_SHORT).show()
                    // Resume playback
                }
                AudioManager.AUDIOFOCUS_REQUEST_FAILED -> {
                    // Audio focus request failed, handle accordingly (e.g., pause or stop playback)
                    Toast.makeText(context, "Audio focus request failed", Toast.LENGTH_SHORT).show()
                    // Pause or stop playback
                }
            }
        }
    }
}
