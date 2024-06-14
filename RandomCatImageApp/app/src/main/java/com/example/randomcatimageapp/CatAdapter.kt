package com.example.randomcatimageapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CatAdapter(
    private var cats: List<Cat>,
    private val onCatClicked: (Cat) -> Unit
) : RecyclerView.Adapter<CatAdapter.CatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cat, parent, false)
        return CatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        val cat = cats[position]
        holder.bind(cat)
    }

    override fun getItemCount(): Int = cats.size

    fun updateCatList(newCats: List<Cat>) {
        cats = newCats
        notifyDataSetChanged()
    }

    inner class CatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewCat: ImageView = itemView.findViewById(R.id.imageViewCat)
        private val textViewCatName: TextView = itemView.findViewById(R.id.textViewCatName)
        private val textViewCatRace: TextView = itemView.findViewById(R.id.textViewCatRace)

        fun bind(cat: Cat) {
            textViewCatName.text = cat.name
            textViewCatRace.text = cat.race
            Glide.with(itemView.context).load(cat.imageUrl).into(imageViewCat)

            itemView.setOnClickListener {
                onCatClicked(cat)
            }
        }
    }
}