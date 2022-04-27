package xyz.scoca.assignment_6.view

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Response
import xyz.scoca.assignment_6.R
import xyz.scoca.assignment_6.data.SharedPref
import xyz.scoca.assignment_6.databinding.FragmentRegisterBinding
import xyz.scoca.assignment_6.models.User
import xyz.scoca.assignment_6.network.Network
import javax.security.auth.callback.Callback


class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.btnRegister.setOnClickListener {
            validate()
        }
        return binding.root
    }

    private fun register(username: String, email: String, password: String, gender: String) {
        Network().apiService?.register(username, email, password, gender)
            ?.enqueue(object : retrofit2.Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val status = response.body()?.status.toString()
                    val errorMsg = response.body()?.message.toString()

                    if (status == "true") {
                        with(SharedPref(requireContext())) {
                            this.setUserEmail(response.body()?.user!![0].email)
                            this.setRememberMe(true)
                        }
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                    } else
                        Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT)
                            .show()
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun validate() {
        val username = binding.etUsername.text.toString()
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        var gender = "male"

        binding.radioGender.setOnCheckedChangeListener { radioGroup, id ->
            val radioButton: RadioButton = radioGroup.findViewById(id)
            gender = radioButton.toString()
        }

        if (TextUtils.isEmpty(username)) {
            binding.etUsername.error = "Enter name"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Invalid email format"
        } else if (TextUtils.isEmpty(password)) {
            binding.etPassword.error = "Enter password"
        } else {
            //binding.progressBar.visibility = View.VISIBLE
            register(username, email, password, gender)
        }
    }
}

