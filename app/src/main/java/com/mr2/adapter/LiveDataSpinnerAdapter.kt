import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.mr2.R
import com.mr2.entity.Tag

class LiveDataSpinnerAdapter(
    context: Context,
    private val data: LiveData<List<Tag>>,
    lifecycleOwner: LifecycleOwner
) : ArrayAdapter<Tag>(context, R.layout.tag_item) {

    init {
        // Observe the LiveData and update the adapter when the data changes
        data.observe(lifecycleOwner, Observer { newData ->
            clear()
            if (newData != null) {
                addAll(newData)
                notifyDataSetChanged()
            }
        })
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Customize the view for the spinner's closed state if needed
        return super.getView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Customize the dropdown view for the spinner's opened state if needed
        return super.getDropDownView(position, convertView, parent)
    }
}
