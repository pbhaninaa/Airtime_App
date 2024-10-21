package com.example.testingmyskills.JavaClasses;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductManager { // Replace with your actual class name

    // Define a callback interface
    public interface ProductsCallback {
        void onProductsLoaded(List<Map<String, Object>> products);

        void onError(String errorMessage);
    }

    // Method to get products
    public void getProducts(ProductsCallback callback) {
        // Running the network request in a background thread
        new Thread(() -> {
            // Initialize the list here
            List<Map<String, Object>> items = new ArrayList<>();

            try {
                // Call the catalog request API
                JSONObject catalogRequestResponse = ApiService.catalogRequest();
                Log.d("API Response", catalogRequestResponse.toString()); // Log the response

                // Check for methodResponse key
                if (catalogRequestResponse.has("methodResponse")) {
                    JSONArray paramsList = catalogRequestResponse
                            .getJSONObject("methodResponse")
                            .getJSONArray("paramsList");

                    // Loop through each product in the paramsList
                    for (int i = 0; i < paramsList.length(); i++) {
                        JSONObject product = paramsList.getJSONObject(i);
                        System.out.println("Product At : " + (i + 1)+"\n"+product);
                        // Create a map to hold product details
                        Map<String, Object> item = new HashMap<>();
                        item.put("type", product.getString("productCategory"));
                        item.put("amount", product.getString("amount"));
                        item.put("lifeTime", product.getString("validity"));
                        item.put("price", product.getString("costPrice"));

                        // Additional product details
                        item.put("num", product.getString("num"));
                        item.put("productID", product.getString("productID"));
                        item.put("productDescription", product.getString("productDescription"));
                        item.put("shortDescription", product.getString("shortDescription"));
                        item.put("network", product.getString("network"));
                        item.put("costPrice", product.getString("costPrice"));
                        item.put("entryDate", product.getString("entryDate"));
                        item.put("providerSplit", product.getString("providerSplit"));

                        // Add the item to the list
                        items.add(item);
                    }

                    // Notify that products have been loaded
                    runOnUiThread(() -> callback.onProductsLoaded(items));
                } else {
                    Log.e("API Error", "methodResponse key not found in response");
                    runOnUiThread(() -> callback.onError("methodResponse key not found"));
                }

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> callback.onError("IO Exception: " + e.getMessage()));
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> callback.onError("Error: " + e.getMessage()));
            }
        }).start();
    }

    private void runOnUiThread(Runnable action) {
        // Implement this method to run the action on the UI thread.
        // For example, in an Activity, use runOnUiThread(action);
        // In a Fragment, use getActivity().runOnUiThread(action);
        // If you have a Handler, you can use that too.
        action.run(); // Placeholder, replace with your implementation
    }
}
