package dev.proptit.messenger.ui.adapter.people

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dev.proptit.messenger.data.contact.entity.ContactEntity
import dev.proptit.messenger.databinding.ItemPeopleContactBinding

class ContactAdapter(private val onItemClick: (Int) -> Unit) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    private var contactList = listOf<ContactEntity>()

    inner class ContactViewHolder(private val binding: ItemPeopleContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(contactModel: ContactEntity){
                Glide.with(itemView.context).load(contactModel.avatar).into(binding.ivContactAvatar)
                binding.ivContactName.text = contactModel.name

                binding.clPeopleContact.setOnClickListener {
                    onItemClick(contactModel.id)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(ItemPeopleContactBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount() = contactList.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contactList[position])
    }

    fun setContactList(contacts: List<ContactEntity>){
        this.contactList = contacts
    }

}