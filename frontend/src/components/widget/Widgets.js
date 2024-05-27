import React, { useState, useEffect, useRef } from "react";
import useDebounce from "../module/useDebounce";
import "./Widgets.css";
import SearchIcon from "@mui/icons-material/Search";
import WidgetsOption from "../widgetOption/WidgetsOption";
import { searchUserApi } from "../service/userService";

const Widgets = () => {
  const [userNameSearch, setUserNameSearch] = useState("");
  const [userDtos, setUserDtos] = useState([]);

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

  useEffect(() => {
    searchUser(debouncedSearchUser);
  }, [debouncedSearchUser]);

  const handleInputClick = () => {
    const input = document.querySelector(".widgets__input input");
    if (input) {
      input.focus();
    }
  };

  return (
    <div className="widgets">
      <div className="widgets__input" onClick={handleInputClick}>
        <SearchIcon className="widgets__searchIcon" />
        <input
          placeholder="Search Twitter"
          type="text"
          value={userNameSearch}
          onChange={(e) => setUserNameSearch(e.target.value)}
        />
      </div>

      <div className="widgets__widgetContainer">
        <h2>Who to follow</h2>
        {userDtos.map((userDto) => {
          return (
            <WidgetsOption
              key={userDto.id}
              userId={userDto.id}
              name={userDto.fullname}
              textId={userDto.textId}
              avatar={userDto.avatar}
            />
          );
        })}
      </div>
    </div>
  );
};

export default Widgets;
