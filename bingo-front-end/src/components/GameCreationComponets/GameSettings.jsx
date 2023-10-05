import { FormControl, FormHelperText, InputLabel, TextField, Select, MenuItem, FormControlLabel, Switch, Box, Button } from '@mui/material';
import { useState } from 'react';

const GameSettings = (props) => {
    const { onSave, onHost } = props;

    // Form feilds
    const [gameName, setGameName] = useState(props.name)
    const [boardSize, setBoardSize] = useState(props.size)
    const [centerFree, setCenterFree] = useState(props.isCenterFree)
    const [allSameWord, setAllSameWord] = useState(props.isAllSameWord)
    const [colorPrimary, setColorPrimary] = useState( props.colorPrimary)
    const [colorSecondary, setColorSecondary] = useState( props.colorSecondary)
    const [colorSelected, setColorSelected] = useState(props.colorSelected)

    const checkVaildText = (event) => {
        const value = event.target.value;
        const invaild = !value && value.trim().length === 0
        const alreadyInvalid = event.target.parentElement.classList.contains("Mui-error")
        setGameName(value)

        if (invaild === alreadyInvalid)
            return

        if (invaild) {
            event.target.parentElement.classList.add("Mui-error")
            return
        }

        event.target.parentElement.classList.remove("Mui-error")
    }

    const buttonPress = (type) => {
        if (!gameName && gameName.trim().length === 0) {
            console.log("Game name invaild")
            return
        }

        const settings = {
            gameName: gameName,
            boardSize: boardSize,
            centerFree: centerFree,
            allSameWord: allSameWord,
            colorPrimary: colorPrimary,
            colorSecondary: colorSecondary,
            colorSelected: colorSelected
        }

        if (type === "SAVE")
            onSave(settings)
        else if (type === "HOST")
            onHost(settings)
    }

    return (
        <div className='GameSettings'>
            <FormControl fullWidth={true} required={true} variant='outlined'>
                <TextField
                    id='name-input'
                    label="Game Name"
                    aria-describedby='name-helper-text'
                    hiddenLabel={true}
                    value={gameName}
                    onChange={checkVaildText}
                />
                <FormHelperText id='name-helper-text'>Name of game is visable to players</FormHelperText>

            </FormControl>

            <FormControl fullWidth={true} required={true} variant='outlined'>
                <InputLabel htmlFor='board-size-input' id="board-size-lable">Board Size</InputLabel>
                <Select
                    id='board-size-input'
                    labelId='board-size-lable'
                    label="Board Size"
                    value={boardSize}
                    onChange={(e) => setBoardSize(e.target.value)}>

                    <MenuItem value={3}>3 X 3</MenuItem>
                    <MenuItem value={5}>5 X 5</MenuItem>
                    <MenuItem value={7}>7 X 7</MenuItem>
                    <MenuItem value={9}>9 X 9</MenuItem>
                </Select>

                <FormControlLabel
                    label="Is Center Free"
                    labelPlacement="top"
                    control={
                        <Switch
                            name='centerFreeInput'
                            checked={centerFree}
                            onChange={(e) => setCenterFree(e.target.checked)}
                        />}
                />

                <FormControlLabel
                    label="Players have the same pharses"
                    labelPlacement="top"
                    control={
                        <Switch
                            name='allSameWordInput'
                            checked={allSameWord}
                            onChange={(e) => setAllSameWord(e.target.checked)}
                        />}
                />

                <TextField
                    label="Gameboard Primary Color"
                    value={'#'+colorPrimary}
                    type={"color"}
                    onChange={(e) => setColorPrimary(e.target.value.substring(1))}
                />

                <TextField
                    label="Gameboard Secondary Color"
                    value={'#'+colorSecondary}
                    type={"color"}
                    onChange={(e) => setColorSecondary(e.target.value.substring(1))}
                />

                <TextField
                    label="Gameboard Selected Color"
                    value={'#'+colorSelected}
                    type={"color"}
                    onChange={(e) => setColorSelected(e.target.value.substring(1))}
                />

                <Button variant='contained' onClick={(e) => { buttonPress('SAVE') }}>Save</Button>
                <Button variant='contained' onClick={(e) => { buttonPress('HOST') }}>Host</Button>

            </FormControl>
        </div>
    )
}

export default GameSettings