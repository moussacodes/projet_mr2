package com.mr2.adapter

/**
 * Adapteur pour les voice notes
 */

import android.util.Log
import com.mr2.entity.Vocal
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mr2.R
import com.mr2.databinding.ListVocalBinding
import com.mr2.method.DateChange

class VocalAdapter : RecyclerView.Adapter<VocalAdapter.VocalViewHolder>() {
    private val dataVocal = ArrayList<Vocal>()
    private lateinit var listener: VocalListener

    fun setOnClicked(listener: VocalListener) {
        this.listener = listener
    }

    fun setData(items: ArrayList<Vocal>) {
        dataVocal.clear()
        dataVocal.addAll(items)
        notifyDataSetChanged()
        Log.d("VocalAdapter", "Data size: ${dataVocal.size}")

    }

    class VocalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ListVocalBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VocalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_vocal, parent, false)
        return VocalViewHolder(view)
    }

    override fun onBindViewHolder(holder: VocalViewHolder, position: Int) {
        val dateChange = DateChange()
        val item = dataVocal[position]
        with(holder) {
            binding.textViewTitle.text = item.title
            binding.tvDate.text = dateChange.changeFormatDate(item.date)
            binding.vocalLength.text = item.length

            itemView.setOnClickListener {
                listener.onItemClicked(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataVocal.size
    }

    interface VocalListener {
        fun onItemClicked(vocal: Vocal)
    }
}