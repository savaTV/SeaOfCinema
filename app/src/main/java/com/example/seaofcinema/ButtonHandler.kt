import android.content.Intent
import android.net.Uri
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ButtonHandler(
    private val coordinatorLayout: CoordinatorLayout,
    private val watchLaterButtonFab: FloatingActionButton,
    private val favoriteButtonFab: FloatingActionButton,
    private val shareButtonFab: FloatingActionButton,
    private val addWatchLaterIcon: Int,
    private val removeWatchLaterIcon: Int,
    private val addFavoriteIcon: Int,
    private val removeFavoriteIcon: Int
) {

    var isAddedToWatchLater = false
    var isAddedToFavorites = false

    init {
        setupClickListeners()
    }

    private fun setupClickListeners() {
        watchLaterButtonFab.setOnClickListener {
            if (!isAddedToWatchLater) {
                Snackbar.make(
                    coordinatorLayout,
                    "Добавлено в смотреть позже",
                    Snackbar.LENGTH_LONG
                ).setAction("Отменить") {
                    isAddedToWatchLater = false
                    watchLaterButtonFab.setImageResource(addWatchLaterIcon)
                    Snackbar.make(coordinatorLayout, "Действие отменено", Snackbar.LENGTH_LONG)
                        .show()
                }.show()
                isAddedToWatchLater = true
                watchLaterButtonFab.setImageResource(removeWatchLaterIcon)
            } else {
                Snackbar.make(
                    coordinatorLayout,
                    "Удалено из смотреть позже",
                    Snackbar.LENGTH_LONG
                ).show()
                isAddedToWatchLater = false
                watchLaterButtonFab.setImageResource(addWatchLaterIcon)
            }
        }

        favoriteButtonFab.setOnClickListener {
            if (!isAddedToFavorites) {
                Snackbar.make(
                    coordinatorLayout,
                    "Добавлено в Избранное",
                    Snackbar.LENGTH_LONG
                ).setAction("Отменить") {
                    isAddedToFavorites = false
                    favoriteButtonFab.setImageResource(addFavoriteIcon)
                    Snackbar.make(coordinatorLayout, "Действие отменено", Snackbar.LENGTH_LONG)
                        .show()
                }.show()
                isAddedToFavorites = true
                favoriteButtonFab.setImageResource(removeFavoriteIcon)
            } else {
                Snackbar.make(
                    coordinatorLayout,
                    "Удалено из Избранного",
                    Snackbar.LENGTH_LONG
                ).show()
                isAddedToFavorites = false
                favoriteButtonFab.setImageResource(addFavoriteIcon)
            }
        }

        shareButtonFab.setOnClickListener {
            Snackbar.make(
                coordinatorLayout,
                "Ссылка скопирована",
                Snackbar.LENGTH_LONG
            ).setAction("Открыть") {
                val url = "https://example.com"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }.show()
        }
    }

    private fun startActivity(intent: Intent) {

    }
}
