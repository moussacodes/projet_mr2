package com.mr2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mr2.dao.NoteDao
import com.mr2.dao.PasswordDao
import com.mr2.dao.VocalDao
import com.mr2.entity.Note
import com.mr2.entity.Password
import com.mr2.entity.Vocal

//Database annotation to specify the entities and set version
@Database(entities = [Note::class, Vocal::class,  Password::class], version = 3, exportSchema = false)
abstract class NoteRoomDatabase : RoomDatabase() {
    abstract fun getNoteDao(): NoteDao

    abstract fun getVocalDao(): VocalDao
    abstract fun getPasswordDao(): PasswordDao
    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        fun getDatabase(context: Context): NoteRoomDatabase {
            return INSTANCE
                ?: synchronized(this) {
                // Create database here
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "note_db"
                )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration() //permet à room de recréer la bdd si une migration est trouvé
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}