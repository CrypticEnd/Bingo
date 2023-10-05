import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import Box from '@mui/material/Box';
import MenuItem from '@mui/material/MenuItem';
import Divider from '@mui/material/Divider';
import IconButton from '@mui/material/IconButton';
import Tooltip from '@mui/material/Tooltip';
import { useState } from 'react';
import LoggedOutMenuItems from './AccountMenuItems/LoggedOut';
import { Menu } from '@mui/material';
import LoggedInMenuItems from './AccountMenuItems/LoggedIn';
import { useNavigate } from "react-router-dom";

const AccountWindow = () => {
    const [anchorEl, setAnchorEl] = useState(null);
    const navigate = useNavigate()
    const name = (localStorage.username ? localStorage.username : "Guest")
    const open = Boolean(anchorEl);

    const handleClick = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    return (
        <div className='AccountMenu'>
            <Box sx={{ display: 'flex', alignItems: 'center', textAlign: 'center' }}>
                <Tooltip title="Account">
                    <IconButton
                        onClick={handleClick}
                        size="large"
                        id='icon-button'
                    >
                        <AccountCircleIcon sx={{ width: 32, height: 32 }}></AccountCircleIcon>
                    </IconButton>
                </Tooltip>
            </Box>
            <Menu
                anchorEl={anchorEl}
                id="account-menu"
                open={open}
                onClose={handleClose}
                transformOrigin={{ horizontal: 'right', vertical: 'top' }}
                anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}>
                <MenuItem disabled={true}>
                    {name}
                </MenuItem>
                <Divider />

                {localStorage.bearer ?
                    <LoggedInMenuItems navigate={navigate} />
                    : <LoggedOutMenuItems navigate={navigate} />}
            </Menu>
        </div>
    )
}

export default AccountWindow