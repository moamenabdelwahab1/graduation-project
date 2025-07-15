package com.example.shoppingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class  DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ShoppingApp.db";
    private static final int DATABASE_VERSION = 5;

    // User Table
    private static final String TABLE_USERS = "users";
    private static final String COL_USER_ID = "user_id";
    private static final String COL_USERNAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_BIRTHDATE = "birthdate";
    private static final String COL_IS_ADMIN = "is_admin";

    // Categories Table
    private static final String TABLE_CATEGORIES = "categories";
    private static final String COL_CATEGORY_ID = "category_id";
    private static final String COL_CATEGORY_NAME = "category_name";

    // Product Table
    private static final String TABLE_PRODUCTS = "products";
    private static final String COL_PRODUCT_ID = "product_id";
    private static final String COL_PRODUCT_NAME = "product_name";
    private static final String COL_CATEGORY_ID_FK = "category_id";
    private static final String COL_PRICE = "price";
    private static final String COL_QUANTITY = "quantity";

    // Cart Table
    private static final String TABLE_CART = "cart";
    private static final String COL_CART_ID = "cart_id";
    private static final String COL_CART_USER_ID = "user_id";
    private static final String COL_CART_PRODUCT_ID = "product_id";
    private static final String COL_CART_QUANTITY = "quantity";
    private static final String COL_CART_UNIT_PRICE = "unit_price";

    // Transactions Table
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COL_TRANSACTION_ID = "transaction_id";
    private static final String COL_TRANSACTION_USER_ID = "user_id";
    private static final String COL_TRANSACTION_DATE = "transaction_date";
    private static final String COL_TRANSACTION_PROFIT = "profit";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_EMAIL + " TEXT UNIQUE, " +
                COL_PASSWORD + " TEXT, " +
                COL_BIRTHDATE + " TEXT, " +
                COL_IS_ADMIN + " INTEGER DEFAULT 0)";
        db.execSQL(createUsersTable);

        // Create Categories Table
        String createCategoriesTable = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COL_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CATEGORY_NAME + " TEXT UNIQUE)";
        db.execSQL(createCategoriesTable);

        // Create Products Table
        String createProductsTable = "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                COL_PRODUCT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PRODUCT_NAME + " TEXT, " +
                COL_CATEGORY_ID_FK + " INTEGER, " +
                COL_PRICE + " REAL, " +
                COL_QUANTITY + " INTEGER, " +
                "FOREIGN KEY(" + COL_CATEGORY_ID_FK + ") REFERENCES " + TABLE_CATEGORIES + "(" + COL_CATEGORY_ID + "))";
        db.execSQL(createProductsTable);

        // Create Cart Table
        String createCartTable = "CREATE TABLE " + TABLE_CART + " (" +
                COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CART_USER_ID + " INTEGER, " +
                COL_CART_PRODUCT_ID + " INTEGER, " +
                COL_CART_QUANTITY + " INTEGER, " +
                COL_CART_UNIT_PRICE + " INTEGER, " +
                "FOREIGN KEY(" + COL_CART_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "), " +
                "FOREIGN KEY(" + COL_CART_PRODUCT_ID + ") REFERENCES " + TABLE_PRODUCTS + "(" + COL_PRODUCT_ID + "))";
        db.execSQL(createCartTable);


        // Create Transactions Table
        String createTransactionsTable = "CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                COL_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TRANSACTION_USER_ID + " INTEGER, " +
                COL_TRANSACTION_DATE + " TEXT, " +
                COL_TRANSACTION_PROFIT + " REAL, " +
                "FOREIGN KEY(" + COL_TRANSACTION_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COL_USER_ID + "))";

        db.execSQL(createTransactionsTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }


    // User Table Methods
    public boolean addUser(String username, String email, String password, String birthdate, int isAdmin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_EMAIL, email);
        values.put(COL_PASSWORD, password);
        values.put(COL_BIRTHDATE, birthdate);
        values.put(COL_IS_ADMIN, isAdmin);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, password});

        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public int getUserId(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COL_USER_ID},
                COL_USERNAME + "=? AND " + COL_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int userId = cursor.getInt(cursor.getColumnIndex(COL_USER_ID));
            cursor.close();
            return userId;
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return -1;  // Return -1 if user not found
        }
    }


    public boolean resetPassword(String input, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the user exists
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email = ? OR username = ?", new String[]{input, input});

        if (cursor.moveToFirst()) {
            // Update the user's password
            ContentValues values = new ContentValues();
            values.put(COL_PASSWORD, newPassword);

            int rowsUpdated = db.update(TABLE_USERS, values, COL_USER_ID + " = ?",
                    new String[]{cursor.getString(cursor.getColumnIndex(COL_USER_ID))});
            cursor.close();

            return rowsUpdated > 0; // Return true if the password was updated
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return false; // No user found with the given input
        }
    }


    public boolean isAdmin(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_IS_ADMIN + " FROM " + TABLE_USERS + " WHERE " + COL_USER_ID + "=?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            boolean isAdmin = cursor.getInt(0) == 1;
            cursor.close();
            return isAdmin;
        }
        cursor.close();
        return false;
    }

    // Category Table Methods
    public boolean addCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("category_name", categoryName);
        long result = db.insert("categories", null, values);
        return result != -1;
    }


    public ArrayList<String> getAllCategories() {
        ArrayList<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_CATEGORY_NAME + " FROM " + TABLE_CATEGORIES, null);

        if (cursor.moveToFirst()) {
            do {
                categories.add(cursor.getString(0).trim()); // Add trimmed category name
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        return categories;
    }


    public boolean updateCategory(String oldCategory, String newCategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CATEGORY_NAME, newCategory);  // Use correct column name

        int rowsAffected = db.update(TABLE_CATEGORIES, values, COL_CATEGORY_NAME + " = ?", new String[]{oldCategory}); // Use correct column name
        db.close();

        return rowsAffected > 0;
    }


    public boolean deleteCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;

        // Start a transaction
        db.beginTransaction();

        try {
            // First, retrieve the category ID based on the category name
            Cursor cursor = db.rawQuery("SELECT " + COL_CATEGORY_ID + " FROM " + TABLE_CATEGORIES + " WHERE " + COL_CATEGORY_NAME + " = ?", new String[]{categoryName});

            if (cursor != null && cursor.moveToFirst()) {
                int categoryId = cursor.getInt(cursor.getColumnIndex(COL_CATEGORY_ID));
                cursor.close();

                // Delete related products from the products table using category ID
                int productsDeleted = db.delete(TABLE_PRODUCTS, COL_CATEGORY_ID_FK + " = ?", new String[]{String.valueOf(categoryId)});

                // Delete the category from the categories table
                int categoryDeleted = db.delete(TABLE_CATEGORIES, COL_CATEGORY_NAME + " = ?", new String[]{categoryName});

                // Check if both deletions were successful
                if (productsDeleted > 0 && categoryDeleted > 0) {
                    db.setTransactionSuccessful();  // Mark the transaction as successful
                    success = true;
                }
            } else {
                // Category not found
                cursor.close();
            }
        } catch (Exception e) {
            // Handle any exceptions (rollback will happen automatically)
            e.printStackTrace();
        } finally {
            // End the transaction
            db.endTransaction();
            db.close();
        }

        return success;
    }



    // Product Table Methods

    public HashMap<String, Integer> getCategoryMap() {
        HashMap<String, Integer> categoryMap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to retrieve all categories
        Cursor cursor = db.rawQuery("SELECT " + COL_CATEGORY_ID + ", " + COL_CATEGORY_NAME + " FROM " + TABLE_CATEGORIES, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0); // Retrieve category ID
                String name = cursor.getString(1); // Retrieve category name
                categoryMap.put(name, id); // Map category name to its ID
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
        return categoryMap;
    }


    public boolean addProduct(String name, int categoryId, double price, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, name);
        values.put(COL_CATEGORY_ID_FK, categoryId);
        values.put(COL_PRICE, price);
        values.put(COL_QUANTITY, quantity);

        long result = db.insert(TABLE_PRODUCTS, null, values);
        return result != -1;
    }

    // Fetch product details by name
    public Cursor getProductDetailsByName(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COL_PRODUCT_ID + ", " + COL_PRICE + ", " + COL_QUANTITY +
                        " FROM " + TABLE_PRODUCTS + " WHERE " + COL_PRODUCT_NAME + " = ?",
                new String[]{productName});
    }

    public List<Product> getProductsByCategory(int categoryId) {
        List<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + COL_CATEGORY_ID_FK + " = ?", new String[]{String.valueOf(categoryId)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PRODUCT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_PRODUCT_NAME));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COL_QUANTITY));
                productList.add(new Product(id, name, price, quantity));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return productList;
    }


    public boolean updateProduct(int productId, double price, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRICE, price);    // Update the price
        values.put(COL_QUANTITY, quantity);  // Update the quantity

        // Perform the update based on the productId
        int rowsUpdated = db.update(TABLE_PRODUCTS, values, COL_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)});
        return rowsUpdated > 0; // Return true if the update was successful
    }


    public boolean deleteProductByNameAndCategory(String productName, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Query to check if the product exists with the given name and category ID
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS +
                        " WHERE " + COL_PRODUCT_NAME + " = ? AND " + COL_CATEGORY_ID_FK + " = ?",
                new String[]{productName, String.valueOf(categoryId)});

        if (cursor.moveToFirst()) {
            // Product exists, now delete it
            int productId = cursor.getInt(cursor.getColumnIndex(COL_PRODUCT_ID));
            cursor.close();
            return deleteProduct(productId);  // Use the existing deleteProduct method to delete by productId
        } else {
            cursor.close();
            return false;  // Product not found
        }

    }

    public boolean deleteProduct(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_PRODUCTS, COL_PRODUCT_ID + "=?", new String[]{String.valueOf(productId)});
        return rowsDeleted > 0;
    }

    // cart

    public boolean addToCart(int userId, int productId, double unitPrice, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Step 1: Check if the product already exists in the user's cart
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART +
                        " WHERE " + COL_CART_USER_ID + " = ? AND " + COL_CART_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(productId)});

        // Step 2: If product exists in cart
        if (cursor.moveToFirst()) {
            // Get current quantity in the cart
            int currentQuantity = cursor.getInt(cursor.getColumnIndex(COL_CART_QUANTITY));

            // Get available stock quantity for the product
            Cursor productCursor = db.rawQuery("SELECT " + COL_QUANTITY + " FROM " + TABLE_PRODUCTS +
                    " WHERE " + COL_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
            int availableStock = 0;
            if (productCursor.moveToFirst()) {
                availableStock = productCursor.getInt(productCursor.getColumnIndex(COL_QUANTITY));
            }
            productCursor.close();

            // Step 3: Check if the new quantity doesn't exceed available stock
            if ((currentQuantity + quantity) <= availableStock) {
                // Increment the quantity in the cart
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_CART_QUANTITY, currentQuantity + quantity);

                // Update the cart table with the new quantity
                int rowsAffected = db.update(TABLE_CART, contentValues,
                        COL_CART_USER_ID + " = ? AND " + COL_CART_PRODUCT_ID + " = ?",
                        new String[]{String.valueOf(userId), String.valueOf(productId)});
                cursor.close();
                return rowsAffected > 0; // Return true if update was successful
            } else {
                // Quantity exceeds available stock
                cursor.close();
                return false; // Return false if the update was unsuccessful
            }
        } else {
            // Step 4: If product is not already in the cart, insert it
            Cursor productCursor = db.rawQuery("SELECT " + COL_QUANTITY + " FROM " + TABLE_PRODUCTS +
                    " WHERE " + COL_PRODUCT_ID + " = ?", new String[]{String.valueOf(productId)});
            int availableStock = 0;
            if (productCursor.moveToFirst()) {
                availableStock = productCursor.getInt(productCursor.getColumnIndex(COL_QUANTITY));
            }
            productCursor.close();

            // Check if the quantity doesn't exceed available stock
            if (quantity <= availableStock) {
                // Insert the new product into the cart
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_CART_USER_ID, userId);
                contentValues.put(COL_CART_PRODUCT_ID, productId);
                contentValues.put(COL_CART_UNIT_PRICE, unitPrice);
                contentValues.put(COL_CART_QUANTITY, quantity);

                long result = db.insert(TABLE_CART, null, contentValues);
                return result != -1; // Return true if insertion was successful, false otherwise
            } else {
                return false; // Return false if the quantity exceeds available stock
            }
        }
    }


    public List<CartItem> getCartItems(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to retrieve all cart items, including product details, for the specified userId
        String query = "SELECT c.cart_id, c.product_id, p.product_name, c.unit_price, c.quantity " +
                "FROM " + TABLE_CART + " c " +
                "JOIN " + TABLE_PRODUCTS + " p ON c.product_id = p.product_id " +
                "WHERE c.user_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                int cartId = cursor.getInt(cursor.getColumnIndex(COL_CART_ID));
                int productId = cursor.getInt(cursor.getColumnIndex("product_id"));
                String productName = cursor.getString(cursor.getColumnIndex("product_name"));
                double unitPrice = cursor.getDouble(cursor.getColumnIndex("unit_price"));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));

                // Adding the cart item to the list
                cartItems.add(new CartItem(cartId, productName, unitPrice, quantity));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }


    public void removeFromCart(int cartId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COL_CART_ID + " = ?", new String[]{String.valueOf(cartId)});
    }
//Transactions
public boolean addTransaction(int userId, String date, double profit) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(COL_TRANSACTION_USER_ID, userId);
    values.put(COL_TRANSACTION_DATE, date);
    values.put(COL_TRANSACTION_PROFIT, profit); // Set profit value

    long result = db.insert(TABLE_TRANSACTIONS, null, values);
    return result != -1;
}
    public Cursor getTransactionsByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM transactions WHERE transaction_date = ?", new String[]{date});
    }



    public void clearCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart", "user_id = ?", new String[]{String.valueOf(userId)});
    }

}



