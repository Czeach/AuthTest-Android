package com.appstyx.authtest.ui.signup

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.appstyx.authtest.R
import com.appstyx.authtest.databinding.FragmentSignupBinding
import com.appstyx.authtest.models.Gender
import com.appstyx.authtest.models.LoginRequest
import com.appstyx.authtest.models.LoginResponse
import com.appstyx.authtest.network.ApiService
import com.appstyx.authtest.network.SessionManager
import com.appstyx.authtest.utils.GenderAdapter
import com.appstyx.authtest.utils.Status
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupFragment: Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var apiService: ApiService

    private lateinit var viewModel: SignupViewModel
    private var genderAdapter = GenderAdapter(arrayListOf())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentSignupBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this, SignUpViewModelFactory(ApiService.getService()))
            .get(SignupViewModel::class.java)

//        binding.editGender.setOnClickListener {
//            AlertDialog.Builder(requireContext())
//                .setTitle("Select Gender")
//                .setAdapter(genderAdapter, object : DialogInterface.OnClickListener {
//                    override fun onClick(dialog: DialogInterface?, which: Int) {
//                        binding.editGender.setText(genderAdapter.getItem(which).toString())
//
//                        dialog?.dismiss()
//                    }
//
//                }).create().show()
//        }

//        binding.editGender.apply {
//            adapter = genderAdapter
//        }

        sessionManager = SessionManager(requireContext())
        apiService = ApiService.getService()

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

                apiService.login(LoginRequest(email, firstname, lastName, gender)).enqueue(
                    object : Callback<LoginResponse> {
                        override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                            val loginResponse = response.body()

                            if (loginResponse?.data != null) {
                                sessionManager.saveAuthToken(loginResponse.data.token)

                                Toast.makeText(requireContext(), loginResponse.data.token, Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(requireContext(), response.message(), Toast.LENGTH_LONG).show()
                                Log.d("RequestBody", call.request().body.toString())
                                Log.d("Response", response.message().toString())
                            }
                        }

                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        }

                    }
                )

            }
        }

        viewModel.getGenders().observe(viewLifecycleOwner, Observer {
            it.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data.let { credits ->

                            val genderList: List<Gender.Data.Gender> = credits?.data?.genders!!

                            val spinnerAdapter = GenderAdapter(genderList)

                            binding.editGender.setOnClickListener {
                                AlertDialog.Builder(requireContext())
                                    .setTitle("Select gender")
                                    .setAdapter(spinnerAdapter) { dialog, which ->
                                        binding.editGender.setText(genderList[which].name.toString())
                                        dialog?.dismiss()
                                    }.create().show()
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

        return binding.root
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding = null
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

//    private fun initViewModelObservers() {
//        // TODO
//    }

//    private fun initActionsListeners() {
//        binding.signupButton.setOnClickListener {
//            viewModel.onSignupClick()
//        }
//    }

    companion object {
        fun newInstance() = SignupFragment()
    }

}
