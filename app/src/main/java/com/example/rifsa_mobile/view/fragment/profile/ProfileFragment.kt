package com.example.rifsa_mobile.view.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rifsa_mobile.databinding.FragmentProfileBinding
import com.example.rifsa_mobile.model.entity.remote.finance.FinanceResponseData
import com.example.rifsa_mobile.model.entity.remote.harvestresult.HarvestResponData
import com.example.rifsa_mobile.model.entity.remote.inventory.InventoryResultResponData
import com.example.rifsa_mobile.view.authetication.login.LoginActivity
import com.example.rifsa_mobile.viewmodel.RemoteViewModel
import com.example.rifsa_mobile.viewmodel.UserPrefrencesViewModel
import com.example.rifsa_mobile.viewmodel.utils.ViewModelFactory


class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding

    private val authViewModel : UserPrefrencesViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    private val remoteViewModel : RemoteViewModel by viewModels { ViewModelFactory.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        authViewModel.getUserName().observe(viewLifecycleOwner){
            binding.tvprofileName.text = it
            binding.tvSignupEmail.setText(it)
        }

        authViewModel.getTokenKey().observe(viewLifecycleOwner){
            binding.tvSignupPassword.setText(it)
        }

        binding.btnShowFarming.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToMapsDiseaseFragment(map_key)
            )
        }


        binding.btnprofileLogout.setOnClickListener {
            authViewModel.saveUserPrefrences(
                true,
                "",
                "",
                ""
            )
            startActivity(Intent(requireContext(),LoginActivity::class.java))
            activity?.finishAffinity()
        }

        showSummaryRemote()

        return binding.root
    }



    private fun showSummaryRemote(){
        authViewModel.getUserId().observe(viewLifecycleOwner){ token->

        }
    }


    private fun summaryHarvest(harvest : List<HarvestResponData>){
        harvest.forEach { value->
            binding.tvsumHarvestAmount.text = harvest.size.toString()
            (harvest.sumOf { value.berat.toInt() }.toString() + "  kg").also { binding.tvsumHarvestWeight.text = it }
            ("Rp " + harvest.sumOf { value.jual.toInt() }.toString()).also { binding.tvsumHarvestHarga.text = it }
        }
    }

    private fun summaryFinance(finance : List<FinanceResponseData>){
        finance.forEach { _ ->
            ("Rp " + finance.sumOf { it.jumlah.toInt() }.toString()).also { binding.tvsumFinanceOut.text = it }
        }
    }

    private fun summaryInventory(data : List<InventoryResultResponData>){
        binding.tvsumInventoryAmount.text = data.size.toString()
    }

    companion object{
        const val map_key = "profile"
    }



}