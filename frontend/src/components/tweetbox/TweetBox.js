import React, { useState, useEffect } from "react";
import "./TweetBox.css";
import { Avatar, Button } from "@mui/material";
import InsertPhotoIcon from "@mui/icons-material/InsertPhoto";
import CloseIcon from "@mui/icons-material/Close";
import { getInfo } from "../service/userService";
import axios from "../service/customAxios";

const TweetBox = () => {
  const [tweetMessage, setTweetMessage] = useState("");
  const [tweetImages, setTweetImages] = useState([]);
  const [images, setImages] = useState([]);
  const [user, setUser] = useState(null);

  useEffect(() => {
    let userId = localStorage.getItem("userId");
    getUserInfo(userId);
  }, []);

  const getUserInfo = async (userId) => {
    let res = await getInfo(userId);
    console.log(res.data);
    setUser(res.data);
  };

  const handleInputClick = () => {
    const input = document.querySelector(".tweetBox__input input");
    if (input) {
      input.focus();
    }
  };

  const handleImageInput = (e) => {
    const files = e.target.files;
    console.log(files);
    if (files) {
      const linkImageArray = Array.from(files).map((file) =>
        URL.createObjectURL(file)
      );
      console.log(linkImageArray);
      const imageArray = Array.from(files);
      setTweetImages((prevImages) => [...prevImages, ...linkImageArray]);
      setImages((prevImages) => [...prevImages, ...imageArray]);

      console.log(images);
    }
  };

  const handleImageDelete = (index) => {
    const newTweetImages = [...tweetImages];
    newTweetImages.splice(index, 1);
    setTweetImages(newTweetImages);
    const newImages = [...images];
    newImages.splice(index, 1);
    setImages(newImages);
  };

  const sendTweet = async (e) => {
    e.preventDefault();

    const formData = new FormData();
    formData.append("content", tweetMessage);

    images.forEach((image, index) => {
      formData.append("images", image);
    });

    let res = axios
      .post("/post/save", formData, {
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

    setTweetMessage("");
    setTweetImages([]);
    setImages([]);
    console.log(res.data);
  };

  return (
    <div className="tweetBox">
      <form>
        <div className="tweetBox__input" onClick={handleInputClick}>
          <Avatar src={user?.avatar || "/default/nullAvatar.jpg"} />
          <input
            onChange={(e) => setTweetMessage(e.target.value)}
            value={tweetMessage}
            placeholder="What's happening?"
            type="text"
          />
        </div>
        <label htmlFor="fileInput" className="custom-file-upload">
          <input
            id="fileInput"
            type="file"
            onChange={handleImageInput}
            multiple
            style={{ display: "none" }}
          />
          <InsertPhotoIcon className="insertPhotoIcon" />
        </label>
        <div className="tweetBox__imagesPreview">
          {images &&
            images.map((image, index) => (
              <div key={index} className="uploaded-image">

                {image.type === "image/png" && <img src={tweetImages[index]} />}
                {image.type === "video/mp4" && (
                  <video controls>
                    <source src={tweetImages[index]} />
                  </video>
                )}
                <CloseIcon
                  className="delete_image"
                  onClick={() => handleImageDelete(index)}
                />
              </div>
            ))}
        </div>
        <Button
          onClick={sendTweet}
          type="submit"
          className="tweetBox__tweetButton"
        >
          Tweet
        </Button>
      </form>
    </div>
  );
};

export default TweetBox;
