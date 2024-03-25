package com.example.finflow

import DashboardFragment
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.finflow.databinding.ActivityStart2Binding
import com.example.finflow.databinding.NavHeaderBinding
import com.example.finflow.debitAppLogic.Calculations
import com.example.finflow.debitAppLogic.Logic
import com.example.finflow.goals.GoalsFragment
import com.example.finflow.moodStatistics.EditThemeFragment
import com.example.finflow.moodStatistics.MoodTrack
import com.example.finflow.startActivity.StartActivityViewModel
import com.example.finflow.startActivity.StartActivityViewModelFactory
import com.example.finflow.statistics.StatisticsFragment
import com.example.finflow.transactionHistory.TransactionFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class StartActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navHeaderBinding: NavHeaderBinding
    lateinit var binding: ActivityStart2Binding
    private lateinit var fdatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start2)

        fdatabase = Firebase.database.reference
        drawerLayout= binding.root as DrawerLayout
        toggle = ActionBarDrawerToggle(
            this@StartActivity,drawerLayout,R.string.open,R.string.close)

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

//        val btn : FloatingActionButton = binding.floatingButton
        binding.constraintLayout3.setOnClickListener() {
            drawerLayout.open()
        }

      //  Calculations(this).saveNewBalanceInSharedPref(0.0F)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        replaceFragment(DashboardFragment(),"Dashboard Fragment")

        val navView: NavigationView = binding.navView
        navView.setNavigationItemSelectedListener {
             it.isChecked = true


              when(it.itemId){
                    R.id.nav_dashboard->{
                        replaceFragment(DashboardFragment(),it.title.toString())
                    }

                    R.id.nav_transaction->{drawerLayout.closeDrawers()
                        replaceFragment(TransactionFragment(),it.title.toString())
//                        val intent = Intent(this, TransactionActivity::class.java)
//                        startActivity(intent)
                    }

                    R.id.nav_debitApps ->{
                        drawerLayout.closeDrawers()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }

                    R.id.nav_creditActivities->{
                        drawerLayout.closeDrawers()
                        replaceFragment(GoalsFragment(),it.title.toString())
                    }

                  R.id.nav_habits->{
                      drawerLayout.closeDrawers()
                      val intent = Intent(this, HabitsActivity::class.java)
                      startActivity(intent)
                  }

                  R.id.nav_stats->{
                      drawerLayout.closeDrawers()
                      replaceFragment(StatisticsFragment(15),it.title.toString())
                  }

                  R.id.nav_moodTrack->{
                      drawerLayout.closeDrawers()
                      replaceFragment(MoodTrack(),it.title.toString())
                  }
                }

            true
        }
        navHeaderBinding = DataBindingUtil.inflate(layoutInflater, R.layout.nav_header, navView.getHeaderView(0) as ViewGroup?, false)
        navView.addHeaderView(navHeaderBinding.root)

        //

// Printing the balance in the console
        val sharedPref: SharedPreferences =
            this.getSharedPreferences(Calculations(this).SHARED_PREF_NAME, MODE_PRIVATE)
        val viewModel = ViewModelProvider(this,StartActivityViewModelFactory(sharedPref,this)).get(StartActivityViewModel::class.java)

        viewModel.getMyLiveData().observe(this) { newValue ->
            navHeaderBinding.balanceNavMenu.text = Logic().formatAmountInCrores(newValue)

            //Practising firebase here
//            fdatabase.child("Balance").setValue(newValue)
        }

        //Changing the value if the balance at the firebase  changes
        fdatabase.addValueEventListener(
            object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val calculations = Calculations(this@StartActivity)
                    val newBalance = snapshot.value as HashMap<*,*>
                    Log.i("Firebase","$newBalance is the updated value")
//                    calculations.saveNewBalanceInSharedPref((newBalance)["Balance"].toString().toFloat())
                    Log.i("Firebase","Balance updated in the app")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("Firebase", "Failed to read value")
                }

            }
        )
    }
    private fun replaceFragment(fragment: Fragment, title: String = "Default"){
        val fragTrans = supportFragmentManager.beginTransaction()
        fragTrans.replace(R.id.nav_host_fragment, fragment)
        fragTrans.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }
}