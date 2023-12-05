package com.mr2.fragment.tag


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.mr2.activity.EditActivity
import com.mr2.databinding.FragmentCreateTagBinding
import com.mr2.entity.Tag
import com.mr2.method.DateChange
import com.mr2.viewModel.TagViewModel


class CreateTagFragment : Fragment() {
    private var _binding: FragmentCreateTagBinding? = null
    private val binding get() = _binding!!
    private val dateChange = DateChange()

    private lateinit var tagViewModel: TagViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateTagBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()

        binding.saveAddTag.setOnClickListener {
            val date = dateChange.getToday()
            val time = dateChange.getTime()

            if(binding.tagName.text?.isEmpty() != true){
                tagViewModel.insertTag(
                    Tag(
                        name = binding.tagName.text.toString(),
                        updatedTime = time,
                        updatedDate = date
                    )
                )
                Toast.makeText(requireContext(), "Tag created", Toast.LENGTH_SHORT).show()
            }
        }
        binding.cancelAddTag.setOnClickListener {
            val intent = Intent(requireContext(), EditActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }

    private fun initViewModel() {
        tagViewModel = ViewModelProvider(this).get(TagViewModel::class.java)
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CreateTagFragment.


        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateTagFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
         */
    }
}