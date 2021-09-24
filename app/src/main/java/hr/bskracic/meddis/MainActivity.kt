package hr.bskracic.meddis

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hr.bskracic.meddis.databinding.ActivityMainBinding
import hr.bskracic.meddis.ui.edits.medication.EditMedicationFragment
import hr.bskracic.meddis.ui.edits.therapy.NewTherapyFragment
import hr.bskracic.meddis.ui.edits.therapy.THERAPY_ID

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        supportActionBar?.title = "Meddis"

        // Setup bottom menu
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val navView: BottomNavigationView = findViewById(R.id.bottom_menu)
        navView.setupWithNavController(navController)

        // FAB configuration
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.nav_medication -> {
                    fab.visibility = View.VISIBLE
                    fab.setOnClickListener {
                        openEditMedication()
                    }
                    navView.visibility = View.VISIBLE
                }
                R.id.nav_therapy -> {
                    fab.visibility = View.VISIBLE
                    fab.setOnClickListener {
                        openEditTherapy()
                    }
                    navView.visibility = View.VISIBLE
                }
                R.id.nav_edit_therapy -> {
                    fab.visibility = View.GONE
                    navView.visibility = View.GONE
                }
                else -> {
                    fab.visibility = View.GONE
                    navView.visibility = View.VISIBLE
                }
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        supportActionBar?.title = "Meddis"
        findViewById<BottomNavigationView>(R.id.bottom_menu).visibility = View.VISIBLE
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun openEditMedication() {
        EditMedicationFragment.newInstance(0).show(supportFragmentManager, null)
    }

    private fun openEditTherapy() {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        navController.navigate(R.id.nav_new_therapy)
        findViewById<BottomNavigationView>(R.id.bottom_menu).visibility = View.GONE
    }
}