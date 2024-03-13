package dev.proptit.messenger.ui.adapter.chats.contact

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import dev.proptit.messenger.data.contact.entity.ContactEntity
import dev.proptit.messenger.databinding.ItemOnlineContactBinding
import dev.proptit.messenger.databinding.ItemYourStoryBinding
import dev.proptit.messenger.ui.adapter.chats.contact.viewholder.OnlineContactViewHolder
import dev.proptit.messenger.ui.adapter.chats.contact.viewholder.YourStoryViewHolder

class OnlineContactAdapter(private val onItemClick: (Int) ->Unit) : RecyclerView.Adapter<ViewHolder>() {

    private var onlineContactLists = listOf<ContactEntity>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            0 -> YourStoryViewHolder(
                ItemYourStoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> OnlineContactViewHolder(
                ItemOnlineContactBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount() = onlineContactLists.size + 1
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder){
            is OnlineContactViewHolder -> holder.bind(onlineContactLists[position - 1], onItemClick)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun setOnlineContactList(onlineContacts: List<ContactEntity>) {
        this.onlineContactLists = onlineContacts
        notifyDataSetChanged()
    }

}