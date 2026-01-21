# 🎓 Tutorial: Adding a New API Endpoint

Adding a new data entity (e.g., "Expenses") requires updates across the SDK and the App.

## Phase 1: Defining the Endpoint (SDK)

1. Open `SingletonVeiGest.java`.
2. Add the URL variable: `private String mUrlAPIExpenses;`.
3. Initialize it in `atualizarEndpoints()`:
   ```java
   mUrlAPIExpenses = baseUrl + "/expense"; // Use singular if using REST rules
   ```

## Phase 2: Creating the Model & Parser

1. Create `Expense.java` in `com.veigest.sdk.models` with getters/setters.
2. Open `VeiGestJsonParser.java` and add:
   - `public static Expense parserJsonExpense(JSONObject obj)`
   - `public static ArrayList<Expense> parserJsonExpenses(JSONArray array)`

## Phase 3: Implementing the SDK Call

In `SingletonVeiGest.java`, create the fetching method:
```java
public void getAllExpensesAPI() {
    String url = mUrlAPIExpenses;
    JsonArrayRequest req = new JsonArrayRequest(Method.GET, url, null,
        response -> {
            expenses = VeiGestJsonParser.parserJsonExpenses(response);
            if (expensesListener != null) expensesListener.onRefresh(expenses);
        },
        error -> { /* log error */ }
    ) { /* getAuthHeaders() */ };
    requestQueue.add(req);
}
```

## Phase 4: Adding a Listener

1. Create the `ExpensesListener` interface.
2. Add a field for it in `SingletonVeiGest` with a setter.
3. Use it in your Fragment to receive updates.
