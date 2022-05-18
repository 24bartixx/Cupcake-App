package com.example.cupcake.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

private const val PRICE_FOR_CUPCAKE = 2.00
private const val PRICE_FOR_SAME_DAY_PICKUP = 3.00

class OrderViewModel: ViewModel() {
    private var _quantity = MutableLiveData<Int>()
    val quantity: LiveData<Int> = _quantity
    private var _flavor = MutableLiveData<String>()
    val flavor: LiveData<String> = _flavor
    private var _date = MutableLiveData<String>()
    val date: LiveData<String> = _date
    private var _price = MutableLiveData<Double>()
    // Transformation.map() function takes source LiveDate and a function as parameters and manipulates the source LiveData
    // getCurrencyInstance().format(it) formats the price to the local currency
    val price: LiveData<String> = Transformations.map(_price) { NumberFormat.getCurrencyInstance().format(it) }

    val dateOptions = getPickupOptions()



    init {
        resetOrder()
    }


    fun resetOrder() {
        _quantity.value = 0
        _flavor.value = ""
        _date.value = dateOptions[0]
        _price.value = 0.0
    }

    private fun updatePrice() {
        // elvis operator ?: - if the expression on the left is null use the one on the left
        var calculatedPrice = (quantity.value ?: 0) * PRICE_FOR_CUPCAKE
        if(dateOptions[0] == _date.value) calculatedPrice += PRICE_FOR_SAME_DAY_PICKUP
            _price.value = calculatedPrice
    }

    private fun getPickupOptions(): List<String> {
        val options = mutableListOf<String>()

        // formatter string
        // SimpleDateFormat is a class for formatting and parsing dates in a locale-sensitive manner
        // Argument 1: representation of Date and Time formats (example: "E MMM d")
        // List of patterns letters: https://developer.android.com/reference/java/text/SimpleDateFormat#date-and-time-patterns
        // Argument 2: Locale.getDDefault() - retrieving the locale information set on the user's device
        // Locale is a combination of language and country code
        // language: two-letter lowercase ISO language codes (e.g. "en"): https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes
        // country code: two-letter uppercase ISO country codes (e.g. "EN"):https://en.wikipedia.org/wiki/List_of_ISO_3166_country_codes
        val formatter = SimpleDateFormat("E MMM d", Locale.getDefault())

        // calendar variable will contain the current date and time
        val calendar = Calendar.getInstance()

        repeat(4){
            // format a date and add it to the list
            options.add(formatter.format(calendar.time))

            // (CODE) Log.d("WTF", options.toString())
            // increment the calendar by 1
            calendar.add(Calendar.DATE, 1)
        }

        return options

    }

    // setter methods
    fun setQuantity(cupcakeQuantity: Int) {
        _quantity.value = cupcakeQuantity
        updatePrice()
    }

    fun setFlavor (desiredFlavor: String) {
        _flavor.value = desiredFlavor
    }

    fun setDate (dateOfPickup: String) {
        _date.value = dateOfPickup
        updatePrice()
    }

    // checks if there is no flavor
    fun noFlavour(): Boolean {
        return _flavor.value.isNullOrEmpty()
    }

}