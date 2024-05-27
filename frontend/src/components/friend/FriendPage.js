import React, { useState, useEffect } from "react";
import style from "./FriendPage.module.scss";
import SearchIcon from "@mui/icons-material/Search";
import useDebounce from "../module/useDebounce";
import { searchUserApi, getAllFriend } from "../service/userService";
import KeyboardBackspaceIcon from "@mui/icons-material/KeyboardBackspace";
import MessageSearchOption from "../MessageSearchOption/MessageSearchOption";
const FriendPage = () => {
  const [userNameSearch, setUserNameSearch] = useState("");
  const [userDtos, setUserDtos] = useState([]);
  const [friends, setFriends] = useState([]);
  const [showSearch, setShowSearch] = useState(false);

  const debouncedSearchUser = useDebounce(userNameSearch, 300);

  const searchUser = async (userNameSearch) => {
    if (!userNameSearch || !userNameSearch.trim()) {
      setUserDtos([]);
    } else {
      try {
        const result = await searchUserApi(userNameSearch);
        setUserDtos(result.data);
        console.log(debouncedSearchUser);
        console.log(result.data);
      } catch (error) {
        console.error("Error fetching userDtos:", error);
      }
    }
  };

  const getFriend = async () => {
    let res = await getAllFriend();
    setFriends(res.data);
  };

  useEffect(() => {
    getFriend();
  }, []);

  useEffect(() => {
    searchUser(debouncedSearchUser);
  }, [debouncedSearchUser]);

  const handleInputClick = () => {
    const input = document.getElementById("searchInput");
    const back = document.getElementById("BackIcon");
    setShowSearch(true);
    if (input && back) {
      input.focus();
      back.style.display = "block";
    }
  };

  const handleBack = () => {
    setShowSearch(false);
    const back = document.getElementById("BackIcon");
    back.style.display = "none";
    setUserNameSearch("");
  };

  return (
    <div className={style.ListUser}>
      <h2>Friends</h2>

      <div className={style.ListUserHeader}>
        <KeyboardBackspaceIcon
          id="BackIcon"
          className={style.back}
          onClick={handleBack}
        />
        <div className={style.messageSearch} onClick={handleInputClick}>
          <SearchIcon className={style.searchIcon} />
          <input
            id="searchInput"
            className={style.searchInput}
            placeholder="Search Twitter"
            type="text"
            value={userNameSearch}
            onChange={(e) => setUserNameSearch(e.target.value)}
          />
        </div>
      </div>

      {showSearch &&
        userDtos.map((userDto) => (
          <MessageSearchOption
            key={userDto.id}
            userId={userDto.id}
            avatar={userDto.avatar}
            name={userDto.fullname}
            textId={userDto.textId}
            to={`/home/profile/${userDto.id}`}
          />
        ))}
      {!showSearch &&
        friends.map((userDto) => (
          <MessageSearchOption
            key={userDto.id}
            userId={userDto.id}
            avatar={userDto.avatar}
            name={userDto.fullname}
            textId={userDto.textId}
            to={`/home/profile/${userDto.id}`}
          />
        ))}
    </div>
  );
};

export default FriendPage;
