package com.mr2.activity


/**
 * ce fichier permet à l'utilisateur d'enregistrer une voice note
 */

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import com.mr2.R
import com.mr2.databinding.ActivityAudioRecordingBinding
import com.mr2.method.DataHelper
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Timer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.transition.TransitionManager
import android.util.Log
import androidx.lifecycle.lifecycleScope
import android.os.SystemClock
import android.provider.MediaStore
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.mr2.databinding.ActivityDetailNoteBinding
import com.mr2.entity.Note
import com.mr2.entity.Vocal
import com.mr2.method.DateChange
import com.mr2.viewModel.NotesViewModel
import com.mr2.viewModel.VocalViewModel
import java.io.File
import java.io.IOException

class AudioRecordingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioRecordingBinding
    private lateinit var vocalViewModel: VocalViewModel
    private val recordingFilePath: String by lazy {
        val currentTime = System.currentTimeMillis()
        "${externalCacheDir?.absolutePath}/recording${currentTime}.m4a"
    }
    private val dateChange = DateChange()


    private var length: String = "";
    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var fileName: String? = null
    private var lastProgress = 0
    private val mHandler = Handler()
    private val RECORD_AUDIO_REQUEST_CODE = 101
    private var isPlaying = false

     @RequiresApi(Build.VERSION_CODES.O)
     fun onClick(view: View?) {
        when (view!!.id) {
            R.id.imgBtRecord -> {
                prepareRecording()
                startRecording()
            }

            R.id.imgBtStop -> {
                prepareStop()
                stopRecording()
            }

        }
    }
    private fun initViewModel() {
        vocalViewModel = ViewModelProvider(this).get(VocalViewModel::class.java)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioRecordingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViewModel()
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.POST_NOTIFICATIONS),111)
        }


        binding.imgBtRecord.setOnClickListener {
            prepareRecording()
            startRecording()
        }

        binding.imgBtStop.setOnClickListener {
            prepareStop()
            stopRecording()
        }

        binding.imgViewPlay.setOnClickListener{
            if (!isPlaying && recordingFilePath != null) {
                isPlaying = true
                startPlaying()
            } else {
                isPlaying = false
                stopPlaying()
            }
        }
    }

    private fun stopPlaying() {
        try {
            mPlayer!!.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mPlayer = null
         binding.imgViewPlay.setImageResource(R.drawable.play_24)
        binding.chronometer.stop()
    }
    private fun startPlaying() {
        mPlayer = MediaPlayer()
        try {
            mPlayer!!.setDataSource(recordingFilePath)
            mPlayer!!.prepare()
            mPlayer!!.start()
        } catch (e: IOException) {

        }

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
    private fun prepareStop() {
        TransitionManager.beginDelayedTransition(binding.llRecorder)
        binding.imgBtRecord.visibility = View.VISIBLE
        binding.imgBtStop.visibility = View.GONE
        binding.llPlay.visibility = View.VISIBLE
    }


    private fun prepareRecording() {
        TransitionManager.beginDelayedTransition(binding.llRecorder)
        binding.imgBtRecord.visibility = View.GONE
        binding.imgBtStop.visibility = View.VISIBLE
        binding.llPlay.visibility = View.GONE
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun startRecording() {

        mRecorder = MediaRecorder()
        mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mRecorder!!.setAudioEncodingBitRate(128000)
        mRecorder!!.setAudioSamplingRate(44100)
        mRecorder!!.setOutputFile(recordingFilePath)


        try {
            mRecorder!!.prepare()
            mRecorder!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        lastProgress = 0
        binding.seekBar.progress = 0
        stopPlaying()
         binding.chronometer.base = SystemClock.elapsedRealtime()
        binding.chronometer.start()
    }

    private fun showSavingDialogBox(){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.save_vocal_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val editText: EditText = dialog.findViewById(R.id.vocal_title_input)
        val saveBtn: Button = dialog.findViewById(R.id.saveBtn)
        val cancelBtn: Button = dialog.findViewById(R.id.cancelBtn)
        val date = dateChange.getToday()
        val time = dateChange.getTime()




        saveBtn.setOnClickListener {
            if(!editText.text.isEmpty()){
                vocalViewModel.insertVocal(
                    Vocal(
                        title = editText.text.toString(),
                        path = recordingFilePath,
                        length = length,
                        date = date,
                        time = time,
                    )
                )
            }
            dialog.dismiss()
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun stopRecording() {
        try {
            mRecorder?.stop()
            mRecorder?.release()
            binding.chronometer.stop()
            binding.chronometer.base = SystemClock.elapsedRealtime()
            length = binding.chronometer.text.toString()
            showSavingDialogBox()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mRecorder = null
        // Stopping the chronometer

         Toast.makeText(this, R.string.recording_saved, Toast.LENGTH_SHORT).show()
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
    private fun getPermissionToRecordAudio() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf( Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE), RECORD_AUDIO_REQUEST_CODE)
        }
    }





    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode != 111 || grantResults[0] != PackageManager.PERMISSION_GRANTED){
            startActivity(Intent(this, MainActivity::class.java))
        }
    }


}
