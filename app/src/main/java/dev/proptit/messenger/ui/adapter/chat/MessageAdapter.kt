package dev.proptit.messenger.ui.adapter.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.orhanobut.hawk.Hawk
import dev.proptit.messenger.data.contact.entity.ContactEntity
import dev.proptit.messenger.data.messge.entity.MessageEntity
import dev.proptit.messenger.databinding.ItemMessageBinding
import dev.proptit.messenger.setup.key.Keys

class MessageAdapter(): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var messageList = listOf<MessageEntity>()
    private var companionAvatar: String ?= null

    inner class MessageViewHolder(private val binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(message: MessageEntity){
            if(message.idSendContact == Hawk.get(Keys.ID_USER, -1)){
                binding.clContactMessage.visibility = View.GONE
                binding.clMyMessage.visibility = View.VISIBLE
                binding.tvMyMessage.text = message.message
            } else{
                binding.clContactMessage.visibility = View.VISIBLE
                binding.clMyMessage.visibility = View.GONE
                binding.tvContactMessage.text = message.message
            }

            Glide.with(itemView.context).load(companionAvatar).into(binding.ivContactAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = messageList.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    fun setMessageList(messages: List<MessageEntity>){
        this.messageList = messages
        notifyDataSetChanged()
    }

    fun setCompanionAvatar(avatar: String){
        this.companionAvatar = avatar
        notifyDataSetChanged()
    }
}