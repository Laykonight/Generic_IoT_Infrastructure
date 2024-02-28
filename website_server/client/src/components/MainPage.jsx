import React, { useState } from "react";
import { GenericButton } from "./GenericButton";
import { darkGray, lightBlue, backgroundColor } from "./Utils";
import { CompanyForm } from "./CompanyForm";
import { ProductForm } from "./ProductForm";

export const MainPage = () => {
  const [appContent, setContent] = useState(MainContent);
  const [form1Data, setForm1Data] = useState({
    companyName: "",
    companyAddress: "",
    contactPhone: "",
    contactEmail: "",
    creditCard: "",
    cardHolderName: "",
    expiryDate: "",
    cvv: "",
  });
  //---------------------------------------------------------------------

  function MainContent() {
    return (
      <>
        <div className="container">
          <div className="row">
            <h1 // Title
              className="col mb-5 display-10"
              style={{
                color: darkGray,
                fontSize: "70px",
                filter: `drop-shadow(0 0 2px ${lightBlue})`,
              }}
            >
              LEV - RON IoT's
            </h1>
          </div>
        </div>
        <GenericButton // register conpany button
          className="btn-outline-info p-2 mb-4"
          type="button"
          borderStyle={`solid 2px ${darkGray}`}
          onClickHandle={() => setContent(RegisterCompanyForm)}
        >
          <label className="fs-4" style={{ color: darkGray }}>
            Register Company
          </label>
          <br />
          <svg
            style={{
              color: darkGray,
            }}
            xmlns="http://www.w3.org/2000/svg"
            height="35"
            fill="currentColor"
            className="bi bi-buildings"
            viewBox="0 0 16 16"
          >
            <path d="M14.763.075A.5.5 0 0 1 15 .5v15a.5.5 0 0 1-.5.5h-3a.5.5 0 0 1-.5-.5V14h-1v1.5a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5V10a.5.5 0 0 1 .342-.474L6 7.64V4.5a.5.5 0 0 1 .276-.447l8-4a.5.5 0 0 1 .487.022M6 8.694 1 10.36V15h5zM7 15h2v-1.5a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 .5.5V15h2V1.309l-7 3.5z" />
            <path d="M2 11h1v1H2zm2 0h1v1H4zm-2 2h1v1H2zm2 0h1v1H4zm4-4h1v1H8zm2 0h1v1h-1zm-2 2h1v1H8zm2 0h1v1h-1zm2-2h1v1h-1zm0 2h1v1h-1zM8 7h1v1H8zm2 0h1v1h-1zm2 0h1v1h-1zM8 5h1v1H8zm2 0h1v1h-1zm2 0h1v1h-1zm0-2h1v1h-1z" />
          </svg>
        </GenericButton>
        <br />
        <GenericButton // register product button
          className="btn-outline-info"
          type="button"
          borderStyle={`solid 2px ${darkGray}`}
          onClickHandle={() => setContent(RegisterProductForm)}
        >
          <label className="fs-4" style={{ color: darkGray }}>
            Register Product
          </label>
          <br />
          <svg
            style={{
              color: darkGray,
            }}
            xmlns="http://www.w3.org/2000/svg"
            height="35"
            fill="currentColor"
            className="bi bi-box-seam"
            viewBox="0 0 16 16"
          >
            <path d="M8.186 1.113a.5.5 0 0 0-.372 0L1.846 3.5l2.404.961L10.404 2zm3.564 1.426L5.596 5 8 5.961 14.154 3.5zm3.25 1.7-6.5 2.6v7.922l6.5-2.6V4.24zM7.5 14.762V6.838L1 4.239v7.923zM7.443.184a1.5 1.5 0 0 1 1.114 0l7.129 2.852A.5.5 0 0 1 16 3.5v8.662a1 1 0 0 1-.629.928l-7.185 2.874a.5.5 0 0 1-.372 0L.63 13.09a1 1 0 0 1-.63-.928V3.5a.5.5 0 0 1 .314-.464z" />
          </svg>
        </GenericButton>
      </>
    );
  }

  //---------------------------------------------------------------------
  function RegisterCompanyForm() {
    return <CompanyForm SubmitForm1={SubmitForm1} />;
  }

  //---------------------------------------------------------------------
  function RegisterProductForm() {
    return <ProductForm SubmitForm2={SubmitForm2} />;
  }

  //---------------------------------------------------------------------
  async function SubmitForm1(event) {
    event.preventDefault();

    const formData = new FormData(event.target);

    const values = {};
    formData.forEach((value, key) => {
      values[key] = value;
    });

    values["serviceFee"] = "250";

    // You can now use the 'data' object to send the data wherever needed
    // console.log("Form 1 Data:", values);

    try {
      const response = await fetch("http://localhost:3001/company", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(values),
      });

      console.log("Server Response:", response);

      if (response.ok) {
        const data = await response.json();
        // alert(JSON.stringify(data, null, 2));
      } else {
        const errorData = await response.json();
        alert("Error: " + errorData.message);
      }
    } catch (error) {
      console.error("Error submitting form: ", error);
      alert(
        "An error occurred while submitting the form. Please check the console for details."
      );
    }
  }

  //---------------------------------------------------------------------
  async function SubmitForm2(event) {
    event.preventDefault();

    const formData = new FormData(event.target);

    const values = {};
    formData.forEach((value, key) => {
      values[key] = value;
    });

    // You can now use the 'data' object to send the data wherever needed
    console.log("Form 2 Data:", values);

    try {
      const response = await fetch("http://localhost:3001/product", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(values),
      });

      console.log("Server Response:", response);

      if (response.ok) {
        const data = await response.json();
        alert(JSON.stringify(data, null, 2));
      } else {
        const errorData = await response.json();
        alert("Error: " + errorData.message);
      }
    } catch (error) {
      console.error("Error submitting form: ", error);
      alert(
        "An error occurred while submitting the form. Please check the console for details."
      );
    }
  }

  //---------------------------------------------------------------------
  return (
    <div
      style={{
        backgroundColor: backgroundColor,
        minHeight: "100vh",
      }}
    >
      <GenericButton
        className="btn-outline-info ms-4 mt-4"
        type="button"
        borderStyle={`solid 2px ${darkGray}`}
        onClickHandle={() => setContent(MainContent)}
      >
        <svg
          style={{
            color: darkGray,
          }}
          xmlns="http://www.w3.org/2000/svg"
          height="35"
          fill="currentColor"
          className="bi bi-house-fill"
          viewBox="0 0 16 16"
        >
          <path d="M8.707 1.5a1 1 0 0 0-1.414 0L.646 8.146a.5.5 0 0 0 .708.708L8 2.207l6.646 6.647a.5.5 0 0 0 .708-.708L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293z" />
          <path d="m8 3.293 6 6V13.5a1.5 1.5 0 0 1-1.5 1.5h-9A1.5 1.5 0 0 1 2 13.5V9.293z" />
        </svg>
      </GenericButton>
      <div className="container text-center">{appContent}</div>
    </div>
  );
};
