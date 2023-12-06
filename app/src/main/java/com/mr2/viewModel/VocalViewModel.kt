package com.mr2.viewModel



import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mr2.dao.NoteDao
import com.mr2.dao.VocalDao
import com.mr2.database.NoteRoomDatabase
import com.mr2.entity.Note
import com.mr2.entity.Vocal

class VocalViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val listVocals = MutableLiveData<ArrayList<Vocal>>()
    private var dao: VocalDao

    init {
        val database = NoteRoomDatabase.getDatabase(context)
        dao = database.getVocalDao()
    }

    fun setVocals() {
        val listItems = arrayListOf<Vocal>()

        listItems.addAll(dao.getAll())
        listVocals.postValue(listItems)
    }


    fun setVocalsByTitle(title: String) {
        val listItems = arrayListOf<Vocal>()

        listItems.addAll(dao.getByTitle(title))
        listVocals.postValue(listItems)
    }

    fun insertVocal(vocal: Vocal){
        dao.insert(vocal)
    }

    fun deleteVocal(vocal: Vocal){
        dao.delete(vocal)
    }

    fun getVocals(): LiveData<ArrayList<Vocal>> {
        return listVocals
    }


}

