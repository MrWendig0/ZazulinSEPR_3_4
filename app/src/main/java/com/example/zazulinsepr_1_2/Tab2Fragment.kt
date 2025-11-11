    package com.example.zazulinsepr_1_2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.zazulinsepr_1_2.testApiPackage.RetrofitClientAdvice
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zazulinsepr_1_2.apiPackage.RetrofitClient
import com.example.zazulinsepr_1_2.apiPackage.Todo
import com.example.zazulinsepr_1_2.apiPackage.TodoAdapter
import com.example.zazulinsepr_1_2.testApiPackage.AdviceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

    // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Tab2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Tab2Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var textAdvice: TextView
    private lateinit var buttonGetAdvice: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab2, container, false)
    }

    private fun showLoadingToast() {
        Toast.makeText(requireContext(), "Загружены 2 tab", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textAdvice = view.findViewById(R.id.textAdvice)
        buttonGetAdvice = view.findViewById(R.id.buttonGetAdvice)
        progressBar = view.findViewById(R.id.progressBarAdvice)

        buttonGetAdvice.setOnClickListener {
            loadAdvice()
        }

        showLoadingToast()
    }


    private fun loadAdvice() {
        progressBar.visibility = View.VISIBLE
        buttonGetAdvice.isEnabled = false

        val call = RetrofitClientAdvice.adviceApi.getRandomAdvice()
        call.enqueue(object : Callback<AdviceResponse> {
            override fun onResponse(call: Call<AdviceResponse>, response: Response<AdviceResponse>) {
                progressBar.visibility = View.GONE
                buttonGetAdvice.isEnabled = true

                if (response.isSuccessful) {
                    val advice = response.body()?.slip?.advice ?: "Совет не найден"
                    textAdvice.text = advice
                } else {
                    textAdvice.text = "Ошибка сервера: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<AdviceResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                buttonGetAdvice.isEnabled = true
                textAdvice.text = "Ошибка сети: ${t.message}"
            }
        })
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Tab2Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Tab2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}