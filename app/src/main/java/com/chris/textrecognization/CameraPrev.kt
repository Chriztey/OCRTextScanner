//package com.chris.textrecognization
//
//import android.graphics.Bitmap
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.viewinterop.AndroidView
//
//@Composable
//fun CameraPreview(onImageCaptured: (Bitmap) -> Unit) {
//    val context = LocalContext.current
//
//    AndroidView(
//        factory = { context ->
//            SurfaceView(context).apply {
//                val surfaceHolder = holder
//                val camera = Camera.open()
//
//                surfaceHolder.addCallback(object : SurfaceHolder.Callback {
//                    override fun surfaceCreated(holder: SurfaceHolder) {
//                        try {
//                            camera.setPreviewDisplay(holder)
//                            camera.setDisplayOrientation(90)
//                            camera.startPreview()
//                        } catch (e: Exception) {
//                            Log.e("CameraPreview", "Error setting up camera preview", e)
//                        }
//                    }
//
//                    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
//                        // Do nothing
//                    }
//
//                    override fun surfaceDestroyed(holder: SurfaceHolder) {
//                        camera.stopPreview()
//                        camera.release()
//                    }
//                })
//
//                camera.setPreviewCallback { data, _ ->
//                    val parameters = camera.parameters
//                    val size = parameters.previewSize
//                    val yuvImage = YuvImage(data, ImageFormat.NV21, size.width, size.height, null)
//                    val out = ByteArrayOutputStream()
//                    yuvImage.compressToJpeg(Rect(0, 0, size.width, size.height), 50, out)
//                    val imageBytes = out.toByteArray()
//                    val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
//                    onImageCaptured(bitmap)
//                }
//            }
//        },
//        update = { /* No-op */ }
//    )
//}