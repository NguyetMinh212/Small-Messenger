package dev.proptit.messenger.ui.adapter.chats.contact.viewholder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.proptit.messenger.data.contact.entity.ContactEntity
import dev.proptit.messenger.databinding.ItemOnlineContactBinding

class OnlineContactViewHolder(private val binding: ItemOnlineContactBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(contactModel: ContactEntity, onItemClick: (Int) -> Unit){
        Log.d("OnlineContactViewHolder", "bind: ${contactModel.avatar}")
        Glide.with(itemView.context).load(contactModel.avatar).into(binding.ivContactAvatar)
        binding.tvContactName.text = contactModel.name
        binding.clOnlineContact.setOnClickListener {
            onItemClick(contactModel.id)
        }
    }
}