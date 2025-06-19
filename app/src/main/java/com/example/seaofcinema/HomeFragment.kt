package com.example.seaofcinema

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seaofcinema.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var filmsAdapter: FilmListRecyclerAdapter
    private val filmsDataBase = listOf(
        Film(
            1,
            "Daredevil",
            R.drawable.daredevil,
            "A blind lawyer by day, vigilante by night. Matt Murdock fights the crime of New York as Daredevil."
        ),
        Film(
            2,
            "Superman look up",
            R.drawable.superman,
            "Superman must reconcile his alien Kryptonian heritage with his human upbringing as reporter Clark Kent. As the embodiment of truth, justice and the human way he soon finds himself in a world that views these as old-fashioned."
        ),
        Film(
            3,
            "Megan 2.0",
            R.drawable.megan_2_0,
            "Two years after M3GAN's rampage, her creator, Gemma, resorts to resurrecting her infamous creation in order to take down Amelia, the military-grade weapon who was built by a defense contractor who stole M3GAN's underlying tech."
        ),
        Film(
            4,
            "Electric State",
            R.drawable.electric_state,
            "An orphaned teen hits the road with a mysterious robot to find her long-lost brother, teaming up with a smuggler and his wisecracking sidekick"
        ),
        Film(
            5,
            "F1: The Movie",
            R.drawable.the_movie,
            "A Formula One driver comes out of retirement to mentor and team up with a younger driver."
        ),
        Film(
            6,
            "The Amateur",
            R.drawable.the_amateur,
            "When his supervisors at the CIA refuse to take action after his wife is killed in a London terrorist attack, a decoder takes matters into his own hands."
        ),
        Film(
            7,
            "The George",
            R.drawable.the_gorge,
            "Two operatives are appointed to posts in guard towers on opposite sides of a classified gorge."
        ),
        Film(
            8,
            "The Eternaut",
            R.drawable.the_eternaut,
            "Follows Juan Salvo along with a group of survivors as they battle an alien threat that is under the direction of an invisible force after a horrific snowfall claims the lives of millions of people."
        ),
        Film(
            9,
            "Havoc",
            R.drawable.havoc,
            "After a drug deal gone wrong, a bruised detective must fight his way through the criminal underworld to rescue a politician's estranged son, unraveling a deep web of corruption and conspiracy that ensnares his entire city."
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        // Обновляем список с помощью DiffUtil
        filmsAdapter.updateItems(filmsDataBase)

    }


}

