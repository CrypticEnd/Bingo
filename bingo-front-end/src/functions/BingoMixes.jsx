import axios from "axios"
import { GetRequestOpps, URL_MIXS } from "../connection"

const LoadAllMixes = (userId, setMixes) => {
    let requestOps = GetRequestOpps()

    axios.get(URL_MIXS + "/all/" + userId, requestOps)
        .then((response) => {
            setMixes(response.data)
        })
        .catch((error) => {
            console.log(error)
        })
}

const GetSingleMix = (mixId, setMix) =>{
    let requestOps = GetRequestOpps()

    axios.get(URL_MIXS + "/" + mixId, requestOps)
        .then((response) => {
            setMix(response.data)
        })
        .catch((error) => {
            console.log(error)
        })
}

const UpdateMix = (mix, onClose, reload) =>{
    let requestOps = GetRequestOpps()

    axios.put(URL_MIXS, mix, requestOps)
        .then((response) => {
            onClose()
            reload()
        })
        .catch((error) => {
            console.log(error)
        })
}

const CopyMix = (mixId, setMix, reload) => {
    let requestOps = GetRequestOpps()

    axios.post(URL_MIXS + "/" + mixId, {}, requestOps)
        .then((response) => {
            setMix(response.data)
            reload()
        })
        .catch((error) => {
            console.log(error)
        })
}

const DeleteMix = (mixId, onClose, reload) =>{
    let requestOps = GetRequestOpps()

    axios.delete(URL_MIXS + "/" + mixId, requestOps)
        .then((response) => {
            onClose()
            reload()
        })
        .catch((error) => {
            console.log(error)
        })
}


export {LoadAllMixes, GetSingleMix, UpdateMix, DeleteMix, CopyMix}