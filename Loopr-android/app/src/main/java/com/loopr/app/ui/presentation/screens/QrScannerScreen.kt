package com.loopr.app.ui.presentation.screens

import android.Manifest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.loopr.app.ui.presentation.modules.bindCameraUseCases

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(onQrCodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    rememberPermissionState(Manifest.permission.CAMERA)


    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    AndroidView(factory = { ctx ->
        PreviewView(ctx).apply {
            this.scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }, modifier = Modifier.fillMaxSize(), update = { previewView ->
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases(
                cameraProvider, lifecycleOwner, previewView, onQrCodeScanned
            )
        }, ContextCompat.getMainExecutor(context))
    })

    // Canvas drawing for QR scanner box
    Canvas(modifier = Modifier.fillMaxSize()) {
        val borderWidth = 4.dp.toPx()
        val cornerLength = 40.dp.toPx()
        val boxSizeRatio = 0.7f
        val canvasWidth = size.width
        val canvasHeight = size.height
        val boxWidth = canvasWidth * boxSizeRatio
        val boxHeight = canvasWidth * boxSizeRatio
        val offsetX = (canvasWidth - boxWidth) / 2
        val offsetY = (canvasHeight - boxHeight) / 2

        val path = Path()

        // Drawing corners for the scanning box
        path.moveTo(offsetX, offsetY + cornerLength)
        path.lineTo(offsetX, offsetY)
        path.lineTo(offsetX + cornerLength, offsetY)
        path.moveTo(offsetX + boxWidth - cornerLength, offsetY)
        path.lineTo(offsetX + boxWidth, offsetY)
        path.lineTo(offsetX + boxWidth, offsetY + cornerLength)
        path.moveTo(offsetX + boxWidth, offsetY + boxHeight - cornerLength)
        path.lineTo(offsetX + boxWidth, offsetY + boxHeight)
        path.lineTo(offsetX + boxWidth - cornerLength, offsetY + boxHeight)
        path.moveTo(offsetX + cornerLength, offsetY + boxHeight)
        path.lineTo(offsetX, offsetY + boxHeight)
        path.lineTo(offsetX, offsetY + boxHeight - cornerLength)

        drawPath(path, color = Color(0xFFffcc90), style = Stroke(width = borderWidth))

        // Overlay outside scanning box (dimmed background)
        drawRect(
            color = Color.Black.copy(alpha = 0.5f), size = Size(canvasWidth, canvasHeight)
        )

        // Transparent area in the middle (clear scanning area)
        drawRect(
            color = Color.Transparent,
            topLeft = Offset(offsetX, offsetY),
            size = Size(boxWidth, boxHeight)
        )
    }

}

