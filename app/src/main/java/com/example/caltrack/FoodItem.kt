package com.example.caltrack

class FoodItem(name: String?, cals: Float?, brand:String?, carbs: Float?, fats: Float?, pro: Float?) {
    var cals: Float? = null
    var name: String? = null
    var brand: String? = null
    var carbs: Float? = null
    var fats: Float? = null
    var pro: Float? = null

    init {
        this.name = name
        this.cals = cals
        this.brand = brand
        this.carbs = carbs
        this.fats = fats
        this.pro = pro
    }
}
