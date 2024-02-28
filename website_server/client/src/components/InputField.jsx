import React from "react";

export const InputField = ({
  id,
  type,
  text,
  placeholder,
  maxLength,
  pattern,
  feedback,
}) => {
  return (
    <>
      <div className="d-flex flex-column align-items-start">
        <label htmlFor={`${id}`} className="form-label text-start fs-4">
          {text}
        </label>
        <input
          type={`${type}`}
          className="form-control fs-4"
          id={`${id}`}
          placeholder={`${placeholder}`}
          pattern={`${pattern}`}
          maxLength={`${maxLength}`}
          required=""
        />
        <div className="invalid-feedback">{feedback}</div>
      </div>
    </>
  );
};
