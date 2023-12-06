package com.mr2.activity

/**
 * ce fichier permet à l'utilisateur de visualiser le contenu de l'application
 */

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.mr2.R
import com.mr2.activity.AudioRecordingActivity
import com.mr2.activity.EditActivity

import com.mr2.adapter.SectionsPagerAdapter
import com.mr2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appUpdateManager: AppUpdateManager

    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom) }

    private var clicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initListener()

        appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    it,
                    AppUpdateType.IMMEDIATE,
                    this,
                    999
                )
            }
        }

    }

    private fun initView() {

        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(findViewById(R.id.view_pager))
        binding.floatingActionButtonEdit.setOnClickListener {
            startActivity(Intent(this, EditActivity::class.java))
        }
        binding.floatingActionButtonVocal.setOnClickListener {
            startActivity(Intent(this, AudioRecordingActivity::class.java))
        }
        binding.floatingActionButtonPassword.setOnClickListener {
            startActivity(Intent(this, PasswordActivity::class.java))
        }

    }

    private fun initListener() {
        binding.toolbar.ibSearch.setOnClickListener(this)
        binding.floatingActionButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.ib_search -> {
                val intent = Intent(this@MainActivity, SearchActivity::class.java)
                startActivity(intent)
            }
            R.id.floatingActionButton -> {
                 onAddButtonClicked()
            }
        }
    }

    private fun onAddButtonClicked(){
        setVisiblility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if(!clicked){
            findViewById<FloatingActionButton>(R.id.floatingActionButtonEdit).startAnimation(fromBottom)
            findViewById<FloatingActionButton>(R.id.floatingActionButtonVocal).startAnimation(fromBottom)
            findViewById<FloatingActionButton>(R.id.floatingActionButtonPassword).startAnimation(fromBottom)

            findViewById<FloatingActionButton>(R.id.floatingActionButton).startAnimation(rotateOpen)
        }else{
            findViewById<FloatingActionButton>(R.id.floatingActionButtonEdit).startAnimation(toBottom)
            findViewById<FloatingActionButton>(R.id.floatingActionButtonVocal).startAnimation(toBottom)
            findViewById<FloatingActionButton>(R.id.floatingActionButtonPassword).startAnimation(toBottom)

            findViewById<FloatingActionButton>(R.id.floatingActionButton).startAnimation(rotateClose)

        }
    }

    private fun setVisiblility(clicked: Boolean) {
        if(!clicked){
            findViewById<FloatingActionButton>(R.id.floatingActionButtonEdit).visibility = View.VISIBLE
            findViewById<FloatingActionButton>(R.id.floatingActionButtonVocal).visibility = View.VISIBLE
            findViewById<FloatingActionButton>(R.id.floatingActionButtonPassword).visibility = View.VISIBLE

        }else{
            findViewById<FloatingActionButton>(R.id.floatingActionButtonEdit).visibility = View.GONE
            findViewById<FloatingActionButton>(R.id.floatingActionButtonVocal).visibility = View.GONE
            findViewById<FloatingActionButton>(R.id.floatingActionButtonPassword).visibility = View.GONE

        }
    }


    override fun onResume() {
        appUpdateManager.appUpdateInfo
            .addOnSuccessListener {
                if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(
                        it,
                        AppUpdateType.IMMEDIATE,
                        this,
                        999
                    )
                }
            }
        super.onResume()
    }





}
