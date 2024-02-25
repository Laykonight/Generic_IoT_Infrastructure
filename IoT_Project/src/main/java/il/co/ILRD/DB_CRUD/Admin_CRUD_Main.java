package il.co.ILRD.DB_CRUD;

import org.json.JSONObject;

import java.sql.SQLException;

public class Admin_CRUD_Main {
    public static void main(String[] args) {
        String mySQLurl = "jdbc:mysql://localhost:3306";
        String mySQLusername = "root";
        String mySQLpassword = "chen1234";
        Admin_CRUD test = null;
        try {
            test = new Admin_CRUD(mySQLurl, mySQLusername, mySQLpassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JSONObject testCompany = new JSONObject()
                .put("start_line", new JSONObject()
                        .put("method", "POST")
                        .put("URL", "Companies")
                        .put("version","1.1"))

                .put("headers", new JSONObject()
                        .put("content_type", "application/json")
                        .put("content_length", "200")
                        .put("Accept", "application/json"))

                .put("body", new JSONObject()
                        .put("company_name", "test_Company")
                        .put("company_address", "test_Planet_Earth")
                        .put("contact_name", "test_Chuck Norris")
                        .put("contact_phone", "0-000-000-1")
                        .put("contact_email", "Norris_mail.com")
                        .put("service_fee", "500")

                        .put("card_number", "xxxx-xxxx-xxxx-xxxx")
                        .put("card_holder_name", "test_Chuck Norris")
                        .put("ex_date", "02/2029")
                        .put("cvv", "666")
                );

        JSONObject testProduct = new JSONObject()
                .put("start_line", new JSONObject()
                        .put("method", "POST")
                        .put("URL", "Companies")
                        .put("version","1.1"))

                .put("headers", new JSONObject()
                        .put("content_type", "application/json")
                        .put("content_length", "200")
                        .put("Accept", "application/json"))

                .put("body", new JSONObject()
                        .put("company_name", "test_Company")
                        .put("product_name", "test_Product")
                        .put("product_description", "test_description")

                );

        JSONObject testOutputCompany = test.registerCompanyCRUD(testCompany);
        System.out.println(testOutputCompany.getJSONObject("start_line").get("status_code") + " for register company");

        JSONObject testOutputProduct = test.registerProductCRUD(testProduct);
        System.out.println(testOutputProduct.getJSONObject("start_line").get("status_code") + " for register product");


    }
}
