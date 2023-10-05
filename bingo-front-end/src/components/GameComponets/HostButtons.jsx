import { useState } from "react"
import { Button, FormControl } from '@mui/material';



const HoastButtons = ({ gamePin, endHoast }) => {
    const buttonText = "Copy Game Link"
    const [copyText, setCopyText] = useState(buttonText)

    const copyCode = async (event) => {
        const gameLink = window.location.host + "/" + gamePin

        if (window.isSecureContext) {
            setCopyText("Copied")
            event.target.classList.add("clicked")
            navigator.clipboard.writeText(gameLink);
        }
        else{
            window.alert("Cannot auto copy, link is below:\n"+ gameLink)
        }

        // Delay 
        await new Promise(res => setTimeout(res, 2000));

        setCopyText(buttonText)
        event.target.classList.remove("clicked")
    }

    return (
        <div className="HoastButtons">
            <FormControl fullWidth={true}>
                <Button variant="contained" onClick={copyCode}>{copyText}</Button>
                <Button variant="contained" onClick={endHoast}>End hoast</Button>
            </FormControl>
        </div>
    )
}

export default HoastButtons