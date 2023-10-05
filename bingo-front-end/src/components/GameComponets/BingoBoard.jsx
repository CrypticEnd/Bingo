import { useEffect } from "react"
import BingoTile from "./BingoTile"

const BingoBoard = ({ board, settings, selectFunction }) => {
    const { boardSize, colorPrim, colorSec, colorSelected } = settings

    const tileSize = 100 / boardSize + "%"

    // Makes text that is cut off scroll on hover
    document.querySelectorAll(".container").forEach(item => {
        if (item.scrollWidth > item.offsetWidth) {
            item.classList.add("scrollable")
        }
    })


    return (
        <>
            {
                boardSize &&
                <div className="bingoBoard">
                    <table style={{ "backgroundColor": "#" + colorPrim }}>
                        <tbody>
                            {
                                board.map((column, columnIndex) =>
                                    <tr key={"col : " + columnIndex}>
                                        {
                                            column.map((tile, rowIndex) =>
                                                <BingoTile value={tile} size={tileSize}
                                                    selectedCol={colorSelected} borderCol={colorSec}
                                                    key={columnIndex * boardSize + rowIndex} id={columnIndex * boardSize + rowIndex}
                                                    onClick={selectFunction}>
                                                </BingoTile>
                                            )
                                        }
                                    </tr>
                                )
                            }
                        </tbody>
                    </table>
                </div>
            }
        </>
    )
}

export default BingoBoard