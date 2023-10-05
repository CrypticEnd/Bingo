import { Button } from "@mui/material"


const BingoButton = ({winner, hasBingo, handleBingo}) =>{

    return(
        <div className="BingoButton">
            {winner==="" && <p>{winner} has won!</p>}

            <Button 
            fullWidth={true}
            disabled={!hasBingo}
            variant="contained"
            onClick={handleBingo}
            
            >BINGO</Button>
        </div>
    )
}

export default BingoButton