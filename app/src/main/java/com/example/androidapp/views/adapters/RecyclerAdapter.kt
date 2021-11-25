package com.example.androidapp.views.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.androidapp.R
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlinx.android.synthetic.main.card_layout.*

class RecyclerAdapter:RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {


    private var title = arrayOf("Chapter 1", "Chapter 2", "Chapter 3", "Chapter 4")



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return title.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = title[position]
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemTitle: TextView = itemView.findViewById(R.id.card_title)
        val countryButton: TextView = itemView.findViewById(R.id.textViewCountryOption)
        val toggleButton: MaterialButtonToggleGroup = itemView.findViewById(R.id.toggleButton)

        init {
            countryButton.setOnClickListener {
                showPopupMenu(it)
            }
            toggleButton.addOnButtonCheckedListener { toggleButton, checkedId, isChecked ->
                toggleBtnListener()
            }


        }


        private fun toggleBtnListener() {
            // TODO: handle toggle buttom listener
        }

        private fun showPopupMenu(view: View) {
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(R.menu.popup_menu)
            popupMenu.show();

            popupMenu.setOnMenuItemClickListener{
                countryButton.text = it.title
                /*when (it.itemId) {
                    R.id.action_item1 -> {
                        Toast.makeText(view.context, "action item2 clicked: ${it.title}", Toast.LENGTH_SHORT).show()

                    }

                    R.id.action_item2 -> {
                        Toast.makeText(view.context, "action item2 clicked: ${it.title}", Toast.LENGTH_SHORT).show()
                        countryButton.text = it.title
                    }
                    R.id.action_item3 -> {
                        Toast.makeText(view.context, "action item3 clicked: ${it.title}", Toast.LENGTH_SHORT).show()
                        countryButton.text = it.title
                    }

                    R.id.action_item4 -> {
                        Toast.makeText(view.context, "action item4 clicked: ${it.title}", Toast.LENGTH_SHORT).show()
                        countryButton.text = it.title
                    }
                    else -> Log.i("Else", "Reached")
                }*/
                true
            }
        }

    }
}