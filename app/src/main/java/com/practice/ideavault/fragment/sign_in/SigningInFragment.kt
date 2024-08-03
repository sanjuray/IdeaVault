package com.practice.ideavault.fragment.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practice.ideavault.MainActivity
import com.practice.ideavault.databinding.FragmentSigningInBinding

class SigningInFragment : Fragment() {

    companion object {
        fun newInstance(): SigningInFragment {
            val fragment = SigningInFragment()
            return fragment
        }
    }

    private lateinit var binding: FragmentSigningInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding = FragmentSigningInBinding.inflate(inflater)
        binding.btSignInButton.setOnClickListener{
            signIn()
        }

        return binding.root
    }

    private fun signIn(){
        (context as MainActivity).signInUser()
    }
}