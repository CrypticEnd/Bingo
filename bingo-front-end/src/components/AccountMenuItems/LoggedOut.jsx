import PersonAddIcon from '@mui/icons-material/PersonAdd';
import ListItemIcon from '@mui/material/ListItemIcon';
import LoginIcon from '@mui/icons-material/Login';
import MenuItem from '@mui/material/MenuItem';
import { useState } from "react";
import AccountDialog from './AccountDialog';
import {Login, Register} from '../../functions/Login';

const LoggedOutMenuItems = ({navigate}) => {
    const [displayDialog, setDisplayDialog] = useState(false);
    const [dialogMsg, setDialogMsg] = useState("");
    const [errorMsg, setErrorMsg] = useState("");

    const loginButton = () => {
        setDisplayDialog(true);
        setDialogMsg("Login")
    };

    const createAccountButton= () => {
        setDisplayDialog(true);
        setDialogMsg("Create Account")
    }

    const handleClose = () => {
        setDisplayDialog(false);
    };

    const handleSubmit = (username, password) => {
        if(dialogMsg.toUpperCase() === "LOGIN"){
            Login(username, password, setErrorMsg, navigate)
        }
        else{
            Register(username, password, setErrorMsg, navigate)
        }
    }

    return (
        <>
            <AccountDialog onClose={handleClose} onSubmit={handleSubmit}
            display={displayDialog} confirmText={dialogMsg} errorMsg={errorMsg} />

            <MenuItem onClick={createAccountButton}>
                <ListItemIcon>
                    <PersonAddIcon fontSize="small" />
                </ListItemIcon>
                Create Account
            </MenuItem>

            <MenuItem onClick={loginButton}>
                <ListItemIcon>
                    <LoginIcon fontSize="small" />
                </ListItemIcon>
                Sign in
            </MenuItem>
        </>
    )
}

export default LoggedOutMenuItems