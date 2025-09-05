package com.loopr.app.ui.presentation.modules

import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage


fun processImageProxy(
    barcodeScanner: BarcodeScanner,
    imageProxy: ImageProxy,
    onQrCodeScanned: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                barcodes.forEach { barcode ->
                    val rawValue = barcode.rawValue
                    rawValue?.let {
                        Log.d("QRCodeProcessor", "QR Code detected: $it")
                        onQrCodeScanned(it)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("QRCodeProcessor", "QR code scanning failed", e)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}
