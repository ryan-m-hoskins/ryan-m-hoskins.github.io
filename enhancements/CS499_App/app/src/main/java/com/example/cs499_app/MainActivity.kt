package com.example.cs499_app

import WeightRecord
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cs499_app.DatabaseRepository
import com.example.cs499_app.databinding.ActivityMainBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class MainActivity :
    AppCompatActivity(),
    DateChecker,
    BottomSheetTargetWeight.TargetWeightListener,
    BottomSheetAddWeight.AddWeightListener,
    BottomSheetEditWeight.EditWeightListener {

    // View binding for MainActivity to handle UI element
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseRepository: DatabaseRepository

    // Lifecycle of activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // Initialize database repository from Firebase
        databaseRepository = DatabaseRepository()

        setupNavigation()
        }

    // == Navigation Setup == //
    private fun setupNavigation(){
        // Use supportFragmentManager to find the NavHostFragment and get the NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Set up the bottom navigation view with the NavController
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    // == Checking to see if an entered date already exists in the database to handle duplicates == //
    override fun checkDateExists(date: Long, onDuplicateFound:() -> Unit, onDateAvailable:() -> Unit, onError:(String) -> Unit) {
        // Check database using passed date
        databaseRepository.checkDateExists(date = date, onResult = { exists ->
            if (exists) onDuplicateFound()
            else onDateAvailable()
        },
            onError = onError)
    }

    // Delegate to current RecordsFragment
    private fun getCurrentRecordsFragment(): RecordsFragment? {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.childFragmentManager.primaryNavigationFragment as? RecordsFragment
    }

    // == Delegate Methods to handle fragments == //
    override fun onTargetWeightSet(weight: Double) {
        getCurrentRecordsFragment()?.onTargetWeightSet(weight)
    }

    override fun onWeightRecordSet(weight: Double, date: Long) {
        getCurrentRecordsFragment()?.onWeightRecordSet(weight, date)
    }

    override fun onWeightRecordUpdate(weightRecord: WeightRecord) {
        getCurrentRecordsFragment()?.onWeightRecordUpdate(weightRecord)
    }

    override fun onWeightRecordDelete(weightRecord: WeightRecord) {
        getCurrentRecordsFragment()?.onWeightRecordDelete(weightRecord)
    }
}

