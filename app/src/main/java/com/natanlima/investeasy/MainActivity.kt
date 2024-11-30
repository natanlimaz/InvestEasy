package com.natanlima.investeasy

import android.icu.text.DecimalFormat
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.pow

fun showSnackBar(context: android.content.Context, message: String, view: android.view.View) {
    val snackbar: Snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
    snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.white));
    snackbar.setTextColor(ContextCompat.getColor(context, R.color.background));
    snackbar.show();
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        window.apply {
            statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.background);
        }

        val tvTotal = findViewById<TextView>(R.id.tv_value);
        val income = findViewById<TextView>(R.id.income);

        val edtContribuition = findViewById<TextInputEditText>(R.id.edt_contribuition);
        val edtMonth= findViewById<TextInputEditText>(R.id.edt_month);
        val edtInterest = findViewById<TextInputEditText>(R.id.edt_interest);

        val btnClear = findViewById<Button>(R.id.btn_clear);
        val btnCalc = findViewById<Button>(R.id.btn_calc);

        btnClear.setOnClickListener {
            edtContribuition.text?.clear();
            edtMonth.text?.clear();
            edtInterest.text?.clear();
            tvTotal.text = "0.0";
            income.text = "0.0";
        }

        btnCalc.setOnClickListener {
            val contribuitionStr = edtContribuition.text.toString();
            val monthStr = edtMonth.text.toString();
            val interestStr = edtInterest.text.toString();

            if(contribuitionStr.isNotEmpty() && monthStr.isNotEmpty() && interestStr.isNotEmpty()) {
                val contribuition = contribuitionStr.toFloat();
                val month = monthStr.toInt();
                val interest = interestStr.toFloat();
                if(contribuition > 0f && interest > 0f && month > 0f) {
                    val percent = (interest / 100f).toDouble();
                    val totalAmount = contribuition * ((1 + percent).pow(month) - 1) / percent;
                    val earnings = totalAmount - (contribuition * month);

                    val moneyFormat = DecimalFormat("R$ #,##0.00");
                    moneyFormat.apply {
                        decimalFormatSymbols = decimalFormatSymbols.apply {
                            decimalSeparator = ',';
                            groupingSeparator = '.';
                        }
                    }

                    val totalAmountFormat = "${moneyFormat.format(totalAmount)}";
                    val earningsFormat = "${moneyFormat.format(earnings)}";

                    tvTotal.text = totalAmountFormat;
                    income.text = earningsFormat;
                }
                else {
                    showSnackBar(this@MainActivity, "Os valores não podem ser 0.", edtContribuition);
                }

            }
            else {
                showSnackBar(this@MainActivity, "Preencha todos os campos!", edtContribuition);
            }
        }
    }
}