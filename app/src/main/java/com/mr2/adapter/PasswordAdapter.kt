

package com.mr2.adapter


/**
 * Adapteur pour les mots de passe
 */

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mr2.R
import com.mr2.databinding.ListItemBinding
import com.mr2.databinding.PasswordItemBinding
import com.mr2.entity.Note
import com.mr2.entity.Password
import com.mr2.method.DateChange

class PasswordAdapter : RecyclerView.Adapter<PasswordAdapter.PasswordViewHolder>() {
    private val dataPassword = ArrayList<Password>()
    private lateinit var listener: PasswordListener

    fun setOnClicked(listener: PasswordListener) {
        this.listener = listener
    }

    fun setData(items: ArrayList<Password>) {
        dataPassword.clear()
        dataPassword.addAll(items)
        notifyDataSetChanged()
    }

    class PasswordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = PasswordItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PasswordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.password_item, parent, false)
        return PasswordViewHolder(view)
    }

    override fun onBindViewHolder(holder: PasswordViewHolder, position: Int) {
        val dateChange = DateChange()
        val item = dataPassword[position]
        with(holder) {
            binding.passwordTitle.text = item.title
            binding.passwordDate.text = dateChange.changeFormatDate(item.updatedDate)
            binding.passwordEmail.text = item.email
            itemView.setOnClickListener {
                listener.onItemClicked(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataPassword.size
    }

    interface PasswordListener {
        fun onItemClicked(password: Password)
    }
}