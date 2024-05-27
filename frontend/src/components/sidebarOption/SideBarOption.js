import React from "react";
import "./SidebarOption.css";
import { Link, useNavigate, useLocation } from "react-router-dom";

const SideBarOption = ({ active, text, Icon, to }) => {

  const navigate = useNavigate();
  const location = useLocation();

  function handleClick() {
    if (location.pathname === to) {
    
      window.location.reload();
    } else {
     
      navigate(to);
    }
  }

  return (
  
      <div onClick={handleClick} className={`sidebarOption ${active && "sidebarOption--active"}`}>
        <Icon />
        <h2>{text}</h2>
      </div>

  );
};

export default SideBarOption;
