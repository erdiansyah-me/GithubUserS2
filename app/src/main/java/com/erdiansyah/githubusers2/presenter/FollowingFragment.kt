package com.erdiansyah.githubusers2.presenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erdiansyah.githubusers2.data.ItemsItem
import com.erdiansyah.githubusers2.data.SharedViewModel
import com.erdiansyah.githubusers2.databinding.FragmentFollowingBinding

class FollowingFragment : Fragment() {


    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private val viewModel : SharedViewModel by activityViewModels()
    private val list =  ArrayList<ItemsItem>()
    private lateinit var rvFollower: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val username = arguments?.getString(UserPageActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        rvFollower = binding.rvFollowing
        val adapter = UserListAdapter(list)
        rvFollower.adapter = adapter
        binding.rvFollowing.setHasFixedSize(true)

        viewModel.setFollowing(username)
        viewModel.isLoading.observe(viewLifecycleOwner){
            visibleLoading(it)
        }
        viewModel.userListFollowing.observe(viewLifecycleOwner){ userList ->
            if (userList !== null) {
                adapter.setUserList(userList)
                visibleLoading(false)
            }
        }

        rvFollower.layoutManager = LinearLayoutManager(view?.context)
        return binding.root
    }
    private fun visibleLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}