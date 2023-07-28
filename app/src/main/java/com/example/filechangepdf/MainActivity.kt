package com.example.filechangepdf

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnClick = findViewById<Button>(R.id.btnClick)

        btnClick.setOnClickListener {
            val constraintLayout = findViewById<ConstraintLayout>(R.id.layout)
//            val filename = "my_constraint_layout"
//            saveConstraintLayoutAsPdf(this, constraintLayout, filename)
            saveConstraintLayoutAsPdf(this, constraintLayout)
        }
    }
    fun saveConstraintLayoutAsPdf(context: Context, constraintLayout: ConstraintLayout) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(constraintLayout.width, constraintLayout.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val density = context.resources.displayMetrics.density
        val scaledBitmap = Bitmap.createScaledBitmap(getBitmapFromView(constraintLayout), constraintLayout.width, constraintLayout.height, true)
        canvas.drawBitmap(scaledBitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)

        val currentTime = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val filename = "my_constraint_layout_$currentTime.pdf"

        val file = File(getPdfDirectory(context), filename)
        try {
            val fileOutputStream = FileOutputStream(file)
            pdfDocument.writeTo(fileOutputStream)
            pdfDocument.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun getPdfDirectory(context: Context): File {
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "PDFs")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return directory
    }
}