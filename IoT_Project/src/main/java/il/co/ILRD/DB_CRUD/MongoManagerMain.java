package il.co.ILRD.DB_CRUD;

import org.json.JSONObject;

public class MongoManagerMain {
    public static JSONObject testCompany = new JSONObject()
            .put("company_name", "test_Company2");

            /*
            request{
                start_line{
                    "method" : "POST",
                    "url" : "Companies",
                    "version" : "1.1"}
                headers{
                    "content_type" : "application/json",
                    "Accept" : "application/json"}
                body{
                    "company_name" : "The_Company"}
            }
            */

    public static JSONObject testProduct = new JSONObject()
            .put("company_name", "test_Company2")
            .put("product_name", "test_Product");

    public static JSONObject testIoT = new JSONObject()
            .put("company_name", "test_Company")
            .put("product_name", "test_Product")
            .put("IoT_Serial_num", "xyz-552233")
            .put("end_user_name", "Random name")
            .put("end_user_email", "RandomName@gmail.com");

    public static JSONObject testUpdate = new JSONObject()
            .put("company_name", "test_Company")
            .put("product_name", "test_Product2")
            .put("IoT_Serial_num", "xyz-552233")
            .put("update", new JSONObject()
                    .put("update_name", "the update")
                    .put("update_type", "daily")
            );
    public static void main(String[] args) {

        MongoManagerCRUD managerTest = new MongoManagerCRUD();

//        managerTest.registerCompanyCRUD(testCompany);
//        managerTest.registerProductCRUD(testProduct);
//        managerTest.registerIoTCRUD(testIoT);
//        managerTest.registerUpdateCRUD(testUpdate);


    }

}
