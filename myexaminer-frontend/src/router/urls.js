export const apiDomain = "http://localhost:8080";

// AccountController
export const loginUrl = apiDomain + "/account/login";
export const registerUrl = apiDomain + "/account";

// ExamController
export const examUrl = apiDomain + "/exam/";

// ArchiveController
export const archiveExcercisesUrl = apiDomain + "/archive/exercises";
export const archiveCheckUrl = apiDomain + "/archive/check";
export const checkedExercisesForExamIdUrl = apiDomain + "/archive/exercises?examId=";
export const individualExamExercisesUrl = apiDomain + "/archive/exercises?individualExamId=";

// ExercisesController
export const exercisesUrl = apiDomain + "/exercises/";

// NotebookController
export const notebookUrl = apiDomain + "/notebook";

// IndividualExamsController
export const individualExamsForLecturerGroupsUrl = apiDomain + "/individual-exams/lecturer-groups";

// TeachingGroupController
export const groupsForAccountUrl = (id) => apiDomain + "/groups/account/" + id;
export const groupsStudentsUrl = apiDomain + "/groups/students";
export const groupByIdUrl = (id) => apiDomain + "/groups/" + id;