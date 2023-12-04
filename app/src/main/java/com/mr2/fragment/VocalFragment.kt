package com.mr2.fragment


import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mr2.R
import com.mr2.activity.DetailNoteActivity
import com.mr2.adapter.NoteAdapter
import com.mr2.adapter.VocalAdapter
import com.mr2.databinding.FragmentVocalBinding
import com.mr2.entity.Note

import com.mr2.entity.Vocal
import com.mr2.viewModel.VocalViewModel
import java.io.IOException

class VocalFragment : Fragment() {
    private var _binding: FragmentVocalBinding? = null
    private val binding get() = _binding!!
    private lateinit var listVocalAdapter: VocalAdapter
    private lateinit var vocalViewModel: VocalViewModel
    private var isPlaying: Boolean = false;
    private var mPlayer: MediaPlayer? = MediaPlayer()


    /* all recordings
        private fun getAllRecordings() {
        val recordArrayList = ArrayList<Recording>()
        val root = android.os.Environment.getExternalStorageDirectory()
        val path = root.absolutePath + "/AndroidCodility/Audios"
        val directory = File(path)
        val files = directory.listFiles()
        if (files != null) {

            for (i in files.indices) {
                val fileName = files[i].name
                val recordingUri = root.absolutePath + "/AndroidCodility/Audios/" + fileName
                recordArrayList.add(Recording(recordingUri, fileName, false))
            }
            tvNoData.visibility = View.GONE
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            myAdapter = MyAdapter(recordArrayList)
            myAdapter!!.setListener(this)
            recyclerView.adapter = myAdapter
        }
    }
     */

    private fun readVocal(view: View, vocal: Vocal){
        var btn = view.findViewById<ImageView>(R.id.play_pause_btn)

        if (!isPlaying) {
            try {
                mPlayer!!.setDataSource(vocal.path)
                mPlayer!!.prepare()
                mPlayer!!.start()
                btn.setImageResource(R.drawable.pause_24)
                isPlaying = true
            } catch (e: IOException) {
                Log.e("LOG_TAG", "prepare() failed")
            }

            mPlayer!!.setOnCompletionListener(MediaPlayer.OnCompletionListener {
                btn.setImageResource(R.drawable.play_24)
                isPlaying = false
                mPlayer!!.seekTo(0)
            })
        } else {
            mPlayer!!.stop()
            mPlayer!!.release()  // Release the MediaPlayer resources
            btn.setImageResource(R.drawable.play_24)
            isPlaying = false
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVocalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("VocalFragment", "onViewCreated called")
        initView(view)
        initViewModel()
        initListener()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initView(view: View) {

        binding.rvPersonal.setHasFixedSize(true)
        listVocalAdapter = VocalAdapter()
        listVocalAdapter.notifyDataSetChanged()

        binding.rvPersonal.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvPersonal.adapter = listVocalAdapter

        listVocalAdapter.setOnClicked(object : VocalAdapter.VocalListener {
            override fun onItemClicked(vocal: Vocal) {
                readVocal(view, vocal)
            }
        })


    }

    private fun initViewModel() {
        vocalViewModel = ViewModelProvider(this).get(VocalViewModel::class.java)

        vocalViewModel.getVocals().observe(viewLifecycleOwner, Observer { vocals ->

            if (vocals.isNotEmpty()) {
                binding.textViewNoteEmpty.visibility = View.GONE
            } else {
                binding.textViewNoteEmpty.visibility = View.VISIBLE
            }

            listVocalAdapter.setData(vocals)
        })
    }

    private fun initListener() {
        vocalViewModel.setVocals()
    }

    override fun onResume() {
        super.onResume()

        //update list
        initListener()
    }
}