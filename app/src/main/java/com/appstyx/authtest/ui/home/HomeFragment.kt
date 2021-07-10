package com.appstyx.authtest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.appstyx.authtest.databinding.FragmentHomeBinding
import com.appstyx.authtest.network.ApiService
import com.appstyx.authtest.utils.SessionManager
import com.appstyx.authtest.utils.Status
import com.bumptech.glide.Glide

class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var viewModel: HomeViewModel

    private lateinit var sessionManager: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, HomeViewModelFactory(ApiService.getService()))
            .get(HomeViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        viewModel.getUserData(token = "Bearer ${sessionManager.fetchAuthToken()}").observe(viewLifecycleOwner, Observer {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data.let { credits ->
                            val userData = credits?.data?.user

                            if (userData != null) {
                                binding.textViewFirstName.text = userData.firstName
                                binding.textViewLastName.text = userData.lastName

                                val avatar = binding.imageViewAvatar
                                Glide.with(requireContext())
                                    .load(userData.avatarURL)
                                    .into(avatar)
                            }
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {

                    }
                }
            }
        })

        binding.logoutButton.setOnClickListener {
            sessionManager.clearAuthToken()
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSignupFragment())
        }
    }
}