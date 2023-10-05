import axios from "axios"
import { GetRequestOpps, URL_BINGO, URL_GAME, URL_MIXS, URL_WS_GAME } from "../connection"


const CreateGame = (navigate) => {
    const gameSetupLink = "../games/setup/"
    let requestOps = GetRequestOpps()

    axios.post(URL_GAME, {}, requestOps)
        .then((response) => {
            navigate(gameSetupLink + response.data)
        })
        .catch(error =>
            console.log(error)
        )
}

const LoadGameData = (setData, setMixId, gameid, navigate) => {
    let requestOps = GetRequestOpps()


    axios.get(URL_GAME + "/setup/" + gameid, requestOps)
        .then((response) => {
            setData(response.data)
            setMixId(response.data.mixId)
        })
        .catch((error) => {
            navigate("../")
        })
}

const SaveGameData = (gameData, gameid, navigate) => {
    const gameViewLink = "../games"

    let requestOps = GetRequestOpps()

    axios.put(URL_GAME + "/setup/" + gameid, gameData, requestOps)
        .then(r =>
            navigate(gameViewLink)
        )
        .catch(error =>
            console.log(error)
        )
}

const GetOwnedGames = (setOwnedGames, navigate) => {
    let requestOps = GetRequestOpps()

    axios.get(URL_GAME, requestOps)
        .then((response) => {
            setOwnedGames(response.data)
        })
        .catch(error =>
            navigate("../")
        )
}

const DeleteOwnedGame = (gameId, reload) => {
    let requestOps = GetRequestOpps()

    axios.delete(URL_GAME + "/" + gameId, requestOps)
        .then(r =>
            reload()
        )
        .catch(error =>
            console.log(error)
        )
}

const BeginHost = (gameData, gameId, navigate) => {
    let requestOps = GetRequestOpps()

    axios.put(URL_GAME + "/host/" + gameId, gameData, requestOps)
        .then((response) => {
            navigate("../game/" + response.data)
        })
        .catch(error =>
            console.log(error)
        )
}

const EndHost = (gamecode, navigate) => {
    let requestOps = GetRequestOpps()

    axios.put(URL_GAME + "/endhost/" + gamecode, {}, requestOps)
        .then(r =>
            navigate("../games")
        )
        .catch(error =>
            console.log(error)
        )
}

const GuestBingo = (stompClient ,gamecode, userId) => {
    stompClient.send(URL_WS_GAME + "/bingo/g/" + gamecode + "/" + userId)
}

const RegisteredBingo = (stompClient, gamecode, userName) => {
    stompClient.send(URL_WS_GAME + "/bingo/r/" + gamecode + "/" + userName)
}

export {
    CreateGame, LoadGameData, SaveGameData,
    GetOwnedGames, DeleteOwnedGame,
    BeginHost, EndHost, GuestBingo, RegisteredBingo
}