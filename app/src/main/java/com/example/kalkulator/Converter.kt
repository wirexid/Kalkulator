package com.example.kalkulator

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class Converter : ComponentActivity() {

    private lateinit var selectUnitsButton: Button
    private lateinit var convertButton: Button
    private lateinit var inputValue: EditText
    private lateinit var resultText: TextView

    private var selectedUnits: String? = null // Shranjena izbrana enota (npr. "kg to pounds")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)

        // Gumb za nazaj
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Zapre trenutno aktivnost
        }

        // Inicializacija UI elementov
        selectUnitsButton = findViewById(R.id.selectUnitsButton)
        convertButton = findViewById(R.id.convertButton)
        inputValue = findViewById(R.id.inputValue)
        resultText = findViewById(R.id.resultText)

        // Gumb za izbiro enot
        selectUnitsButton.setOnClickListener {
            showUnitsDialog()
        }

        // Gumb za izvedbo pretvorbe
        convertButton.setOnClickListener {
            performConversion()
        }
    }

    private fun showUnitsDialog() {
        val unitOptions = arrayOf(
            "kg to pounds",
            "pounds to kg",
            "m to ft",
            "ft to m",
            "s to h",
            "h to s"
        )

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Units")
        builder.setItems(unitOptions) { _, which ->
            selectedUnits = unitOptions[which]
            selectUnitsButton.text = selectedUnits // Prikaže izbrano enoto na gumbu
        }
        builder.show()
    }

    private fun performConversion() {
        if (selectedUnits == null) {
            Toast.makeText(this, "Please select units first!", Toast.LENGTH_SHORT).show()
            return
        }

        val input = inputValue.text.toString().toFloatOrNull()
        if (input == null) {
            Toast.makeText(this, "Please enter a valid value!", Toast.LENGTH_SHORT).show()
            return
        }

        val result = when (selectedUnits) {
            "kg to pounds" -> input * 2.20462f
            "pounds to kg" -> input / 2.20462f
            "m to ft" -> input * 3.28084f
            "ft to m" -> input / 3.28084f
            "s to h" -> input / 3600f
            "h to s" -> input * 3600f
            else -> 0f
        }

        resultText.text = "Result: $result"
    }
}
