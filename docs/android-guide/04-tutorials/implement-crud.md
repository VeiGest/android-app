# 🎓 Tutorial: Implementing CRUD

CRUD (Create, Read, Update, Delete) is the basis of most features in VeiGest.

## 1. Create (POST)

1. SDK: Create `adicionarItemAPI(Item item)`.
2. Convert the object to JSON using a helper.
3. Send a `POST` request to the endpoint.
4. On success, either add the new item to the memory list or call `getAllItemsAPI()` to refresh everything.

## 2. Read (GET)

1. SDK: Use `getAllItemsAPI()` (List) or `getItemByIdAPI(int id)` (Single).
2. UI: Implement the listener and show a loading spinner until data arrives.

## 3. Update (PUT)

1. SDK: Create `editarItemAPI(Item item)`.
2. Send a `PUT` request to `mUrlAPI + "/" + item.getId()`.
3. IMPORTANT: Make sure the JSON sent matches what the backend expects.

## 4. Delete (DELETE)

1. SDK: Create `removerItemAPI(int id)`.
2. Send a `DELETE` request.
3. On success, remove the item from the local memory list and notify the adapter.

## Pro Tip: Optimistic Updates
To make the app feel faster, you can update the UI list *before* the API call finishes. If the API returns an error, revert the change and show a Toast message.
