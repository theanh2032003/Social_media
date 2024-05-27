// CallNotificationComponent.js
import React, { useEffect } from "react";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import Button from "@mui/material/Button";

const CallNotificationComponent = ({
  caller,
  isOpen,
  onClose,
  onAcceptCall,
  isCalled
}) => {
  useEffect(() => {
    // Thiết lập hẹn giờ đóng thông báo sau 30 giây khi thông báo mở
    let timer;
    if (isOpen) {
      timer = setTimeout(() => {
        onClose();
      }, 30000); // 30 giây
    }

    // Clear hẹn giờ khi thông báo đóng
    return () => {
      clearTimeout(timer);
    };
  }, [isOpen, onClose]);

  useEffect(() => {
    console.log(caller)
    console.log(isCalled)

  },[])

  return (
    <Dialog open={isOpen} onClose={onClose}>
      {caller && localStorage.getItem("userId") != caller.id && (
        <>
          <DialogTitle style={{fontWeight:"600"}}>Cuộc gọi đến</DialogTitle>
          <DialogContent>
          <DialogContentText style={{fontWeight:"700"}}>{caller.fullname} </DialogContentText>
            <DialogContentText>đang gọi cho bạn...</DialogContentText>
          </DialogContent>
          <DialogActions>
            <Button onClick={onClose} color="secondary">
              Từ chối
            </Button>
            <Button onClick={onAcceptCall} color="primary">
              Chấp nhận
            </Button>
          </DialogActions>
        </>
      )}
      {isCalled && localStorage.getItem("userId") == caller.id && (
        <>
          <DialogTitle style={{fontWeight:"700"}}>Cuộc gọi đi</DialogTitle>
          <DialogContent>
            <DialogContentText>đang gọi cho </DialogContentText>
          <DialogContentText style={{fontWeight:"600"}}>{isCalled.fullname}</DialogContentText>

          </DialogContent>
          <DialogActions>
            <Button onClick={onClose} color="secondary">
              Hủy
            </Button>
          </DialogActions>
        </>
      )}
    </Dialog>
  );
};

export default CallNotificationComponent;
