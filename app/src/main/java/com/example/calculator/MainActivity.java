package com.example.calculator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calculator.databinding.ActivityMainBinding;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Calculator";
    private ActivityMainBinding binding = null;
    private EditText editText;
    private TextView textViewResult;
    private MainActivityViewModel viewModel;
    private boolean whetherFirstCharacterInEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        viewModel = ViewModelProviders.of(this)
                .get(MainActivityViewModel.class);
        binding.setMainActivityViewModel(viewModel);
        binding.setLifecycleOwner(this);
//        editText = (EditText) findViewById(R.id.editText);
//        textViewResult = (TextView) findViewById(R.id.textViewResult);
//        editText.setText(viewModel.getEditTextText().getValue());
//        textViewResult.setText(viewModel.get_textViewText());
        whetherFirstCharacterInEditText = viewModel.isWhetherFirstCharacterInEditText();
        final Observer<String> toastMessageObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                Toast toast = Toast.makeText(MainActivity.this,
                                        "This is too many",Toast.LENGTH_SHORT);
                                toast.show();
            }
        };
        viewModel.getToastMessage().observe(this,toastMessageObserver);
//        final Observer<String> textViewResultObserver = new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable final String newName) {
//                // Update the UI, in this case, a TextView.
//                textViewResult.setText(viewModel.getTextViewText().getValue());
//            }
//        };
//        viewModel.getTextViewText().observe(this,textViewResultObserver);
//        final Observer<String> editTextObserver = new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable final String newName) {
//                // Update the UI, in this case, a TextView.
//                editText.setText(viewModel.getEditTextText().getValue());
//            }
//        };
//        viewModel.getEditTextText().observe(this,editTextObserver);
    }

    public void onbtn0Click(View view) {
        viewModel.performOperation(0, view);
    }

    public void onbtn1Click(View view) {
        viewModel.performOperation(1, view);
    }

    public void onbtn2Click(View view) {
        viewModel.performOperation(2, view);
    }

    public void onbtn3Click(View view) {
        viewModel.performOperation(3, view);
    }

    public void onbtn4Click(View view) { viewModel.performOperation(4, view); }

    public void onbtn5Click(View view) {
        viewModel.performOperation(5, view);
    }

    public void onbtn6Click(View view) {
        viewModel.performOperation(6, view);
    }

    public void onbtn7Click(View view) {
        viewModel.performOperation(7, view);
    }

    public void onbtn8Click(View view) {
        viewModel.performOperation(8, view);
    }

    public void onbtn9Click(View view) {
        viewModel.performOperation(9, view);
    }

    public void onbtnDivideClick(View view) {
        String set = viewModel.getEditTextText().getValue();
        if(set.length() == 0) return;
        if (set.charAt(set.length() - 1) == '+' || set.charAt(set.length() - 1) == '-' || set.charAt(set.length() - 1) == '\u00D7' || set.charAt(set.length() - 1) == '\u00F7') {
            Toast toast = Toast.makeText(MainActivity.this, "Can't put two operation signs next to each other", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        set += "\u00F7";
        viewModel.set_editTextText(set);
    }

    public void onbtnMultiplyClick(View view) {
        String set = viewModel.getEditTextText().getValue();
        if(set.length() == 0) return;
        if (set.charAt(set.length() - 1) == '+' || set.charAt(set.length() - 1) == '-' || set.charAt(set.length() - 1) == '\u00D7' || set.charAt(set.length() - 1) == '\u00F7') {
            Toast toast = Toast.makeText(MainActivity.this, "Can't put two operation signs next to each other", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        set += "\u00D7";
        viewModel.set_editTextText(set);
    }

    public void onbtnAddClick(View view) {
        String set = viewModel.getEditTextText().getValue();
        if(set.length() == 0) return;
        if (set.charAt(set.length() - 1) == '+' || set.charAt(set.length() - 1) == '-' || set.charAt(set.length() - 1) == '\u00D7' || set.charAt(set.length() - 1) == '\u00F7') {
            Toast toast = Toast.makeText(MainActivity.this, "Can't put two operation signs next to each other", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        set += "+";
        viewModel.set_editTextText(set);
    }

    public void onbtnSubtractClick(View view) {
        String set = viewModel.getEditTextText().getValue();
        if (set.length() == 0)
        {
            set += "-";
            viewModel.set_editTextText(set);
            return;
        }
        else if(set.equals("0"))
        {
            set = "-";
            viewModel.set_editTextText(set);
            whetherFirstCharacterInEditText = false;
            return;
        }
        else if (set.charAt(set.length() - 1) == '+' || set.charAt(set.length() - 1) == '-' || set.charAt(set.length() - 1) == '\u00D7' || set.charAt(set.length() - 1) == '\u00F7') {
            Toast toast = Toast.makeText(MainActivity.this, "Can't put two operation signs next to each other", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        set += "-";
        viewModel.set_editTextText(set);
    }

    public void onbtnClearClick(View view) {
        viewModel.set_editTextText("");
        viewModel.set_textViewText("");
    }

    public void onbtnEqualsClick(View view) {
        viewModel.set_editTextText(viewModel.get_textViewText());
    }

    public void onbtnDotClick(View view) {
        String set = viewModel.getEditTextText().getValue();
        if (!set.contains(".")) {
            set += ".";
            viewModel.set_editTextText(set);
            return;
        }
        int firstLocalMax;
        int secondLocalMax;
        if (set.lastIndexOf("+") > set.lastIndexOf("-")) {
            firstLocalMax = set.lastIndexOf("+");
        } else {
            firstLocalMax = set.lastIndexOf("-");
        }
        if (set.lastIndexOf("\u00D7") > set.lastIndexOf("\u00F7")) {
            secondLocalMax = set.lastIndexOf("\u00D7");
        } else {
            secondLocalMax = set.lastIndexOf("\u00F7");
        }
        int maxFinal;
        if (firstLocalMax > secondLocalMax) {
            maxFinal = firstLocalMax;
        } else {
            maxFinal = secondLocalMax;
        }
        if (set.lastIndexOf(".") > maxFinal) {
            Toast toast = Toast.makeText(MainActivity.this,
                    "You can't put another dot here",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        set += ".";
        viewModel.set_editTextText(set);

    }
    public void onbtnBackspaceClick(View view)
    {
        String set = viewModel.getEditTextText().getValue();
        try {
            set = set.substring(0,set.length()-1);
        }
        catch(StringIndexOutOfBoundsException e)
        {
            Toast toast = Toast.makeText(MainActivity.this,
                    "Can't backspace",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        catch (Exception e){
            Toast toast = Toast.makeText(MainActivity.this,
                    "Can't backspace",
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        viewModel.set_editTextText(set);
        if(set.length() > 0){
            if(set.charAt(set.length()-1) != '-' && set.charAt(set.length()-1) != '+' && set.charAt(set.length()-1) != '\u00D7' && set.charAt(set.length()-1) != '\u00F7')
            {
                viewModel.performOperation(-1,view);//won't add any number there
            }
        }
    }
}
