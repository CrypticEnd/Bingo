import { useState } from "react"
import { useEffect } from "react"



const BingoTile = (props) => {
    const { phrase, color, font, selected } = props.value
    const { size, selectedCol, borderCol, id, onClick } = props
    let tdStyle =
    {
        "borderColor": "#" + borderCol,
        "height": size,
        "width": size,
        "maxWidth": size,
        "maxHeight": size,
        "color": "#" + color,
        "fontFamily": font
    }

    const isSelected = () => {
        if (selected) {
            tdStyle = {
                ...tdStyle,
                "backgroundColor": "#" + selectedCol
            };
        }
    }
    isSelected()

    return (
        <td style={tdStyle} onClick={onClick} id={id}>
            <div className="container" id={id}>{phrase}</div>
        </td>
    )
}

export default BingoTile