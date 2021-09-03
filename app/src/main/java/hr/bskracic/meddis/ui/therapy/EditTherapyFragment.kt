package hr.bskracic.meddis.ui.therapy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.bskracic.meddis.R

private const val THERAPY_ID = "THERAPY_ID"

class EditTherapyFragment : Fragment() {
    private var therapyID: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            therapyID = it.getInt(THERAPY_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_therapy, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param therapyID Therapy ID or 0 for new therapy item.
         * @return A new instance of fragment EditTherapyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(therapyID: Int) =
            EditTherapyFragment().apply {
                arguments = Bundle().apply {
                    putInt(THERAPY_ID, therapyID)
                }
            }
    }
}