import { useEffect, useState } from "react"
import BingoMixes from "../components/GameCreationComponets/BingoMixes"
import GameSettings from "../components/GameCreationComponets/GameSettings"
import BingoLogo from "../components/BingoLogo"
import { useNavigate, useParams } from "react-router-dom"
import { BeginHost, LoadGameData, SaveGameData } from "../functions/Game"



const GameSetup = () => {
    const { gameid } = useParams()
    const navigate = useNavigate()
    const [gameData, setGameData] = useState({})

    const { userId, gameName, colorPrimary, colorSecondary, colorSelected, centerFree, allSameWord, boardSize } = gameData;

    const [selectedMix, setSelectedMix] = useState(-1)
    const [phraseCount, SetPhraseCount] = useState(-1)

    const loadData = () => {
        LoadGameData(setGameData, setSelectedMix, gameid, navigate)
    }
    useEffect(loadData, [])

    const saveGame = (settings) => {
        settings = {
            ...settings,
            userId: userId,
            mixId: selectedMix
        }

        SaveGameData(settings, gameid, navigate)
    }

    const hostGame = (settings) => {
        settings = {
            ...settings,
            userId: userId,
            mixId: selectedMix
        }

        BeginHost(settings, gameid, navigate)
    }

    const updateSelectedMix = (mixId, phraseCount) => {
        setSelectedMix(mixId)
        //TODO update board size based on max board size selected
        // Maybe update center free as well? 
    }

    return (
        <>
            {Object.keys(gameData).length === 9 &&
                <div className="GameSetup">
                    <BingoLogo />
                    <GameSettings
                        name={gameName} size={boardSize}
                        isCenterFree={centerFree} isAllSameWord={allSameWord}
                        colorPrimary={colorPrimary} colorSecondary={colorSecondary}
                        colorSelected={colorSelected}
                        onSave={saveGame} onHost={hostGame}
                    />
                    <BingoMixes userId={userId} mixId={selectedMix} setMixId={updateSelectedMix} SetPhraseCount={SetPhraseCount} />
                </div>
            }
        </>
    )
}


export default GameSetup