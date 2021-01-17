package com.appkwan.webdroid.menumore

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appkwan.webdroid.R
import kotlinx.android.synthetic.main.more_menu_item_layout.view.*

class MoreMenuAdapter(var list: ArrayList<MoreMenuItem>, var listener: MoreMenuClickListener):
    RecyclerView.Adapter<MoreMenuAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View = LayoutInflater.from(parent.context).inflate(R.layout.more_menu_item_layout, parent, false)
        context = parent.context
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var menu = list[position]

        holder.itemView.item_more_menu_imageview.setImageDrawable(menu.drawable)
        holder.itemView.item_more_menu_textview.text = menu.name

        holder.itemView.setOnClickListener {
            listener.onMoreMenuItemClicked(position)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}