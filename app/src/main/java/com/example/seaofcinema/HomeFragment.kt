package com.example.seaofcinema

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seaofcinema.FilmsData.filmsDataBase
import com.example.seaofcinema.databinding.FragmentHomeBinding
import java.util.Locale
import android.view.animation.DecelerateInterpolator

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private var lastScrollY = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Настройка RecyclerView
        binding.mainRecycler.apply {
            filmsAdapter =
                FilmListRecyclerAdapter(object : FilmListRecyclerAdapter.OnItemClickListener {
                    override fun click(film: Film) {
                        (requireActivity() as MainActivity).launchDetailsFragment(film)
                    }
                })
            adapter = filmsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)
        }

        // Настройка SearchView
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }

        // Обработчик ввода в SearchView
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    filmsAdapter.addItems(filmsDataBase)
                    return true
                }
                val result = filmsDataBase.filter {
                    it.title.toLowerCase(Locale.getDefault()).contains(
                        newText.toLowerCase(Locale.getDefault())
                    )
                }
                filmsAdapter.addItems(result)
                return true
            }
        })

        // Инициализация списка фильмов
        filmsAdapter.updateItems(filmsDataBase)

        // Настройка слушателя прокрутки для анимации SearchView
        initScrollListener()
    }

    private fun initScrollListener() {
        binding.mainRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.searchView.translationY == 0f) {
                    // Скролл вниз - скрываем SearchView
                    animateSearchBar(true)
                } else if (dy < 0 && binding.searchView.translationY != 0f) {
                    // Скролл вверх - показываем SearchView
                    animateSearchBar(false)
                }
            }
        })
    }

    private fun animateSearchBar(hide: Boolean) {
        val translateY = if (hide) {
            -160F
        } else {
            0f
        }

        binding.searchView.animate()
            .translationY(translateY)
            .setDuration(300)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }
}