package com.example.seaofcinema

// Импортируем необходимые классы для работы с RecyclerView и метриками
import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

// Класс для добавления отступов между элементами RecyclerView
class TopSpacingItemDecoration(
    // Параметр paddingInDp - размер отступа в dp (density-independent pixels)
    private val paddingInDp: Int
) : RecyclerView.ItemDecoration() {

    // Свойство для конвертации dp в пиксели
    private val Int.convertPx: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()

    // Основной метод, который определяет отступы для каждого элемента
    override fun getItemOffsets(
        outRect: Rect,    // Объект для хранения отступов
        view: View,       // Текущий элемент RecyclerView
        parent: RecyclerView,  // Родительский RecyclerView
        state: RecyclerView.State  // Состояние RecyclerView
    ) {
        // Вызываем реализацию родительского класса
        super.getItemOffsets(outRect, view, parent, state)

        // Устанавливаем отступы для текущего элемента
        // Конвертируем значение из dp в пиксели с помощью свойства convertPx
        outRect.top = paddingInDp.convertPx  // Отступ сверху
        outRect.right = paddingInDp.convertPx // Отступ справа
        outRect.left = paddingInDp.convertPx  // Отступ слева
    }
}