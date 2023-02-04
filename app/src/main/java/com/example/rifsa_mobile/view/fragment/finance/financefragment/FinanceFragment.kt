package com.example.rifsa_mobile.view.fragment.finance.financefragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentFinanceBinding
import com.example.rifsa_mobile.model.entity.remotefirebase.FinancialEntity
import com.example.rifsa_mobile.view.fragment.finance.adapter.FinanceRecyclerViewAdapter
import com.example.rifsa_mobile.viewmodel.remoteviewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.userpreferences.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.viewmodelfactory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch


class FinanceFragment : Fragment() {
    private lateinit var binding : FragmentFinanceBinding
    private val viewModel : RemoteViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }
    private val authViewModel : UserPrefrencesViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private var dataList = ArrayList<FinancialEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinanceBinding.inflate(layoutInflater)

        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE


        binding.fabFiannceInsert.setOnClickListener {
            findNavController().navigate(
                FinanceFragmentDirections.actionFinanceFragmentToFinanceInsertDetailFragment(
                    null
                )
            )
        }

        authViewModel.getUserId().observe(viewLifecycleOwner){ userId ->
            binding.pgbFinanceBar.visibility = View.VISIBLE
            getFinanceList(userId)
        }

        return binding.root
    }


    private fun getFinanceList(userId : String){
        lifecycleScope.launch{
            viewModel.readFinancial(userId).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        snapshot.children.forEach { child ->
                            child.children.forEach { main ->
                                val data = main.getValue(FinancialEntity::class.java)
                                data?.let { dataList.add(data) }
                                showFinancialList(dataList)
                                dataChecker(dataList.size)
                            }
                        }
                    }else{
                        dataChecker(0)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showStatus(error.message)
                }
            })
        }
    }

    private fun showFinancialList(data : List<FinancialEntity>){
        try {
            binding.pgbFinanceBar.visibility = View.GONE
            val adapter = FinanceRecyclerViewAdapter(data)
            val recyclerView = binding.rvFinance
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter.onItemCallBack(object : FinanceRecyclerViewAdapter.ItemDetailCallback{
                override fun onItemCallback(data: FinancialEntity) {
                    findNavController().navigate(
                        FinanceFragmentDirections.actionFinanceFragmentToFinanceInsertDetailFragment(data)
                    )
                }
            })
        }catch (e : Exception){
            Log.d("FinanceFragment",e.message.toString())
        }
    }

    private fun showStatus(title : String){
        binding.pgbKeaunganTitle.visibility = View.VISIBLE
        binding.pgbKeaunganTitle.text = title
        if (title.isNotEmpty()){
            binding.pgbFinanceBar.visibility = View.GONE
        }
        Log.d("FinanceFragment",title)
    }

    private fun dataChecker(total : Int){
        if (total == 0){
            binding.pgbFinanceBar.visibility = View.GONE
            binding.financeEmptyState.emptyState.visibility = View.VISIBLE
        }
    }
}