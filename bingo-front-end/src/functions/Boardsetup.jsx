import { URL_GAME } from "../connection"
import axios from "axios"

const BoardSetup = (gameCode, setGameData, onGameDataError) => {

    let requestOps = {}

    if (localStorage.bearer !== null) {
        requestOps.headers = { Authorization: localStorage.getItem("bearer") }
    }

    axios.get(URL_GAME + "/loadBoard/" + gameCode, requestOps)
        .then(response =>
            setGameData(response.data)
        )
        .catch(error =>
            onGameDataError(error)
        )
}

export default BoardSetup;