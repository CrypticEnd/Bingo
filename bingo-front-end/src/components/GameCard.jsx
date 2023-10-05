import { Button } from '@mui/material';

const GameCard = (props) => {
    const { onDelete, view } = props
    const { gameId, gameName, winnerName, bingoSet, hostLink } = props.game;

    const winnerDefined = winnerName !== null
    const bingoSetDefined = bingoSet !== null

    const viewButton = () => {
        view(gameId, hostLink)
    }

    const deleteButton = () => {
        onDelete(gameId)
    }

    return (
        <div className="GameCard">
            <h2>{gameName}</h2>
            {bingoSetDefined ? <p>Mix: {bingoSet}</p> : <p>-</p>}
            {winnerDefined ? <p>Winner: {winnerName}</p> : <p>No Winner</p>}


            <Button
                variant="outlined"
                onClick={deleteButton}
                disabled={hostLink > 0}
            >
                Delete
            </Button>
            <Button
                variant="outlined"
                onClick={viewButton}
            >
                View
            </Button>
        </div>
    )
}

export default GameCard