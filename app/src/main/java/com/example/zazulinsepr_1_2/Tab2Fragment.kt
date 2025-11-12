package com.example.zazulinsepr_1_2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.zazulinsepr_1_2.postAPItestpackage.HttpBinResponse
import com.example.zazulinsepr_1_2.postAPItestpackage.RetrofitClientHttpBin
import com.example.zazulinsepr_1_2.testApiPackage.RetrofitClientAdvice
import com.example.zazulinsepr_1_2.testApiPackage.AdviceResponse
import com.example.zazulinsepr_1_2.postAPItestpackage.UserRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Tab2Fragment : Fragment() {

    private lateinit var textAdvice: TextView
    private lateinit var buttonGetAdvice: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var editName: EditText
    private lateinit var editJob: EditText
    private lateinit var buttonCreateUser: Button
    private lateinit var textUserResult: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tab2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Advice API элементы
        textAdvice = view.findViewById(R.id.textAdvice)
        buttonGetAdvice = view.findViewById(R.id.buttonGetAdvice)
        progressBar = view.findViewById(R.id.progressBarAdvice)

        // ReqRes элементы
        editName = view.findViewById(R.id.editName)
        editJob = view.findViewById(R.id.editJob)
        buttonCreateUser = view.findViewById(R.id.buttonCreateUser)
        textUserResult = view.findViewById(R.id.textUserResult)

        buttonGetAdvice.setOnClickListener { loadAdvice() }
        buttonCreateUser.setOnClickListener { createUserHttpBin() }
    }

    // -----------------------
    // GET-запрос к Advice API
    // -----------------------
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

    // -----------------------
    // POST-запрос к ReqRes API
    // -----------------------
    private fun createUserHttpBin() {
        val name = editName.text.toString().trim()
        val job = editJob.text.toString().trim()

        if (name.isEmpty() || job.isEmpty()) {
            Toast.makeText(requireContext(), "Введите имя и профессию", Toast.LENGTH_SHORT).show()
            return
        }

        val request = UserRequest(name, job)
        textUserResult.text = "Отправка запроса..."
        buttonCreateUser.isEnabled = false

        RetrofitClientHttpBin.api.sendData(request).enqueue(object : Callback<HttpBinResponse> {
            override fun onResponse(call: Call<HttpBinResponse>, response: Response<HttpBinResponse>) {
                buttonCreateUser.isEnabled = true
                if (response.isSuccessful) {
                    val resp = response.body()
                    textUserResult.text = """
                    Пользователь отправлен:
                    Имя: ${resp?.json?.name}
                    Работа: ${resp?.json?.job}
                """.trimIndent()
                } else {
                    textUserResult.text = "Ошибка сервера: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<HttpBinResponse>, t: Throwable) {
                buttonCreateUser.isEnabled = true
                textUserResult.text = "Ошибка сети: ${t.message}"
            }
        })
    }


}
