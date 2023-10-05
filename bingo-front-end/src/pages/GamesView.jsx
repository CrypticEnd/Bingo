import { useEffect, useState } from "react"
import BingoLogo from "../components/BingoLogo"
import { DeleteOwnedGame, GetOwnedGames } from "../functions/Game"
import GameCard from "../components/GameCard"
import { useNavigate } from "react-router-dom"


const GameView = () => {
    const [gameList, setGameList] = useState([])
    const navigate = useNavigate();

    const LoadData = () => {
        GetOwnedGames(setGameList, navigate)
    }
    useEffect(LoadData, [])

    const view = (gameId, hostLink) => {
        if (hostLink > 0) {
            navigate("../game/" + hostLink)
        }
        else {
            navigate("../games/setup/" + gameId)
        }
    }

    const onDelete = async (gameId) => {
        DeleteOwnedGame(gameId, LoadData)
    }

    return (
        <div className="ViewGames">
            <BingoLogo />

            <div className="GamesBox">
                {gameList.map(game =>
                    <GameCard game={game} key={game.gameId}
                        onDelete={onDelete} view={view} />
                )}
            </div>
        </div>
    )
}

export default GameView