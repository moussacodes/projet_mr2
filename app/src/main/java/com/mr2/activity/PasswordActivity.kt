package com.mr2.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mr2.R
import com.mr2.adapter.PasswordAdapter
import com.mr2.databinding.ActivityPasswordBinding
import com.mr2.entity.Note
import com.mr2.entity.Password
import com.mr2.method.DateChange
import com.mr2.viewModel.NotesViewModel
import com.mr2.viewModel.PasswordViewModel

class PasswordActivity : AppCompatActivity(), View.OnClickListener {
    val editPassword = "edit_password"
    private var isUpdate = false
    private lateinit var password: Password
    private lateinit var binding: ActivityPasswordBinding
    private lateinit var listPasswordAdapter: PasswordAdapter
    private lateinit var passwordViewModel: PasswordViewModel
    private val dateChange = DateChange()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        initListener()



    }

    private fun initView() {

        if (intent.getParcelableExtra<Note>(editPassword) != null) {
            isUpdate = true

            password = intent.getParcelableExtra(editPassword)!!
            binding.serviceName.setText(password.title)
            binding.password.setText(password.password)
            binding.emailInput.setText(password.email)

            binding.notesInput.setText(password.notes)
            binding.serviceName.setSelection(password.title.length)

        }

    }

    private fun initViewModel() {
        passwordViewModel = ViewModelProvider(this).get(PasswordViewModel::class.java)
    }


    private fun initListener() {
        binding.toolbarEditPassword.nibBack.setOnClickListener(this)
        binding.toolbarEditPassword.btnSave.setOnClickListener(this)
        binding.seePassword.setOnClickListener(this)
        binding.generatePassword.setOnClickListener(this)
    }

    private fun deleteNote(password: Password) {
        passwordViewModel.deletePassword(password)
        Toast.makeText(this@PasswordActivity, "Password removed", Toast.LENGTH_SHORT).show()
    }


    fun generateRandomPassword(): String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_-+=<>?"
        return (1..30)
            .map { allowedChars.random() }
            .joinToString("")
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.nib_back -> {
                onBackPressed()
            }
            R.id.generate_password ->{
                val password = generateRandomPassword()
                binding.password.setText(password)
                binding.password.setSelection(30)
            }
            R.id.see_password->{
                binding.password.inputType = if (binding.password.inputType == android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD) {
                    android.text.InputType.TYPE_CLASS_TEXT
                } else {
                    android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                }

            }
            R.id.btn_save -> {
                Log.d("clicked", "it's being clicked")
                val title = binding.serviceName.text.toString()
                val email = binding.emailInput.text.toString()
                val passwordContent = binding.password.text.toString()
                val notes = binding.notesInput.text.toString()
                val date = dateChange.getToday()
                val time = dateChange.getTime()

                if (title.isEmpty() || passwordContent.isEmpty()) {
                    Toast.makeText(this@PasswordActivity, "Password cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (isUpdate) {
                        passwordViewModel.updatePassword(
                            Password(
                                id = password.id,
                                title = title,
                                email = password.email,
                                notes = password.notes,
                                password = passwordContent,
                                updatedDate = date,
                                updatedTime = time,
                            )
                        )
                    } else {
                        passwordViewModel.insertPassword(
                            Password(
                                title = title,
                                email = email,
                                notes = notes,
                                password = passwordContent,
                            )
                        )
                    }

                    Toast.makeText(this@PasswordActivity, "Password saved", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                    finish()
                }

            }

        }
    }

}