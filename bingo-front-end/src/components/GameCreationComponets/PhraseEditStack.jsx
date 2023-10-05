import { Stack, TextField, Select, MenuItem, FormGroup, FormControl, Tooltip } from "@mui/material"
import { useState } from "react";
import { styled } from "@mui/system";
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';

// Add phrase should only be set for global settings
const PharseEditStack = ({ value, onChange, index, addPhrase }) => {
    const { phrase, color, font } = value

    const updateCol = (event) => {
        let updated = value;
        value.color = event.target.value.substring(1);
        onChange(index, updated)
    }

    const updatefont = (event) => {
        let updated = value;
        value.font = event.target.value;
        onChange(index, updated)
    }

    const updatePhrase = (event) => {
        let updated = value;
        value.phrase = event.target.value;
        onChange(index, updated)
    }

    const removePhrase = (event) => {
        onChange(index, null)
    }

    return (
        <FormControl fullWidth={true}>
            <Stack direction="row" spacing={0} style={{display: 'flex', alignItems:'center'}}>
                <TextField
                    style={{ width: 100 }}
                    value={'#' + color}
                    type={"color"}
                    onChange={updateCol}
                />

                <Select
                    value={font}
                    style={{ width: 360 }}
                    onChange={updatefont}
                >
                    <MenuItem value="Arial">Arial</MenuItem>
                    <MenuItem value="Verdana">Verdana</MenuItem>
                    <MenuItem value="Tahoma">Tahoma</MenuItem>
                    <MenuItem value="Times New Roman">Times New Roman</MenuItem>
                    <MenuItem value="Georgia">Georgia</MenuItem>
                </Select>

                <TextField value={phrase} onChange={updatePhrase} fullWidth={true}
                    disabled={Boolean(addPhrase)}
                />

                {addPhrase ?
                    <Tooltip title="Add Phrase">
                        <AddIcon fontSize="large" onClick={addPhrase} />
                    </Tooltip>
                    :
                    <Tooltip title="Remove Phrase">
                        <DeleteIcon fontSize="large" onClick={removePhrase} />
                    </Tooltip>
                }
            </Stack>
        </FormControl>
    )
}

export default PharseEditStack