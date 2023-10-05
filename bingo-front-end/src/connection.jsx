const URL_BASE = process.env.REACT_APP_CONNECTION_URL;

const URL_LOGIN = URL_BASE + "/auth/login"
const URL_USER = URL_BASE + "/user"
const URL_USER_REGISTERED = URL_USER + "/registered"

const URL_GAME = URL_BASE + "/game"
const URL_MIXS = URL_BASE + "/mix"

const URL_BINGO = URL_GAME + "/bingo"

const URL_WS_CONNECTION = URL_BASE + "/ws"
const URL_WS_GAME = "/app/game"

const GetRequestOpps = () => {
    let requestOps = {}

    if (localStorage.bearer !== null) {
        requestOps.headers = { Authorization: localStorage.getItem("bearer") }
    }

    return requestOps
}

export { URL_BASE, URL_LOGIN, URL_USER, URL_USER_REGISTERED, URL_GAME, URL_MIXS, URL_BINGO, URL_WS_CONNECTION, URL_WS_GAME, GetRequestOpps }