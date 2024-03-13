package dev.proptit.messenger.ui.screen.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.orhanobut.hawk.Hawk
import dev.proptit.messenger.R
import dev.proptit.messenger.data.contact.repository.ContactRepository
import dev.proptit.messenger.data.messge.entity.MessageEntity
import dev.proptit.messenger.data.messge.repository.MessageRepository
import dev.proptit.messenger.databinding.FragmentChatBinding
import dev.proptit.messenger.setup.key.Keys
import dev.proptit.messenger.ui.MainViewModel
import dev.proptit.messenger.ui.MainViewModelFactory
import dev.proptit.messenger.ui.adapter.chat.MessageAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var messageAdapter: MessageAdapter
    private val viewModel: MainViewModel by activityViewModels {
        MainViewModelFactory(
            ContactRepository(),
            MessageRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        val idContact = arguments?.getInt("idContact") ?: 0
//        viewModel.getContactById(idContact){
//            Glide.with(requireContext()).load(it.avatar).into(binding.ivAvatar)
//            binding.tvName.text = it.name
//
//        }
//        val contactModel = viewModel.contactById.value!!
//        Glide.with(requireContext()).load(contactModel.avatar).into(binding.ivAvatar)
//        binding.tvName.text = contactModel.name
        binding.iconBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.etMessage.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                val message = binding.etMessage.text.toString().trim()
                if (message.isEmpty()) {
                    Glide.with(requireContext()).load(R.drawable.icon_like).into(binding.ivLike)
                    Glide.with(requireContext()).load(R.drawable.iconemoji).into(binding.ivEmoji)
                    viewLifecycleOwner.lifecycleScope.launch {
                        onHideActions()
                        delay(3000)
                        onShowActions()
                    }
                } else {
                    Glide.with(requireContext()).load(R.drawable.icon_send).into(binding.ivLike)
                    Glide.with(requireContext()).load(R.drawable.icon_search).into(binding.ivEmoji)
                    onHideActions()
                }


            }
        }

        binding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val message = s.toString().trim()
                binding.ivLike.isEnabled = message.isNotEmpty()
                if (message.isNotEmpty()) {
                    Glide.with(requireContext()).load(R.drawable.icon_send).into(binding.ivLike)
                    Glide.with(requireContext()).load(R.drawable.icon_search).into(binding.ivEmoji)
                    onHideActions()
                } else {
                    Glide.with(requireContext()).load(R.drawable.icon_like).into(binding.ivLike)
                    Glide.with(requireContext()).load(R.drawable.iconemoji).into(binding.ivEmoji)
                    onShowActions()
                }
            }

        })

        binding.etMessage.setOnClickListener {
            if (binding.etMessage.text.isEmpty()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    onHideActions()
                    delay(3000)
                    if (binding.etMessage.text.isEmpty()) {
                        onShowActions()
                    } else {
                        onHideActions()
                    }
                }
            }
        }

        binding.ivLike.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                binding.ivLike.alpha = 0.5f
                delay(100)
                val message = binding.etMessage.text.toString().trim()
                if (message.isNotEmpty()) {
                    viewModel.createNewMessage(
                        message = message,
                        idReceiveContact = viewModel.curContactId.value!!,
                        idSendContact = Hawk.get(Keys.ID_USER, -1),
                        System.currentTimeMillis(),
                        {
                            Toast.makeText(requireContext(), "Send message success", Toast.LENGTH_SHORT).show()
                        },
                        {
                            Toast.makeText(requireContext(), "Send message failed", Toast.LENGTH_SHORT).show()
                        }
                    )
//                    viewModel.addMessages(MessageEntity(message = message, idSendContact = Hawk.get(Keys.ID_USER, -1), idReceiveContact = viewModel.curContactId.value!!))
                }
                binding.ivLike.alpha = 1f
                binding.etMessage.text.clear()
            }
        }

        binding.ivArrowForward.setOnClickListener {
            onShowActions()
        }

        messageAdapter = MessageAdapter()

        binding.rvMessages.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false).also {
                    it.stackFromEnd = true
                }
            adapter = messageAdapter
        }


//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.getAllMessageByContactId(idContact).observe(viewLifecycleOwner) {
//                messageAdapter.setMessageList(it)
//                binding.rvMessages.scrollToPosition(it.size-1)
//            }
//        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.curContact.observe(viewLifecycleOwner) {
                    if (it != null){
                        messageAdapter.setCompanionAvatar(it.avatar)
                        Glide.with(requireContext()).load(it.avatar).into(binding.ivAvatar)
                        binding.tvName.text = it.name
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getAllMessagesByContactId().collect {
                    Log.d("messageList", it.toString())
                    messageAdapter.setMessageList(it)
                    binding.rvMessages.scrollToPosition(it.size - 1)
                }
            }
        }

    }

    private fun onShowActions() {
        binding.ivArrowForward.visibility = View.GONE
        binding.ivAudio.visibility = View.VISIBLE
        binding.ivGallery.visibility = View.VISIBLE
        binding.ivPhoto.visibility = View.VISIBLE
        binding.ivActions.visibility = View.VISIBLE
    }

    private fun onHideActions() {
        binding.ivArrowForward.visibility = View.VISIBLE
        binding.ivAudio.visibility = View.GONE
        binding.ivGallery.visibility = View.GONE
        binding.ivPhoto.visibility = View.GONE
        binding.ivActions.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}