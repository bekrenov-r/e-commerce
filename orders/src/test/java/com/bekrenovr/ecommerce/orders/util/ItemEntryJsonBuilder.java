package com.bekrenovr.ecommerce.orders.util;

import com.bekrenovr.ecommerce.common.util.TestUtil;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class ItemEntryJsonBuilder {
    private static final String JSON_RESOURCE_PATH = "/json/item-entry.json";
    private JSONObject json;

    private ItemEntryJsonBuilder() throws JSONException {
        String template = TestUtil.getResourceAsString(JSON_RESOURCE_PATH);
        this.json = new JSONObject(template);
    }

    public static ItemEntryJsonBuilder create() throws JSONException {
        return new ItemEntryJsonBuilder();
    }

    public ItemEntryJsonBuilder itemId(UUID itemId) throws JSONException {
        json.put("itemId", itemId);
        return this;
    }

    public ItemEntryJsonBuilder quantity(int quantity) throws JSONException {
        json.put("quantity", quantity);
        return this;
    }

    public ItemEntryJsonBuilder size(String size) throws JSONException {
        json.put("size", size);
        return this;
    }

    public JSONObject build() {
        return json;
    }
}
