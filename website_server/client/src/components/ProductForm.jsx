import React from "react";

import { GenericButton } from "./GenericButton";
import { InputField } from "./InputField";
import { TextAreaField } from "./TextAreaField";
import { darkGray, lightBlue, backgroundColor } from "./Utils";

export const ProductForm = ({ SubmitForm2 }) => {
  return (
    <>
      <form
        className="needs-validation"
        id="form2"
        action="ProductRegistration"
        method="post"
        onSubmit={SubmitForm2}
        noValidate=""
      >
        <div className="container-md">
          <div className="row g-3 justify-content-center">
            <div // company name field ------------------------------------------------------
              className="col-lg-6"
            >
              <InputField
                id="companyName"
                type="text"
                text="Company name:"
                placeholder="The Company"
                feedback="Valid Company name is required."
              />
            </div>
            <br />

            <div // Product name field ------------------------------------------------------
              className="col-lg-6"
            >
              <InputField
                id="productName"
                type="text"
                text="Product name:"
                placeholder="The Product"
                feedback="Valid Product name is required."
              />
            </div>
            <br />

            <div // Product description field ------------------------------------------------------
              className="col-lg-10"
            >
              <TextAreaField
                id="productName"
                text="Product name:"
                placeholder="Please enter description for this product"
                feedback="A product description is required."
              />
            </div>
            <br />
            <hr className="col-lg-6 my-3" />
          </div>
          <GenericButton
            className="btn-outline-info mb-4"
            type="submit"
            onClickHandle={SubmitForm2}
            borderStyle={`solid 2px ${darkGray}`}
          >
            <label className="fs-4" style={{ color: darkGray }}>
              Submit
            </label>
          </GenericButton>
        </div>
      </form>
    </>
  );
};
