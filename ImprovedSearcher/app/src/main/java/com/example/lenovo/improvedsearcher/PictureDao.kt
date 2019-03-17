package com.example.lenovo.improvedsearcher

import android.arch.persistence.room.*

@Dao
interface PictureDao {

    @android.arch.persistence.room.Query("SELECT * FROM picture")
    fun getAll(): List<Picture>


    @android.arch.persistence.room.Query("SELECT * FROM picture WHERE id = :id")
    fun getById(id: String?): Picture

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: Picture)

    @Update
    fun update(photo: Picture)

    @Delete
    fun delete(photo: Picture)

    @android.arch.persistence.room.Query("SELECT COUNT(*) FROM picture WHERE id = :id")
    fun exist(id: String?): Boolean

}