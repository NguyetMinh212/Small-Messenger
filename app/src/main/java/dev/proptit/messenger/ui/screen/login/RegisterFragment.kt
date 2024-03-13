package dev.proptit.messenger.ui.screen.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.orhanobut.hawk.Hawk
import dev.proptit.messenger.R
import dev.proptit.messenger.data.contact.repository.ContactRepository
import dev.proptit.messenger.data.messge.repository.MessageRepository
import dev.proptit.messenger.databinding.FragmentRegisterBinding
import dev.proptit.messenger.setup.key.Keys
import dev.proptit.messenger.ui.MainViewModel
import dev.proptit.messenger.ui.MainViewModelFactory

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding?=null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createBtn.setOnClickListener {
            val name = binding.nameRegisEdt.text.toString()
            val username = binding.emailRegisEdt.text.toString()
            val password = binding.passwordRegisEdt.text.toString()
            viewModel.registerNewAccount(name, username, password,
                {
                    findNavController().navigate(R.id.action_registerFragment_to_logInFragment)
                },
                {
                    Toast.makeText(requireContext(), "Register failed", Toast.LENGTH_SHORT).show()
                })
        }
    }

}