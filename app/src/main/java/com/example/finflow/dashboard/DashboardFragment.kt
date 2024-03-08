import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.finflow.R
import com.example.finflow.creditActivities.CreditAppFragment
import com.example.finflow.dashboard.DashboardViewModel
import com.example.finflow.dashboard.DashboardViewModelFactory
import com.example.finflow.databinding.FragmentDashboardBinding
import com.example.finflow.databinding.FragmentHomeBinding
import com.example.finflow.debitAppLogic.Calculations
import com.example.finflow.goals.GoalsFragment
import com.example.finflow.goals.LevelLogic
import com.example.finflow.statistics.StatisticsFragment
import com.example.finflow.transactionHistory.TransactionFragment

class DashboardFragment : Fragment() {


    lateinit var bind: FragmentDashboardBinding
    lateinit var viewModel: DashboardViewModel

    companion object {
        fun newInstance(param1: String, param2: String): DashboardFragment {
            val fragment = DashboardFragment()
            val args = Bundle()
            args.putString("ARG_PARAM1", param1)
            args.putString("ARG_PARAM2", param2)
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this, DashboardViewModelFactory(requireActivity()))[DashboardViewModel::class.java]

        // Use DataBindingUtil to inflate the layout
        bind = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        val contextOfMain = requireContext()
        val levelText = "Level ${LevelLogic(contextOfMain).getLevelInSharedPref()}"
        bind.printLevelInDashboard.text = Editable.Factory.getInstance().newEditable(levelText)

        // LevelLogic(contextOfMain).saveNewLevelInSharedPref(0)
        val arr = resources.getStringArray(R.array.level_titles)
        val arr2 = resources.getIntArray(R.array.level_colors)
        Log.e("Level", LevelLogic(contextOfMain).getLevelInSharedPref().toString())
        bind.printLevelNameInDashboard.text = Editable.Factory.getInstance().newEditable(arr[LevelLogic(contextOfMain).getLevelInSharedPref()].toString())
      //  bind.levelDisplay.setTextColor((arr2[LevelLogic(contextOfMain).getLevelInSharedPref()]))

        bind.printNextTarget.text = Editable.Factory.getInstance().newEditable("${LevelLogic(contextOfMain).getLevelInSharedPref()*50 + 50} hours")


        getAmount()
        buttonListeners()

        return bind.root
    }


    fun initializingAmountUI(amt: Float){
        bind.mainbalance.text = amt.toLong().toString()
        bind.fractionalpart.text = (amt - amt.toLong()).toLong().toString()
    }

    fun getAmount(){
        viewModel.getMyLiveData().observe(viewLifecycleOwner){
            newValue ->
            initializingAmountUI(newValue)
        }
    }

    fun buttonListeners(){
        bind.dashboardAddButton.setOnClickListener(){
            replaceFragment(GoalsFragment())
        }
        bind.dashboardEnjoyButton.setOnClickListener(){
            replaceFragment(GoalsFragment())
        }
        bind.dashboardWalletButton.setOnClickListener(){
            replaceFragment(TransactionFragment())
        }
        bind.dashboardStatsButton.setOnClickListener(){
            replaceFragment(StatisticsFragment(10))
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragTrans = parentFragmentManager.beginTransaction()
        fragTrans.replace(R.id.nav_host_fragment, fragment)
        fragTrans.addToBackStack(null)
        fragTrans.commit()
    }

}