/*package com.mr2.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.Manifest
import android.content.ContentValues
import android.media.MediaRecorder
import android.os.Environment
import android.provider.MediaStore
import com.mr2.R
import com.mr2.databinding.ActivityAudioRecordingBinding
import com.mr2.databinding.ActivityMainBinding
import com.mr2.method.DataHelper
import java.util.Date
import java.util.Timer
import java.util.TimerTask

class AudioRecordingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioRecordingBinding
    lateinit var dataHelper: DataHelper
    lateinit var mr: MediaRecorder
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioRecordingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataHelper = DataHelper(applicationContext)

        val path = Environment.getExternalStorageDirectory().toString()+"/voice_note.3gp"
        mr = MediaRecorder()


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),111)
        }
        binding.audioStart.setOnClickListener{
            startStopAction()
            binding.audioStart.visibility = View.GONE
            binding.audioPause.visibility = View.VISIBLE
            runOnUiThread {
                // UI-related operations
                try {
                    mr.setAudioSource(MediaRecorder.AudioSource.MIC)
                    mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    mr.setAudioEncoder(MediaRecorder.AudioEncoder.AAC) // or AMR_NB
                    mr.setOutputFile(path)
                    mr.prepare()
                    mr.start()
                } catch (e: Exception) {
                    // Handle exceptions, such as IOException or IllegalStateException
                    e.printStackTrace()
                }
            }
        }

        binding.audioPause.setOnClickListener{
                mr.stop()
                resetAction()
                binding.audioPause.visibility = View.GONE
                binding.audioStart.visibility = View.VISIBLE
                startActivity(Intent(this, MainActivity::class.java))


        }

        if(dataHelper.timerCounting())
        {
            startTimer()
        }
        else
        {
            stopTimer()
            if(dataHelper.startTime() != null && dataHelper.stopTime() != null)
            {
                val time = Date().time - calcRestartTime().time
                binding.timerView.text = timeStringFromLong(time)
            }
        }

        timer.scheduleAtFixedRate(TimeTask(), 0, 500)

    }

    private inner class TimeTask : TimerTask() {
        override fun run() {
            // This code will run on a new thread
            if (dataHelper.timerCounting()) {
                val time = Date().time - dataHelper.startTime()!!.time

                // Use runOnUiThread to update UI components on the main thread
                    binding.timerView.text = timeStringFromLong(time)

            }
        }
    }


    private fun resetAction()
    {
        dataHelper.setStopTime(null)
        dataHelper.setStartTime(null)
        stopTimer()
        binding.timerView.text = timeStringFromLong(0)
    }

    private fun stopTimer()
    {
        dataHelper.setTimerCounting(false)
        binding.audioStart.text = getString(R.string.start)
    }

    private fun startTimer()
    {
        dataHelper.setTimerCounting(true)
        binding.audioStart.text = getString(R.string.stop)
    }

    private fun startStopAction()
    {
        if(dataHelper.timerCounting())
        {
            dataHelper.setStopTime(Date())
            stopTimer()
        }
        else
        {
            if(dataHelper.stopTime() != null)
            {
                dataHelper.setStartTime(calcRestartTime())
                dataHelper.setStopTime(null)
            }
            else
            {
                dataHelper.setStartTime(Date())
            }
            startTimer()
        }
    }

    private fun calcRestartTime(): Date
    {
        val diff = dataHelper.startTime()!!.time - dataHelper.stopTime()!!.time
        return Date(System.currentTimeMillis() + diff)
    }

    private fun timeStringFromLong(ms: Long): String
    {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String
    {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 999 && resultCode == Activity.RESULT_OK) {
            // TODO: do something in here if in-app updates success
        } else {
            // TODO: do something in here if in-app updates failure
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mr.release()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode != 111 || grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}*/


package com.mr2.activity

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.Manifest
import android.os.Environment
import android.provider.MediaStore
import com.mr2.R
import com.mr2.activity.MainActivity
import com.mr2.databinding.ActivityAudioRecordingBinding
import com.mr2.databinding.ActivityMainBinding
import com.mr2.method.DataHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Timer
import java.util.TimerTask
import android.media.MediaRecorder
import kotlinx.coroutines.withContext

class AudioRecordingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioRecordingBinding
    lateinit var dataHelper: DataHelper
    lateinit var mr: MediaRecorder
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioRecordingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataHelper = DataHelper(applicationContext)

        val path = Environment.getExternalStorageDirectory().toString() + "/voice_note.3gp"
        mr = MediaRecorder()

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.RECORD_AUDIO

                ), 111
            )
        }
        //Manifest.permission.WRITE_EXTERNAL_STORAGE
        binding.audioStart.setOnClickListener {
            startStopAction()
            binding.audioStart.visibility = View.GONE
            binding.audioPause.visibility = View.VISIBLE
            GlobalScope.launch(Dispatchers.IO) {
                // Background thread for media recording
                try {
                    mr.setAudioSource(MediaRecorder.AudioSource.MIC)
                    mr.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    mr.setAudioEncoder(MediaRecorder.AudioEncoder.AAC) // or AMR_NB
                    mr.setOutputFile(path)
                    mr.prepare()
                    mr.start()
                } catch (e: Exception) {
                    // Handle exceptions, such as IOException or IllegalStateException
                    e.printStackTrace()
                }
            }
        }

        binding.audioPause.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                mr.stop()
                withContext(Dispatchers.Main) {
                    resetAction()
                    binding.audioPause.visibility = View.GONE
                    binding.audioStart.visibility = View.VISIBLE
                    startActivity(Intent(this@AudioRecordingActivity, MainActivity::class.java))
                }
            }

        }

        if (dataHelper.timerCounting()) {
            startTimer()
        } else {
            stopTimer()
            if (dataHelper.startTime() != null && dataHelper.stopTime() != null) {
                val time = Date().time - calcRestartTime().time
                binding.timerView.text = timeStringFromLong(time)
            }
        }

        timer.scheduleAtFixedRate(TimeTask(), 0, 500)
    }

    private inner class TimeTask : TimerTask() {
        override fun run() {
            // This code will run on a new thread
            if (dataHelper.timerCounting()) {
                val time = Date().time - dataHelper.startTime()!!.time

                // Use runOnUiThread to update UI components on the main thread
                runOnUiThread {
                    binding.timerView.text = timeStringFromLong(time)
                }
            }
        }
    }

    private fun resetAction() {
        dataHelper.setStopTime(null)
        dataHelper.setStartTime(null)
        stopTimer()
        binding.timerView.text = timeStringFromLong(0)
    }

    private fun stopTimer() {
        dataHelper.setTimerCounting(false)
        binding.audioStart.text = getString(R.string.start)
    }

    private fun startTimer() {
        dataHelper.setTimerCounting(true)
        binding.audioStart.text = getString(R.string.stop)
    }

    private fun startStopAction() {
        if (dataHelper.timerCounting()) {
            dataHelper.setStopTime(Date())
            stopTimer()
        } else {
            if (dataHelper.stopTime() != null) {
                dataHelper.setStartTime(calcRestartTime())
                dataHelper.setStopTime(null)
            } else {
                dataHelper.setStartTime(Date())
            }
            startTimer()
        }
    }

    private fun calcRestartTime(): Date {
        val diff = dataHelper.startTime()!!.time - dataHelper.stopTime()!!.time
        return Date(System.currentTimeMillis() + diff)
    }

    private fun timeStringFromLong(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String {
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 999 && resultCode == Activity.RESULT_OK) {
            // TODO: do something in here if in-app updates success
        } else {
            // TODO: do something in here if in-app updates failure
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GlobalScope.launch(Dispatchers.IO) {
            mr.release()
        }
        timer.cancel()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != 111 || grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
