package com.mr2.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mr2.activity.DetailNoteActivity
import com.mr2.activity.ViewPasswordActivity
import com.mr2.adapter.NoteAdapter
import com.mr2.adapter.PasswordAdapter
import com.mr2.databinding.FragmentPasswordListBinding
import com.mr2.entity.Note
import com.mr2.entity.Password
import com.mr2.viewModel.NotesViewModel
import com.mr2.viewModel.PasswordViewModel

class PasswordFragment : Fragment() {
    private var _binding: FragmentPasswordListBinding? = null
    private val binding get() = _binding!!
    private lateinit var listPasswordAdapter: PasswordAdapter
    private lateinit var passwordViewModel: PasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPasswordListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initViewModel()
        initListener()

    }

    private fun initView() {

        binding.rvWork.setHasFixedSize(true)
        listPasswordAdapter = PasswordAdapter()
        listPasswordAdapter.notifyDataSetChanged()

        binding.rvWork.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rvWork.adapter = listPasswordAdapter

        listPasswordAdapter.setOnClicked(object : PasswordAdapter.PasswordListener {
            override fun onItemClicked(password: Password) {
                val intent = Intent(context, ViewPasswordActivity::class.java)
                intent.putExtra(ViewPasswordActivity().editPassword, password)
                startActivity(intent)
            }

        })

    }

    private fun initViewModel() {
        passwordViewModel = ViewModelProvider(this).get(PasswordViewModel::class.java)

        passwordViewModel.getPasswords().observe(viewLifecycleOwner, Observer { passwords ->

            if (passwords.isNotEmpty()) {
                binding.textViewNoteEmpty.visibility = View.GONE
            } else {
                binding.textViewNoteEmpty.visibility = View.VISIBLE
            }

            listPasswordAdapter.setData(passwords)
        })

    }

    private fun initListener() {
        passwordViewModel.setPassword()
    }

    override fun onResume() {
        super.onResume()

        //update list
        initListener()
    }
}