package com.loopr.ui.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawStyle
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
import androidx.navigation.NavController

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QrScannerScreen(onQrScanned: (String) -> Unit, navController: NavController) {
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (cameraPermissionState.status.isGranted.not()) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    if (cameraPermissionState.status.isGranted) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black) // Set a sleek black background
        ) {
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

                        this.resume()
                        this.setStatusText("")
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

            // Draw only the rounded corners of the viewfinder frame
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width * 0.6f // Reduced width
                val height = size.height * 0.3f
                val left = (size.width - width) / 2
                val top = (size.height - height) / 2
                val rect = Rect(left, top, left + width, top + height)
                val cornerLength = 32.dp.toPx()
                val strokeWidth = 8.dp.toPx()

                // Top-left corner
                drawLine(
                    color = Color.Cyan,
                    start = rect.topLeft.copy(x = rect.topLeft.x + strokeWidth / 2),
                    end = rect.topLeft.copy(x = rect.topLeft.x + cornerLength),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color.Cyan,
                    start = rect.topLeft.copy(y = rect.topLeft.y + strokeWidth / 2),
                    end = rect.topLeft.copy(y = rect.topLeft.y + cornerLength),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )

                // Top-right corner
                drawLine(
                    color = Color.Cyan,
                    start = rect.topRight.copy(x = rect.topRight.x - cornerLength),
                    end = rect.topRight.copy(x = rect.topRight.x - strokeWidth / 2),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color.Cyan,
                    start = rect.topRight.copy(y = rect.topRight.y + strokeWidth / 2),
                    end = rect.topRight.copy(y = rect.topRight.y + cornerLength),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )

                // Bottom-left corner
                drawLine(
                    color = Color.Cyan,
                    start = rect.bottomLeft.copy(x = rect.bottomLeft.x + strokeWidth / 2),
                    end = rect.bottomLeft.copy(x = rect.bottomLeft.x + cornerLength),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color.Cyan,
                    start = rect.bottomLeft.copy(y = rect.bottomLeft.y - cornerLength),
                    end = rect.bottomLeft.copy(y = rect.bottomLeft.y - strokeWidth / 2),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )

                // Bottom-right corner
                drawLine(
                    color = Color.Cyan,
                    start = rect.bottomRight.copy(x = rect.bottomRight.x - cornerLength),
                    end = rect.bottomRight.copy(x = rect.bottomRight.x - strokeWidth / 2),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color.Cyan,
                    start = rect.bottomRight.copy(y = rect.bottomRight.y - cornerLength),
                    end = rect.bottomRight.copy(y = rect.bottomRight.y - strokeWidth / 2),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }

            // Add a text overlay for instructions
            Column(
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Scan and Set Autopay",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Align the QR code within the frame",
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Add a circular button at the bottom for cancel
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { navController.popBackStack() } // Navigate back to the home screen
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cancel",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
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
