package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Calculator";
    //ArrayList<Button> numbersButtonList= new ArrayList<>();

    private Button btn0;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;
    private Button btn9;
    private Button btnDivide;
    private Button btnMultiply;
    private Button btnAdd;
    private Button btnSubtract;
    private Button btnDot;
    private Button btnEquals;
    private Button btnBackspace;
    private EditText editText;
    private TextView textViewResult;
    private boolean whetherFirstCharacterInEditText = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn0 = (Button) findViewById(R.id.btn0);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        btn8 = (Button) findViewById(R.id.btn8);
        btn9 = (Button) findViewById(R.id.btn9);
        editText = (EditText) findViewById(R.id.editText);
        textViewResult = (TextView) findViewById(R.id.textViewResult);
    }

    public void manipulateEditText(int whichOne, View view) {
        //String set = editText.getText().toString();
        //long textToInt = Integer.parseInt(set);
        if (whetherFirstCharacterInEditText) {
            editText.setText("");//getting rid of initial 0
            whetherFirstCharacterInEditText = false;
        }
        String set;
        if(whichOne != -1)//-1 is used in backspaceButton onClick method - I don't add any number through it
        {
            set = editText.getText().toString() + whichOne;
            editText.setText(set);
        }
        else
        {
            set = editText.getText().toString();
        }
        ArrayList<Integer> delimetersOperationSigns = getOperationSigns(set);
        if (delimetersOperationSigns.size() > 2)
        {
            Double result = 0d;
            int whichIndexDelimiter = 0;//index of Integer number in delimetersOperationSigns ArrayList
            boolean whetherAchievedEnd = false;
            do
            {
                for (Integer number: delimetersOperationSigns//performing multiplication and division first
                ) {
                    if(number == set.length())
                    {
                        whetherAchievedEnd = true;
                        continue;
                    }
                    if (set.charAt(number) == '\u00D7' || set.charAt(number) == '\u00F7')
                    {
                        double first;
                        int whichIndexTaken;
                        if(delimetersOperationSigns.get(whichIndexDelimiter-1) == 0)//beginning of string set
                        {
                            whichIndexTaken = delimetersOperationSigns.get(whichIndexDelimiter-1);
                            first = Double.parseDouble(set.substring(whichIndexTaken,number));
                        }
                        else //not a beginning of string set
                        {
                            whichIndexTaken = delimetersOperationSigns.get(whichIndexDelimiter-1)+1;
                            first = Double.parseDouble(set.substring(whichIndexTaken,number));//position starting +1 in order to not get operation sign
                        }
                        double second = Double.parseDouble(set.substring(number+1,delimetersOperationSigns.get(whichIndexDelimiter+1)));
                        if (set.charAt(number) == '\u00D7')//multiplication
                        {
                            result=(first*second);
                        }
                        else if(set.charAt(number) == '\u00F7')//division
                        {
                            result=(first/second);
                        }
                        set = set.substring(0,whichIndexTaken) +  Double.toString(result) + set.substring(delimetersOperationSigns.get(whichIndexDelimiter+1));
                        delimetersOperationSigns = getOperationSigns(set);
                        Log.d(TAG, "manipulateEditText: przyjął nową tablicę (*//)");
                        whichIndexDelimiter = 0;
                        break;
                    }
                    ++whichIndexDelimiter;
                }
            }while(!whetherAchievedEnd);

            whichIndexDelimiter = 0;
            boolean whetherAchievedEndSecond = false;
            do {
                for (Integer number: delimetersOperationSigns//performing adding and subtracting
                ) {
                    if(number == set.length())
                    {
                        whetherAchievedEndSecond = true;
                        continue;
                    }
                    if (set.charAt(number) == '+' || set.charAt(number) == '-' && number != 0)
                    {
                        double first;
                        int whichIndexTaken;
                        if(delimetersOperationSigns.get(whichIndexDelimiter-1) == 0)//beginning of string set
                        {
                            whichIndexTaken = delimetersOperationSigns.get(whichIndexDelimiter-1);
                            try {
                                first = Double.parseDouble(set.substring(whichIndexTaken,number));

                            }
                            catch (NumberFormatException e)
                            {
                                Toast toast = Toast.makeText(MainActivity.this,
                                        "This is too many",Toast.LENGTH_SHORT);
                                toast.show();
                                Log.d(TAG, e.getMessage());
                                return;
                            }
                        }
                        else //not a beginning of string set
                        {
                            whichIndexTaken = delimetersOperationSigns.get(whichIndexDelimiter-1)+1;
                            try {
                                first = Double.parseDouble(set.substring(whichIndexTaken,number));//position starting +1 in order to not get operation sign
                            }
                            catch (NumberFormatException e)
                            {
                                Toast toast = Toast.makeText(MainActivity.this,
                                        "This is too many",Toast.LENGTH_SHORT);
                                toast.show();
                                e.getMessage();
                                return;
                            }
                        }
                        double second;
                        try {
                            second = Double.parseDouble(set.substring(number+1,delimetersOperationSigns.get(whichIndexDelimiter+1)));
                        }
                        catch (NumberFormatException e)
                        {
                            Toast toast = Toast.makeText(MainActivity.this,
                                    "This is too many",Toast.LENGTH_SHORT);
                            toast.show();
                            e.getMessage();
                            return;
                        }

                        if (set.charAt(number) == '+')//adding
                        {
                            result=(first+second);
                        }
                        else if(set.charAt(number) == '-')//subtracting
                        {
                            result=(first-second);
                        }
                        set = set.substring(0,whichIndexTaken) + Double.toString(result) + set.substring(delimetersOperationSigns.get(whichIndexDelimiter+1));
                        delimetersOperationSigns = getOperationSigns(set);
                        Log.d(TAG, "manipulateEditText: przyjął nową tablicę(+/-)");
                        whichIndexDelimiter = 0;
                        break;
                    }
                    ++whichIndexDelimiter;
                }
            }while(!whetherAchievedEndSecond);
        }
        if(Double.parseDouble(set) == Math.floor(Double.parseDouble(set)))
        {
            Long result = new Long((long) Double.parseDouble(set));
            set = result.toString();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        set = decimalFormat.format(Double.parseDouble(set));
        textViewResult.setText(set);
    }
    public ArrayList<Integer> getOperationSigns(String text)
    {
        ArrayList<Integer> delimetersOperationSigns = new ArrayList<>();
        delimetersOperationSigns.add(0);
        for(int i = 0;i < text.length();++i)//where are the signs - they will delimit numbers
        {
            if (text.charAt(i) == '+' ||
                    (text.charAt(i) == '-' && i != 0) ||
                    text.charAt(i) == '\u00D7' ||
                    text.charAt(i) == '\u00F7')
            {
                delimetersOperationSigns.add(i);
            }
        }
        delimetersOperationSigns.add(text.length());
        return delimetersOperationSigns;

    }

    public void onbtn0Click(View view) {
        manipulateEditText(0, view);
    }

    public void onbtn1Click(View view) {
        manipulateEditText(1, view);
    }

    public void onbtn2Click(View view) {
        manipulateEditText(2, view);
    }

    public void onbtn3Click(View view) {
        manipulateEditText(3, view);
    }

    public void onbtn4Click(View view) {
        manipulateEditText(4, view);
    }

    public void onbtn5Click(View view) {
        manipulateEditText(5, view);
    }

    public void onbtn6Click(View view) {
        manipulateEditText(6, view);
    }

    public void onbtn7Click(View view) {
        manipulateEditText(7, view);
    }

    public void onbtn8Click(View view) {
        manipulateEditText(8, view);
    }

    public void onbtn9Click(View view) {
        manipulateEditText(9, view);
    }

    public void onbtnDivideClick(View view) {
        String set = editText.getText().toString();
        if(set.length() == 0) return;
        if (set.charAt(set.length() - 1) == '+' || set.charAt(set.length() - 1) == '-' || set.charAt(set.length() - 1) == '\u00D7' || set.charAt(set.length() - 1) == '\u00F7') {
            Toast toast = Toast.makeText(MainActivity.this, "Can't put two operation signs next to each other", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        set += "\u00F7";
        editText.setText(set);
    }

    public void onbtnMultiplyClick(View view) {
        String set = editText.getText().toString();
        if(set.length() == 0) return;
        if (set.charAt(set.length() - 1) == '+' || set.charAt(set.length() - 1) == '-' || set.charAt(set.length() - 1) == '\u00D7' || set.charAt(set.length() - 1) == '\u00F7') {
            Toast toast = Toast.makeText(MainActivity.this, "Can't put two operation signs next to each other", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        set += "\u00D7";
        editText.setText(set);
    }

    public void onbtnAddClick(View view) {
        String set = editText.getText().toString();
        if(set.length() == 0) return;
        if (set.charAt(set.length() - 1) == '+' || set.charAt(set.length() - 1) == '-' || set.charAt(set.length() - 1) == '\u00D7' || set.charAt(set.length() - 1) == '\u00F7') {
            Toast toast = Toast.makeText(MainActivity.this, "Can't put two operation signs next to each other", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        set += "+";
        editText.setText(set);
    }

    public void onbtnSubtractClick(View view) {
        String set = editText.getText().toString();
        if (set.length() == 0)
        {
            set += "-";
            editText.setText(set);
            return;
        }
        else if (set.charAt(set.length() - 1) == '+' || set.charAt(set.length() - 1) == '-' || set.charAt(set.length() - 1) == '\u00D7' || set.charAt(set.length() - 1) == '\u00F7') {
            Toast toast = Toast.makeText(MainActivity.this, "Can't put two operation signs next to each other", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        set += "-";
        editText.setText(set);
    }

    public void onbtnClearClick(View view) {
        editText.setText("");
        textViewResult.setText("");
    }

    public void onbtnEqualsClick(View view) {

        editText.setText(textViewResult.getText().toString());
    }

    public void onbtnDotClick(View view) {
        String set = editText.getText().toString();
        if (!set.contains(".")) {
            set += ".";
            editText.setText(set);
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
        editText.setText(set);

    }
    public void onbtnBackspceClick(View view)
    {
        String set = editText.getText().toString();
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

        editText.setText(set);
        if(set.charAt(set.length()-1) != '-' && set.charAt(set.length()-1) != '+' && set.charAt(set.length()-1) != '\u00D7' && set.charAt(set.length()-1) != '\u00F7')
        {
            manipulateEditText(-1,view);//won't add any number there
        }


    }
}
