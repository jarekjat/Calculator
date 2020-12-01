package com.example.calculator;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class MainActivityViewModel extends ViewModel {
    private boolean whetherFirstCharacterInEditText;
    private MutableLiveData<String> _toastMessage = new MutableLiveData<String>();
    public LiveData<String> getToastMessage(){
        return _toastMessage;
    }
    private MutableLiveData<String> _textViewText = new MutableLiveData<String>();
    public LiveData<String> getTextViewText(){
        return _textViewText;
    }
    private MutableLiveData<String> _editTextText = new MutableLiveData<String>();
    public LiveData<String> getEditTextText(){
        return  _editTextText;
    }
    public static final String TAG  = "MainActivityViewModel";

    public boolean isWhetherFirstCharacterInEditText() {
        return whetherFirstCharacterInEditText;
    }

    public MainActivityViewModel() {
        this.whetherFirstCharacterInEditText = true;
        set_editTextText("0");
        set_textViewText("0");
    }

    public void setWhetherFirstCharacterInEditText(boolean whetherFirstCharacterInEditText) {
        this.whetherFirstCharacterInEditText = whetherFirstCharacterInEditText;
    }

    public String get_textViewText() {
        return _textViewText.getValue();
    }

    public void set_textViewText(String value) {
        _textViewText.setValue(value);
    }

    public void set_editTextText(String value) {
        _editTextText.setValue(value);
    }
    public void performOperation(int whichOne, View view) {
        //String set = editText.getText().toString();
        //long textToInt = Integer.parseInt(set);
        if (whetherFirstCharacterInEditText) {
            _editTextText.setValue("");//getting rid of initial 0
            whetherFirstCharacterInEditText = false;
        }
        String set;
        set = _editTextText.getValue();
        if(set.length() >=1 && whichOne==0 && set.charAt(set.length()-1) == '\u00F7'){
            _textViewText.setValue("Can't divide by 0");
            //editText.setText("");
            return;
        }
        if(whichOne != -1)//-1 is used in backspaceButton onClick method - I don't add any number through it
        {
            set += whichOne;
            _editTextText.setValue(set);
        }
        else
        {
            set = _editTextText.getValue();
        }
        ArrayList<Integer> delimetersOperationSigns = getOperationSigns(set);
        if (delimetersOperationSigns.size() > 2)
        {
            BigDecimal result = new BigDecimal(0);
            MathContext m = new MathContext(17);
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
                        BigDecimal first;

                        int whichIndexTaken;
                        if(delimetersOperationSigns.get(whichIndexDelimiter-1) == 0)//beginning of string set
                        {
                            whichIndexTaken = delimetersOperationSigns.get(whichIndexDelimiter-1);
                            //first = Double.parseDouble(set.substring(whichIndexTaken,number));
                            first = new BigDecimal(set.substring(whichIndexTaken,number));
                        }
                        else //not a beginning of string set
                        {
                            whichIndexTaken = delimetersOperationSigns.get(whichIndexDelimiter-1)+1;
                            //first = Double.parseDouble(set.substring(whichIndexTaken,number));
                            first = new BigDecimal(set.substring(whichIndexTaken,number));//position starting +1 in order to not get operation sign
                        }
                        BigDecimal second = new BigDecimal(set.substring(number+1,delimetersOperationSigns.get(whichIndexDelimiter+1)));//Double.parseDouble(set.substring(number+1,delimetersOperationSigns.get(whichIndexDelimiter+1)));
                        if (set.charAt(number) == '\u00D7')//multiplication
                        {
                            result=first.multiply(second,m);
                        }
                        else if(set.charAt(number) == '\u00F7')//division
                        {
                            result=first.divide(second,m);
                        }
                        set = set.substring(0,whichIndexTaken) +  result.toString() + set.substring(delimetersOperationSigns.get(whichIndexDelimiter+1));
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
                        BigDecimal first;
                        int whichIndexTaken;
                        if(delimetersOperationSigns.get(whichIndexDelimiter-1) == 0)//beginning of string set
                        {
                            whichIndexTaken = delimetersOperationSigns.get(whichIndexDelimiter-1);
                            try {
                                //first = Double.parseDouble(set.substring(whichIndexTaken,number));
                                first = new BigDecimal(set.substring(whichIndexTaken,number));

                            }
                            catch (NumberFormatException e)
                            {
                                _toastMessage.setValue("This is too many");
//                                Toast toast = Toast.makeText(MainActivity.this,
//                                        "This is too many",Toast.LENGTH_SHORT);
//                                toast.show();
                                Log.d(TAG, e.getMessage());
                                return;
                            }
                        }
                        else //not a beginning of string set
                        {
                            whichIndexTaken = delimetersOperationSigns.get(whichIndexDelimiter-1)+1;
                            try {
                                first = new BigDecimal(set.substring(whichIndexTaken,number));//position starting +1 in order to not get operation sign

                            }
                            catch (NumberFormatException e)
                            {
                                _toastMessage.setValue("This is too many");
//                                Toast toast = Toast.makeText(MainActivity.this,
//                                        "This is too many",Toast.LENGTH_SHORT);
//                                toast.show();
                                e.getMessage();
                                return;
                            }
                        }
                        BigDecimal second;
                        try {
                            second = new BigDecimal(set.substring(number+1,delimetersOperationSigns.get(whichIndexDelimiter+1)));
                        }
                        catch (NumberFormatException e)
                        {
//                            Toast toast = Toast.makeText(,
//                                    "This is too many",Toast.LENGTH_SHORT);
//                            toast.show();
                            e.getMessage();
                            return;
                        }

                        if (set.charAt(number) == '+')//adding
                        {
                            result=(first.add(second,m));
                        }
                        else if(set.charAt(number) == '-')//subtracting
                        {
                            result=first.subtract(second,m);
                        }
                        set = set.substring(0,whichIndexTaken) + result.toString() + set.substring(delimetersOperationSigns.get(whichIndexDelimiter+1));
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
        //DecimalFormat decimalFormat = new DecimalFormat("###,###.###");
        //set = decimalFormat.format(Double.parseDouble(set));
        _textViewText.setValue(set);
        Log.i(TAG,"perform operation: Result value: " + _textViewText.getValue());
    }
    private static ArrayList<Integer> getOperationSigns(String text)
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

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG,"onCleared, ViewModel destroyed");
    }
}
