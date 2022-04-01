package com.erdiansyah.githubusers2.presenter

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.erdiansyah.githubusers2.R
import com.erdiansyah.githubusers2.data.ItemsItem
import com.erdiansyah.githubusers2.data.SharedViewModel
import com.erdiansyah.githubusers2.databinding.FragmentUserListBinding

class UseListFragment : Fragment() {

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!
    private val viewModel : SharedViewModel by activityViewModels()
    private val list =  ArrayList<ItemsItem>()
    private lateinit var rvUserList: RecyclerView

    private var tabName: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val username = arguments?.getString(UserPageActivity.EXTRA_USERNAME).toString()
        _binding = FragmentUserListBinding.inflate(inflater, container, false)

        tabName = arguments?.getString(TAB_NAME)

        rvUserList = binding.rvUserList
        val adapter = UserListAdapter(list)
        rvUserList.adapter = adapter
        binding.rvUserList.setHasFixedSize(true)

        viewModel.isLoading.observe(viewLifecycleOwner){
            visibleLoading(it)
        }
        viewModel.setFollowing(username)
        viewModel.setFollowers(username)
        if (tabName == TAB_FOLLOWERS) {
            viewModel.userListFollower.observe(viewLifecycleOwner){ userList ->
                if (userList !== null) {
                    adapter.setUserList(userList)
                    visibleLoading(false)
                    binding.tvFollState.visibility = View.GONE
                    if (adapter.itemCount == 0) {
                        binding.tvFollState.text = getString(R.string.Follower_empty)
                        binding.tvFollState.visibility = View.VISIBLE
                    }
                }
            }
        } else if (tabName == TAB_FOLLOWING) {
            viewModel.userListFollowing.observe(viewLifecycleOwner){ userList ->
                if (userList !== null) {
                    adapter.setUserList(userList)
                    visibleLoading(false)
                    binding.tvFollState.visibility = View.GONE
                    if (adapter.itemCount == 0) {
                        binding.tvFollState.text = getString(R.string.Following_empty)
                        binding.tvFollState.visibility = View.VISIBLE
                    }
                }
            }
        }

        if (view?.resources?.configuration?.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvUserList.layoutManager = GridLayoutManager(view?.context,2)
        } else {
            rvUserList.layoutManager = LinearLayoutManager(view?.context)
        }
        adapter.setOnItemClickListener(object : UserListAdapter.OnItemClickListener {
            override fun onItemClicked(data: ItemsItem) {
                showUserDetail(data)
            }
        })
        return binding.root
    }

    private fun visibleLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showUserDetail(user: ItemsItem){
        val userDetailIntent = Intent(view?.context, UserPageActivity::class.java)
        userDetailIntent.putExtra(UserPageActivity.EXTRA_USERNAME, user.login)
        startActivity(userDetailIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
        const val TAB_NAME = "tab_name"
        const val TAB_FOLLOWERS = "followers"
        const val TAB_FOLLOWING = "following"
    }
}