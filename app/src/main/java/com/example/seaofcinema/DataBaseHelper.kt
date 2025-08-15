package com.example.seaofcinema

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

// Класс DatabaseHelper управляет базой данных SQLite для приложения Sea of Cinema.
// Он отвечает за создание, обновление базы данных и операции, такие как добавление/удаление фильмов из избранного и запросы к фильмам.
class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // Объект-компаньон для хранения констант, связанных с конфигурацией базы данных и именами таблиц/столбцов.
    companion object {
        private const val DATABASE_NAME = "movies.db" // Имя файла базы данных SQLite.
        private const val DATABASE_VERSION = 1 // Версия схемы базы данных.
        private const val TABLE_MOVIES = "movies" // Имя таблицы для хранения данных о фильмах.
        private const val COLUMN_ID = "id" // Столбец для идентификатора фильма (первичный ключ).
        private const val COLUMN_TITLE = "title" // Столбец для названия фильма.
        private const val COLUMN_POSTER =
            "poster" // Столбец для идентификатора ресурса постера фильма.
        private const val COLUMN_DESCRIPTION = "description" // Столбец для описания фильма.
        private const val COLUMN_IS_IN_FAVORITES =
            "isInFavorites" // Столбец для отслеживания статуса избранного (0 или 1).
        private const val TABLE_FAVORITES =
            "favorites" // Имя таблицы для хранения избранных фильмов пользователей.
        private const val COLUMN_USER_ID =
            "user_id" // Столбец для идентификатора пользователя в таблице избранного.
        private const val COLUMN_MOVIE_ID =
            "movie_id" // Столбец для идентификатора фильма в таблице избранного.
    }

    // Вызывается при первом создании базы данных.
    // Создает таблицы для фильмов и избранного, а также заполняет таблицу фильмов начальными данными.
    override fun onCreate(db: SQLiteDatabase) {
        Log.d("DatabaseHelper", "Создание базы данных")
        // Создание таблицы movies с колонками для ID, названия, постера, описания и статуса избранного.
        db.execSQL(
            """
            CREATE TABLE $TABLE_MOVIES (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_POSTER INTEGER,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_IS_IN_FAVORITES INTEGER DEFAULT 0
            )
        """
        )
        // Создание таблицы favorites с составным первичным ключом (user_id, movie_id)
        // и внешним ключом, связывающим movie_id с id таблицы movies.
        db.execSQL(
            """
            CREATE TABLE $TABLE_FAVORITES (
                $COLUMN_USER_ID INTEGER,
                $COLUMN_MOVIE_ID INTEGER,
                PRIMARY KEY ($COLUMN_USER_ID, $COLUMN_MOVIE_ID),
                FOREIGN KEY ($COLUMN_MOVIE_ID) REFERENCES $TABLE_MOVIES($COLUMN_ID)
            )
        """
        )
        // Заполнение таблицы movies начальными данными из FilmsData.filmsDataBase.
        FilmsData.filmsDataBase.forEach { film ->
            Log.d("DatabaseHelper", "Inserting film: ${film.title}, poster ID: ${film.poster}")
            val values = ContentValues().apply {
                put(COLUMN_ID, film.id) // Вставка ID фильма.
                put(COLUMN_TITLE, film.title) // Вставка названия фильма.
                put(COLUMN_POSTER, film.poster) // Вставка идентификатора ресурса постера.
                put(COLUMN_DESCRIPTION, film.description) // Вставка описания фильма.
                put(
                    COLUMN_IS_IN_FAVORITES,
                    if (film.isInFavorites) 1 else 0
                ) // Вставка статуса избранного (1 - да, 0 - нет).
            }
            db.insert(TABLE_MOVIES, null, values) // Вставка записи о фильме в таблицу movies.
        }
        Log.d("DatabaseHelper", "База данных создана с ${FilmsData.filmsDataBase.size} фильмами")

    }

    // Вызывается при обновлении версии базы данных.
    // Удаляет существующие таблицы и пересоздает их с новой схемой.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("DatabaseHelper", "Обновление базы данных с версии $oldVersion до $newVersion")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES") // Удаление таблицы favorites, если она существует.
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MOVIES") // Удаление таблицы movies, если она существует.
        onCreate(db) // Пересоздание базы данных с новой схемой.
    }

    // Получает информацию о фильме по его ID.
    // Возвращает объект Film, если фильм найден, или null, если нет.
    fun getMovie(movieId: Int): Film? {
        val db = readableDatabase // Получение экземпляра базы данных для чтения.
        // Запрос к таблице movies для получения фильма с указанным ID.
        val cursor = db.query(
            TABLE_MOVIES,
            arrayOf(
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_POSTER,
                COLUMN_DESCRIPTION,
                COLUMN_IS_IN_FAVORITES
            ),
            "$COLUMN_ID = ?",
            arrayOf(movieId.toString()),
            null, null, null
        )
        return if (cursor.moveToFirst()) {
            // Если фильм найден, создается объект Film на основе данных из курсора.
            val film = Film(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                poster = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POSTER)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                isInFavorites = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_IN_FAVORITES)) == 1
            )
            cursor.close() // Закрытие курсора для освобождения ресурсов.
            film // Возврат объекта Film.
        } else {
            cursor.close() // Закрытие курсора, если фильм не найден.
            null // Возврат null, если фильм с указанным ID не найден.
        }
    }

    // Добавляет фильм в избранное пользователя.
    // Возвращает true, если добавление успешно, или false, если фильм не существует или уже в избранном.
    fun addToFavorites(userId: Long, movieId: Int): Boolean {
        val db = writableDatabase // Получение экземпляра базы данных для записи.
        if (getMovie(movieId) == null) return false // Проверка, существует ли фильм; если нет, возвращается false.
        // Проверка, не находится ли фильм уже в избранном пользователя.
        val cursor = db.query(
            TABLE_FAVORITES,
            arrayOf(COLUMN_USER_ID, COLUMN_MOVIE_ID),
            "$COLUMN_USER_ID = ? AND $COLUMN_MOVIE_ID = ?",
            arrayOf(userId.toString(), movieId.toString()),
            null, null, null
        )
        if (cursor.moveToFirst()) {
            cursor.close() // Закрытие курсора, если фильм уже в избранном.
            return false // Возврат false, так как фильм уже добавлен в избранное.
        }
        cursor.close() // Закрытие курсора после проверки.
        // Вставка новой записи в таблицу favorites.
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId) // Вставка ID пользователя.
            put(COLUMN_MOVIE_ID, movieId) // Вставка ID фильма.
        }
        val result =
            db.insert(TABLE_FAVORITES, null, values) // Выполнение вставки в таблицу favorites.
        // Обновление статуса isInFavorites в таблице movies.
        val updateValues = ContentValues().apply {
            put(COLUMN_IS_IN_FAVORITES, 1) // Установка статуса избранного в 1.
        }
        db.update(
            TABLE_MOVIES,
            updateValues,
            "$COLUMN_ID = ?",
            arrayOf(movieId.toString())
        ) // Обновление записи фильма.
        db.close() // Закрытие базы данных.
        Log.d(
            "DatabaseHelper",
            "Добавлен фильм movieId=$movieId в избранное для userId=$userId, результат=$result"
        )
        return result != -1L // Возврат true, если вставка успешна, иначе false.
    }

    fun debugMoviesTable() {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_MOVIES", null)
        Log.d("DatabaseHelper", "Содержимое таблицы movies:")
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val poster = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POSTER))
                Log.d("DatabaseHelper", "id=$id, title=$title, poster=$poster")
            } while (cursor.moveToNext())
        } else {
            Log.d("DatabaseHelper", "Таблица movies пуста")
        }
        cursor.close()
        db.close()
    }

    // Удаляет фильм из избранного пользователя.
    // Возвращает true, если удаление успешно, или false, если запись не была найдена.
    fun removeFromFavorites(userId: Long, movieId: Int): Boolean {
        val db = writableDatabase // Получение экземпляра базы данных для записи.
        // Удаление записи из таблицы favorites для указанного пользователя и фильма.
        val rowsAffected = db.delete(
            TABLE_FAVORITES,
            "$COLUMN_USER_ID = ? AND $COLUMN_MOVIE_ID = ?",
            arrayOf(userId.toString(), movieId.toString())
        )
        // Обновление статуса isInFavorites в таблице movies.
        val updateValues = ContentValues().apply {
            put(COLUMN_IS_IN_FAVORITES, 0) // Установка статуса избранного в 0.
        }
        db.update(
            TABLE_MOVIES,
            updateValues,
            "$COLUMN_ID = ?",
            arrayOf(movieId.toString())
        ) // Обновление записи фильма.
        db.close() // Закрытие базы данных.
        Log.d(
            "DatabaseHelper",
            "Удален фильм movieId=$movieId из избранного для userId=$userId, строк затронуто=$rowsAffected"
        )
        return rowsAffected > 0 // Возврат true, если удаление выполнено, иначе false.
    }

    // Получает список всех фильмов, находящихся в избранном пользователя.
    fun getFavorites(userId: Long): List<Film> {
        val movies = mutableListOf<Film>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
        SELECT m.$COLUMN_ID, m.$COLUMN_TITLE, m.$COLUMN_POSTER, m.$COLUMN_DESCRIPTION, m.$COLUMN_IS_IN_FAVORITES
        FROM $TABLE_MOVIES m
        JOIN $TABLE_FAVORITES f ON m.$COLUMN_ID = f.$COLUMN_MOVIE_ID
        WHERE f.$COLUMN_USER_ID = ?
    """, arrayOf(userId.toString())
        )
        Log.d(
            "DatabaseHelper",
            "Запрос избранного для userId=$userId, количество записей=${cursor.count}"
        )
        if (cursor.moveToFirst()) {
            do {
                val movie = Film(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    poster = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POSTER)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    isInFavorites = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_IS_IN_FAVORITES
                        )
                    ) == 1
                )
                Log.d(
                    "DatabaseHelper",
                    "Найден избранный фильм: ${movie.title}, isInFavorites=${movie.isInFavorites}, poster=${movie.poster}"
                )
                movies.add(movie)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return movies
    }

    // Получает список всех фильмов в базе данных.
    fun getAllMovies(userId: Long): List<Film> {
        val movies = mutableListOf<Film>() // Список для хранения фильмов.
        val db = readableDatabase // Получение экземпляра базы данных для чтения.
        // Запрос всех записей из таблицы movies.
        val cursor = db.query(
            TABLE_MOVIES,
            arrayOf(
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_POSTER,
                COLUMN_DESCRIPTION,
                COLUMN_IS_IN_FAVORITES
            ),
            null, null, null, null, null
        )
        if (cursor.moveToFirst()) {
            do {
                // Создание объекта Film для каждой записи в курсоре.
                val movie = Film(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    poster = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_POSTER)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    isInFavorites = cursor.getInt(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_IS_IN_FAVORITES
                        )
                    ) == 1
                )
                movies.add(movie) // Добавление фильма в список.
            } while (cursor.moveToNext())
        }
        cursor.close() // Закрытие курсора.
        db.close() // Закрытие базы данных.
        return movies // Возврат списка всех фильмов.
    }

    // Метод для отладки содержимого таблицы favorites.
    // Выводит в лог все записи таблицы.
    fun debugFavoritesTable() {
        val db = readableDatabase // Получение экземпляра базы данных для чтения.
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_FAVORITES",
            null
        ) // Запрос всех записей из таблицы favorites.
        Log.d("DatabaseHelper", "Содержимое таблицы favorites:")
        if (cursor.moveToFirst()) {
            do {
                val userId =
                    cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)) // Получение ID пользователя.
                val movieId =
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MOVIE_ID)) // Получение ID фильма.
                Log.d("DatabaseHelper", "user_id=$userId, movie_id=$movieId") // Вывод записи в лог.
            } while (cursor.moveToNext())
        } else {
            Log.d("DatabaseHelper", "Таблица favorites пуста") // Лог, если таблица пуста.
        }
        cursor.close() // Закрытие курсора.
        db.close() // Закрытие базы данных.
    }
}