import React from "react";
import { useNavigate } from "react-router-dom";
import { Avatar } from "@mui/material";
import style from "./MessageSearchOption.module.scss";
const MessageSearchOption = ({ userId, avatar, name, textId, to }) => {

  const navigate = useNavigate();

  return (
    <div
      className={style.user}
      onClick={() => {
        navigate(to);
      }}
    >
      <Avatar
        className={style.avatar}
        src={avatar}
        alt="Profile Picture"
        onClick={(e) => {
          e.stopPropagation();
          navigate(`/home/profile/${userId}`);
        }}
      />
      <div className={style.info}>
        <strong className={style.fullname}>{name}</strong>
        <span className={style.textId}>{textId}</span>
      </div>
    </div>
  );
};

export default MessageSearchOption;
