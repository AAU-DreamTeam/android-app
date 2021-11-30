package com.example.androidapp.data.models

import com.example.androidapp.data.EmissionCalculator
import java.lang.Exception
import java.util.*

class StoreItem (val id: Int,
                 var product: Product,
                 var country: Country,
                 _receiptText: String,
                 var organic: Boolean,
                 var packaged: Boolean,
                 var weight: Double,
                 val store: String = "Føtex"){

    constructor(product: Product,
                country: Country,
                _receiptText: String,
                organic: Boolean,
                packaged: Boolean,
                weight: Double,
                store: String = "Føtex"): this(0, product, country, _receiptText, organic, packaged, weight, store)

    var receiptText = _receiptText.toLowerCase(Locale.getDefault())
    val emissionPerKg = EmissionCalculator.calcEmission(this)
    val emissionPerItem = weight * emissionPerKg

    fun hasValidWeight(): Boolean {
        return weight > 0.0
    }
    fun isValid(): Boolean{
        return receiptText.isNotEmpty() && hasValidWeight() && product.isValid() && country.validate()
    }

    override fun toString(): String {
        return "${product.name}, ${country.name}${if (organic) ", Øko" else ""}${if (!packaged) ", Løs" else ""}"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StoreItem

        if (product.id != other.product.id) return false
        if (country.id != other.country.id) return false
        if (organic != other.organic) return false
        if (packaged != other.packaged) return false

        return true
    }

    override fun hashCode(): Int {
        var result = product.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + organic.hashCode()
        result = 31 * result + packaged.hashCode()
        return result
    }
}