package com.example.seaofcinema

// Импорт необходимого класса для сравнения
import androidx.recyclerview.widget.DiffUtil

// Класс для сравнения списков фильмов
class FilmDiffCallback(
    // Старый список фильмов
    private val oldList: List<Film>,
    // Новый список фильмов
    private val newList: List<Film>,
) : DiffUtil.Callback() {

    // Возвращает размер старого списка
    override fun getOldListSize(): Int = oldList.size

    // Возвращает размер нового списка
    override fun getNewListSize(): Int = newList.size

    // Проверяет, являются ли элементы одинаковыми по ID
    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean {
        // Сравниваем ID фильмов
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    // Проверяет, являются ли содержимое элементов одинаковым
    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int,
    ): Boolean {
        // Получаем фильмы из списков
        val oldFilm = oldList[oldItemPosition]
        val newFilm = newList[newItemPosition]

        // Сравниваем все важные поля фильма
        return oldFilm.title == newFilm.title &&
                oldFilm.poster == newFilm.poster &&
                oldFilm.description == newFilm.description
    }
}
