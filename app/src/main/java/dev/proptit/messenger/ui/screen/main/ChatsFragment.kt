package dev.proptit.messenger.ui.screen.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.proptit.messenger.R
import dev.proptit.messenger.data.contact.repository.ContactRepository
import dev.proptit.messenger.data.messge.repository.MessageRepository
import dev.proptit.messenger.databinding.FragmentChatsBinding
import dev.proptit.messenger.ui.MainViewModel
import dev.proptit.messenger.ui.MainViewModelFactory
import dev.proptit.messenger.ui.adapter.chats.ConversationAdapter
import dev.proptit.messenger.ui.adapter.chats.contact.OnlineContactAdapter
import kotlinx.coroutines.launch


class ChatsFragment : Fragment() {

    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MainViewModel by activityViewModels{
        MainViewModelFactory(
            ContactRepository(),
            MessageRepository()
        )
    }
    private lateinit var conversationAdapter: ConversationAdapter
    private lateinit var onlineContactAdapter: OnlineContactAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.startFetchingAllContactsWithUser()
        viewModel.startFetchingAllMessagesFromContactId()

        conversationAdapter = ConversationAdapter(
            onItemClick = { id ->
//                val bundle = Bundle()
//                bundle.putInt("idContact", id)
//                val fragmentChat = ChatFragment()
//                fragmentChat.arguments = bundle
//                findNavController().navigate(R.id.action_navigation_chats_to_chatFragment, bundle)
                viewModel.updateCurContactId(id)
                findNavController().navigate(R.id.action_navigation_chats_to_chatFragment)
            })

        binding.rvConversations.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = conversationAdapter
        }

        onlineContactAdapter = OnlineContactAdapter(onItemClick = { id ->
//            val bundle = Bundle()
//            bundle.putInt("idContact", id)
//            val fragmentChat = ChatFragment()
//            fragmentChat.arguments = bundle
//            findNavController().navigate(R.id.action_navigation_chats_to_chatFragment, bundle)
            viewModel.updateCurContactId(id)
            findNavController().navigate(R.id.action_navigation_chats_to_chatFragment)
        })

        binding.rvOnlineContact.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = onlineContactAdapter
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allContacts.observe(viewLifecycleOwner) {
                    onlineContactAdapter.setOnlineContactList(it)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allContactsByMessages.observe(viewLifecycleOwner) {
                    conversationAdapter.setConversation(it)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.allMessages.observe(viewLifecycleOwner) {
                    conversationAdapter.setMessagesList(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}