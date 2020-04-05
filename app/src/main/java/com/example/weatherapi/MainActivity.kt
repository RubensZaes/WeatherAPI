package com.example.weatherapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.weatherapi.model.OpenWeatherResponse
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    companion object {
        const val BASE_URL = "http://api.openweathermap.org/"
        const val API_KEY = "583d97e571ea0e8afbe53a0c2f310232"
    }

    private lateinit var cityEditText: EditText
    private lateinit var countryEditText: EditText
    private lateinit var cityValueTextView: TextView
    private lateinit var weatherValueTextView: TextView
    private lateinit var temperaturaValueTextView: TextView
    private lateinit var feelsLikeValueTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.cityEditText = findViewById(R.id.cityEditText)
        this.countryEditText = findViewById(R.id.countryEditText)
        this.cityValueTextView = findViewById(R.id.cityValueTextView)
        this.weatherValueTextView = findViewById(R.id.weatherValueTextView)
        this.temperaturaValueTextView = findViewById(R.id.temperatureValueTextView)
        this.feelsLikeValueTextView = findViewById(R.id.feelsLikeValueTextView)

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener(View.OnClickListener { getWeather() })
    }

    private fun getWeather() {
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(OpenWeatherInterface::class.java)
        val call = service.getCurrentyWeatherByCity(cityEditText.text.toString(), API_KEY)

        call.enqueue(object : Callback<OpenWeatherResponse>{
            override fun onFailure(call: Call<OpenWeatherResponse>?, t: Throwable?) {
                Toast.makeText(baseContext, t?.message, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<OpenWeatherResponse>?,
                response: Response<OpenWeatherResponse>?
            ) {
                if (response?.code() == 200) {
                    val responseWeather = response.body()

                    cityValueTextView.text = "${responseWeather.name}"
                    weatherValueTextView.text = "${responseWeather.weather[0].main} (${responseWeather.weather[0].description})"
                    temperaturaValueTextView.text = "${responseWeather.main.temp} ºC"
                    feelsLikeValueTextView.text = "${responseWeather.main.feelsLike} ºC"
                }
            }

        })
    }
}
