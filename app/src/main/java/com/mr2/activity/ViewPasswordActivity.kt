package com.mr2.activity

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mr2.R
import com.mr2.adapter.PasswordAdapter
import com.mr2.databinding.ActivityDetailNoteBinding
import com.mr2.databinding.ActivityPasswordBinding
import com.mr2.databinding.ActivityViewPasswordBinding
import com.mr2.databinding.BottomsheetNoteBinding
import com.mr2.entity.Note
import com.mr2.entity.Password
import com.mr2.method.DateChange
import com.mr2.viewModel.NotesViewModel
import com.mr2.viewModel.PasswordViewModel


class ViewPasswordActivity : ComponentActivity(), View.OnClickListener {

    val editPassword = "edit_password"
    private lateinit var password: Password
    private lateinit var notesViewModel: NotesViewModel
    private val dateChange = DateChange()
    private lateinit var dialog: BottomSheetDialog

    private lateinit var binding: ActivityViewPasswordBinding

    private lateinit var listPasswordAdapter: PasswordAdapter
    private lateinit var passwordViewModel: PasswordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initViewModel()
        initListener()
    }

    private fun initView() {

        if (intent.getParcelableExtra<Note>(editPassword) != null) {

            password = intent.getParcelableExtra(editPassword)!!
            binding.tvTitle.text = password.title
            binding.tvDate.text = dateChange.changeFormatDate(password.updatedDate)
            binding.tvEdited.text = password.updatedTime
            binding.emailContent.text = password?.email
            binding.passwordContent.text = password?.password
            binding.extraNotesContainer.text = password?.notes


            if (password.updatedDate.isNotEmpty()) {
                if (dateChange.getToday() == password.updatedDate) {
                    binding.tvEdited.text =
                        "Last edit ${dateChange.changeFormatDate(password.updatedTime)}"
                } else {
                    binding.tvEdited.text =
                        "Last edit ${dateChange.changeFormatDate(password.updatedDate)}"
                }
            } else {
                if (dateChange.getToday() == password.updatedDate) {
                    binding.tvEdited.text =
                        "Last edit ${dateChange.changeFormatDate(password.updatedTime)}"
                } else {
                    binding.tvEdited.text =
                        "Last edit ${dateChange.changeFormatDate(password.updatedDate)}"
                }
            }

        }

    }

    private fun initViewModel() {
        passwordViewModel = ViewModelProvider(this).get(PasswordViewModel::class.java)
    }

    private fun initListener() {
        binding.toolbar.nibBack.setOnClickListener(this)
        binding.toolbar.nibEdit.setOnClickListener(this)
        binding.ibMenu.setOnClickListener(this)
        binding.copyButton.setOnClickListener(this)
     }

    private fun deletePassword(password: Password) {
        passwordViewModel.deletePassword(password)
        Toast.makeText(this@ViewPasswordActivity, "Note removed", Toast.LENGTH_SHORT).show()
    }

    private fun showBottomSheet() {
        val views: View =
            LayoutInflater.from(this).inflate(R.layout.bottomsheet_note, null)

        val bindingBottom = BottomsheetNoteBinding.bind(views)

        dialog = BottomSheetDialog(this)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(views)
        dialog.show()

        bindingBottom.clDelete.setOnClickListener(this)
        bindingBottom.clShare.setOnClickListener(this)
    }




    private fun showCustomDialogBox(title: String, description: String){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        var tvTitle: TextView = dialog.findViewById(R.id.dialogTitle)
        var tvDesc: TextView = dialog.findViewById(R.id.dialogDescription)
        var btnYes: Button = dialog.findViewById(R.id.dialogBtnYes)
        var btnNo: Button = dialog.findViewById(R.id.dialogBtnNo)

        tvTitle.text = title;
        tvDesc.text = description;

        btnYes.setOnClickListener {
            deletePassword(password)
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
    fun copyToClipboard(context: Context, textToCopy: String, label: String = "Copied Text") {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, textToCopy)
        clipboard.setPrimaryClip(clip)
    }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.nib_back -> {
                onBackPressed()
            }
            R.id.nib_edit -> {
                val intent = Intent(this, PasswordActivity::class.java)
                intent.putExtra(PasswordActivity().editPassword, password)
                startActivity(intent)
            }
            R.id.ib_menu -> {
                showBottomSheet()
            }
            R.id.copyButton -> {
                copyToClipboard(this, binding.passwordContent.text.toString())
            }
            R.id.cl_delete -> {
                //showDialog()
                showCustomDialogBox("Delete this note", "Are you sure you want to delete this note")
            }
            R.id.cl_share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${password.title}\n\nemail: ${password.email}\npassword: ${password.password}")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }


}

