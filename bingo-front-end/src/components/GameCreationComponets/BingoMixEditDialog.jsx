import { Dialog, DialogContent, FormControl, Button, DialogActions, InputLabel, TextField, FormGroup, Tooltip, Hidden } from "@mui/material"
import DeleteIcon from '@mui/icons-material/Delete';

import Stack from '@mui/material/Stack';
import Paper from '@mui/material/Paper';
import { useState } from "react";
import PharseEditStack from "./PhraseEditStack";
import { DeleteMix, UpdateMix } from "../../functions/BingoMixes";



const BingoMixEditDialog = ({ display, onClose, mix, reload }) => {
    const defaultPhrase = {
        color: "000000",
        font: "Arial",
        phrase: "new phrase"
    }

    const [name, setName] = useState(mix.name);
    const [phrases, setPhrases] = useState(mix.phrases);
    const [bulkPhrases, setBulkPhrases] = useState("");
    const [gloablSettings, setGloablSettings] = useState({
        ...defaultPhrase,
        phrase: "GLOBAL"
    })



    const handleSubmit = (event) => {
        // Collect bulk phrases into phrases
        let allPhrases = [...phrases]
        let phraseArray = bulkPhrases.split("\n")

        phraseArray.forEach(p => {
            if (p.trim().length != 0) {
                allPhrases = [{ ...defaultPhrase, phrase: p }, ...allPhrases]
            }
        });

        const updatedMix = { ...mix, name: name, phrases: allPhrases }

        UpdateMix(updatedMix, onClose, reload)
    }

    const deleteMix = (event) => {
        DeleteMix(mix.id, onClose, reload)
    }

    const changeGlobal = (index, value) => {
        setGloablSettings({ ...value })
    }

    const changePhrase = (index, value) => {
        let allPhrases = [...phrases]

        // remove value
        if (value === null) {
            allPhrases.splice(index, 1)
        }
        // Only want to update
        else {
            allPhrases[index] = value;
        }
        setPhrases(allPhrases)
    }

    const addPhrase = () => {
        setPhrases([defaultPhrase, ...phrases])
    }

    return (
        <Dialog open={display} >
            <DialogContent >

                <Stack spacing={2}>
                    <Stack direction="row" spacing={0} style={{ display: 'flex', alignItems: 'center' }} >
                        <TextField
                            label="Mix Name"
                            fullWidth={true}
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                        />

                        <Tooltip title="Delete Mix">
                            <DeleteIcon fontSize="large" onClick={deleteMix} />
                        </Tooltip>

                    </Stack>

                    <PharseEditStack value={gloablSettings} index={0} onChange={changeGlobal} addPhrase={addPhrase} />

                    {phrases.map((phrase, index) =>
                        <PharseEditStack key={index} value={phrase} index={index} onChange={changePhrase} />
                    )}

                    <TextField
                        label="Bulk Workds"
                        fullWidth={true}
                        placeholder="Enter phrases per line. Will use golbal settings!"
                        multiline={true}
                        value={bulkPhrases}
                        onChange={(e) => setBulkPhrases(e.target.value)}
                    />
                </Stack>

            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>Cancel</Button>
                <Button onClick={handleSubmit}>Save</Button>
            </DialogActions>
        </Dialog>
    )
}

export default BingoMixEditDialog