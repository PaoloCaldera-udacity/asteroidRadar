package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.databinding.MainListItemBinding
import com.udacity.asteroidradar.repository.Asteroid

class MainListAdapter(private val listener: MainListItemListener) :
    ListAdapter<Asteroid, MainListAdapter.MainListViewHolder>(MainListDiffCallback()) {

    /*
        VIEW HOLDER class
     */
    class MainListViewHolder(private val binding: MainListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): MainListViewHolder {
                val binding = MainListItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MainListViewHolder(binding)
            }
        }

        fun bind(item: Asteroid, listener: MainListItemListener) {
            binding.apply {
                asteroid = item
                itemListener = listener
                executePendingBindings()
            }
        }
    }

    /*
        DIFF UTIL class
     */
    class MainListDiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }
    }

    /*
        LISTENER class
     */
    class MainListItemListener(val clickListener: (Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }


    /*
        Adapter methods override
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListViewHolder {
        return MainListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MainListViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}