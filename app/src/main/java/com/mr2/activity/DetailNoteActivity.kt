package com.mr2.activity

/**
 * ce fichier permet à l'utilisateur de consulter, supprimer une note
 */

import android.app.Dialog
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
import com.mr2.R
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mr2.databinding.ActivityDetailNoteBinding
import com.mr2.databinding.BottomsheetNoteBinding
import com.mr2.entity.Note
import com.mr2.method.DateChange
import com.mr2.viewModel.NotesViewModel


class DetailNoteActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityDetailNoteBinding
    val editNoteExtra = "edit_note_extra"
    private lateinit var note: Note
    private lateinit var notesViewModel: NotesViewModel
    private val dateChange = DateChange()
    private lateinit var dialog: BottomSheetDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        initListener()

    }

    private fun initView() {

        if (intent.getParcelableExtra<Note>(editNoteExtra) != null) {

            note = intent.getParcelableExtra(editNoteExtra)!!
            binding.tvTitle.text = note.title
            binding.tvDate.text = dateChange.changeFormatDate(note.date)


            binding.tvBody.text = note?.body

            if (note.updatedDate.isNotEmpty()) {
                if (dateChange.getToday() == note.updatedDate) {
                    binding.tvEdited.text =
                        "Last edit ${dateChange.changeFormatDate(note.updatedTime)}"
                } else {
                    binding.tvEdited.text =
                        "Last edit ${dateChange.changeFormatDate(note.updatedDate)}"
                }
            } else {
                if (dateChange.getToday() == note.date) {
                    binding.tvEdited.text =
                        "Last edit ${dateChange.changeFormatDate(note.time)}"
                } else {
                    binding.tvEdited.text =
                        "Last edit ${dateChange.changeFormatDate(note.date)}"
                }
            }

        }

    }

    private fun initViewModel() {
        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
    }

    private fun initListener() {
        binding.toolbar.nibBack.setOnClickListener(this)
        binding.toolbar.nibEdit.setOnClickListener(this)
        binding.ibMenu.setOnClickListener(this)
    }

    private fun deleteNote(note: Note) {
        notesViewModel.deleteNote(note)
        Toast.makeText(this@DetailNoteActivity, "Note removed", Toast.LENGTH_SHORT).show()
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
            deleteNote(note)
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.nib_back -> {
                onBackPressed()
            }
            R.id.nib_edit -> {
                val intent = Intent(this, EditActivity::class.java)
                intent.putExtra(EditActivity().editNoteExtra, note)
                startActivity(intent)
            }
            R.id.ib_menu -> {
                showBottomSheet()
            }
            R.id.cl_delete -> {
                //showDialog()
                showCustomDialogBox("Delete this note", "Are you sure you want to delete this note")
            }
            R.id.cl_share -> {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "${note.title}\n\n${note.body}")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }
}