package com.example.lenovo.calc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView
import com.github.keelar.exprk.Expressions
import java.lang.Exception
import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var expression: StringBuilder = StringBuilder("")
    private var message = false

    override fun onClick(view: View?) {
        if (message) {
            val message_text = findViewById<TextView>(R.id.message_text)
            message_text.text = ""
        }
        message = false
        
        when (view?.id) {
            R.id.dig_one -> {
                expression.append(1)
            }
            R.id.dig_two -> {
                expression.append(2)
            }
            R.id.dig_three -> {
                expression.append(3)
            }
            R.id.dig_four -> {
                expression.append(4)
            }
            R.id.dig_five -> {
                expression.append(5)
            }
            R.id.dig_six -> {
                expression.append(6)
            }
            R.id.dig_seven -> {
                expression.append(7)
            }
            R.id.dig_eight -> {
                expression.append(8)
            }
            R.id.dig_nine -> {
                expression.append(9)
            }
            R.id.dig_zero -> {
                expression.append(0)
            }
            R.id.minus -> {
                expression.append('-')
            }
            R.id.plus -> {
                expression.append('+')
            }
            R.id.dot -> {
                expression.append('.')
            }
            R.id.mul -> {
                expression.append('*')
            }
            R.id.div -> {
                expression.append('/')
            }
            R.id.percent -> {
                expression.append('%')
            }
            R.id.del_butt -> {
                if (expression.length != 0)
                    expression.deleteCharAt(expression.length - 1)
            }
            R.id.clear -> {
                expression.setLength(0)
            }
            R.id.equal -> {
                try {
                    val result = Expressions().eval(expression.toString())
                    expression.setLength(0)
                    expression.append(result)
                } catch (e: Exception) {
                    message = true;
                    val message_text = findViewById<TextView>(R.id.message_text)
                    message_text.setText(R.string.error_message)
                }
            }
        }
        val expr_text = findViewById<TextView>(R.id.expr_text)
        expr_text.setText(expression)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dig_one = findViewById<TextView>(R.id.dig_one)
        val dig_two = findViewById<TextView>(R.id.dig_two)
        val dig_three = findViewById<TextView>(R.id.dig_three)
        val dig_four = findViewById<TextView>(R.id.dig_four)
        val dig_five = findViewById<TextView>(R.id.dig_five)
        val dig_six = findViewById<TextView>(R.id.dig_six)
        val dig_seven = findViewById<TextView>(R.id.dig_seven)
        val dig_eight = findViewById<TextView>(R.id.dig_eight)
        val dig_nine = findViewById<TextView>(R.id.dig_nine)
        val dig_zero = findViewById<TextView>(R.id.dig_zero)
        val plus = findViewById<TextView>(R.id.plus)
        val minus = findViewById<TextView>(R.id.minus)
        val dot = findViewById<TextView>(R.id.dot)
        val mul = findViewById<TextView>(R.id.mul)
        val div = findViewById<TextView>(R.id.div)
        val percent = findViewById<TextView>(R.id.percent)
        val equal = findViewById<TextView>(R.id.equal)
        val del_butt = findViewById<TextView>(R.id.del_butt)
        val clear = findViewById<TextView>(R.id.clear)

        dig_one.setOnClickListener(this)
        dig_two.setOnClickListener(this)
        dig_three.setOnClickListener(this)
        dig_four.setOnClickListener(this)
        dig_five.setOnClickListener(this)
        dig_six.setOnClickListener(this)
        dig_seven.setOnClickListener(this)
        dig_eight.setOnClickListener(this)
        dig_nine.setOnClickListener(this)
        dig_zero.setOnClickListener(this)
        plus.setOnClickListener(this)
        minus.setOnClickListener(this)
        dot.setOnClickListener(this)
        mul.setOnClickListener(this)
        div.setOnClickListener(this)
        percent.setOnClickListener(this)
        equal.setOnClickListener(this)
        del_butt.setOnClickListener(this)
        clear.setOnClickListener(this)

        val expr_text = findViewById<TextView>(R.id.expr_text)
        expr_text.movementMethod = ScrollingMovementMethod()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("expression", expression.toString());
        outState.putBoolean("message", message);
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        expression = StringBuilder(savedInstanceState.getString("expression")!!)
        message = savedInstanceState.getBoolean("message")
        val expr_text = findViewById<TextView>(R.id.expr_text)
        val message_text = findViewById<TextView>(R.id.message_text)
        expr_text.setText(expression)
        if (message) {
            message_text.setText(R.string.error_message)
        }
    }
}
