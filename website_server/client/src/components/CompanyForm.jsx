import React from "react";

import { GenericButton } from "./GenericButton";
import { InputField } from "./InputField";
import { darkGray, lightBlue, backgroundColor } from "./Utils";

export const CompanyForm = ({ SubmitForm1 }) => {
  return (
    <>
      <form
        className="needs-validation"
        id="form1"
        action="companyRegistration"
        method="post"
        onSubmit={SubmitForm1}
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
              {/* <div className="d-flex flex-column align-items-start">
                <label htmlFor="firstName" className="form-label text-start">
                  Company name:
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="companyName"
                  placeholder="The Company"
                  value=""
                  required=""
                />
                <div className="invalid-feedback">
                  Valid Company name is required.
                </div>
              </div> */}
            </div>
            <br />

            <div // company Address field ------------------------------------------------------
              className="col-lg-6"
            >
              <InputField
                id="companyAddress"
                type="text"
                text="Company Address:"
                placeholder="1234 Main St"
                feedback="Valid Company Address is required"
              />
              {/* <div className="d-flex flex-column align-items-start">
                <label htmlFor="companyAddress" className="form-label">
                  Company Address:
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="companyAddress"
                  placeholder=""
                  value=""
                  required=""
                />
                <div className="invalid-feedback">
                  Valid Company Address is required.
                </div>
              </div> */}
            </div>
            <br />

            <div // contact name field ------------------------------------------------------
              className="col-lg-6"
            >
              <InputField
                id="contactName"
                type="text"
                text="Contact Name:"
                placeholder="The name Of your contact"
                feedback="Your contact name is required."
              />
              {/* <div className="d-flex flex-column align-items-start">
                <label htmlFor="contactName" className="form-label">
                  Contact Name:
                </label>
                <div className="input-group has-validation">
                  <input
                    type="text"
                    className="form-control"
                    id="contactName"
                    placeholder="The name Of your contact"
                    required=""
                  />
                  <div className="invalid-feedback">
                    Your contact name is required.
                  </div>
                </div>
              </div> */}
            </div>
            <br />

            <div // contact Phone field ------------------------------------------------------
              className="col-lg-6"
            >
              <InputField
                id="contactPhone"
                type="tel"
                text="Contact Phone:"
                placeholder="05x-xxxxxxx"
                maxLength="10"
                feedback="Your contact phone is required."
              />
              {/* <div className="d-flex flex-column align-items-start">
                <label htmlFor="contactPhone" className="form-label">
                  Contact Phone:
                </label>
                <input
                  type="tel"
                  className="form-control"
                  id="contactPhone"
                  placeholder="05x-xxxxxxx"
                  maxLength="10"
                  required=""
                />
                <div className="invalid-feedback">
                  Your contact phone is required.
                </div>
              </div> */}
            </div>
            <br />

            <div // contact email field ------------------------------------------------------
              className="col-lg-6"
            >
              <InputField
                id="contactEmail"
                type="email"
                text="Contact Email:"
                placeholder="you@example.com"
                feedback="A valid email address is required."
              />
              {/* <div className="d-flex flex-column align-items-start">
                <label htmlFor="contactEmail" className="form-label">
                  Contact Email:
                </label>
                <input
                  type="email"
                  className="form-control"
                  id="contactEmail"
                  placeholder="you@example.com"
                  required=""
                />
                <div className="invalid-feedback">
                  A valid email address is required.
                </div>
              </div> */}
            </div>
            <br />
            <hr className="col-lg-6 my-3" />
            <br />

            <div // Card Number field ------------------------------------------------------
              className="col-lg-6"
            >
              <InputField
                id="creditCard"
                type="text"
                text="Credit Card Number:"
                placeholder="xxxx-xxxx-xxxx-xxxx"
                pattern="[0-9]{16}"
                maxLength="16"
                feedback="A valid credit card number is required."
              />
              {/* <div className="d-flex flex-column align-items-start">
                <label htmlFor="creditCard" className="form-label">
                  Credit Card Number:
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="creditCard"
                  pattern="[0-9]{16}"
                  maxLength="16"
                  placeholder="xxxx-xxxx-xxxx-xxxx"
                  required=""
                />
                <div className="invalid-feedback">
                  A valid credit card number is required.
                </div>
              </div> */}
            </div>
            <br />

            <div // Card Holder Name field ------------------------------------------------------
              className="col-lg-6"
            >
              <InputField
                id="cardHolderName"
                type="text"
                text="Card Holder Name:"
                placeholder=""
                feedback="The card holder name is required."
              />
              {/* <div className="d-flex flex-column align-items-start">
                <label htmlFor="cardHolderName" className="form-label">
                  Card Holder Name:
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="cardHolderName"
                  required=""
                />
                <div className="invalid-feedback">
                  The card holder name is required.
                </div>
              </div> */}
            </div>
            <br />

            <div // Expiry Date field ------------------------------------------------------
              className="col-lg-6"
            >
              <InputField
                id="expiryDate"
                type="text"
                text="Card Holder Name:"
                pattern="(0[1-9]|1[0-2])\/[0-9]{2}"
                placeholder="Ex. MM/YY"
                feedback="The credit card expiry date is required."
              />
              {/* <div className="d-flex flex-column align-items-start">
                <label htmlFor="expiryDate" className="form-label">
                  Expiry Date:
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="expiryDate"
                  pattern="(0[1-9]|1[0-2])\/[0-9]{2}"
                  placeholder="Ex. MM/YY"
                  required=""
                />
                <div className="invalid-feedback">
                  The credit card expiry date is required.
                </div>
              </div> */}
            </div>
            <br />

            <div // CVV field ------------------------------------------------------
              className="col-lg-6"
            >
              <InputField
                id="cvv"
                type="text"
                text="Card Holder Name:"
                pattern="[0-9]{3}"
                maxLength="3"
                placeholder=""
                feedback="The credit card CVV number is required."
              />
              {/* <div className="d-flex flex-column align-items-start">
                <label htmlFor="cvv" className="form-label">
                  CVV:
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="cvv"
                  pattern="[0-9]{3}"
                  maxLength="3"
                  required=""
                />
                <div className="invalid-feedback">
                  The credit card expiry date is required.
                </div>
              </div> */}
            </div>
            <br />
            <hr className="col-lg-6 my-3" />
          </div>

          <GenericButton
            className="btn-outline-info mb-4"
            type="submit"
            onClickHandle={SubmitForm1}
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
