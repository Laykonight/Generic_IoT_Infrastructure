package il.co.ILRD.DB_CRUD;

import org.json.JSONObject;


public interface DB_CRUD {
    JSONObject create(JSONObject data);
    JSONObject read(JSONObject data);
    JSONObject update(JSONObject data);
    JSONObject delete(JSONObject data);
}
