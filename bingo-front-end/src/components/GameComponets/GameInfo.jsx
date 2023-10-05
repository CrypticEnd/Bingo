import HoastButtons from "./HostButtons"

const GameInfo = (props) => {
    const { boardSetting, winner, endHoast } = props
    const { gameName, hoastName, gamePin, mixName, isCenterFree, isAllSameWords, hoast } = boardSetting

    return (
        <div className="GameInfo">
            {hoast && <HoastButtons gamePin={gamePin} endHoast={endHoast} />}

            <div className="InfoBox">
                <h1>{gamePin}</h1>
                <h2>{gameName}</h2>
                {winner !== null && <p>Game won by: <u>{winner}</u></p>}
                <p>Hoasted by: {hoastName}</p>
                <p>Bingo Mix: {mixName}</p>
                {isCenterFree && <p>Center is free! You're off to an easy start</p>}
                {isAllSameWords && <p>Everyone has the same pharses</p>}
            </div>
        </div>
    )
}

export default GameInfo