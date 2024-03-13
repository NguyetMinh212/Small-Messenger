package dev.proptit.messenger.ui.screen.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.orhanobut.hawk.Hawk
import dev.proptit.messenger.R
import dev.proptit.messenger.data.contact.repository.ContactRepository
import dev.proptit.messenger.data.messge.repository.MessageRepository
import dev.proptit.messenger.databinding.FragmentLogInBinding
import dev.proptit.messenger.setup.key.Keys
import dev.proptit.messenger.ui.MainViewModel
import dev.proptit.messenger.ui.MainViewModelFactory
import dev.proptit.messenger.ui.screen.main.MainActivity

class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            val username = binding.loginEmailEdt.text.toString()
            val password = binding.passwordLoginEdt.text.toString()
            viewModel.getIdUserByLogin(username, password,
                {
                    Hawk.put(Keys.ID_USER, it)
                    startActivity(Intent(activity, MainActivity::class.java))
                    activity?.finish()
                },
                {
                    Toast.makeText(
                        requireContext(),
                        "Login failed! Your account hasn't been created yet!",
                        Toast.LENGTH_SHORT
                    ).show()
                })
        }

        binding.createNewBtn.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment_to_registerFragment)
        }
    }

}