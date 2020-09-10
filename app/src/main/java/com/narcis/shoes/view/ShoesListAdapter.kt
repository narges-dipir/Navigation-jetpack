package com.narcis.shoes.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.narcis.shoes.R
import com.narcis.shoes.databinding.ItemShoeBinding
import com.narcis.shoes.model.ShoeBreed
import kotlinx.android.synthetic.main.item_shoe.view.*

class ShoesListAdapter(val shoesList: ArrayList<ShoeBreed>) : RecyclerView.Adapter<ShoesListAdapter.ShoeViewHolder>(),
    ShoeClickListener {

    fun updateShoeList(newShoesList: List<ShoeBreed>) {
        shoesList.clear()
        shoesList.addAll(newShoesList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemShoeBinding>(inflater, R.layout.item_shoe, parent, false)
        return ShoeViewHolder(view)
    }

    override fun getItemCount() = shoesList.size

    override fun onBindViewHolder(holder: ShoeViewHolder, position: Int) {
        holder.view.shoe = shoesList[position]
        holder.view.listener = this
    }


    class ShoeViewHolder(var view: ItemShoeBinding) : RecyclerView.ViewHolder(view.root)

    override fun onShoeClicked(v: View) {
        val uuid = v.shoeId.text.toString().toInt()
        val action = ListFragmentDirections.actionDetailFragment()
        action.shoeUuid = uuid
        Navigation.findNavController(v).navigate(action)
    }
}