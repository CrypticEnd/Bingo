import AddOutlinedIcon from '@mui/icons-material/AddOutlined';
import GamepadOutlinedIcon from '@mui/icons-material/GamepadOutlined';
import SmartToyOutlinedIcon from '@mui/icons-material/SmartToyOutlined';
import ListItemIcon from '@mui/material/ListItemIcon';
import LogoutIcon from '@mui/icons-material/Logout';
import MenuItem from '@mui/material/MenuItem';
import { Logout } from '../../functions/Login';
import { Link } from 'react-router-dom';
import { CreateGame } from '../../functions/Game';


const LoggedInMenuItems = ({ navigate }) => {
    const gamesOwnedLink = "../games"
    const homeLink = "../"

    const logoutButton = (event) => {
        Logout(navigate)
    }

    const createGameButton = (event) =>{
        CreateGame(navigate);
    }

    return (
        <>

            <MenuItem onClick={createGameButton}>
                <ListItemIcon>
                    <AddOutlinedIcon fontSize="small" />
                </ListItemIcon>
                Create Game
            </MenuItem>


            <Link to={gamesOwnedLink}>
                <MenuItem>
                    <ListItemIcon>
                        <GamepadOutlinedIcon fontSize="small" />
                    </ListItemIcon>
                    Games
                </MenuItem>

            </Link>
            <Link to={homeLink}>
                <MenuItem>
                    <ListItemIcon>
                        <SmartToyOutlinedIcon fontSize="small" />
                    </ListItemIcon>
                    Join Game
                </MenuItem>
            </Link>

            <MenuItem onClick={logoutButton}>
                <ListItemIcon>
                    <LogoutIcon fontSize="small" />
                </ListItemIcon>
                Logout
            </MenuItem>
        </>
    )
}

export default LoggedInMenuItems