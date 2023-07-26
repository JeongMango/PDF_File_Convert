package com.example.filechangepdf

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfDocument
import com.itextpdf.text.pdf.PdfWriter
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 저장소 권한이 있는지 확인하고 없으면 권한 요청
        if (!hasStoragePermission()) {
            requestStoragePermission()
        }
    }

    private fun hasStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }

    fun createPdf(view: View) {
        if (hasStoragePermission()) {
            // PDF로 변환할 레이아웃의 부모 레이아웃을 찾기
            val viewToConvert = findViewById<View>(R.id.parentLayout)
            // 레이아웃을 비트맵으로 변환
            val bitmap = viewToBitmap(viewToConvert)
            // 비트맵으로부터 PDF 생성
            createPdfFromBitmap(bitmap)
        } else {
            Toast.makeText(
                this,
                "외부 저장소에 쓰기 권한이 필요합니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun viewToBitmap(view: View): Bitmap {
        // 뷰의 너비와 높이를 측정
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        // 뷰와 같은 크기의 비트맵 생성
        val bitmap = Bitmap.createBitmap(
            view.measuredWidth,
            view.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        // 뷰를 비트맵에 그림
        val canvas = Canvas(bitmap)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.draw(canvas)
        return bitmap
    }

    private fun createPdfFromBitmap(bitmap: Bitmap) {
        // 외부 저장소에 파일 저장 경로 설정
        val pdfFilePath = "${externalCacheDir?.absolutePath}/output.pdf"
        val uri = Uri.parse("file://$pdfFilePath")

        contentResolver.openOutputStream(uri)?.let { outputStream ->
            val pdfDocument = PdfDocument(PdfWriter(outputStream))
            val document = Document(pdfDocument)

            try {
                val image = Image.getInstance(bitmapToByteArray(bitmap))

                pdfDocument.addNewPage()
                document.add(image)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                document.close()
                pdfDocument.close()
                outputStream.close()
            }
        }
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
}