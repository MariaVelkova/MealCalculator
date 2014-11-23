package com.example.maria.mealcalculator;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.CheckedInputStream;

public class MealCalculationActivity
        extends Activity
        implements
            AdapterView.OnItemSelectedListener,
            SeekBar.OnSeekBarChangeListener,
            View.OnClickListener, TextWatcher,
            CompoundButton.OnCheckedChangeListener {

    private Button usdCurrency;
    private Button bgnCurrency;
    private Button eurCurrency;
    private Button calculate;
    private Button about;

    private Spinner dishName;
    private TextView dishPrice;
    private TextView dishCurrency;
    private EditText dishCount;
    private Button addDish;
    private Button removeDish;

    private Spinner desertName;
    private TextView desertPrice;
    private TextView desertCurrency;
    private EditText desertCount;
    private Button addDesert;
    private Button removeDesert;

    private Spinner drinkName;
    private TextView drinkPrice;
    private TextView drinkCurrency;
    private SeekBar drinkCount;

    private CheckBox makeDelivery;
    private TextView deliveryPrice;
    private TextView deliveryCurrency;

    private TextView totalPrice;
    private TextView totalCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_calculation);

        usdCurrency = (Button) findViewById(R.id.usdCurrency);
        usdCurrency.setOnClickListener(this);

        bgnCurrency = (Button) findViewById(R.id.bgnCurrency);
        bgnCurrency.setOnClickListener(this);

        eurCurrency = (Button) findViewById(R.id.eurCurrency);
        eurCurrency.setOnClickListener(this);

        calculate = (Button) findViewById(R.id.btnCalculate);
        calculate.setOnClickListener(this);

        about = (Button) findViewById(R.id.aboutUs);
        about.setOnClickListener(this);

        Resources res = getResources();
        InputStream jsonStream = res.openRawResource(R.raw.meals);
        Scanner scanner = new Scanner(jsonStream).useDelimiter("\\A");
        String jsonItems =  scanner.hasNext() ? scanner.next():"";

        List<MealItem> dishItems = new ArrayList<MealItem>();

        try {
            JSONObject jsonObject = new JSONObject(jsonItems);
            JSONArray jsonDishes = jsonObject.getJSONArray("dishes");
            for (int i = 0, m = jsonDishes.length(); i < m; i++) {
                JSONObject jsonDishItem = jsonDishes.getJSONObject(i);
                dishItems.add(new MealItem( jsonDishItem.getString("name"), jsonDishItem.getDouble("price")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<MealItem> desertItems = new ArrayList<MealItem>();

        try {
            JSONObject jsonObject = new JSONObject(jsonItems);
            JSONArray jsonDeserts = jsonObject.getJSONArray("deserts");
            for (int i = 0, m = jsonDeserts.length(); i < m; i++) {
                JSONObject jsonDesertItem = jsonDeserts.getJSONObject(i);
                desertItems.add(new MealItem( jsonDesertItem.getString("name"), jsonDesertItem.getDouble("price")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        List<MealItem> drinkItems = new ArrayList<MealItem>();

        try {
            JSONObject jsonObject = new JSONObject(jsonItems);
            JSONArray jsonDrinks = jsonObject.getJSONArray("drinks");
            for (int i = 0, m = jsonDrinks.length(); i < m; i++) {
                JSONObject jsonDrinkItem = jsonDrinks.getJSONObject(i);
                drinkItems.add(new MealItem( jsonDrinkItem.getString("name"), jsonDrinkItem.getDouble("price")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        dishName = (Spinner) findViewById(R.id.dishName);
        ArrayAdapter dishNameAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, dishItems);
        dishName.setAdapter(dishNameAdapter);
        dishName.setOnItemSelectedListener(this);

        dishPrice = (TextView) findViewById(R.id.dishPrice);
        dishCurrency = (TextView) findViewById(R.id.dishCurrency);
        dishCount = (EditText) findViewById(R.id.dishCount);
        dishCount.addTextChangedListener(this);
        addDish = (Button) findViewById(R.id.addDish);
        addDish.setOnClickListener(this);
        removeDish = (Button) findViewById(R.id.removeDish);
        removeDish.setOnClickListener(this);

        desertName = (Spinner) findViewById(R.id.desertName);
        ArrayAdapter desertNameAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, desertItems);
        desertName.setAdapter(desertNameAdapter);
        desertName.setOnItemSelectedListener(this);

        desertPrice = (TextView) findViewById(R.id.desertPrice);
        desertCurrency = (TextView) findViewById(R.id.desertCurrency);
        desertCount = (EditText) findViewById(R.id.desertCount);
        desertCount.addTextChangedListener(this);
        addDesert = (Button) findViewById(R.id.addDesert);
        addDesert.setOnClickListener(this);
        removeDesert = (Button) findViewById(R.id.removeDesert);
        removeDesert.setOnClickListener(this);

        drinkName = (Spinner) findViewById(R.id.drinkName);
        ArrayAdapter drinkNameAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, drinkItems);
        drinkName.setAdapter(drinkNameAdapter);
        drinkName.setOnItemSelectedListener(this);

        drinkPrice = (TextView) findViewById(R.id.drinkPrice);
        drinkCurrency = (TextView) findViewById(R.id.drinkCurrency);
        drinkCount = (SeekBar) findViewById(R.id.drinkCount);
        drinkCount.setOnSeekBarChangeListener(this);

        makeDelivery = (CheckBox) findViewById(R.id.homeDelivery);
        makeDelivery.setOnCheckedChangeListener(this);
        deliveryPrice = (TextView) findViewById(R.id.homeDeliveryPrice);
        deliveryCurrency = (TextView) findViewById(R.id.homeDeliveryCurrency);

        totalPrice = (TextView) findViewById(R.id.totalPrice);
        totalCurrency = (TextView) findViewById(R.id.currencyLabel);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_meal_calculation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          String currency = totalCurrency.getText().toString();
          MealItem item = (MealItem) parent.getSelectedItem();
          String price = Double.toString(changeCurrncy(item.getPrice(), new String(getResources().getString(R.string.eur)), currency));
        switch (parent.getId()) {
              case R.id.dishName:
                dishPrice.setText(price);
               break;
            case R.id.desertName:
                desertPrice.setText(price);
                break;
            case R.id.drinkName:
                drinkPrice.setText(price);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        int itemCount = drinkCount.getProgress() + 1;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        int itemCount;

        switch(v.getId()) {
            case R.id.usdCurrency:
            case R.id.bgnCurrency:
            case R.id.eurCurrency:
                String previousCurrency = totalCurrency.getText().toString();
                int newCurrencyId;
                String newCurrency;
                switch(v.getId()) {
                    case R.id.usdCurrency:
                        newCurrencyId = R.string.usd;
                        break;
                    case R.id.bgnCurrency:
                        newCurrencyId = R.string.bgn;
                        break;
                    case R.id.eurCurrency:
                        newCurrencyId = R.string.eur;
                        break;
                    default:
                        newCurrencyId = R.string.eur;
                        break;
                }
                newCurrency = new String(getResources().getString(newCurrencyId));
                if (previousCurrency.equals(newCurrency) == false) {
                    Double dishPriceValue = Double.parseDouble(dishPrice.getText().toString());
                    Double newDishPriceValue = changeCurrncy(dishPriceValue, previousCurrency, newCurrency);
                    dishPrice.setText(Double.toString(newDishPriceValue));
                    dishCurrency.setText(newCurrencyId);

                    Double desertPriceValue = Double.parseDouble(desertPrice.getText().toString());
                    Double newDesertPriceValue = changeCurrncy(desertPriceValue, previousCurrency, newCurrency);
                    desertPrice.setText(Double.toString(newDesertPriceValue));
                    desertCurrency.setText(newCurrencyId);

                    Double drinkPriceValue = Double.parseDouble(drinkPrice.getText().toString());
                    Double newDrinkPriceValue = changeCurrncy(drinkPriceValue, previousCurrency, newCurrency);
                    drinkPrice.setText(Double.toString(newDrinkPriceValue));
                    drinkCurrency.setText(newCurrencyId);

                    Double deliveryPriceValue = Double.parseDouble(deliveryPrice.getText().toString());
                    Double newDeliveryPriceValue = changeCurrncy(deliveryPriceValue, previousCurrency, newCurrency);
                    deliveryPrice.setText(Double.toString(newDeliveryPriceValue));
                    deliveryCurrency.setText(newCurrencyId);

                    Double totalPriceValue = Double.parseDouble(totalPrice.getText().toString());
                    Double newTotalPriceValue = changeCurrncy(totalPriceValue, previousCurrency, newCurrency);
                    totalPrice.setText(Double.toString(newTotalPriceValue));
                    totalCurrency.setText(newCurrencyId);
                }
                break;
            case R.id.addDish:
                itemCount = Integer.parseInt(dishCount.getText().toString()) + 1;
                if (itemCount > 10) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.dishes_max_limit), Toast.LENGTH_SHORT).show();
                } else {
                    dishCount.setText(Integer.toString(itemCount));
                }
                break;
            case R.id.removeDish:
                itemCount = Integer.parseInt(dishCount.getText().toString()) - 1;
                if (itemCount < 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.dishes_min_limit), Toast.LENGTH_SHORT).show();
                } else {
                    dishCount.setText(Integer.toString(itemCount));
                }
                break;
            case R.id.addDesert:
                itemCount = Integer.parseInt(desertCount.getText().toString()) + 1;
                if (itemCount > 10) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.deserts_max_limit), Toast.LENGTH_SHORT).show();
                } else {
                    desertCount.setText(Integer.toString(itemCount));
                }
                break;
            case R.id.removeDesert:
                itemCount = Integer.parseInt(desertCount.getText().toString()) - 1;
                if (itemCount < 0) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.deserts_min_limit), Toast.LENGTH_SHORT).show();
                } else {
                    desertCount.setText(Integer.toString(itemCount));
                }
                break;
            case R.id.btnCalculate:
                Double total = 0.0;
                Double dishPriceValue = Double.parseDouble(dishPrice.getText().toString());
                int dishCountValue = Integer.parseInt(dishCount.getText().toString());
                Double desertPriceValue = Double.parseDouble(desertPrice.getText().toString());
                int desertCountValue = Integer.parseInt(desertCount.getText().toString());
                Double drinkPriceValue = Double.parseDouble(drinkPrice.getText().toString());
                int drinkCountValue = drinkCount.getProgress();
                Double deliveryPriceValue = Double.parseDouble(deliveryPrice.getText().toString());
                int deliveryCountValue = makeDelivery.isChecked()?1:0;
                total = Double.valueOf(dishPriceValue*dishCountValue +
                        desertPriceValue*desertCountValue +
                        drinkPriceValue*drinkCountValue +
                        deliveryPriceValue*deliveryCountValue);
                totalPrice.setText(Double.toString(Math.round(total*100.0)/100.0));
                break;
            case  R.id.aboutUs:
                Intent intent = new Intent(this, AboutUsActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public Double changeCurrncy(Double price, String prevCurrency, String newCurrency) {

        String eurCurrency = getResources().getString(R.string.eur);
        String usdCurrency = getResources().getString(R.string.usd);
        String bgnCurrency = getResources().getString(R.string.bgn);

        Map<String, Float> convertToEURIndex= new HashMap<String, Float>();
        convertToEURIndex.put(eurCurrency, Float.valueOf("1"));
        convertToEURIndex.put(usdCurrency, Float.valueOf("1.23904"));
        convertToEURIndex.put(bgnCurrency,Float.valueOf("1.95704434"));
        return (double) Math.round(price * convertToEURIndex.get(newCurrency) * 100 / convertToEURIndex.get(prevCurrency))/100;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Double totalPriceValue = Double.valueOf(totalPrice.getText().toString());
        if (totalPriceValue != 0.0) {
            Double deliveryPriceValue = Double.valueOf(deliveryPrice.getText().toString());
            if (isChecked) {
                totalPriceValue += deliveryPriceValue;
            } else {
                totalPriceValue -= deliveryPriceValue;
            }
            totalPrice.setText(Double.toString(Math.round(totalPriceValue*100.0)/100.0));
        }
    }
}
