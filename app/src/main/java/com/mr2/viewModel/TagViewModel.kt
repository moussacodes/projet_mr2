package com.mr2.viewModel


import android.app.Application
import android.service.autofill.Transformation
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.mr2.dao.TagDao
import com.mr2.database.NoteRoomDatabase
import com.mr2.entity.Tag

class TagViewModel (application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val listTags = MutableLiveData<ArrayList<Tag>>()
    private val mSpinnerData = MutableLiveData<List<Tag>>()
    private var dao: TagDao

    init {
        val database = NoteRoomDatabase.getDatabase(context)
        dao = database.getTagDao()
    }

    fun setTags() {
        val listItems = arrayListOf<Tag>()

        listItems.addAll(dao.getAll())
        listTags.postValue(listItems)
    }

    fun fetchSpinnerItems(): LiveData<List<Tag>> {
        //fetch data
        mSpinnerData.value = listTags.value
        return mSpinnerData
    }

    fun setTagsByTitle(title: String) {
        val listItems = arrayListOf<Tag>()

        listItems.addAll(dao.getByTitle(title))
        listTags.postValue(listItems)
    }

    fun insertTag(tag: Tag){
        dao.insert(tag)
    }

    fun deleteTag(tag: Tag){
        dao.delete(tag)
    }

    fun getTags(): LiveData<ArrayList<Tag>> {
        return listTags
    }

    fun getTagNames(): LiveData<ArrayList<String>> {
        return listTags.map { tags ->
            val names = ArrayList<String>()
            tags?.let {
                for (tag in it) {
                    names.add(tag.name)
                }
            }
            names
        }
    }

}

