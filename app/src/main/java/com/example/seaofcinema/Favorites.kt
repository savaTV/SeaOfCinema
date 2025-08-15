package com.example.seaofcinema

// Класс Favorites управляет операциями с избранными фильмами для конкретного пользователя
class Favorites(
    private val userId: Long, // Идентификатор пользователя, для которого выполняются операции с избранным
    private val dbHelper: DatabaseHelper // Экземпляр DatabaseHelper для взаимодействия с базой данных
) {

    // Добавляет фильм в список избранного для пользователя
    fun add(movieId: Int): Boolean {
        // Вызывает метод addToFavorites из DatabaseHelper, передавая userId и movieId
        return dbHelper.addToFavorites(userId, movieId)
    }

    // Удаляет фильм из списка избранного пользователя
    fun remove(movieId: Int): Boolean {
        // Вызывает метод removeFromFavorites из DatabaseHelper, передавая userId и movieId
        return dbHelper.removeFromFavorites(userId, movieId)
    }

    // Получает список всех избранных фильмов пользователя
    fun getAll(): List<Film> {
        // Вызывает метод getFavorites из DatabaseHelper, передавая userId
        return dbHelper.getFavorites(userId)
    }
}