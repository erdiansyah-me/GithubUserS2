package com.erdiansyah.githubusers2.presenter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erdiansyah.githubusers2.R
import com.erdiansyah.githubusers2.data.ItemsItem
import com.erdiansyah.githubusers2.databinding.ItemUserListBinding
import com.erdiansyah.githubusers2.util.DiffUtilList

class UserListAdapter(private val itemUser: ArrayList<ItemsItem>) : RecyclerView.Adapter<UserListAdapter.ListViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null
    private var oldUserItem = ArrayList<ItemsItem>()

    interface OnItemClickListener{
        fun onItemClicked(data: ItemsItem)
    }
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener){
        this.onItemClickListener = onItemClickListener
    }

    inner class ListViewHolder(var binding: ItemUserListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (login, avatarUrl) = itemUser[position]
        holder.apply {
            val context = itemView.context
            binding.apply {
                tvUserNameList.text = login
                Glide.with(context)
                    .load(avatarUrl)
                    .error(R.drawable.ic_broken_image)
                    .circleCrop()
                    .into(avaUserList)
            }
            itemView.setOnClickListener{
                onItemClickListener?.onItemClicked(itemUser[adapterPosition])
            }
        }
    }

    override fun getItemCount(): Int = itemUser.size

    fun setUserList(user: ArrayList<ItemsItem>){
        val diffUtil = DiffUtilList(oldUserItem, user)
        itemUser.clear()
        itemUser.addAll(user)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        oldUserItem = user
        diffResults.dispatchUpdatesTo(this)
    }
}