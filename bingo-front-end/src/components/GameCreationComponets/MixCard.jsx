import { Button } from '@mui/material';

const MixCard = (props) => {
    const { id, owned, name, phrases } = props.value;
    const { selected, buttonSelect, buttonEdit } = props;

    return (
        <div className={selected ? "card selected" : "card"} id={id}>
            <h2>{name}</h2>
            <p>{phrases} phrases</p>

            <Button variant="outlined" onClick={buttonEdit} id={owned ? "EDIT" : "COPY"}>
                {owned ? "Edit" : "Copy+Edit"}
            </Button>
            <Button variant="outlined" onClick={buttonSelect}>
                Select{selected && "ed"}
            </Button>
        </div>
    )
}

export default MixCard