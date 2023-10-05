import { useEffect, useState } from "react"
import MixCard from "./MixCard";
import BingoMixEditDialog from "./BingoMixEditDialog";
import { CopyMix, GetSingleMix, LoadAllMixes } from "../../functions/BingoMixes";


const BingoMixes = ({ userId, mixId, setMixId, SetPhraseCount }) => {
    const [mixes, setMixes] = useState([])
    const [mixEdit, setMixEdit] = useState({});

    const loadData = () => {
        LoadAllMixes(userId, setMixes)
    }
    useEffect(loadData, [])

    const closeMixEditDialog = () => {
        setMixEdit({})
    }

    const createMix = () => {
        console.log("create")
        setMixEdit({
            name: "New Bingo Mix",
            phrases: [],
            owner: null
        })
    }

    const selectButton = (event) => {
        const id = event.target.parentElement.id

        setMixId(parseInt(id))

    }

    const editButton = (event) => {
        // Need to check what type of edit it is
        const type = event.target.id
        const id = event.target.parentElement.id

        if (type === "COPY") {
            CopyMix(id, setMixEdit, loadData)
        }
        else {
            GetSingleMix(id, setMixEdit)
        }
    }


    return (
        <>
            {Object.keys(mixEdit).length >= 3 &&
                <BingoMixEditDialog onClose={closeMixEditDialog} display={true}
                    mix={mixEdit} reload={loadData} />
            }
            <div className="BingoMixes">

                <h1>Pharse Mixes</h1>
                <div className="card" id="create" onClick={createMix}>
                    <h2>Create</h2>
                </div>

                {mixes.map(mix =>

                    <MixCard key={mix.id} value={mix} selected={mix.id === mixId} buttonSelect={selectButton} buttonEdit={editButton} />

                )}
            </div>
        </>
    )
}

export default BingoMixes