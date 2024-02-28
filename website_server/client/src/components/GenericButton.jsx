import React from "react";

export const GenericButton = ({
  onClickHandle,
  children,
  className,
  borderStyle,
  type,
}) => {
  return (
    <>
      <button
        type={`${type}`}
        className={`btn ${className}`}
        style={{
          border: `${borderStyle}`,
        }}
        onClick={onClickHandle}
      >
        {children}
      </button>
    </>
  );
};
