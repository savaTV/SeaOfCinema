package com.example.seaofcinema

// Импорты необходимых классов
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.seaofcinema.databinding.FragmentDetailsBinding

// Класс фрагмента с деталями фильма
class DetailsFragment : Fragment() {
    // Привязка к layout через Data Binding
    private lateinit var binding: FragmentDetailsBinding

    // Текущий фильм
    private lateinit var film: Film

    // Помощник для работы с базой данных
    private lateinit var dbHelper: DatabaseHelper

    // Создание представления
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Инициализация компонентов
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация помощника БД
        dbHelper = DatabaseHelper(requireContext())

        // Установка деталей фильма
        setFilmsDetails()

        // Обработчик кнопки избранного
        binding.snackbarButtonFavorite.setOnClickListener {
            if (!film.isInFavorites) {
                // Добавление в избранное
                Log.d("DetailsFragment", "Adding film ${film.title} to favorites")
                dbHelper.addToFavorites(1L, film.id)
                binding.snackbarButtonFavorite.setImageResource(R.drawable.baseline_favorite_24)
                film.isInFavorites = true
            } else {
                // Удаление из избранного
                Log.d("DetailsFragment", "Removing film ${film.title} from favorites")
                dbHelper.removeFromFavorites(1L, film.id)
                binding.snackbarButtonFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                film.isInFavorites = false
            }
        }

        // Обработчик кнопки поделиться
        binding.snackbarDetailsFabShare.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Check out this film: ${film.title}\n\n${film.description}"
                )
                type = "text/plain"
            }
            startActivity(Intent.createChooser(intent, "Share To:"))
        }
    }

    // Метод установки деталей фильма
    private fun setFilmsDetails() {
        // Получаем фильм из аргументов
        film = arguments?.getParcelable("film")
            ?: throw IllegalArgumentException("Film not provided")

        // Проверяем статус избранного в БД
        val isFavoriteInDb = dbHelper.getFavorites(1L).any { it.id == film.id }
        film.isInFavorites = isFavoriteInDb
        Log.d("DetailsFragment", "Film ${film.title}, isInFavorites=${film.isInFavorites}")

        // Заполняем элементы интерфейса
        binding.detailsToolbar.title = film.title
        binding.detailsPoster.setImageResource(film.poster)
        binding.detailsDescription.text = film.description
        binding.snackbarButtonFavorite.setImageResource(
            if (film.isInFavorites)
                R.drawable.baseline_favorite_24
            else
                R.drawable.baseline_favorite_border_24
        )
    }
}
