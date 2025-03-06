package com.example.seaofcinema

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var menuItemsContainer: LinearLayout
    private lateinit var buttonMenu: Button
    private var isMenuOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        initMenuButtons()



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Находим элементы
        menuItemsContainer = findViewById(R.id.menuItemsContainer)
        buttonMenu = findViewById(R.id.buttonMenu)

        // Устанавливаем обработчик клика
        buttonMenu.setOnClickListener {
            toggleMenu()
        }
    }

    fun initMenuButtons() {
        val buttons = mapOf(
            R.id.buttonFilms to "Фильмы Скоро будут",
            R.id.buttonSeries to "Сериалы Скоро будут",
            R.id.buttonSelect to "В разработке",
            R.id.buttonWatchLater to "Скоро будет",
            R.id.buttonSetting to "В разработке"
        )

        buttons.forEach { (id, message) ->
            findViewById<Button>(id).setOnClickListener {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun toggleMenu() {
        val menuButtonHeight = buttonMenu.height.toFloat()
        val delayBetweenAnimations = 100L

        if (isMenuOpen) {
            // Скрываем кнопки в обратном порядке
            for (i in 0 until menuItemsContainer.childCount) {
                val child = menuItemsContainer.getChildAt(i)
                child.animate()
                    .translationY(-menuButtonHeight - (i *child.height.toFloat()))  //
                    .setDuration(300)
                    .setStartDelay(i * delayBetweenAnimations) // Задержка увеличивается для каждой следующей кнопки
                    .withEndAction {
                        if (i == menuItemsContainer.childCount - 1) { // Когда последняя кнопка закончила свертку
                            menuItemsContainer.visibility = View.GONE
                        }
                    }
                    .start()

            }
        } else {
            // Показываем кнопки последовательно, выезжая из-под кнопки меню
            menuItemsContainer.visibility = View.VISIBLE
            for (i in 0 until menuItemsContainer.childCount) {
                val child = menuItemsContainer.getChildAt(i)
                child.translationY = -menuButtonHeight - (i * child.height.toFloat())
                child.animate()
                    .translationY(0f)
                    .setDuration(300)
                    .setStartDelay(i * delayBetweenAnimations)
                    .start()
            }
        }
        isMenuOpen = !isMenuOpen
    }
}