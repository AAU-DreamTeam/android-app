package com.example.androidapp.data.models.daos

import android.content.ContentValues
import android.content.Context
import android.content.pm.ChangedPackages
import android.database.Cursor
import androidx.core.content.contentValuesOf
import com.example.androidapp.data.DBManager
import com.example.androidapp.data.EmissionCalculator
import com.example.androidapp.data.models.Product
import com.example.androidapp.data.models.StoreItem

class StoreItemDao(private val dbManager: DBManager) {
    constructor(context: Context): this(DBManager(context))

    fun loadAlternatives(storeItem: StoreItem) : List<StoreItem> {
        val query =
                "SELECT ${COLUMN_ID}, " +               // 0
                        "${COLUMN_RECEIPT_TEXT}, " +     // 1
                        "${COLUMN_ORGANIC}, " +         // 2
                        "${COLUMN_PACKAGED}, " +        // 3
                        "${COLUMN_WEIGHT}, " +          // 4
                        "${COLUMN_STORE}, " +           // 5
                        "${ProductDao.COLUMN_ID}, " +                // 6
                        "${ProductDao.COLUMN_NAME}, " +              // 7
                        "${ProductDao.COLUMN_CULTIVATION}, " +       // 8
                        "${ProductDao.COLUMN_ILUC}, " +              // 9
                        "${ProductDao.COLUMN_PROCESSING}, " +        // 10
                        "${ProductDao.COLUMN_PACKAGING}, " +         // 11
                        "${ProductDao.COLUMN_RETAIL}, " +            // 12
                        "${ProductDao.COLUMN_GHCULTIVATED}, " +      // 13
                        "${CountryDao.COLUMN_ID}, " +                   // 14
                        "${CountryDao.COLUMN_NAME}, " +                 // 15
                        "${CountryDao.COLUMN_TRANSPORT_EMISSION}, " +   // 16
                        "${CountryDao.COLUMN_GHPENALTY}, " +            // 17
                        "MIN(${EmissionCalculator.sqlEmissionFormula()}) " +
                        "FROM $TABLE " +
                        "INNER JOIN ${ProductDao.TABLE} ON $COLUMN_PRODUCT_ID = ${ProductDao.COLUMN_ID} " +
                        "INNER JOIN ${CountryDao.TABLE} ON $COLUMN_COUNTRY_ID = ${CountryDao.COLUMN_ID} " +
                        "WHERE $COLUMN_PRODUCT_ID = ${storeItem.product.id} " +
                        "GROUP BY $COLUMN_ORGANIC, $COLUMN_PACKAGED;"

        return dbManager.selectMultiple(query) {
            produceStoreItem(it)
        }
    }

    fun generateStoreItem(receiptText: String): StoreItem {
        var result: StoreItem? = null
        val formattedReceiptText = formatReceiptText(receiptText)
        val query =
                "SELECT ${COLUMN_ID}, " +               // 0
                       "${COLUMN_RECEIPT_TEXT}, " +     // 1
                       "${COLUMN_ORGANIC}, " +         // 2
                       "${COLUMN_PACKAGED}, " +        // 3
                       "${COLUMN_WEIGHT}, " +          // 4
                       "${COLUMN_STORE}, " +           // 5
                       "${ProductDao.COLUMN_ID}, " +                // 6
                       "${ProductDao.COLUMN_NAME}, " +              // 7
                       "${ProductDao.COLUMN_CULTIVATION}, " +       // 8
                       "${ProductDao.COLUMN_ILUC}, " +              // 9
                       "${ProductDao.COLUMN_PROCESSING}, " +        // 10
                       "${ProductDao.COLUMN_PACKAGING}, " +         // 11
                       "${ProductDao.COLUMN_RETAIL}, " +            // 12
                       "${ProductDao.COLUMN_GHCULTIVATED}, " +      // 13
                       "${CountryDao.COLUMN_ID}, " +                   // 14
                       "${CountryDao.COLUMN_NAME}, " +                 // 15
                       "${CountryDao.COLUMN_TRANSPORT_EMISSION}, " +   // 16
                       "${CountryDao.COLUMN_GHPENALTY} " +            // 17
                "FROM $TABLE " +
                "INNER JOIN ${ProductDao.TABLE} ON $COLUMN_PRODUCT_ID = ${ProductDao.COLUMN_ID} " +
                "INNER JOIN ${CountryDao.TABLE} ON $COLUMN_COUNTRY_ID = ${CountryDao.COLUMN_ID} " +
                "WHERE $COLUMN_RECEIPT_TEXT = '$formattedReceiptText';"

        dbManager.select(query) {
            result = produceStoreItem(it)
        }

        if (result == null) {
            result = extractStoreItem(formattedReceiptText)
        }

        return result as StoreItem
    }

    private fun formatReceiptText(receiptText: String): String {
        return receiptText.replace('ø', 'o').replace('å', 'a').replace('æ','e')
    }

    private fun extractStoreItem(receiptText: String): StoreItem {
        val product = ProductDao(dbManager).extractProduct(receiptText)
        val country = CountryDao(dbManager).extractCountry(receiptText)
        return StoreItem(
                product,
                country,
                receiptText,
                isOrganic(receiptText),
                isPackaged(receiptText),
                extractWeight()
        )
    }

    private fun isOrganic(receiptText: String): Boolean {
        return receiptText.contains("oko")
    }

    private fun isPackaged(receiptText: String): Boolean {
        return !receiptText.contains("los")
    }

    private fun extractWeight(): Double{
        return 0.0
    }

    private fun loadId(storeItem: StoreItem): Long{
        var id: Long = dbManager.INVALID_ID
        val query =
                "SELECT $COLUMN_ID " +
                "FROM $TABLE " +
                "WHERE $COLUMN_PRODUCT_ID = ${storeItem.product.id} AND + "
                      "$COLUMN_COUNTRY_ID = ${storeItem.country.id} AND + "
                      "$COLUMN_RECEIPT_TEXT = ${storeItem.receiptText} AND + "
                      "$COLUMN_ORGANIC = ${storeItem.organic} AND " +         // 2
                      "$COLUMN_PACKAGED =  ${storeItem.packaged} AND" +        // 3
                      "$COLUMN_WEIGHT = ${storeItem.weight} AND " +          // 4
                      "$COLUMN_STORE = ${storeItem.store};"           // 5

        dbManager.select(query){
            id = it.getLong(0)
        }

        return id
    }

    fun saveOrLoadStoreItem(storeItem: StoreItem): Long {
        val id = dbManager.INVALID_ID

        loadId(storeItem)

        return if (id != dbManager.INVALID_ID) {
            id
        } else {
            saveStoreItem(storeItem)
        }
    }

    private fun saveStoreItem(storeItem: StoreItem): Long {
        val contentValues = ContentValues()

        contentValues.put(COLUMN_PRODUCT_ID, storeItem.product.id)
        contentValues.put(COLUMN_COUNTRY_ID, storeItem.country.id)
        contentValues.put(COLUMN_RECEIPT_TEXT, formatReceiptText(storeItem.receiptText))
        contentValues.put(COLUMN_ORGANIC, storeItem.organic)
        contentValues.put(COLUMN_WEIGHT, storeItem.weight)
        contentValues.put(COLUMN_STORE, storeItem.store)

        return dbManager.insert(TABLE, contentValues)
    }

    companion object{
        val TABLE = "storeItem"
        val COLUMN_COUNT = 6
        val COLUMN_COUNTRY_ID = "$TABLE.countryID"
        val COLUMN_PRODUCT_ID = "$TABLE.productID"

        val COLUMN_ID = "$TABLE.id"
        val COLUMN_ID_POSITION = 0

        val COLUMN_RECEIPT_TEXT = "$TABLE.receiptText"
        val COLUMN_RECEIPT_TEXT_POSITION = 1

        val COLUMN_ORGANIC = "$TABLE.organic"
        val COLUMN_ORGANIC_POSITION = 2

        val COLUMN_PACKAGED = "$TABLE.packaged"
        val COLUMN_PACKAGED_POSITION = 3

        val COLUMN_WEIGHT = "$TABLE.weight"
        val COLUMN_WEIGHT_POSITION = 4

        val COLUMN_STORE = "$TABLE.store"
        val COLUMN_STORE_POSITION = 5

        fun produceStoreItem(cursor: Cursor, startIndex: Int = 0): StoreItem {
            val product = ProductDao.produceProduct(cursor, startIndex + COLUMN_COUNT)
            val country = CountryDao.produceCountry(cursor, startIndex + COLUMN_COUNT + ProductDao.COLUMN_COUNT)

            return StoreItem(
                    cursor.getInt(startIndex + COLUMN_ID_POSITION),
                    product,
                    country,
                    cursor.getString(startIndex + COLUMN_RECEIPT_TEXT_POSITION),
                    cursor.getInt(startIndex + COLUMN_ORGANIC_POSITION) != 0,
                    cursor.getInt(startIndex + COLUMN_PACKAGED_POSITION) != 0,
                    cursor.getDouble(startIndex + COLUMN_WEIGHT_POSITION),
                    cursor.getString(startIndex + COLUMN_STORE_POSITION)
            )
        }
    }
}