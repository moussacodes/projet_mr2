package com.mr2.activity

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.activity.ComponentActivity

import com.mr2.R
import com.mr2.databinding.ActivityAudioRecordingBinding

import com.mr2.databinding.ActivityPreviewAudioBinding

import com.mr2.entity.Vocal
import java.io.IOException

class PreviewAudioActivity : ComponentActivity(){
    private lateinit var binding: ActivityPreviewAudioBinding

    private lateinit var vocal: Vocal
    val readVocal = "read_vocal"
    private var length: String = "";
    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var fileName: String? = null
    private var lastProgress = 0
    private val mHandler = Handler()
    private val RECORD_AUDIO_REQUEST_CODE = 101
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        binding.imgViewPlay.setOnClickListener{
            if (!isPlaying && vocal.path != null) {
                isPlaying = true
                startPlaying()
            } else {
                isPlaying = false
                stopPlaying()
            }
        }
    }




    private var runnable: Runnable = Runnable { seekBarUpdate() }

    private fun seekBarUpdate() {
        if (mPlayer != null) {
            val mCurrentPosition = mPlayer!!.currentPosition
            binding.seekBar.progress = mCurrentPosition
            lastProgress = mCurrentPosition
        }
        mHandler.postDelayed(runnable, 100)
    }

    private fun initViews(){
        if (intent.getParcelableExtra<Vocal>(readVocal) != null) {
            vocal = intent.getParcelableExtra(readVocal)!!
            binding.vocalDate.setText(vocal.date)
        }
    }

    private fun stopPlaying() {
        try {
            mPlayer!!.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mPlayer = null
        //showing the play button
        binding.imgViewPlay.setImageResource(R.drawable.play_24)
        binding.chronometer.stop()
    }
    private fun startPlaying() {
        mPlayer = MediaPlayer()
        try {
            mPlayer!!.setDataSource(vocal.path)
            mPlayer!!.prepare()
            mPlayer!!.start()
        } catch (e: IOException) {
            Log.e("LOG_TAG", "prepare() failed")
        }

        //making the imageView pause button
        binding.imgViewPlay.setImageResource(R.drawable.pause_24)

        binding.seekBar.progress = lastProgress
        mPlayer!!.seekTo(lastProgress)
        binding.seekBar.max = mPlayer!!.duration
        seekBarUpdate()
        binding.chronometer.start()

        mPlayer!!.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            binding.imgViewPlay.setImageResource(R.drawable.play_24)
            isPlaying = false
            binding.chronometer.stop()
            binding.chronometer.base = SystemClock.elapsedRealtime()
            mPlayer!!.seekTo(0)
        })

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (mPlayer != null && fromUser) {
                    mPlayer!!.seekTo(progress)
                    binding.chronometer.base = SystemClock.elapsedRealtime() - mPlayer!!.currentPosition
                    lastProgress = progress
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }
}

