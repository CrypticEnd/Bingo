import { Link } from "@mui/material"
import bingo_logo from "../content/bingo_logo.png"
import { useNavigate } from "react-router-dom"

const BingoLogo = () => {
    const navigate = useNavigate();

    const goHome = () =>{
        navigate("../")
    }

    return (

        <div className="logo-container">
                <img className="logo" src={bingo_logo} alt="Bingo Logo" onClick={goHome} />
        </div>
    )
}
export default BingoLogo