import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.seaofcinema.Film
import com.example.seaofcinema.R
import com.example.seaofcinema.databinding.ActivityDetailsBinding

class DetailsFragment : Fragment() {

    private lateinit var binding: ActivityDetailsBinding
    private lateinit var buttonHandler: ButtonHandler

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ActivityDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val coordinatorLayout = binding.coordinatorLayout
        val watchLaterButtonFab = binding.snackbarButtonWatchLaters
        val favoriteButtonFab = binding.snackbarButtonFavorite
        val shareButtonFab = binding.detailsFab

        buttonHandler = ButtonHandler(
            coordinatorLayout,
            watchLaterButtonFab,
            favoriteButtonFab,
            shareButtonFab,
            R.drawable.baseline_bookmark_add_24,
            R.drawable.baseline_bookmark_added_24,
            R.drawable.baseline_favorite_border_24,
            R.drawable.baseline_favorite_24
        )

        setFilmsDetails()
    }

    private fun setFilmsDetails() {
        val film = arguments?.get("film") as Film
        binding.detailsPoster.setImageResource(film.poster)
        binding.detailsDescription.text = film.description
        (activity as? AppCompatActivity)?.supportActionBar?.title = film.title
    }
}