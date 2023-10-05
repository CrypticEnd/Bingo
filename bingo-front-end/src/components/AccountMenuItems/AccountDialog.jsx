import { Dialog, DialogTitle, DialogContent, DialogContentText, TextField, DialogActions, Button, FormControl } from "@mui/material"
import { useEffect, useState } from "react"


const AccountDialog = ({ display, onClose, confirmText, onSubmit, errorMsg }) => {
    const [username, setUsername] = useState(localStorage.username ? localStorage.username : "")
    const [password, setPassword] = useState("")


    const handleSubmit = () => {
        onSubmit(username, password)
    }

    // Hanle close and cancel are different 
    // Want to keep username state if the close was accidental
    // but on cancel clean the states fully  
    const handleCancel = () => {
        setUsername("")
        setPassword("")
        onClose()
    }

    const handleClose = () => {
        setPassword("")
        onClose()
    }

    return (
        <div>
            <Dialog open={display} onClose={handleClose}>
                <DialogTitle>{confirmText}</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        {errorMsg}
                    </DialogContentText>

                    <TextField

                        type="text"
                        label="Username"
                        fullWidth
                        variant="standard"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                    />
                    <TextField

                        label="Password"
                        type="password"
                        fullWidth
                        variant="standard"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                    />

                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCancel}>Cancel</Button>
                    <Button onClick={handleSubmit}>{confirmText}</Button>
                </DialogActions>
            </Dialog>
        </div>
    )


}

export default AccountDialog