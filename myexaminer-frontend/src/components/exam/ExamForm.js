import React, { useEffect, useState } from "react";
import { useForm, Controller } from "react-hook-form";
import { Box, Button, FormControl, FormHelperText, InputLabel, MenuItem, Select, TextField } from "@material-ui/core";
import styles from "components/group/group.module.css";
import { examUrl, examIdUrl, groupsNameIdForAccountUrl } from "router/urls";
import authHeader from "services/auth-header";
import CheckIcon from '@material-ui/icons/Check';
import CloseIcon from '@material-ui/icons/Close';
import SunEditor from "suneditor-react";
import 'suneditor/dist/css/suneditor.min.css'; // Import Sun Editor's CSS File
import { isNumeric, isWholeNumber } from "utils/validationUtils";
import { getCurrentAccount } from "services/auth-service";
import { useHistory } from "react-router";
import ExamStateEnum from "./ExamStateEnum";

const buttons = [
  ['undo', 'redo'],
  ['font', 'fontSize', 'formatBlock'],
  ['bold', 'underline', 'italic', 'strike', 'subscript', 'superscript'],
  ['fontColor', 'hiliteColor', 'textStyle'],
  ['removeFormat'],
  "/", // line break
  ['outdent', 'indent'],
  ['align', 'horizontalRule', 'list', 'lineHeight'],
  ['table', 'blockquote', 'link'],
  ['fullScreen', 'showBlocks', 'preview'] //, 'codeView'
];

const fonts = [
  "Arial",
  "Calibri",
  "Comic Sans",
  "Courier",
  "Garamond",
  "Georgia",
  "Impact",
  "Lucida Console",
  "Palatino Linotype",
  "Roboto",
  "Segoe UI",
  "Tahoma",
  "Times New Roman",
  "Trebuchet MS"
];

export default function ExamForm(props) {
  const { examDetails, mode, loadExams, closeModal } = props;
  const examId = examDetails?.id;

  const [lecturerGroups, setLecturerGroups] = useState([]);

  const { control, formState: { errors }, reset, handleSubmit, setValue } = useForm({
    defaultValues: examDetails || {
      name: "",
      availableFrom: null,
      duration: 5,
      description: "",
      groupId: null,
      state: null
    }
  });

  const history = useHistory();

  const getGroups = async () => {
    const account = getCurrentAccount();

    const response = await fetch(groupsNameIdForAccountUrl(account.id), {
      method: "GET",
      headers: authHeader(),
    });

    const data = await response.json();

    setLecturerGroups(data);
  }

  useEffect(() => {
    getGroups();
  },[])

  const createDraftExam = (exam) => {
    fetch(examUrl, {
      method: "POST",
      headers: {
        ...authHeader(),
        "Content-Type": "application/json"
      },
      body: JSON.stringify(exam)
    })
    .then(async res => {
        if (res.status === 200) {
          reset();
          return res.json();
        }
        else {
          console.log("Adding exam failed");
          var msg = await res.text();
          console.log(msg);
          throw new Error("Adding exam failed: " + msg);
        }
    })
    .then(examId => history.push(`/landing/new-exam/${examId}`))
    .catch(err => { console.error(err) })
  }

  const editExamProperties = (exam) => {
    fetch(examIdUrl(examId), {
      method: "PATCH",
      headers: {
        ...authHeader(),
        "Content-Type": "application/json"
      },
      body: JSON.stringify(exam)
    })
    .then(async res => {
        if (res.status === 200) {
          closeModal();
          loadExams();
        }
        else {
          console.log("Editing exam failed");
          var msg = await res.text();
          console.log(msg);
          throw new Error("Editing exam failed: " + msg);
        }
    })
    .catch(err => { console.error(err) })
  }

  let action;
  let submitText;
  
  switch (mode) {
    case 'create':
      action = (exam) => createDraftExam(exam);
      submitText = "Utwórz Egzamin";
      break;
    case 'edit':
      action = (exam) => editExamProperties(exam);
      submitText = "Edytuj Egzamin";
      break;
    default:
      action = (exam) => {
        console.log("Form type not provided, performing default action:");
        console.log(exam);
      }
      submitText = "Zatwierdź";
  }

  return (
    <form onSubmit={handleSubmit(action)} className={styles.chapterForm}>
      <Box className={styles.lessonInputBox}>
        <Controller
          control={control}
          name="name"
          render={({ field }) =>
            <TextField
              variant="outlined"
              label="Tytuł egzaminu"
              error={errors.name ? true : false}
              fullWidth
              helperText={ errors.name ? errors.name?.message : null }
              {...field}
            />
          }
          rules={{
            required: "Wypełnij to pole",
          }}
        />

        <Controller
          control={control}
          name="groupId"
          render={({ field:{ value, ...rest } }) =>
            <FormControl error={errors.groupId ? true : false} fullWidth>
              <InputLabel id="select-exam-group-label" variant="outlined">Grupa</InputLabel>
              <Select
                variant="outlined"
                id="test"
                labelId="select-exam-group-label"
                label="Grupa"
                value={value || ''}
                renderValue={value => lecturerGroups?.find(dto => dto.id === value)?.name}
                {...rest}
              >
                {lecturerGroups && lecturerGroups.map(group => {
                  return <MenuItem key={`group#${group.id}`} value={group.id}>{group.name}</MenuItem>;
                })}
              </Select>
              <FormHelperText>{ errors.groupId ? errors.groupId?.message : null }</FormHelperText>
            </FormControl>
          }
          rules={{
            required: "Wypełnij to pole",
          }}
        />

        {mode === "edit" &&
          <Controller
            control={control}
            name="state"
            render={({ field: { value, ...rest } }) =>
              <FormControl error={errors.groupId ? true : false} fullWidth>
                <InputLabel id="select-exam-state-label" variant="outlined">Stan egzaminu</InputLabel>
                <Select
                  variant="outlined"
                  id="state-select"
                  labelId="select-exam-state-label"
                  label="Stan Egzaminu"
                  value={value || ''}
                  style={{ fontWeight: "bold", color: ExamStateEnum[value]?.color }}
                  {...rest}
                >
                  {Object.values(ExamStateEnum).map(state => (
                    <MenuItem key={`state#${state.name}`} value={state.name} style={{ fontWeight: "bold", color: state?.color }}>
                      {state.name}
                    </MenuItem>
                  ))}
                </Select>
                <FormHelperText>{errors.groupId ? errors.groupId?.message : null}</FormHelperText>
              </FormControl>
            }
            rules={{
              required: "Wypełnij to pole",
            }}
          />
        }

        <Controller 
          control={control}
          name="availableFrom"
          render={({ field:{value, ...other} }) =>
            <TextField
              variant="outlined"
              label="Data Egzaminu"
              type="datetime-local"
              error={errors.availableFrom ? true : false}
              fullWidth
              helperText={ errors.availableFrom ? errors.availableFrom?.message : null }
              InputLabelProps={{ shrink: true }}
              value={value || ""}
              {...other}
            />
          }
          rules={{
            required: "Wypełnij to pole",
          }}
        />

        <Controller
          control={control}
          name="duration"
          render={({ field: { onChange, ...rest } }) =>      
            <TextField
              variant="outlined"
              label="Czas (min.)"
              fullWidth
              type="number"
              inputProps={{ step: 5 }}
              error={errors.duration ? true : false}
              helperText={errors.duration ? errors.duration?.message : null}
              onChange={(event) => {
                var num = event.target.value;
                onChange(num);
                setValue("duration", num, { shouldValidate: true });
              } }
              {...rest}
            />}
          rules={{
            required: "Wypełnij to pole",
            min: {
              value: 5,
              message: "Czas egzaminu musi być dodatni i równy minimum 5 minut"
            },
            validate: {
              numeric: v => isNumeric(v) || "Podana wartość musi być liczbą",
              wholeNumber: v => isWholeNumber(v) || "Czas egzaminu muszi być liczbą całkowitą"
            }
          }}
        />
      
        <Controller
          control={control}
          name="description"
          render={({
            field: { value, ref, ...rest }
          }) => {
            return (
            <>
              <SunEditor
                setOptions={{
                  buttonList: buttons,
                  font: fonts,
                  imageFileInput: false
                }}
                lang="pl"
                placeholder="Uzupełnij opis egzaminu"
                setDefaultStyle="font-family: roboto; font-size: 16px;"
                defaultValue={value}
                {...rest}
              />
            </>)
          }}
          rules={{
            required: "Wypełnij opis egzaminu!",
            validate: value => value !== null || "Opis egzaminu nie może być pusty"
          }}
        />
        { errors.description && <span style={{color: "#f44336", marginLeft: "14px", top: "-20px", position: "relative"}} className="MuiFormHelperText-root">{errors.description.message}</span> }
      </Box>
      <Box display="flex" justifyContent="flex-end">
        <Button color="primary" type="submit" variant="contained" startIcon={<CheckIcon />}>
          {submitText}
        </Button >
        {mode === "edit" &&
          <Button color="secondary" variant="contained" startIcon={<CloseIcon />} onClick={ closeModal } style={{marginLeft: "30px"}}>
            Anuluj
          </Button >
        }
      </Box>
    </form>
  );
}
