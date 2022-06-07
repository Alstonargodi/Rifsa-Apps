package com.example.rifsa_mobile.view.fragment.finance

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rifsa_mobile.R
import com.example.rifsa_mobile.databinding.FragmentFinanceBinding
import com.example.rifsa_mobile.model.entity.local.finance.Finance
import com.example.rifsa_mobile.model.entity.remote.finance.FinanceResponseData
import com.example.rifsa_mobile.utils.FetchResult
import com.example.rifsa_mobile.view.fragment.finance.adapter.FinanceRvAdapter
import com.example.rifsa_mobile.viewmodel.LocalViewModel
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.utils.ObtainViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch


class FinanceFragment : Fragment() {
    private lateinit var binding : FragmentFinanceBinding
    private val remoteViewModel : RemoteViewModel by viewModels{ ViewModelFactory.getInstance(requireContext()) }

    private lateinit var dataList: ArrayList<FinanceResponseData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinanceBinding.inflate(layoutInflater)
//        viewModel = ObtainViewModel(requireActivity())
        val bottomMenu = requireActivity().findViewById<BottomNavigationView>(R.id.main_bottommenu)
        bottomMenu.visibility = View.VISIBLE

        dataList = arrayListOf()

        showFinanceList()

        binding.fabFiannceInsert.setOnClickListener {
            findNavController().navigate(
                FinanceFragmentDirections.actionFinanceFragmentToFinanceInsertDetailFragment(null))
        }


        return binding.root
    }


    private fun showFinanceList(){
        lifecycleScope.launch{
            remoteViewModel.getFinance().observe(viewLifecycleOwner){
                when(it){
                    is FetchResult.Success->{
                        it.data.financeResponseData.forEach { respon ->
                            dataList.add(respon)

                            val adapter = FinanceRvAdapter(dataList)
                            val recview = binding.rvFinance
                            recview.adapter = adapter
                            recview.layoutManager = LinearLayoutManager(requireContext())

                            adapter.onItemCallBack(object : FinanceRvAdapter.ItemDetailCallback{
                                override fun onItemCallback(data: FinanceResponseData) {
                                    findNavController().navigate(
                                        FinanceFragmentDirections.actionFinanceFragmentToFinanceInsertDetailFragment(data))
                                }
                            })
                            if (dataList.isEmpty()){
                                binding.financeEmptyState.emptyState.visibility =
                                    View.VISIBLE
                            }
                        }
                    }
                    is FetchResult.Error->{
                        showToast(it.error)
                        Log.d("Read Finance Result",it.error)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun showToast(title : String){
        Toast.makeText(requireContext(),title, Toast.LENGTH_SHORT).show()
    }
}