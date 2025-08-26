package com.loopr.ui.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.CompoundBarcodeView

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QrScannerScreen(onQrScanned: (String) -> Unit) {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (cameraPermissionState.status.isGranted.not()) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    if (cameraPermissionState.status.isGranted) {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    CompoundBarcodeView(ctx).apply {
                        decodeContinuous(object : BarcodeCallback {
                            override fun barcodeResult(result: BarcodeResult?) {
                                result?.text?.let { scannedText ->
                                    if (scannedText.startsWith("loopr://")) {
                                        onQrScanned(scannedText)
                                    }
                                }
                            }

                            override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {}
                        })

                        this.resume() // Ensure the camera preview starts
                        this.setStatusText("") // Hide the default status text
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Draw a viewfinder frame
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width * 0.8f
                val height = size.height * 0.4f
                val left = (size.width - width) / 2
                val top = (size.height - height) / 2
                val rect = Rect(left, top, left + width, top + height)

                drawRect(
                    color = Color.White,
                    style = Stroke(width = 4.dp.toPx()),
                    topLeft = rect.topLeft,
                    size = rect.size
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Camera permission is required to scan QR codes.",
                color = Color.Red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
