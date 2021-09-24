package hr.bskracic.meddis.ui.edits

import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.view.View
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import hr.bskracic.meddis.R
import hr.bskracic.meddis.viewmodels.ViewRepo

abstract class EditItemFragment<T>(@LayoutRes contentLayoutRes: Int) : Fragment(contentLayoutRes) {
    protected var saveButton: Button? = null
    protected var deleteButton: Button? = null

    abstract val viewModel: ViewRepo<T>
    abstract val ITEM_ID: String
    var isUpdating: Boolean = false
    var item: T? = null
    var itemId: Int = 0

    abstract fun populateView()
    abstract fun updateItemFromView(): T
    abstract fun validateItem(): String?

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if user is creating new item or updating an existing one (and retrieve it)
        // Could be on earlier stages of lifecycle?
        isUpdating = if(itemId == 0) {
            deleteButton?.visibility = View.GONE
            populateView()
            false
        } else {
            viewModel.getById(itemId).observe(viewLifecycleOwner, {
                item = it
                populateView()
            })
            true
        }

        // If user is creating new item, insert it, otherwise update it
        saveButton?.setOnClickListener {
            item = updateItemFromView()
            val message = validateItem()
            if(message == null) {
                saveChanges()
                removeSelf()
            } else {
                val builder = SpannableStringBuilder()
                builder.append("   $message")
                builder.setSpan(ImageSpan(requireContext(), R.drawable.ic_warning), 0,  1 , 0 )
                Snackbar.make(requireView(), builder, Snackbar.LENGTH_SHORT).show()
            }
        }

        deleteButton?.setOnClickListener {
            deleteItem()
            removeSelf()
        }
    }

    private fun removeSelf() {
        activity?.findNavController(R.id.nav_host_fragment_content_main)?.popBackStack()
    }

    private fun saveChanges() {
        if(item == null) return
        if(isUpdating) {
            viewModel.update(item!!)
        } else {
            viewModel.insert(item!!)
        }
    }

    private fun deleteItem() {
        if(item == null) return
        viewModel.delete(item!!)
    }
}