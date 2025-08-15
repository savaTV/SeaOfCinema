package com.example.seaofcinema

// Импорты необходимых классов
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seaofcinema.databinding.FragmentFavoritesBinding

// Класс фрагмента избранного
class FavoritesFragment : Fragment() {
    // Привязка к layout через Data Binding
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    // Помощник для работы с базой данных
    private lateinit var dbHelper: DatabaseHelper

    // ID пользователя (получаем из аргументов или используем значение по умолчанию)
    private val userId: Long by lazy { arguments?.getLong("userId") ?: 1L }

    // onCreateView - создание представления
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    // onViewCreated - инициализация компонентов
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация помощника БД
        dbHelper = DatabaseHelper(requireContext())

        // Настройка RecyclerView
        binding.favoritesRecycler.layoutManager = LinearLayoutManager(requireContext())

        // Создание адаптера с обработчиком кликов
        val filmsAdapter =
            FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                override fun click(film: Film) {
                    // При клике запускаем фрагмент с деталями фильма
                    (requireActivity() as MainActivity).launchDetailsFragment(film)
                }
            })

        // Установка адаптера
        binding.favoritesRecycler.adapter = filmsAdapter

        // Обновление списка избранного
        updateFavoritesList()
    }

    // onResume - обновление данных при возвращении к фрагменту
    override fun onResume() {
        super.onResume()
        updateFavoritesList()
    }

    // Метод обновления списка избранного
    private fun updateFavoritesList() {
        // Получаем избранные фильмы из БД
        val favorites = dbHelper.getFavorites(userId)

        // Логирование для отладки
        Log.d(
            "FavoritesFragment",
            "Favorites count: ${favorites.size}, titles: ${favorites.map { it.title }}"
        )

        // Обновляем адаптер
        (binding.favoritesRecycler.adapter as FilmListRecyclerAdapter).updateItems(favorites)
    }

    // onDestroyView - освобождение ресурсов
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
