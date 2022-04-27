package xyz.scoca.assignment_6.view

import android.os.Bundle
import android.util.Log
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
import xyz.scoca.assignment_6.databinding.FragmentHomeBinding
import xyz.scoca.assignment_6.network.Network
import xyz.scoca.mobileassignment6.model.crud.User

class HomeFragment : Fragment() {
    private lateinit var binding : FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        fetchUserDetail()

        binding.buttonLogout.setOnClickListener {
            logout()
        }
        binding.btnUpdate.setOnClickListener {
            updateUser()
        }
        binding.btnDelete.setOnClickListener {
            deleteUser()
        }
        return binding.root
    }


    private fun fetchUserDetail(){
        var username = ""
        var gender = ""
        val email = SharedPref(requireContext()).getUserEmail().toString()
        binding.tvUserEmail.text = email

        Network().apiService?.fetchUser(email)
            ?.enqueue(object : Callback<User>{
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    username = response.body()?.user?.username.toString()
                    gender = response.body()?.user?.gender.toString()

                    binding.etUsername.setText(username)
                    binding.etGender.setText(gender)
                }

                override fun onFailure(
                    call: Call<User>,
                    t: Throwable
                ) {
                    Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun updateUser(){
        val username = binding.etUsername.text.toString()
        val email = SharedPref(requireContext()).getUserEmail().toString()
        val gender = binding.etGender.text.toString()

        Network().apiService?.updateUser(username,email,gender)
            ?.enqueue(object : Callback<User>{
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    Toast.makeText(requireContext(),"Successfully Updated.",Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(
                    call: Call<User>,
                    t: Throwable
                ) {
                    Toast.makeText(requireContext(),t.message.toString(),Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun deleteUser(){
        val email = SharedPref(requireContext()).getUserEmail().toString()

        Network().apiService?.deleteUser(email)
            ?.enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    Toast.makeText(requireContext(),"Account Deleted Successfully", Toast.LENGTH_SHORT).show()
                    logout()
                }

                override fun onFailure(
                    call: Call<User>,
                    t: Throwable
                ) {
                    Toast.makeText(requireContext(),t.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("ERRORRRRR",t.message.toString())
                }

            })

    }
    private fun logout() {
        SharedPref(requireContext()).clearSharedPref()
        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }
}