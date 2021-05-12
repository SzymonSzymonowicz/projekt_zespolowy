import { Paper, TextField, Typography } from '@material-ui/core'
import React from 'react'

export default function OpenTask(props) {
  const [assignedPoints, setAssignedPoints] = React.useState(props.modify ? props.answered[props.index]['gainedPoints'] : null);

  const handleChange = (event) => {
    if(!props.answered.some(item => item['id'] === props.id))
      props.setAnswered(props.answered.concat({id: props.id, answer: event.target.value}))
    else
      props.setAnswered(props.answered.map(item => item['id'] === props.id ? {id: props.id, answer: event.target.value} : item))
  };

  const handlePointsChange = (event) => {
    setAssignedPoints(event.target.value);
    
    props.setAnswered(props.answered.map(item => item['id'] === props.id ? {...item, gainedPoints: event.target.value} : item))
  };

  const pointsString = props.loadValue === true ?  `(${props.answered[props.index]['gainedPoints']} / ${props.points} pkt.)` : `(${props.points} pkt.)`

  const pointsInput = <>
( <TextField inputProps={{style: { textAlign: 'center', width: 40, transform: 'translateY(-4px)' }}} value={assignedPoints} onChange={handlePointsChange}></TextField> {`/ ${props.points} pkt.)`} 
</>

  return (
    <Paper elevation={4} style={{padding: 20}}>
      <Typography>{`Zadanie. ${props.index + 1}   `}{props.modify ? pointsInput : pointsString}</Typography>
      <Typography>{props.instruction}</Typography>
      <TextField
          id="outlined-multiline-static"
          label="Odpowiedź"
          multiline
          rows={4}
          variant="outlined"
          fullWidth
          onChange={handleChange}

          {...(props.loadValue === true && {
            value: props.answered.find(item => item['id'] === props.id)['answer'],
            disabled: true,
            label: null,
          })}
      />
    </Paper>
  )
}
