package com.example.ipizza.presentation.fragment.bottomFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.ipizza.R
import com.example.ipizza.presentation.contract.navigator
import com.example.ipizza.domain.model.CartModel
import com.example.ipizza.databinding.BottomSheetLayoutBinding
import com.example.ipizza.presentation.fragment.PreviewPizzaFragment.PreviewPizzaFragment
import com.example.ipizza.presentation.fragment.FragmentMainMenu.FragmentMainMenu
import com.example.ipizza.presentation.fragment.FragmentMainMenu.FragmentMainMenuViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomFragment() : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetLayoutBinding
    private lateinit var viewModel: FragmentMainMenuViewModel

    private var urlImgPizza:ArrayList<String> = ArrayList()
    private var namePizza:String = ""
    private var descriptPizza:String = ""
    private var costPizza:String = ""
    private var idPizza:Int = 0

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FragmentMainMenuViewModel::class.java)

        idPizza = requireArguments().getInt(fragmentArg1)

        viewModel.searchSpecificPizza(idPizza)

    }

    companion object {

        val fragmentArg1 = "idPizza"

        fun myNewInstance(
            idPizza:Int
        ): BottomFragment {
            val bottomFragment = BottomFragment()
            val args = Bundle()
            args.putInt(fragmentArg1, idPizza)
            bottomFragment.setArguments(args)

            return bottomFragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = BottomSheetLayoutBinding.bind(inflater.inflate(R.layout.bottom_sheet_layout, container))

        dialog?.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
            val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.getSpecificPizza {

            namePizza = it.name
            descriptPizza = it.description
            costPizza = it.price.toString()
            urlImgPizza = it.imageUrls!!

            binding.nameRowPizza.text = namePizza

            binding.descriptRowPizza.text = descriptPizza

            Glide.with(binding.previewPizza)
                .load(urlImgPizza[0])
                .centerCrop()
                .into(binding.previewPizza)

            binding.costCartBottomLayout.text = costPizza+ "₽"

            viewModel.searchSpecificOrderPizza(it.name)

            binding.addCartButton.setOnClickListener() {

                    val tmpOrder = CartModel()
                    tmpOrder.imgUrl = urlImgPizza[0]
                    tmpOrder.price = costPizza.toInt()
                    tmpOrder.quantity =1
                    tmpOrder.name = namePizza
                    viewModel.insertOrderDataRoom(tmpOrder)

                    val fragment = FragmentMainMenu.newInstance(true)

                    this.dismiss()

                    navigator().replaceMainMenu(fragment, true)

            }



            binding.previewPizza.setOnClickListener(){
                this.dismiss()
                val fragment = PreviewPizzaFragment.newInstance(idPizza)//urlImgPizza,namePizza, costPizza)
                navigator().showNewScreen(fragment)
            }
        }

        viewModel.getSpecificOrderPizza(){ specificOrderPizza->

            binding.addCartButton.setOnClickListener() {

                specificOrderPizza.quantity++
                viewModel.insertOrderDataRoom(specificOrderPizza)

                val fragment = FragmentMainMenu.newInstance(true)

                this.dismiss()

                navigator().replaceMainMenu(fragment, true)

            }
        }
    }

    override fun onDestroy() {
        viewModel.clearComposite()
        super.onDestroy()
    }


}