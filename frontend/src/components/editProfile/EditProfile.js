import React, { useState, useEffect, useRef } from "react";
import CloseIcon from "@mui/icons-material/Close";
import AddPhotoAlternateIcon from "@mui/icons-material/AddPhotoAlternate";
import anh from "./avatar-dep-84.jpg";
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import { Avatar } from "@mui/material";
import style from "./EditProfile.module.scss";
import { editInfo, getInfo } from "../service/userService";
import axios from "../service/customAxios";
import { useNavigate } from "react-router-dom";

const EditProfile = ({ user, onOptionClick }) => {
  const [avatar, setAvatar] = useState(null);
  const [banner, setBanner] = useState(null);
  const [linkAvatar, setLinkAvatar] = useState(null);
  const [linkBanner, setLinkBanner] = useState(null);
  const [fullname, setFullname] = useState("");
  const [bio, setBio] = useState("");
  const [location, setLocation] = useState("");
  const fileInputRefAvatar = useRef(null);
  const fileInputRefBanner = useRef(null);
  const userId = localStorage.getItem("userId");

  const navigate = useNavigate();

  const handleImageInput = (e) => {
    const file = e.target.files;
    console.log(file);
    if (file) {
      const imageArray = Array.from(file);
      const linkImageArray = Array.from(file).map((file) =>
        URL.createObjectURL(file)
      );
      console.log(linkImageArray[0])
      console.log(imageArray[0])

      return imageArray[0], linkImageArray[0];
    }
  };

  const getUserInfo = async (userId) => {
    let res = await getInfo(userId);
    let user = res.data;
    console.log(user);
    setAvatar(user.avatar);
    setBanner(user.banner);
    setFullname(user.fullname);
    setBio(user.bio);
    setLocation(user.location);
    setLinkAvatar(user.avatar);
    setLinkBanner(user.banner);
  };

  const handleChangeInfo = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    if (typeof avatar === "string") {
      formData.append("avatarFile", null);
    } else {
      formData.append("avatarFile", avatar);
    }
    if (typeof banner === "string") {
      formData.append("bannerFile", null);
    } else {
      formData.append("bannerFile", banner);
    }

    formData.append("fullname", fullname);
    formData.append("bio", bio);
    formData.append("location", location);
    let res = axios
      .patch("/user/change_info", formData, {
        headers: {
          "Content-Type": "multipart/form-data", // Đảm bảo định dạng yêu cầu là multipart/form-data
        },
      })
      .then((res) => {
        console.log(res);
      })
      .catch((error) => {
        console.log(error);
      });
    navigate(`/home/profile/${userId}`);
  };

  useEffect(() => {
    getUserInfo(userId).then(() => {
      console.log(avatar);
      console.log(banner);
      console.log(fullname);
      console.log(bio);
      console.log(location);
    });
  }, []);

  return (
    <div className={style.editProfile}>
      <div className={style.editHeader}>
        <div className={style.text}>
          <div
            className={style.closeIcon}
            onClick={() => {
              navigate(`/home/profile/${userId}`);
            }}
          >
            <CloseIcon />
          </div>

          <p>Edit Profile</p>
        </div>
        <div className={style.save} onClick={(e) => handleChangeInfo(e)}>
          Save
        </div>
      </div>

      <div className={style.editBanner}>
        <img
          className={style.bannerImage}
          src={linkBanner || "/default/defaultBanner.jpg"}
        />
        <input
          ref={fileInputRefBanner}
          type="file"
          style={{ display: "none" }}
          onChange={(e) => {
            const file = e.target.files;
            if (file) {
              const imageArray = Array.from(file);
              const linkImageArray = Array.from(file).map((file) =>
                URL.createObjectURL(file)
              );
              setBanner(imageArray[0]);
              // Cập nhật linkAvatar khi chọn file mới
              setLinkBanner(linkImageArray[0]);
            }
          }}
        />
        <div className={style.photoBannerIcon}>
          <AddPhotoAlternateIcon
            onClick={() => {
              fileInputRefBanner.current.click();
            }}
          />
        </div>
      </div>

      <div className={style.editAvatar}>
        <div className={style.avatarImage}>
          <Avatar
            className={style.avatar}
            src={linkAvatar || "/default/nullAvatar.jpg"}
          />
        </div>

        <input
          ref={fileInputRefAvatar}
          type="file"
          style={{ display: "none" }}
          onChange={(e) => {
            const file = e.target.files;
            if (file) {
              const imageArray = Array.from(file);
              const linkImageArray = Array.from(file).map((file) =>
                URL.createObjectURL(file)
              );
              setAvatar(imageArray[0]);
              // Cập nhật linkAvatar khi chọn file mới
              setLinkAvatar(linkImageArray[0]);
            }
          }}
        />
        <div className={style.photoAvatar}>
          <AddPhotoAlternateIcon
            className={style.photoAvatarIcon}
            onClick={() => {
              fileInputRefAvatar.current.click();
            }}
          />
        </div>
      </div>

      <div style={{ marginTop: "100px" }}>
        <TextField
          id="outlined-multiline-flexible"
          sx={{ width: "100%", margin: " 10px 0" }}
          label="Name"
          multiline
          maxRows={1}
          InputLabelProps={{
            sx: { color: "black" },
          }}
          value={fullname}
          onChange={(e) => setFullname(e.target.value)}
        />

        <TextField
          id="outlined-multiline-flexible"
          sx={{ width: "100%", margin: " 10px 0" }}
          label="Bio"
          multiline
          maxRows={1}
          InputLabelProps={{
            sx: { color: "black" },
          }}
          value={bio}
          onChange={(e) => setBio(e.target.value)}
        />

        <TextField
          id="outlined-multiline-flexible"
          sx={{ width: "100%", margin: " 10px 0" }}
          label="Location"
          multiline
          maxRows={1}
          InputLabelProps={{
            sx: { color: "black" },
          }}
          value={location}
          onChange={(e) => setLocation(e.target.value)}
        />
      </div>
    </div>
  );
};

export default EditProfile;
