import React from "react";

export const TextAreaField = ({ id, text, placeholder, feedback }) => {
  return (
    <>
      <div className="d-flex flex-column align-items-start">
        <label htmlFor={`${id}`} className="form-label text-start fs-4">
          {text}
        </label>
        <textarea
          type="text"
          className="form-control fs-4"
          id={`${id}`}
          placeholder={`${placeholder}`}
          required=""
        ></textarea>
        <div className="invalid-feedback">{feedback}</div>
      </div>
    </>
  );
};
