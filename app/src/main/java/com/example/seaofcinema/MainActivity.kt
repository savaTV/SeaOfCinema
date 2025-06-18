package com.example.seaofcinema

import DetailsFragment
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seaofcinema.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Зупускаем фрагмент при старте
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, HomeFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun AlertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выйти из приложения")
        builder.setMessage("Вы уверены, что хотите выйти?")
        builder.setPositiveButton("Да") { dialog, which ->
            finish()
        }
        builder.setNeutralButton("Нет") { dialog, which ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onBackPressed() {
        AlertDialog()
    }


    fun launchDetailsFragment(film: Film) {
        //Создаем "посылку"
        val bundle = Bundle()
        //Кладем наш фильм в "посылку"
        bundle.putParcelable("film", film)
        //Кладем фрагмент с деталями в перменную
        val fragment = DetailsFragment()
        //Прикрепляем нашу "посылку" к фрагменту
        fragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_placeholder, fragment)
            .addToBackStack(null)
            .commit()


        fun initNavigation(topAppBar: MaterialToolbar) {
            topAppBar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.settings -> {
                        Toast.makeText(this, "Настройки", Toast.LENGTH_SHORT).show()
                        true
                    }

                    else -> false
                }
            }

            val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNavigation.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.favorites -> {
                        Toast.makeText(this, "Избранное", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.watch_later -> {
                        Toast.makeText(this, "Посмотреть позже", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.selections -> {
                        Toast.makeText(this, "Подборки", Toast.LENGTH_SHORT).show()
                        true
                    }

                    else -> false
                }
            }
        }
    }
}