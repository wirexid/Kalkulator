package com.example.kalkulator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputBinding
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.kalkulator.databinding.ActivityMainBinding
import com.example.kalkulator.ui.theme.KalkulatorTheme

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding
    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val conButton: Button = binding.btnCon
        conButton.setOnClickListener {
            val intent = Intent(this, Converter::class.java)
            startActivity(intent)
        }

    }


    fun numberAction(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal)
                    binding.workings.append(view.text)
                canAddDecimal = false
            } else
                binding.workings.append(view.text)
            canAddOperation = true
        }
    }
    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            binding.workings.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }
    fun allClearAction(view: View) {
        binding.workings.text = ""
        binding.results.text = ""
    }
    fun backSpaceAction(view: View) {
        val length = binding.workings.length()
        if (length > 0)
            binding.workings.text = binding.workings.text.subSequence(0, length - 1)
    }
    fun equalsAction(view: View) {
        binding.results.text = calculateResults()
    }

    private fun calculateResults(): String {
        val digitsOperators = digitsOperator()
        if (digitsOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitsOperators)
        if (timesDivision.isEmpty()) return ""

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float
        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('x') || list.contains('/')) {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when (operator) {
                    'x' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if (i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun digitsOperator(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in binding.workings.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KalkulatorTheme {
        Greeting("Android")
    }
}