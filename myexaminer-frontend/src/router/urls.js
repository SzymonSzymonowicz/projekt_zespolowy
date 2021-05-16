export const domain = "http://localhost:8080";

// AccountController
export const loginUrl = domain + "/account/login";
export const registerUrl = domain + "/account";

// ExamController
export const examUrl = domain + "/exam/";

// ArchiveController
export const archiveExcercisesUrl = domain + "/archive/exercises";
export const archiveCheckUrl = domain + "/archive/check";
export const checkedExercisesForExamIdUrl = domain + "/archive/exercises?examId=";
export const individualExamExercisesUrl = domain + "/archive/exercises?individualExamId=";

// ExercisesController
export const exercisesUrl = domain + "/exercises/";

// NotebookController
export const notebookUrl = domain + "/notebook";

// IndividualExamsController
export const individualExamsForLecturerGroupsUrl = domain + "/individual-exams/lecturer-groups";

// TeachingGroupController
export const groupsForAccountUrl = (id) => domain + "/groups/account/" + id;
export const groupsStudentsUrl = domain + "/groups/students";