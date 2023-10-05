import { FormControl, TextField, Button } from "@mui/material"
import { useState } from "react"
import { useParams, useNavigate } from "react-router-dom"
import BingoLogo from "../components/BingoLogo"
import axios from "axios"
import { URL_GAME } from "../connection"
import { NewGuestUser, UpdateGuestUser } from "../functions/Login"

//TODO check if user is logged in and get user name from stored value
// Disable user name feild if logged in
const Main = () => {
    const { gamecode } = useParams()
    const navigate = useNavigate()

    const [username, setUsername] = useState(localStorage.username ? localStorage.username : "")
    const [code, setCode] = useState(gamecode ? gamecode : "")
    const [err, setErr] = useState("")

    const joinGame = () => {
        axios.get(URL_GAME + "/exist/" + code)
            .then((response) => {
                const navPath = "../game/" + code

                if (response.data) {

                    // Checking if user is logged in, if so go to game
                    if (localStorage.bearer !== undefined)
                        navigate(navPath)

                    // If guest user is already set
                    else if (localStorage.userId !== undefined)

                        // if user is not changing their name, go to game
                        // else update user name
                        if (localStorage.username === username)
                            navigate(navPath)
                        else
                            UpdateGuestUser(username, navPath, setErr, navigate)

                    // New user
                    else
                        NewGuestUser(username, navPath, setErr, navigate)
                }
                else {
                    setErr("Game code is incorrect, or the game is no longer being hoasted")
                }
            })
            .catch(error => console.log(error))
    }

    return (
        <div className="Main">
            <BingoLogo />
            <div className="JoinGameForm">
                <FormControl fullWidth={true} variant="outlined">
                    <TextField
                        label="User name"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                        disabled={Boolean(localStorage.bearer)}
                    />
                    <TextField
                        label="Game code"
                        value={code}
                        onChange={e => setCode(e.target.value)}
                    />

                    <Button variant='contained' onClick={joinGame}>Join</Button>
                    {err}
                </FormControl>
            </div>
        </div>
    )
}

export default Main