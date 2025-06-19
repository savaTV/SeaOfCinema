package com.example.seaofcinema

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

@Suppress("DEPRECATION")
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
        val ad = AlertDialog.Builder(this)

        ad.setTitle("Выйти из приложения")
        ad.setIcon(R.drawable.ic_launcher_background)
        ad.setMessage("Вы уверены, что хотите выйти?")
        ad.setPositiveButton("Да") { _, _ ->
            finish()
        }
        ad.setNeutralButton("Нет") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = ad.create()
        dialog.show()
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_placeholder)
        if (currentFragment is HomeFragment){
            AlertDialog()
            }else {
                super.onBackPressed()
        }
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