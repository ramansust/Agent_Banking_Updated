package com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.general

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.data.dto.config.ProductConfig
import com.datasoft.abs.databinding.GeneralAccountFragmentBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.view.dashboard.fragments.accountOpening.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class GeneralFragment : Fragment() {

    private val accountViewModel: AccountViewModel by activityViewModels()
    private val viewModel: GeneralViewModel by activityViewModels()
    private var _binding: GeneralAccountFragmentBinding? = null

    private val myCalendar: Calendar = Calendar.getInstance()

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    @Inject
    lateinit var customerAdapter: CustomerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = GeneralAccountFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        val productCategoryList = mutableListOf<CommonModel>()
        val operatingInstructionList = mutableListOf<CommonModel>()
        val currencyList = mutableListOf<CommonModel>()
        val typeOfAccountList = mutableListOf<ProductConfig>()
        val accountList = mutableListOf<ProductConfig>()
        val customerNameList = mutableListOf<CommonModel>()
        val sourceOfFundList = mutableListOf<CommonModel>()

        viewModel.getCustomerData().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        customerAdapter.differ.submitList(it.customerData)
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("TAG", "An error occurred: $message")
                    }
                }

                is Resource.Loading -> {
                }
            }
        })

        viewModel.getProductID().observe(viewLifecycleOwner, { categoryID ->

            accountList.addAll(typeOfAccountList.filter {
                it.categoryId == categoryID
            })

            binding.spinnerTypeOfAccount.adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item, accountList)
        })

        accountViewModel.getConfigData().observe(viewLifecycleOwner, { response ->

            when (response) {
                is Resource.Success -> {
                    response.data?.let {

                        productCategoryList.addAll(it.productCategoryList)
                        binding.spinnerProductCategory.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                productCategoryList
                            )

                        operatingInstructionList.addAll(it.operationInstructionList)
                        binding.spinnerOperatingInstruction.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                operatingInstructionList
                            )

                        currencyList.addAll(it.currencyList)
                        binding.spinnerCurrency.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                currencyList
                            )

                        typeOfAccountList.addAll(it.productConfig)

                        customerNameList.addAll(it.customerList)
                        binding.spinnerCustomerName.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                customerNameList
                            )

                        sourceOfFundList.addAll(it.sourceOfFundList)
                        binding.spinnerSourceOfFund.adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_spinner_item,
                                sourceOfFundList
                            )
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("TAG", "An error occurred: $message")
                    }
                }
                is Resource.Loading -> {
                }
            }
        })

        viewModel.getSavedData().observe(viewLifecycleOwner, { response ->
            with(binding) {

            }
        })

        binding.spinnerProductCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.setProductID(productCategoryList[position].id)
                }
            }

        binding.spinnerCustomerName.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.customerData(customerNameList[position].id.toString())
                }
            }

        binding.spinnerTypeOfAccount.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    accountViewModel.transactionProfileData(accountList[position].id)
                }
            }

        binding.edTxtOpeningDate.setOnClickListener {
            DatePickerDialog(
                requireContext(), date, myCalendar[Calendar.YEAR],
                myCalendar[Calendar.MONTH],
                myCalendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        binding.btnNext.setOnClickListener {
            accountViewModel.requestCurrentStep(1)
        }
    }

    private var date = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateLabel()
    }

    private fun updateLabel() {
        val myFormat = "MM-dd-yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        binding.edTxtOpeningDate.setText(sdf.format(myCalendar.time))
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = customerAdapter
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}