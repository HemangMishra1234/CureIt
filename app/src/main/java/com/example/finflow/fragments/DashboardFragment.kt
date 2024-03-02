import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.finflow.R
import com.example.finflow.databinding.FragmentDashboardBinding
import com.example.finflow.databinding.FragmentHomeBinding
import com.example.finflow.goals.LevelLogic

class DashboardFragment : Fragment() {


    lateinit var bind: FragmentDashboardBinding

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




        return bind.root
    }


}