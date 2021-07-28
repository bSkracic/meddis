package hr.bskracic.meddis.ui.therapy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import hr.bskracic.meddis.databinding.FragmentTherapyBinding

class TherapyFragment : Fragment() {

    private lateinit var therapyViewModel: TherapyViewModel
    private var _binding: FragmentTherapyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        therapyViewModel =
            ViewModelProvider(this).get(TherapyViewModel::class.java)

        _binding = FragmentTherapyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}