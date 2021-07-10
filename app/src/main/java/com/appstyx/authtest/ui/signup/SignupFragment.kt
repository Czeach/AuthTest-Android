package com.appstyx.authtest.ui.signup

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.appstyx.authtest.R
import com.appstyx.authtest.databinding.FragmentSignupBinding
import com.appstyx.authtest.models.Gender
import com.appstyx.authtest.models.LoginRequest
import com.appstyx.authtest.network.ApiService
import com.appstyx.authtest.utils.SessionManager
import com.appstyx.authtest.utils.GenderAdapter
import com.appstyx.authtest.utils.Status
import com.appstyx.authtest.utils.genderClickListener

class SignupFragment: Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var sessionManager: SessionManager

    private lateinit var viewModel: SignupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentSignupBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, SignUpViewModelFactory(ApiService.getService()))
            .get(SignupViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val genderClickListener by lazy {
            object : genderClickListener {
                override fun invoke(it: Gender.Data.Gender) {
                    binding.editGender.setText(it.id.toString())
                }
            }
        }
        val genderAdapter = GenderAdapter(arrayListOf(), genderClickListener)

        binding.genderRecycler.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = genderAdapter
        }

        viewModel.getGenders().observe(viewLifecycleOwner, Observer {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data.let { credits ->
                            credits?.data?.genders?.let { it1 -> genderAdapter.updateList(it1) }
                        }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {

                    }
                }
            }
        })

        binding.editGender.setOnClickListener {
            hideKeyboard()

            if (binding.genderRecycler.visibility == View.GONE) {
                binding.genderRecycler.visibility = View.VISIBLE
            } else {
                binding.genderRecycler.visibility = View.GONE
            }
        }

        sessionManager = SessionManager(requireContext())

        binding.signupButton.setOnClickListener {
            if (binding.editTextEmail.text.toString().isEmpty()) {
                binding.inputLayoutEmail.error = "This field is empty!"
            }
            if (binding.editTextFirstName.text.toString().isEmpty()) {
                binding.inputLayoutFirstName.error = "This field is empty!"
            }
            if (binding.editTextLastName.text.toString().isEmpty()) {
                binding.inputLayoutLastName.error = "This field is empty!"
            }
            if (binding.editGender.text.toString().isEmpty()) {
                binding.editGenderInputLayout.error = "Select gender!"
            }
            if (binding.editTextEmail.text.toString().isNotEmpty() && binding.editTextFirstName.text.toString().isNotEmpty() &&
                binding.editTextLastName.text.toString().isNotEmpty() && binding.editGender.text.toString().isNotEmpty()) {

                val email = binding.editTextEmail.text.toString()
                val firstname = binding.editTextFirstName.text.toString()
                val lastName = binding.editTextLastName.text.toString()
                val gender = binding.editGender.text.toString()

                val loginRequest = LoginRequest(
                    email = email,
                    firstName = firstname,
                    lastName = lastName,
                    gender = gender
                )

                viewModel.login(loginRequest).observe(viewLifecycleOwner, Observer {
                    it.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                resource.data.let { credits ->
                                    val loginResponse = credits?.data

                                    if (loginResponse != null) {
                                        sessionManager.saveAuthToken(loginResponse.token)

                                        val options = NavOptions.Builder().setPopUpTo(R.id.signupFragment, true).build()
                                        findNavController().navigate(SignupFragmentDirections.actionSignupFragmentToHomeFragment(), options)

                                    }
                                }
                            }
                            Status.LOADING -> {

                            }
                            Status.ERROR -> {
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                                it.message?.let { it1 -> Log.d("ErrorResponse", it1) }
                            }
                        }
                    }
                })

            }
        }

    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

}
