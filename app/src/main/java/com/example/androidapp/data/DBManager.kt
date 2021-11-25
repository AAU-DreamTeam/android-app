package com.example.androidapp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper;
import com.example.androidapp.data.models.Country
import com.example.androidapp.data.models.Product
import com.example.androidapp.data.models.Purchase
import com.example.androidapp.data.models.StoreItem
import com.example.androidapp.viewmodels.MONTH
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

public class DBManager(context: Context?) : SQLiteOpenHelper(context, "FoodEmission.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        createTables(db)
        insertProductData(db)
        insertCountryData(db)
        insertStoreItemData(db)
        insertPurchaseData(db)
    }

    private fun createTables(db: SQLiteDatabase) {
        /*val createUserTableStmnt = "CREATE TABLE user(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, password TEXT NOT NULL, created DATE NOT NULL);"
        db.execSQL(createUserTableStmnt)*/

        db.execSQL("PRAGMA foreign_keys = ON;")

        val createCountryTableStmnt = "CREATE TABLE country(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, transportEmission REAL NOT NULL, GHPenalty BOOLEAN NOT NULL CHECK(GHPenalty IN (0, 1)));"
        db.execSQL(createCountryTableStmnt)

        val createProductTableStmnt = "CREATE TABLE product(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, cultivation REAL NOT NULL, iluc REAL NOT NULL, processing REAL NOT NULL, packaging REAL NOT NULL, retail REAL NOT NULL, GHCultivated BOOLEAN NOT NULL CHECK(GHCultivated IN (0, 1)));"
        db.execSQL(createProductTableStmnt)

        val createStoreItemTableStmnt = "CREATE TABLE storeItem(id INTEGER PRIMARY KEY AUTOINCREMENT, productID INTEGER NOT NULL, countryID INTEGER NOT NULL, receiptText TEXT NOT NULL, organic BOOLEAN NOT NULL CHECK(organic IN (0, 1)), packaged BOOLEAN NOT NULL CHECK(packaged IN (0, 1)), weight REAL NOT NULL, store TEXT NOT NULL, FOREIGN KEY(productID) REFERENCES product(id), FOREIGN KEY(countryID) REFERENCES country(id));"
        db.execSQL(createStoreItemTableStmnt)

        val createPurchaseTableStmnt = "CREATE TABLE purchase(id INTEGER PRIMARY KEY AUTOINCREMENT, storeItemID INTEGER NOT NULL, quantity INTEGER NOT NULL, timestamp TEXT NOT NULL, FOREIGN KEY(storeItemID) REFERENCES storeItem(id));"
        db.execSQL(createPurchaseTableStmnt)
    }

    private fun insertCountryData(db: SQLiteDatabase) {
        insertCountry(db, "Italien", 0.62, false)
        insertCountry(db, "Polen", 0.32, true)
        insertCountry(db, "Spanien", 0.74, false)
        insertCountry(db, "Holland", 0.26, true)
        insertCountry(db, "Danmark", 0.05, true)
        insertCountry(db, "Marokko", 0.55, false)
    }

    private fun insertCountry(db: SQLiteDatabase, name: String, emission: Double, GHPenalty: Boolean) : Long {
        val contentValues = ContentValues()

        contentValues.put("name", name)
        contentValues.put("transportEmission", emission)
        contentValues.put("GHPenalty", GHPenalty)

        return db.insert("country", null, contentValues)
    }

    private fun insertProductData(db: SQLiteDatabase) {
        insertProduct(db, "Tomat",0.07,0.01,0.0,0.14,0.01,true)
        insertProduct(db, "Agurk", 0.05,0.01,0.0,0.14,0.01, true)
        insertProduct(db, "Salat", 0.08, 0.02,0.0,0.06,0.01,false)
        insertProduct(db, "Rødkål",0.1, 0.02, 0.0,0.06,0.01,false)
        insertProduct(db, "Hvidkål",0.1, 0.02, 0.0,0.06,0.01,false)
        insertProduct(db, "Æble",0.18,0.02,0.0,0.14,0.01,false)
        insertProduct(db, "Spidskål",0.1,0.02,0.0,0.06,0.01,false)
        insertProduct(db, "Blomkål",0.15,0.04,0.0,0.06,0.01,false)
        insertProduct(db, "Champignon",0.01,0.01,0.0,0.26,0.0,true)
        insertProduct(db, "Peberfrugt", 0.25,0.03,0.0,0.14,0.01,true)
        insertProduct(db, "Broccoli",0.15,0.04,0.0,0.06,0.01,false)
        insertProduct(db, "Gulerod",0.11,0.02,0.0,0.06,0.01,false)
    }

    private  fun insertProduct(db: SQLiteDatabase, name: String, cultivation: Double, iluc: Double, processing: Double, packaging: Double, retail: Double, GHCultivated: Boolean ): Long {
        val contentValues = ContentValues()

        contentValues.put("name", name)
        contentValues.put("cultivation", cultivation)
        contentValues.put("iluc", iluc)
        contentValues.put("processing", processing)
        contentValues.put("packaging", packaging)
        contentValues.put("retail", retail)
        contentValues.put("GHCultivated", GHCultivated)

        return db.insert("product", null, contentValues)
    }

    private fun insertStoreItemData(db: SQLiteDatabase){
        insertStoreItem(db, 1, 3,"TOMATER ØKO LØSE SPANIEN", true, false, 0.045)
        insertStoreItem(db, 1, 3,"TOMATER ØKO LØSE SPANIEN", true, false, 0.065)
        insertStoreItem(db, 1, 5,"TOMATER ØKO LØSE DANMARK", true, false, 0.045)
        insertStoreItem(db, 1, 5,"TOMATER ØKO LØSE DK", true, false, 0.045)
        insertStoreItem(db, 1, 5,"TOMATER DANMARK", false, true, 0.065)
        insertStoreItem(db, 6, 1,"ÆBLER ØKO LØSE ITALIEN", true, false, 0.045)
        insertStoreItem(db, 6, 1,"ÆBLER ØKO LØSE ITALIEN", true, false, 0.065)
        insertStoreItem(db, 6, 5,"ÆBLER ØKO LØSE DANMARK", true, false, 0.045)
        insertStoreItem(db, 6, 5,"ÆBLER ØKO LØSE DK", true, false, 0.045)
        insertStoreItem(db, 6, 5,"ÆBLER DANMARK", false, true, 0.065)
    }

    fun insertStoreItem(db: SQLiteDatabase, productID: Int, countryID: Int, receiptText: String, organic: Boolean, packaged: Boolean, weight: Double, store: String = "Føtex"): Long {
        val contentValues = ContentValues()

        contentValues.put("productID", productID)
        contentValues.put("countryID", countryID)
        contentValues.put("receiptText", receiptText)
        contentValues.put("organic", organic)
        contentValues.put("packaged", packaged)
        contentValues.put("weight", weight)
        contentValues.put("store", store)

        return db.insert("storeItem", null, contentValues)
    }

    private fun insertPurchaseData(db: SQLiteDatabase){
        insertPurchase(db,1, 2, "2021-11-01 14:30:00")
        insertPurchase(db,2, 3, "2021-11-01 14:30:00")
        insertPurchase(db,3, 1, "2021-11-05 14:30:00")
        insertPurchase(db,4, 7, "2021-11-10 14:30:00")
        insertPurchase(db,5, 3, "2021-11-13 14:30:00")
        insertPurchase(db,6, 5, "2021-11-15 14:30:00")
        insertPurchase(db,7, 1, "2021-11-16 14:30:00")
        insertPurchase(db,8, 3, "2021-11-20 14:30:00")
        insertPurchase(db,9, 2, "2021-11-21 14:30:00")
        insertPurchase(db,10, 5, "2021-11-23 14:30:00")

    }

    fun insertPurchase(db: SQLiteDatabase, storeItemID: Int, quantity: Int, timestamp: String): Long {
        val contentValues = ContentValues()

        contentValues.put("storeItemID", storeItemID)
        contentValues.put("quantity", quantity)
        contentValues.put("timestamp", timestamp)

        return db.insert("purchase", null, contentValues)
    }

    //id INTEGER PRIMARY KEY AUTOINCREMENT, productID INTEGER NOT NULL, countryID INTEGER NOT NULL, receiptText TEXT NOT NULL, organic BOOLEAN NOT NULL CHECK(organic IN (0, 1)), packaged BOOLEAN NOT NULL CHECK(packaged IN (0, 1)), weight REAL NOT NULL, store TEXT NOT NULL, FOREIGN KEY(productID) REFERENCES product(id), FOREIGN KEY(countryID) REFERENCES country(id)

    fun fetchAllPurchasesFromMonth(month: MONTH, year: String): List<Purchase> {
        val db = readableDatabase
        val purchases : MutableList<Purchase> = mutableListOf()
        val query = "SELECT storeItem.organic, storeItem.packaged, storeItem.weight, product.name, product.cultivation, product.iluc, product.processing, product.packaging, product.retail, product.GHCultivated, country.name, country.transportEmission, country.GHPenalty, SUM(purchase.quantity) FROM purchase INNER JOIN storeItem ON purchase.storeItemID = storeItem.id INNER JOIN product ON storeItem.productID = product.id INNER JOIN country ON storeItem.countryID = country.id WHERE strftime('%Y', purchase.timestamp) = '$year' AND strftime('%m', purchase.timestamp) = '${month.position}' GROUP BY product.name, country.name, storeItem.organic, storeItem.packaged, storeItem.weight ORDER BY product.name, country.name, storeItem.organic, storeItem.packaged, storeItem.weight;"


        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val country = Country(cursor.getString(10), cursor.getDouble(11), cursor.getInt(12) != 0)
                val product = Product(cursor.getString(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getDouble(7), cursor.getDouble(8), cursor.getInt(9) != 0)
                val storeItem = StoreItem(product, country, cursor.getInt(0) != 0, cursor.getInt(1) != 0, cursor.getDouble(2))
                purchases.add(Purchase(storeItem, cursor.getInt(13)))
            } while (cursor.moveToNext())
        }

        cursor.close()

        return purchases
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}