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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.datasoft.abs.R
import com.datasoft.abs.data.dto.config.CommonModel
import com.datasoft.abs.data.dto.config.ProductConfig
import com.datasoft.abs.databinding.GeneralAccountFragmentBinding
import com.datasoft.abs.presenter.states.Resource
import com.datasoft.abs.presenter.utils.Constant.DATE_FORMAT
import com.datasoft.abs.presenter.utils.ToastHelper
import com.datasoft.abs.presenter.utils.showToast
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
    private var isClicked = false

    @Inject
    lateinit var customerAdapter: CustomerAdapter

    @Inject
    lateinit var toastHelper: ToastHelper

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

        accountViewModel.requestVisibility(false)
        accountViewModel.requestListener(false)

        setupRecyclerView()

        toastHelper.toastMessages.startListening {
            showToast(it)
        }

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
                    response.data?.let { data ->
                        customerAdapter.differ.submitList(null)
                        customerAdapter.differ.submitList(data.customerData)

                        binding.btnNext.isEnabled = data.customerData.isNotEmpty()
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

            accountList.clear()

            accountList.addAll(typeOfAccountList.filter {
                it.categoryId == categoryID
            })

            binding.spinnerTypeOfAccount.adapter =
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item, accountList
                )
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

        viewModel.getAccountInfo().observe(viewLifecycleOwner, { response ->
            when (response) {

                is Resource.Success -> {
                    response.data?.let {

                        if (productCategoryList.isNotEmpty()) binding.spinnerProductCategory.setSelection(
                            productCategoryList.indexOf(
                                CommonModel(it.categoryId)
                            )
                        )

                        if (accountList.isNotEmpty()) binding.spinnerTypeOfAccount.setSelection(
                            accountList.indexOf(
                                ProductConfig(id = it.accountId)
                            )
                        )

                        if (operatingInstructionList.isNotEmpty()) binding.spinnerOperatingInstruction.setSelection(
                            operatingInstructionList.indexOf(
                                CommonModel(it.operatingId)
                            )
                        )

                        if (sourceOfFundList.isNotEmpty()) binding.spinnerSourceOfFund.setSelection(
                            sourceOfFundList.indexOf(
                                CommonModel(it.fundId)
                            )
                        )

                        if (currencyList.isNotEmpty()) binding.spinnerCurrency.setSelection(
                            currencyList.indexOf(
                                CommonModel(it.currencyId)
                            )
                        )

                        binding.spinnerCustomerName.text = it.customerId
                        binding.edTxtAccountTitle.setText(it.accountTitle)
                        binding.edTxtOpeningDate.setText(it.openingDate)
                        binding.edTxtInitialAmount.setText(it.initialAmount.toString())

                        if (isClicked)
                            accountViewModel.requestCurrentStep(1)
                    }
                }

                is Resource.Error -> {
                    response.message?.let { message ->
                        toastHelper.sendToast(message)
                    }
                }

                is Resource.Loading -> {

                }

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

        binding.spinnerCustomerName.setOnClickListener {
            showDialog(
                customerNameList,
                !operatingInstructionList[binding.spinnerOperatingInstruction.selectedItemPosition].name.contains(
                    "Single Account",
                    true
                )
            )
        }

        binding.btnNext.setOnClickListener {
            isClicked = true

            viewModel.setAccountInfo(
                if (productCategoryList.isNotEmpty()) productCategoryList[binding.spinnerProductCategory.selectedItemPosition].id else 0,
                if (productCategoryList.isNotEmpty()) productCategoryList[binding.spinnerProductCategory.selectedItemPosition].name else "",
                if (accountList.isNotEmpty()) accountList[binding.spinnerTypeOfAccount.selectedItemPosition].id else 0,
                if (accountList.isNotEmpty()) accountList[binding.spinnerTypeOfAccount.selectedItemPosition].name else "",
                if (operatingInstructionList.isNotEmpty()) operatingInstructionList[binding.spinnerOperatingInstruction.selectedItemPosition].id else 0,
                if (operatingInstructionList.isNotEmpty()) operatingInstructionList[binding.spinnerOperatingInstruction.selectedItemPosition].name else "",
                binding.spinnerCustomerName.text.trim().toString(),
                binding.edTxtAccountTitle.text.trim().toString(),
                binding.edTxtOpeningDate.text.trim().toString(),
                if (currencyList.isNotEmpty()) currencyList[binding.spinnerCurrency.selectedItemPosition].id else 0,
                if (currencyList.isNotEmpty()) currencyList[binding.spinnerCurrency.selectedItemPosition].name else "",
                if (sourceOfFundList.isNotEmpty()) sourceOfFundList[binding.spinnerSourceOfFund.selectedItemPosition].id else 0,
                if (binding.edTxtInitialAmount.text.trim().toString()
                        .isNotEmpty()
                ) binding.edTxtInitialAmount.text.trim().toString().toInt() else 0
            )
        }
    }

    private var date = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        myCalendar.set(Calendar.YEAR, year)
        myCalendar.set(Calendar.MONTH, monthOfYear)
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateLabel()
    }

    private fun updateLabel() {
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
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

    private fun showDialog(list: List<CommonModel>, isMultiple: Boolean) {

        lateinit var dialog: AlertDialog

        val customerArray = mutableListOf<String>()
        val customerID = mutableListOf<Int>()
        val arrayChecked = mutableListOf<Boolean>()

        var selectedCustomer = ""
        var selectedCustomerID = ""

        for (value in list) {
            customerArray.add(value.name)
            arrayChecked.add(false)
            customerID.add(value.id)
        }

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.select_customer_name))
        if (isMultiple) {
            builder.setMultiChoiceItems(
                customerArray.toTypedArray(),
                null
            ) { _, which, isChecked ->
                arrayChecked[which] = isChecked
            }
        } else {
            builder.setSingleChoiceItems(customerArray.toTypedArray(), -1) { _, which ->
                selectedCustomer = customerArray[which]
                selectedCustomerID = customerID[which].toString()
            }
        }

        builder.setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
            if (isMultiple) {
                val stringBuilder = StringBuffer()
                val stringBuilderID = StringBuffer()
                for (i in customerArray.indices) {
                    val checked = arrayChecked[i]
                    if (checked) {
                        stringBuilder.append(customerArray[i] + ", ")
                        stringBuilderID.append(customerID[i].toString() + ", ")
                    }
                }

                if (stringBuilder.length > 2 && stringBuilderID.length > 2) {
                    binding.spinnerCustomerName.text =
                        stringBuilder.toString().subSequence(0, stringBuilder.length - 2)
                    viewModel.customerData(stringBuilderID.substring(0, stringBuilderID.length - 2))
                }
            } else {
                if (selectedCustomer.isNotEmpty() && selectedCustomerID.isNotEmpty()) {
                    binding.spinnerCustomerName.text = selectedCustomer
                    viewModel.customerData(selectedCustomerID)
                }
            }
        }

        dialog = builder.create()
        dialog.show()
    }
}