package com.example.seaofcinema

// Импорты необходимых классов
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.seaofcinema.databinding.FilmItemBinding

// Основной класс адаптера
class FilmListRecyclerAdapter(
    // Интерфейс для обработки кликов по элементам
    private val clickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // Список элементов для отображения
    private val items = mutableListOf<Film>()

    // Создание ViewHolder'а
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        // Создаем привязку к layout
        val binding = FilmItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        // Возвращаем ViewHolder
        return FilmViewHolder(binding)
    }

    // Привязка данных к ViewHolder'у
    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int
    ) {
        when (holder) {
            is FilmViewHolder -> {
                // Привязываем данные к ViewHolder'у
                holder.bind(items[position])
                // Устанавливаем обработчик клика
                holder.itemView.setOnClickListener {
                    clickListener.click(items[position])
                }
            }
        }
    }

    // Возвращаем количество элементов
    override fun getItemCount(): Int = items.size

    // Метод для обновления списка с использованием DiffUtil
    fun updateItems(newItems: List<Film>) {
        // Создаем callback для сравнения списков
        val diffCallback = FilmDiffCallback(items, newItems)
        // Вычисляем разницу между списками
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        // Обновляем данные
        items.clear()
        items.addAll(newItems)
        // Применяем изменения
        diffResult.dispatchUpdatesTo(this)
    }

    // Метод для добавления элементов
    fun addItems(list: List<Film>) {
        // Очищаем текущий список
        items.clear()
        // Добавляем новые элементы
        items.addAll(list)
        // Уведомляем RecyclerView об изменениях
        notifyDataSetChanged()
    }

    // Интерфейс для обработки кликов
    interface OnItemClickListener {
        fun click(film: Film)
    }
}
