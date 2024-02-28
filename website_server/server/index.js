// server/index.js
import * as admin from "./AdminDBManager.js";
import express from "express";
import cors from "cors"; // Import the 'cors' middleware

const PORT = process.env.PORT || 3001;

const app = express();

// Use the 'cors' middleware to enable CORS
app.use(cors());

// Middleware to parse JSON data
app.use(express.json());

//---------------------------------------------------------------------
app.post("/company", async (req, res) => {
  try {
    admin.registerCompany(req.body);
    // Log the request body
    console.log("Request Body:", req.body);

    // Simulate some asynchronous operation (replace this with your actual logic)
    await new Promise((resolve) => setTimeout(resolve, 1000));

    res.status(200).json({ message: "Data received successfully" });
  } catch (error) {
    console.error("Error processing request:", error);
    res.status(500).json({ error: "Internal Server Error" });
  }
});

//---------------------------------------------------------------------
app.post("/product", async (req, res) => {
  try {
    admin.registerProduct(req.body);
    // Log the request body
    console.log("Request Body:", req.body);

    // Simulate some asynchronous operation (replace this with your actual logic)
    await new Promise((resolve) => setTimeout(resolve, 1000));

    res.status(200).json({ message: "Data received successfully" });
  } catch (error) {
    console.error("Error processing request:", error);
    res.status(500).json({ error: "Internal Server Error" });
  }
});

//---------------------------------------------------------------------
app.listen(PORT, () => {
  console.log(`Server listening on ${PORT}`);
});
