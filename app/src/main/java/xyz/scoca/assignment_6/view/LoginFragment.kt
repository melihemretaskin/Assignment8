package xyz.scoca.assignment_6.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xyz.scoca.assignment_6.R
import xyz.scoca.assignment_6.data.SharedPref
import xyz.scoca.assignment_6.databinding.FragmentLoginBinding
import xyz.scoca.assignment_6.models.User
import xyz.scoca.assignment_6.network.Network

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater,container,false)

        binding.btnLogin.setOnClickListener {
            login()
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        return binding.root
    }

    private fun login(){
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()

        Network().apiService?.login(username,password)
            ?.enqueue(object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val status = response.body()?.status.toString()
                    val errorMsg = response.body()?.message.toString()

                    if (status == "true") {
                        with(SharedPref(requireContext())) {
                            this.setUserEmail(response.body()?.user!![0].email)
                            this.setRememberMe(true)
                        }
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }else
                        Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT)
                            .show()
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

            })
    }

}