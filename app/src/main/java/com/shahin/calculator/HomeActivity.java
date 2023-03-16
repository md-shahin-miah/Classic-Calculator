package com.shahin.calculator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.shahin.calculator.databinding.ActivityHomeBinding;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private String data = "";
    private boolean flag = false;    //for operator (+,-,÷,x,%,.)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        onClickListeners();

    }

    @SuppressLint("SetTextI18n")
    private void onClickListeners() {

        // ROW 1
        binding.buttons.btnAc.setOnClickListener(view -> {
            if (binding.values.inputText.getText() == "") {
                showToast();
            } else {
                binding.values.inputText.setText("");
                binding.values.outputText.setText("");
            }
        });
        binding.buttons.btnPlusMinus.setOnClickListener(view -> {
            if (binding.values.inputText.getText() == "") {
                showToast();
            } else {
                data = binding.values.inputText.getText().toString();
                if (data.charAt(0) != '-') {
                    binding.values.inputText.setText("-" + data);
                } else {
                    StringBuilder new_data = new StringBuilder();
                    for (int i = 1; i < data.length(); i++)
                        new_data = new_data.append(data.charAt(i));
                    binding.values.inputText.setText(new_data.toString());
                }
            }
        });
        binding.buttons.btnPerc.setOnClickListener(view -> {
            if (flag) setInputData("%");
        });
        binding.buttons.btnDiv.setOnClickListener(view -> {
            if (flag) setInputData("÷");
        });

        // ROW 2
        binding.buttons.btn7.setOnClickListener(view -> setInputData("7"));
        binding.buttons.btn8.setOnClickListener(view -> setInputData("8"));
        binding.buttons.btn9.setOnClickListener(view -> setInputData("9"));
        binding.buttons.btnMul.setOnClickListener(view -> {
            if (flag) setInputData("x");
        });

        // ROW 3
        binding.buttons.btn4.setOnClickListener(view -> setInputData("4"));
        binding.buttons.btn5.setOnClickListener(view -> setInputData("5"));
        binding.buttons.btn6.setOnClickListener(view -> setInputData("6"));
        binding.buttons.btnMinus.setOnClickListener(view -> {
            if (flag) setInputData("-");
        });

        // ROW 4
        binding.buttons.btn1.setOnClickListener(view -> setInputData("1"));
        binding.buttons.btn2.setOnClickListener(view -> setInputData("2"));
        binding.buttons.btn3.setOnClickListener(view -> setInputData("3"));
        binding.buttons.btnPlus.setOnClickListener(view -> {
            if (flag) setInputData("+");
        });

        // ROW 5
        binding.buttons.btnDel.setOnClickListener(view -> {
            if (binding.values.inputText.getText() == "") showToast();
            else {
             /*
              @params flag - when use change the operator, and want to add new one.
             */
                flag = true;
                data = binding.values.inputText.getText().toString();
                StringBuilder new_data = new StringBuilder();
                for (int i = 0; i < data.length() - 1; i++)
                    new_data = new_data.append(data.charAt(i));
                binding.values.inputText.setText(new_data.toString());
            }
        });
        binding.buttons.btn0.setOnClickListener(view -> setInputData("0"));
        binding.buttons.btnDot.setOnClickListener(view -> {
            if (flag) setInputData(".");
        });
        binding.buttons.btnEqual.setOnClickListener(view -> {
            if (binding.values.inputText.getText() == "")
                showToast();
            else
                doCalculation();
        });

    }

    @SuppressLint("SetTextI18n")
    private void setInputData(String data) {
        this.data = binding.values.inputText.getText().toString();
        binding.values.inputText.setText(this.data + data);
        setFlag(data);
    }


    private void setFlag(String data) {
        switch (data) {
            case "+":
            case "-":
            case "÷":
            case "x":
            case "%":
            case ".":
                flag = false;
                break;
            default:
                flag = true;
        }
    }

    private void replaceString() {
        data = binding.values.inputText.getText().toString();
        data = data.replace("%", "/100");
        data = data.replace("÷", "/");
        data = data.replace("x", "*");
    }

    private void doCalculation() {
        replaceString();

        Context rhino = Context.enter();
        rhino.setOptimizationLevel(-1);     //when nothing is generated.
        String result = "";
        try {
            Scriptable scriptable = rhino.initSafeStandardObjects();
            result = rhino.evaluateString(scriptable, data, "javascript", 1, null).toString();
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        binding.values.outputText.setText(result);
    }

    private void showToast() {
        Toast.makeText(this, "dev-aniketj (github)", Toast.LENGTH_SHORT).show();
    }
}