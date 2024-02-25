package il.co.ILRD.DB_CRUD;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.json.JSONObject;



public class MongoManagerCRUD {
    public final String connectionURL;
    public static final String COMPANY_NAME_KEY = "company_name";
    public static final String PRODUCT_USERS_NAME_SPACE = "%s_users";
    public static final String PRODUCT_UPDATES_NAME_SPACE = "%s_updates";
    public static final String PRODUCT_NAME_KEY = "product_name";
    private DB_CRUD company;
    private DB_CRUD product;
    private DB_CRUD IoT;
    private DB_CRUD update;

    public MongoManagerCRUD(String URL) {
        this.connectionURL = "mongodb://" + URL;
        this.company = new companyCRUDImp();
        this.product = new productCRUDImp();
        this.IoT = new IoTCRUDImp();
        this.update = new updateCRUDImp();
    }

    public MongoManagerCRUD(){
        this("localhost:27017");
        System.out.println(this.connectionURL);
    }
    //-----------------------------------------------------------------------------------------------
    public static JSONObject convertDocumentToJSONObject(Document document) {
        JSONObject jsonObject = new JSONObject();

        for (String key : document.keySet()) {
            Object value = document.get(key);

            if (value instanceof Document) {
                // Recursively convert nested Document
                jsonObject.put(key, convertDocumentToJSONObject((Document) value));
            } else {
                // Convert primitive types to JSON
                jsonObject.put(key, value);
            }
        }

        return jsonObject;
    }

    //-----------------------------------------------------------------------------------------------
    public void registerCompanyCRUD(JSONObject data){
        company.create(data);
    }
    public void registerProductCRUD(JSONObject data){
        product.create(data);
    }
    public void registerIoTCRUD(JSONObject data){
        IoT.create(data);
    }
    public void registerUpdateCRUD(JSONObject data){
        update.create(data);
    }

    //-----------------------------------------------------------------------------------------------

    public boolean isDBExist(String DBName){
        try (MongoClient mongoClient = MongoClients.create(connectionURL)) {
            for (String DBExist : mongoClient.listDatabaseNames()){
                if (DBExist.equals(DBName)){
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isCollectionExist(String DBName, String collectionName){
        try (MongoClient mongoClient = MongoClients.create(connectionURL)) {
            MongoDatabase db = mongoClient.getDatabase(DBName);
            for (String collectionExist : db.listCollectionNames()){
                if (collectionExist.equals(collectionName)){
                    return true;
                }
            }
            return false;
        }
    }


    //-----------------------------------------------------------------------------------------------
    private class companyCRUDImp implements DB_CRUD {

        @Override // todo -> this is doing nothing
        public JSONObject create(JSONObject data) {
            String DBName = data.getString(COMPANY_NAME_KEY);

            try (MongoClient mongoClient = MongoClients.create(connectionURL)) {
                MongoDatabase db = mongoClient.getDatabase(DBName);
                db.getCollection("dummy").insertOne(new Document().append("dummy_key", "dummy_value"));
            }
            return null;
        }

        @Override
        public JSONObject read(JSONObject data) {
            return null;
        }

        @Override
        public JSONObject update(JSONObject data) {
            return null;
        }

        @Override
        public JSONObject delete(JSONObject data) {
            String DBName = data.getString(COMPANY_NAME_KEY);
            try (MongoClient mongoClient = MongoClients.create(connectionURL)) {
                mongoClient.getDatabase(DBName).drop();
            }
            return null;
        }
    }
    private class productCRUDImp implements DB_CRUD {

        @Override
        public JSONObject create(JSONObject data) {
            String DBName = data.getString(COMPANY_NAME_KEY);
            String productName = data.getString(PRODUCT_NAME_KEY);

            String productUsers = String.format(PRODUCT_USERS_NAME_SPACE, productName);
            String productUpdates = String.format(PRODUCT_UPDATES_NAME_SPACE, productName);

            try (MongoClient mongoClient = MongoClients.create(connectionURL)) {
                MongoDatabase db = mongoClient.getDatabase(DBName);
                db.getCollection(productUsers).insertOne(new Document().append("dummy_key", "dummy_value"));
                db.getCollection(productUpdates).insertOne(new Document().append("dummy_key", "dummy_value"));
            }
            return null;
        }

        @Override
        public JSONObject read(JSONObject data) {
            return null;
        }

        @Override
        public JSONObject update(JSONObject data) {
            return null;
        }

        @Override
        public JSONObject delete(JSONObject data) {
            String DBName = data.getString(COMPANY_NAME_KEY);
            String productName = data.getString(PRODUCT_NAME_KEY);

            String productUsers = String.format(PRODUCT_USERS_NAME_SPACE, productName);
            String productUpdates = String.format(PRODUCT_UPDATES_NAME_SPACE, productName);

            try (MongoClient mongoClient = MongoClients.create(connectionURL)) {
                MongoDatabase db = mongoClient.getDatabase(DBName);
                MongoCollection<Document> collectionProductUsers = db.getCollection(productUsers);
                collectionProductUsers.drop();
                MongoCollection<Document> collectionProductUpdates = db.getCollection(productUpdates);
                collectionProductUpdates.drop();
            }
            return null;
        }
    }
    private class IoTCRUDImp implements DB_CRUD {

        @Override
        public JSONObject create(JSONObject data) {
            String CompanyName = data.getString(COMPANY_NAME_KEY);
            String productName = data.getString(PRODUCT_NAME_KEY);

            try (MongoClient mongoClient = MongoClients.create(connectionURL)) {
                MongoDatabase db = mongoClient.getDatabase(CompanyName);
                MongoCollection<Document> collection = db.getCollection(
                        String.format(PRODUCT_USERS_NAME_SPACE, productName));

                Document doc = Document.parse(data.toString());
//                Document doc = new  Document(data);
                collection.insertOne(doc);
                return convertDocumentToJSONObject(doc);
            }
        }

        @Override
        public JSONObject read(JSONObject data) {
            String CompanyName = data.getString(COMPANY_NAME_KEY);
            String productName = data.getString(PRODUCT_NAME_KEY);

            try (MongoClient mongoClient = MongoClients.create(connectionURL)) {
                MongoDatabase db = mongoClient.getDatabase(CompanyName);
                MongoCollection<Document> collection = db.getCollection(
                        String.format(PRODUCT_USERS_NAME_SPACE, productName));

//                Document doc = Document.parse(data.toString());
                Document doc = collection.find().first();
                if (null == doc){
                    return null;
                }
                return convertDocumentToJSONObject(doc);
            }
        }

        @Override
        public JSONObject update(JSONObject data) {
            String CompanyName = data.getString(COMPANY_NAME_KEY);
            String productName = data.getString(PRODUCT_NAME_KEY);

            try (MongoClient mongoClient = MongoClients.create(connectionURL)) {
                MongoDatabase db = mongoClient.getDatabase(CompanyName);
                MongoCollection<Document> collection = db.getCollection(
                        String.format(PRODUCT_USERS_NAME_SPACE, productName));

                Document doc = Document.parse(data.toString());
//                Document doc = new  Document(data);

                collection.updateOne(Filters.eq("_id", data.getString("_id")), new Document("$set", doc));
                return null;
            }
        }

        @Override
        public JSONObject delete(JSONObject data) {
            String CompanyName = data.getString(COMPANY_NAME_KEY);
            String productName = data.getString(PRODUCT_NAME_KEY);

            try (MongoClient mongoClient = MongoClients.create(connectionURL)) {
                MongoDatabase db = mongoClient.getDatabase(CompanyName);
                MongoCollection<Document> collection = db.getCollection(
                        String.format(PRODUCT_USERS_NAME_SPACE, productName));

                collection.findOneAndDelete(Filters.eq("_id", data.getString("_id")));
                return null;
            }
        }
    }
    private class updateCRUDImp implements DB_CRUD {

        @Override
        public JSONObject create(JSONObject data) {
            String DBName = data.getString(COMPANY_NAME_KEY);
            String productName = data.getString(PRODUCT_NAME_KEY);

            try (MongoClient mongoClient = MongoClients.create(connectionURL)) {
                MongoDatabase db = mongoClient.getDatabase(DBName);
                MongoCollection<Document> collection = db.getCollection(
                        String.format(PRODUCT_UPDATES_NAME_SPACE, productName));

                Document doc = Document.parse(data.getJSONObject("update").toString());
//                Document doc = new  Document(data);
                collection.insertOne(doc);
                return convertDocumentToJSONObject(doc);
            }
        }

        @Override
        public JSONObject read(JSONObject data) {
            String CompanyName = data.getString(COMPANY_NAME_KEY);
            String productName = data.getString(PRODUCT_NAME_KEY);

            try (MongoClient mongoClient = MongoClients.create(connectionURL)) {
                MongoDatabase db = mongoClient.getDatabase(CompanyName);
                MongoCollection<Document> collection = db.getCollection(
                        String.format(PRODUCT_UPDATES_NAME_SPACE, productName));

//                Document doc = Document.parse(data.toString());
                Document doc = collection.find().first();
                if (null == doc){
                    return null;
                }
                return convertDocumentToJSONObject(doc);
            }
        }

        @Override
        public JSONObject update(JSONObject data) {
            String CompanyName = data.getString(COMPANY_NAME_KEY);
            String productName = data.getString(PRODUCT_NAME_KEY);

            try (MongoClient mongoClient = MongoClients.create(connectionURL)) {
                MongoDatabase db = mongoClient.getDatabase(CompanyName);
                MongoCollection<Document> collection = db.getCollection(
                        String.format(PRODUCT_UPDATES_NAME_SPACE, productName));

                Document doc = Document.parse(data.toString());
//                Document doc = new  Document(data);

                collection.updateOne(Filters.eq("_id", data.getString("_id")), new Document("$set", doc));
                return null;
            }
        }

        @Override
        public JSONObject delete(JSONObject data) {
            String CompanyName = data.getString(COMPANY_NAME_KEY);
            String productName = data.getString(PRODUCT_NAME_KEY);

            try (MongoClient mongoClient = MongoClients.create(connectionURL)) {
                MongoDatabase db = mongoClient.getDatabase(CompanyName);
                MongoCollection<Document> collection = db.getCollection(
                        String.format(PRODUCT_UPDATES_NAME_SPACE, productName));

                collection.findOneAndDelete(Filters.eq("_id", data.getString("_id")));
                return null;
            }
        }
    }
}
