package com.erdiansyah.githubusers2.util

import androidx.recyclerview.widget.DiffUtil
import com.erdiansyah.githubusers2.data.ItemsItem

class DiffUtilList(
    private val oldList: ArrayList<ItemsItem>,
    private val newList: ArrayList<ItemsItem>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList == newList
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val latest = newList[newItemPosition]
        return when {
            old.login == latest.login -> true
            old.avatarUrl == latest.avatarUrl -> true
            else -> false
        }
    }
}