package com.mr2.adapter

import android.util.Log
import com.mr2.entity.Vocal
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mr2.R
import com.mr2.databinding.ListItemCheckboxBinding
import com.mr2.databinding.ListVocalBinding
import com.mr2.entity.Tag
import com.mr2.method.DateChange

class TagAdapter : RecyclerView.Adapter<TagAdapter.TagViewHolder>() {
    private val dataTag = ArrayList<Tag>()
    private var selectedItemPosition: Int = RecyclerView.NO_POSITION
    private lateinit var listener: TagListener

    fun setOnClicked(listener: TagListener) {
        this.listener = listener
    }

    fun setData(items: ArrayList<Tag>) {
        dataTag.clear()
        dataTag.addAll(items)
        notifyDataSetChanged()

    }

    class TagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ListItemCheckboxBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_checkbox, parent, false)
        return TagViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        val item = dataTag[position]
        with(holder) {
            binding.tagName.text = item.name
            itemView.setOnClickListener {
                listener.onItemClicked(item)
            }
        }
    }

    private fun setItemSelection(position: Int) {
        val previousSelectedItemPosition = selectedItemPosition
        selectedItemPosition = position
        notifyItemChanged(previousSelectedItemPosition)
        notifyItemChanged(selectedItemPosition)
    }

    override fun getItemCount(): Int {
        return dataTag.size
    }

    interface TagListener {
        fun onItemClicked(tag: Tag)
    }
}