import sequelize, { Sequelize, DataTypes } from "sequelize";

const db = new Sequelize("Admin_DB", "root", "chen1234", {
  host: "localhost",
  dialect: "mysql",
});

db.authenticate()
  .then(() => {
    console.log("Connection has been established successfully.");
  })
  .catch((error) => {
    console.error("Unable to connect to the database: ", error);
  });

//---------------------------------------------------------------------
const company = db.define("Companies", {
  company_name: {
    type: DataTypes.STRING,
    allowNull: false,
    primaryKey: true,
  },
  company_address: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  contact_name: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  contact_phone: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  contact_email: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  service_fee: {
    type: DataTypes.STRING,
    allowNull: false,
  },
});

//---------------------------------------------------------------------
const card = db.define("CreditCardDetails", {
  card_number: {
    type: DataTypes.STRING,
    allowNull: false,
    primaryKey: true,
  },
  company_name: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  card_holder_name: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  ex_date: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  CVV: {
    type: DataTypes.STRING,
    allowNull: false,
  },
});

const product = db.define("Products", {
  product_id: {
    type: DataTypes.INTEGER,
    allowNull: false,
    primaryKey: true,
    autoIncrement: true,
  },
  company_name: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  product_name: {
    type: DataTypes.STRING,
    allowNull: false,
  },
  product_description: {
    type: DataTypes.TEXT,
    allowNull: false,
  },
});

company.hasOne(card, { onDelete: "CASCADE", foreignKey: "company_name" });
// card.belongsTo(company);

company.hasMany(product, { onDelete: "CASCADE", foreignKey: "company_name" });
// product.belongsTo(company);

db.sync()
  .then(() => {
    console.log("Book table created successfully!");
  })
  .catch((error) => {
    console.error("Unable to create table : ", error);
  });

function registerCompany(companyData) {
  company.create({
    company_name: companyData.companyName,
    company_address: companyData.companyAddress,
    contact_name: companyData.contactName,
    contact_phone: companyData.contactPhone,
    contact_email: companyData.contactEmail,
    service_fee: companyData.serviceFee,
  });
  card.create({
    card_number: companyData.creditCard,
    company_name: companyData.companyName,
    card_holder_name: companyData.cardHolderName,
    ex_date: companyData.expiryDate,
    CVV: companyData.cvv,
  });
}

function registerProduct(productData) {
  product.create({
    company_name: productData.companyName,
    product_name: productData.productName,
    product_description: productData.productDescription,
  });
}

// module.exports = { registerCompany, registerProduct };
export { registerCompany, registerProduct };
