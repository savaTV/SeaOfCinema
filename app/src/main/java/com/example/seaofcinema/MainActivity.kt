package com.example.seaofcinema

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.widget.NestedScrollView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.seaofcinema.databinding.ActivityMainBinding
import com.example.seaofcinema.databinding.FragmentHomeBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var lastScrollY = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Создание экземпляра DatabaseHelper
        val dbHelper = DatabaseHelper(this)

        // Проверка сохраненного состояния
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
        // Инициализация навигации
        initNavigation()
    }

    private fun initScrollListener(scrollView: NestedScrollView, searchView: SearchView) {
        scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > lastScrollY) {
                // Скролл вниз
                animateSearchBar(searchView, true)
            } else if (scrollY < lastScrollY) {
                // Скролл вверх
                animateSearchBar(searchView, false)
            }
            lastScrollY = scrollY
        }
    }

    private fun animateSearchBar(searchView: SearchView, hide: Boolean) {
        val translateY = if (hide) {
            -searchView.height.toFloat()
        } else {
            0f
        }

        searchView.animate()
            .translationY(translateY)
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    // Метод инициализации Bottom Navigation
    private fun initNavigation() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.favorites -> {
                    // Переход во фрагмент избранного
                    val fragment = FavoritesFragment().apply {
                        arguments = Bundle().apply {
                            putLong("userId", 1L) // Передача ID пользователя
                        }
                    }
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                    true
                }

                R.id.watch_later -> {
                    // Обработка пункта "Посмотреть позже"
                    Toast.makeText(this, "Посмотреть позже", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.selections -> {
                    // Обработка пункта "Подборки"
                    Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }

    // Метод показа диалогового окна выхода
    private fun showExitDialog() {
        val ad = AlertDialog.Builder(this)
        ad.setTitle("Выйти из приложения")
        ad.setIcon(R.drawable.ic_launcher_background)
        ad.setMessage("Вы уверены, что хотите выйти?")
        ad.setPositiveButton("Да") { _, _ -> finish() } // Завершение приложения
        ad.setNeutralButton("Нет") { dialog, _ -> dialog.dismiss() } // Закрытие диалога
        ad.create().show()
    }

    // Переопределение обработки кнопки "Назад"
    @Deprecated("This method has been deprecated in favor of using the OnBackPressedDispatcher")
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment is HomeFragment) {
            // Если текущий фрагмент - HomeFragment, показываем диалог выхода
            showExitDialog()
        } else {
            // Иначе обрабатываем как обычно
            super.onBackPressed()
        }
    }

    // Метод для запуска фрагмента с деталями фильма
    fun launchDetailsFragment(film: Film) {
        val bundle = Bundle().apply { putParcelable("film", film) }
        val fragment = DetailsFragment().apply { arguments = bundle }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}