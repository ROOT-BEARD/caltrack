package com.example.caltrack

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class FoodCatalog {
    // create a client
    private val client = OkHttpClient()

    // create a callback interface
    interface FoodCallBack {
        fun onSuccess(food: FoodItem?)
        fun onFailure(errorMsg: String?)
    }

    //gets a food by a code and calls from the callback interface
    fun getFood(callBack: FoodCallBack, code: String?) {
        // url from openfoodfacts
        val url = "https://world.openfoodfacts.org/api/v2/product/" + code + ".json"

        // creates a new request
        val request = Request.Builder()
            .url(url)
            .header("User-agent", "calTrack - Android - Version 1.0")
            .build()

        // creates a call with request
        client.newCall(request).enqueue(object : Callback {
            //if it fails log message
            override fun onFailure(call: Call, e: IOException) {
                Log.e("Caltracker", "Failed Network Request: " + e.message)
            }

            // on response back
            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                // if the response is a success and the response back is not null, continue
                if (response.isSuccessful && response.body != null) {
                    // gets the response as a string
                    val responseText = response.body!!.string()

                    try {
                        // create a json object for the root and product
                        val root = JSONObject(responseText)
                        val product = root.optJSONObject("product")
                        // if there is a product continue
                        if (product != null) {
                            // get a json object for the nutrients
                            val nutrients = product.optJSONObject("nutriments")
                            // get the name
                            val name = product.optString("product_name", "unknown product")
                            val brand = product.optString("brands", "unknown")
                            val carbs = nutrients.optString("carbohydrates_100g","unknown").toFloat()/100
                            val fats = nutrients.optString("fat_100g","unknown").toFloat()/100
                            val pro = nutrients.optString("proteins_100g","unknown").toFloat()/100

                            var cals = 0f;
                            // if it has nutritional info, set the corresponding variables
                            if (nutrients != null) {
                                cals = (nutrients.optString("energy-kcal_100g", "unknown")).toFloat() / 100
                            }
                            // create a new food item
                            val food = FoodItem(name, cals, brand, carbs, fats, pro)

                            callBack.onSuccess(food)
                        }
                    } catch (e: Exception) {
                        // otherwise print errors
                        Log.e("Caltracker", "error fecthing response: " + e.message)
                        callBack.onFailure(e.message)
                    }
                }
            }
        })
    }
}
