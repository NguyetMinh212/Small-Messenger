package dev.proptit.messenger.ui.screen.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.proptit.messenger.R
import dev.proptit.messenger.data.contact.repository.ContactRepository
import dev.proptit.messenger.data.messge.repository.MessageRepository
import dev.proptit.messenger.databinding.FragmentPeopleBinding
import dev.proptit.messenger.ui.MainViewModel
import dev.proptit.messenger.ui.MainViewModelFactory
import dev.proptit.messenger.ui.adapter.people.ContactAdapter

class PeopleFragment : Fragment() {

    private var _binding: FragmentPeopleBinding?=null
    private val binding get() = _binding!!
    private lateinit var contactAdapter: ContactAdapter
    private val viewModel : MainViewModel by activityViewModels{
        MainViewModelFactory(
            ContactRepository(),
            MessageRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contactAdapter = ContactAdapter { id ->
            viewModel.updateCurContactId(id)
            findNavController().navigate(R.id.action_navigation_people_to_chatFragment)}
        contactAdapter.setContactList(viewModel.allContacts.value!!)
        binding.rvPeopleContact.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = contactAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}