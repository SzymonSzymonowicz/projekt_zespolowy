export const apiDomain = "http://localhost:8080";

// AccountController
export const loginUrl = apiDomain + "/account/login";
export const registerUrl = apiDomain + "/account";

// ExamController
export const examUrl = apiDomain + "/exam";
export const examIdUrl = (groupId) => apiDomain + "/exam/" + groupId;

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
export const groupsNameIdForAccountUrl = (id) => apiDomain + "/groups/name-id/account/" + id;
export const groupsStudentsUrl = apiDomain + "/groups/students";
export const groupByIdUrl = (id) => apiDomain + "/groups/" + id;
export const groupIsUniqueNameUrl = (name) => apiDomain + "/groups/unique?groupName=" + name;
export const createGroupUrl = apiDomain + "/groups";
export const removeStudentFromGroupUrl = (groupId, studentId) => apiDomain + "/groups/" + groupId + "/students/" + studentId;

// LessonController
export const lessonIdUrl = (lessonId) => apiDomain + "/lessons/" + lessonId;
export const createLessonUrl = (groupId) => apiDomain + "/groups/" + groupId + "/lessons";
export const groupIdLessonIdUrl = (groupId, lessonId) => apiDomain + "/groups/" + groupId + "/lessons/" + lessonId;

// ChapterController
export const editChapterUrl = (chapterId) => apiDomain + "/chapters/" + chapterId;
export const createChapterUrl = (lessonId) => apiDomain + "/lessons/" + lessonId + "/chapters";
export const chapterIdUrl = (lessonId, chapterId) => apiDomain + "/lessons/" + lessonId + "/chapters/" + chapterId;