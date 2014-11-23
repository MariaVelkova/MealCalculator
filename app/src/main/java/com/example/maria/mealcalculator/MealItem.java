package com.example.maria.mealcalculator;

/**
 * Created by Maria on 11/22/2014.
 */
public class MealItem {

        private String name = "";
        private Double price = 0.0;

        public MealItem( String _name, Double _price )
        {
            name = _name;
            price = _price;
        }

        public String toString()
        {
            return name;
        }

        public String getName()
        {
            return name;
        }

        public Double getPrice()
        {
              return price;
        }
}
