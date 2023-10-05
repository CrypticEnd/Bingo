import { URL_LOGIN, URL_USER, URL_USER_REGISTERED } from "../connection"
import axios from "axios"

const Login = (username, password, setErrorMsg, navigate) => {

    const payload = {
        "username": username,
        "password": password
    }

    axios.post(URL_LOGIN, payload)
        .then((response) => {
            localStorage.setItem("bearer",
                response.headers.get("Authorization"))
            localStorage.setItem("username", username)
            navigate("../")
        })
        .catch((error) => {
            setErrorMsg("Username or password incorrect")
            console.error(error)
        })
}

const Logout = (navigate) => {
    localStorage.clear()
    navigate("./")
}

const Register = (username, password, setErrorMsg, navigate) => {
    let payload = {
        "username": username,
        "password": password
    }

    if (localStorage.userId !== undefined) {
        payload = { ...payload, "id": localStorage.userId }
    }

    axios.post(URL_USER_REGISTERED, payload)
        .then(response =>
            Login(username, password, setErrorMsg, navigate)
        )
        .catch((error) => {
            setErrorMsg("Something went wrong")
            console.error(error.response.data.message)
        })
}

const NewGuestUser = (username, path, setErrorMsg, navigate) => {
    const payload = {
        "username": username
    }

    axios.post(URL_USER, payload)
        .then((response) => {
            localStorage.setItem("username", username)
            localStorage.setItem("userId", response.data)
            navigate(path)
        })
        .catch((error) => {
            setErrorMsg("Something went wrong")
            console.error(error.response.data.message)
        })
}

const UpdateGuestUser = (username, path, setErrorMsg, navigate) => {
    const payload = {
        "id": localStorage.userId,
        "username": username
    }

    axios.put(URL_USER, payload)
        .then((response) => {
            localStorage.setItem("username", username)
            navigate(path)
        })
        .catch((error) => {
            setErrorMsg("Something went wrong")
            console.error(error.response.data.message)
        })
}

export { Login, Logout, Register, NewGuestUser, UpdateGuestUser }