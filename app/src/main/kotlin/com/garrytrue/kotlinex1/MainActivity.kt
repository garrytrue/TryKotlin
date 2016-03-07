package com.garrytrue.kotlinex1

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    val TAG: String = "MainActivity";
    val KEY_INPUT_DATA: String = "INPUT_DATA_KEY";
    val KEY_DIALOG_SHOW: String = "KEY_IS_DIALOG_SHOWED";
    var mTextView: TextView? = null;
    var dialog: AlertDialog? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI();
    }

    fun initUI() {
        mTextView = findViewById(R.id.text_view) as TextView;
        if( PreferenceManager.getDefaultSharedPreferences(applicationContext).contains(KEY_INPUT_DATA)){
            mTextView?.text = PreferenceManager.getDefaultSharedPreferences(applicationContext).getString(KEY_INPUT_DATA, "");

        }
        findViewById(R.id.button).setOnClickListener() {
            clearLastInput()
            showDialog() };
    }

    fun showDialog() {
        val builder = AlertDialog.Builder(this);
        val inputData = EditText(this);
        dialogWasShowed()

        //Init dialog view
        with(builder) {
            print("ShowDialog");
            Log.d(TAG, "showDialog");
            setTitle(R.string.dialog_title);
            setMessage(R.string.dialog_msg);
            setView(inputData);
            if( PreferenceManager.getDefaultSharedPreferences(applicationContext).contains(KEY_INPUT_DATA)){
                inputData.setText(PreferenceManager.getDefaultSharedPreferences(applicationContext).getString(KEY_INPUT_DATA, ""));
            }
            setPositiveButton(android.R.string.ok) {
                dialog, whichBtn ->
                mTextView?.text = inputData.text;
                resetDialogShow()
            }
            setNegativeButton(android.R.string.cancel) {
                dialog, whichBtn ->
                resetDialogShow()
            }
        }
        dialog = builder.create();
        dialog?.show()
        inputData.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.d(TAG, "TextChanged")
                PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putString(KEY_INPUT_DATA, p0.toString()).apply();
            }
        })
    }

    override fun onPause() {
        dialog?.dismiss()
        super.onPause()
    }
    override fun onResume(){
        super.onResume()
        if(isDialogShowed()){
            showDialog();
        }
    }
    fun isDialogShowed(): Boolean
       =  PreferenceManager.getDefaultSharedPreferences(applicationContext).contains(KEY_DIALOG_SHOW);

    fun dialogWasShowed(){
        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().putBoolean(KEY_DIALOG_SHOW, true).apply();
    }
    fun resetDialogShow(){
        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().remove(KEY_DIALOG_SHOW).apply()
    }
    fun clearLastInput(){
        PreferenceManager.getDefaultSharedPreferences(applicationContext).edit().remove(KEY_INPUT_DATA).apply()
    }
}
