
package com.mr2.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mr2.dao.NoteDao
import com.mr2.dao.PasswordDao
import com.mr2.database.NoteRoomDatabase
import com.mr2.entity.Note
import com.mr2.entity.Password

class PasswordViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val listPasswords = MutableLiveData<ArrayList<Password>>()
    private var dao: PasswordDao

    init {
        val database = NoteRoomDatabase.getDatabase(context)
        dao = database.getPasswordDao()
    }

    fun setPassword() {
        val listItems = arrayListOf<Password>()

        listItems.addAll(dao.getAll())
        listPasswords.postValue(listItems)
    }

    fun setPasswordByTitle(title: String) {
        val listItems = arrayListOf<Password>()

        listItems.addAll(dao.getByTitle(title))
        listPasswords.postValue(listItems)
    }

    fun insertPassword(password: Password){
        dao.insert(password)
    }

    fun getById(id: Int): Password {
        return dao.getById(id)
    }
    fun updatePassword(password: Password){
        dao.update(password)
    }

    fun deletePassword(password: Password){
        dao.delete(password)
    }

    fun getPasswords(): LiveData<ArrayList<Password>> {
        return listPasswords
    }
}

