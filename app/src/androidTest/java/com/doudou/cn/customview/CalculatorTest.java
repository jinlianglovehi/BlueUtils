package com.doudou.cn.customview;

import android.util.Log;

import com.android.volley.toolbox.Volley;

import junit.framework.TestCase;

/**
 * Created by jinliang on 15/11/15.
 */
public class CalculatorTest extends TestCase {
    private Calculator mCalculator =new Calculator();
    private static  final String TAG = CalculatorTest.class.getSimpleName();
    public void setUp() throws Exception {
        super.setUp();
        Log.i(TAG, "----setUp----");

    }

    public void tearDown() throws Exception {
        Log.i(TAG,"----tearDown----");
    }

    public void testSum() throws Exception {
          Log.i(TAG, "----result---" + mCalculator.sum(1, 3));

    }
}