import React from "react";
import "./SideBar.css";
import { Link, useNavigate } from "react-router-dom";
import InterestsIcon from "@mui/icons-material/Interests";
import SideBarOption from "../sidebarOption/SideBarOption";
import HomeIcon from "@mui/icons-material/Home";
import NotificationsNoneIcon from "@mui/icons-material/NotificationsNone";
import PermIdentityIcon from "@mui/icons-material/PermIdentity";
import MoreHorizIcon from "@mui/icons-material/MoreHoriz";
import ChatBubbleOutlineIcon from "@mui/icons-material/ChatBubbleOutline";
import { Button } from "@mui/material";
import PeopleOutlineIcon from '@mui/icons-material/PeopleOutline';

function SideBar() {
  const userId = localStorage.getItem("userId");

  return (
    <div className="sidebar">
      <InterestsIcon className="sidebar__twitterIcon" />

      <SideBarOption Icon={HomeIcon} text="Home" to="/home" />
      <SideBarOption
        Icon={NotificationsNoneIcon}
        text="Notifications"
        to="/home/notification"
      />
      <SideBarOption
        Icon={ChatBubbleOutlineIcon}
        text="Messages"
        to={"/home/message_null"}
      />
      <SideBarOption
        Icon={PermIdentityIcon}
        text="Profile"
        to={`/home/profile/${userId}`}
      />
      <SideBarOption Icon={PeopleOutlineIcon} text="Friend" to={`/home/friends`} />

      {/* <Button variant="outlined" className="sidebar__tweet" fullWidth>
        <p>Tweet</p>
      </Button> */}
    </div>
  );
}

export default SideBar;
