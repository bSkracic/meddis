package hr.bskracic.meddis.ui.preference

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import hr.bskracic.meddis.databinding.FragmentPreferenceBinding

class PreferenceFragment : Fragment() {

    private lateinit var preferenceViewModel: PreferenceViewModel
    private var _binding: FragmentPreferenceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preferenceViewModel =
            ViewModelProvider(this).get(PreferenceViewModel::class.java)

        _binding = FragmentPreferenceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}