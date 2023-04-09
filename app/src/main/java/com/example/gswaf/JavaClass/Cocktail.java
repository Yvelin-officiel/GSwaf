package com.example.gswaf.JavaClass;

import java.util.List;

public class Cocktail {

    private int id;
    private String name;
    private String instruction;
    private String imageURL;


    /* Les deux listes sont instanciées ensemble donc pour un id,
     * on a un ingredient dans ingredients et sa mesure dans measures
     */
    private List<String> ingredients;
    private List<String> measures;

    public Cocktail(){

    }

    public Cocktail(int id){
        this.id = id;
    }

    public Cocktail(int id, String name, String instruction, String imageURL, List<String> ingredients, List<String> measures) {
        this.id = id;
        this.name = name;
        this.instruction = instruction;
        this.imageURL = imageURL;
        this.measures = measures;
        this.ingredients = ingredients;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getInstruction() {
        return instruction;
    }

    public String getImageURL() {
        return imageURL;
    }

    public List<String> getMeasures() {
        return measures;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public String getRecipe(){
        String recipe = "";
        for (int i = 0; i < this.ingredients.size(); i++){
            recipe += this.ingredients.get(i)+" / ";
        }
        return recipe;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Cocktail{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", instruction='" + instruction + '\'' +
                ", imageURL=" + imageURL +
                ", ingredients=" + ingredients +
                ", measures=" + measures +
                '}';
    }
}
