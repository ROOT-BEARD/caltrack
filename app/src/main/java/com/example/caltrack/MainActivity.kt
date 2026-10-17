package com.example.caltrack

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.compose.runtime.getValue
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.caltrack.FoodCatalog.FoodCallBack

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

class MainActivity : AppCompatActivity() {
    private var barcodeScannerHelper: BarcodeScannerHelper? = null
    private var foodCatalog: FoodCatalog? = null

    var foodName: TextView? = null
    var calorieText: TextView? = null
    var gramInput: EditText? = null
    private var sheetOpen by mutableStateOf(false)
    lateinit var currentFood: FoodItem;

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()

        setContent {
            MaterialTheme {
                // Scaffold automatically consumes system bars (status bar, navigation bar)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding) // Applies safe padding so content doesn't sit under system bars
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()

                        ){
                            Button(
                                onClick = { startScanning() },
                                modifier = Modifier
                                    .width(128.dp)
                                    .height(64.dp))
                            {
                                Text("Scan", fontWeight = FontWeight.Bold)
                            }
                        }
                        if (sheetOpen) {
                            FoodBottomSheet({ sheetOpen = false }, currentFood);
                        }
                    }
                }
            }

            foodCatalog = FoodCatalog()
            barcodeScannerHelper = BarcodeScannerHelper(this)

            foodName = findViewById<TextView>(R.id.textView_name)
            calorieText = findViewById<TextView>(R.id.textView_cal)
            gramInput = findViewById<EditText>(R.id.editTextNumber_grams)


            val scanButton = findViewById<Button>(R.id.button_scan)
        }

    }
    // starts scanning and handles getting a barcode value and getting the food by the barcode
    fun startScanning() {
        // start scanning with a new callback interface
        barcodeScannerHelper!!.startScan(object : BarcodeScannerHelper.Listener {
            // if the scan is successful, get the food item from FoodCatalog
            override fun onScanSuccess(barcode: String?) {
                // call get food with a new callback interface
                foodCatalog!!.getFood(object : FoodCallBack {
                    // if it finds a food, set the labels to the foods information
                    override fun onSuccess(food: FoodItem?) {
                        if (food != null) {
                            currentFood = food
                        };
                        sheetOpen = true;
                    }

                    // if failed print error message
                    override fun onFailure(errorMsg: String?) {
                        println(errorMsg)
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, errorMsg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }, barcode)
            }

            // if the scan is cancelled
            override fun onScanCancelled() {
                println("cancelled")
            }

            // if the scan is a failure
            override fun onScanFailed(e: Exception?) {
                println("failed")
            }
        })
    }}