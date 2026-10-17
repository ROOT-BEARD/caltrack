package com.example.caltrack

import android.content.Context
import com.google.android.gms.tasks.OnCanceledListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class BarcodeScannerHelper(context: Context) {
    // callback listener
    interface Listener {
        fun onScanSuccess(barcode: String?)
        fun onScanCancelled()
        fun onScanFailed(e: Exception?)
    }

    // barcode scanner object
    private val scanner: GmsBarcodeScanner

    // constructor
    init {
        // sets the time of barcode to look for to improve speed
        val options = GmsBarcodeScannerOptions.Builder().setBarcodeFormats(
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E,
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_EAN_8
        ).build()

        this.scanner = GmsBarcodeScanning.getClient(context, options)
    }

    // opens the scanner
    fun startScan(listener: Listener) {
        // calls the corresponding callback function depending on succuss/failure/cancelled
        scanner.startScan()
            .addOnSuccessListener(OnSuccessListener { barcode: Barcode? ->
                if (barcode!!.getRawValue() != null) {
                    listener.onScanSuccess(barcode.getRawValue())
                }
            })
            .addOnCanceledListener(OnCanceledListener { listener.onScanCancelled() })
            .addOnFailureListener(OnFailureListener { e: Exception? -> listener.onScanFailed(e) })
    }
}
