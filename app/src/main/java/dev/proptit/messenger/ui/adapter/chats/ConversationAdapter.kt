package dev.proptit.messenger.ui.adapter.chats

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orhanobut.hawk.Hawk
import dev.proptit.messenger.data.contact.entity.ContactEntity
import dev.proptit.messenger.data.messge.entity.MessageEntity
import dev.proptit.messenger.databinding.ItemConversationBinding
import dev.proptit.messenger.setup.key.Keys
import dev.proptit.messenger.setup.utils.TimeUtils

class ConversationAdapter(
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    private var conversationList = listOf<ContactEntity>()
    private var messageList = listOf<MessageEntity>()

    inner class ConversationViewHolder(private val binding: ItemConversationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(contactModel: ContactEntity) {
            val message = getLastMessageByContactId(contactModel.id)
            Log.d("ConversationAdapter", "bind: ${message?.message}")
            Log.d("ConversationAdapter", "bind: ${contactModel.avatar}")
            Glide.with(itemView.context).load(contactModel.avatar).into(binding.ivAvatar)
            binding.tvName.text = contactModel.name
            binding.tvLastMessage.text = message?.message
            binding.tvDate.text = TimeUtils.format(message?.time ?: 0L, "HH:mm")
            binding.clItemConversation.setOnClickListener {
                onItemClick(contactModel.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        return ConversationViewHolder(
            ItemConversationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = conversationList.size

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(conversationList[position])
    }

    private fun getLastMessageByContactId(idContact: Int): MessageEntity? {
        return this.messageList.filter {
            if (it.idReceiveContact == Hawk.get(Keys.ID_USER, -1)) {
                it.idSendContact == idContact
            } else {
                it.idReceiveContact == idContact
            }
        }.maxByOrNull { it.id }
    }


    fun setConversation(conversations: List<ContactEntity>) {
        this.conversationList = conversations.sortedByDescending { getLastMessageByContactId(it.id)?.time }
        notifyDataSetChanged()
    }

    fun setMessagesList(messages: List<MessageEntity>) {
        this.messageList = messages
        notifyDataSetChanged()
    }
}
