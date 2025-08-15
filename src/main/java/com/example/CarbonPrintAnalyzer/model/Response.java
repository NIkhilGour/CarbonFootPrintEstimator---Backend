package com.example.CarbonPrintAnalyzer.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Response {
    private String dish;
    @JsonProperty("estimated_carbon_kg")
    private double estimatedCarbonKg;
    private List<Ingredient> ingredients;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    // getters and setters
    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public double getEstimatedCarbonKg() {
        return estimatedCarbonKg;
    }

    public void setEstimatedCarbonKg(double estimatedCarbonKg) {
        this.estimatedCarbonKg = estimatedCarbonKg;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public static class Ingredient {
        private String name;
        @JsonProperty("carbon_kg")
        private double carbonKg;

        // getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getCarbonKg() {
            return carbonKg;
        }

        public void setCarbonKg(double carbonKg) {
            this.carbonKg = carbonKg;
        }
    }
}
