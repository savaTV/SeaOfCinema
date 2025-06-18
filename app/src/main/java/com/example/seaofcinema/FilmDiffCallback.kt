package com.example.seaofcinema

import androidx.recyclerview.widget.DiffUtil

class FilmDiffCallback(
    private val oldList: List<Film>,
    private val newList: List<Film>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldFilm = oldList[oldItemPosition]
        val newFilm = newList[newItemPosition]
        return oldFilm.title == newFilm.title &&
                oldFilm.poster == newFilm.poster &&
                oldFilm.description == newFilm.description
    }
}