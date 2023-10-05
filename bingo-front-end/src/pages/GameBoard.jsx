import BingoBoard from "../components/GameComponets/BingoBoard"
import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import BingoLogo from "../components/BingoLogo"
import GameInfo from "../components/GameComponets/GameInfo"
import BingoButton from "../components/GameComponets/BingoButton"
import BoardSetup from "../functions/Boardsetup"
import { EndHost, GuestBingo, RegisteredBingo } from "../functions/Game"
import { over } from 'stompjs';
import SockJS from 'sockjs-client';
import { URL_WS_CONNECTION } from "../connection"

var stompClient = null;
const GameBoard = () => {
    const { gamecode } = useParams()
    const navigate = useNavigate();

    const [board, setBoard] = useState([])
    const [boardSettings, setBoardSettings] = useState([])

    const [gameWinner, setGameWinner] = useState(null)

    // TODO make into a method not a sate
    const [hasBingo, setHasBingo] = useState(true)

    useEffect(() => {
        BoardSetup(gamecode, setGameData, onGameDataError)
        stompClientConnect()
    }, [])

    const setGameData = (gameData) => {
        setBoardSettings(gameData.settings)
        setBoard(gameData.board)
        setGameWinner(gameData.winner)
    }

    const onGameDataError = (error) => {
        console.log(error)
        window.alert("An error has occured with loading gamecode: " + gamecode)
        navigate("../")
    }

    const stompClientConnect = () => {
        // Connecting to socket 
        let sock = new SockJS(URL_WS_CONNECTION)
        stompClient = over(sock)

        // remove debug lines
        stompClient.debug = () => { }

        stompClient.connect({}, onClientConnected, onClientError);
    }

    const onClientConnected = () => {
        stompClient.subscribe('/game/' + gamecode + '/winner', onWinnerMessage)
    }

    const onClientError = () => {
        setTimeout(stompClientConnect, 10000)
        console.log('STOMP: Reconecting in 10 seconds');
    }

    const selectTile = (event) => {
        const id = parseInt(event.target.id)
        const column = parseInt(id / boardSettings.boardSize)
        const row = parseInt(id % boardSettings.boardSize)

        const isSelected = board[column][row].selected
        board[column][row].selected = !isSelected

        setBoard([...board])
    }

    const endHoast = () => {
        EndHost(gamecode, navigate)
    }

    const bingoButton = () => {
        if (stompClient.connected)
            if (localStorage.userId === undefined) {
                RegisteredBingo(stompClient, gamecode, localStorage.username)
            }
            else {
                GuestBingo(stompClient, gamecode, localStorage.userId)
            }
        else {
            window.alert("Client is not connected to web server")
        }
    }

    const onWinnerMessage = (payload) => {
        let payloadData = JSON.parse(payload.body)

        setGameWinner(payloadData.name)
    }

    return (
        <div className="GameBoard">
            <BingoLogo />
            <div className="Game">
                <GameInfo boardSetting={boardSettings} winner={gameWinner} endHoast={endHoast} />
                <BingoBoard board={board} settings={boardSettings} selectFunction={selectTile} />
                <BingoButton hasBingo={hasBingo} handleBingo={bingoButton} />
            </div>
        </div>
    )
}

export default GameBoard