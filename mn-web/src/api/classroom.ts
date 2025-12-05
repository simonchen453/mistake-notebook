import api from './index'

export interface Classroom {
    id?: number
    name: string
    grade?: string
    teacherId: number
    schoolName?: string
    createdAt?: string
}

export interface StudentClass {
    id?: number
    studentId: number
    classId: number
    studentNo?: string
    joinedAt?: string
}

export interface ParentStudent {
    id?: number
    parentId: number
    studentId: number
    relationship?: string
    status?: string
    createdAt?: string
}

export const classroomApi = {
    // Classroom
    list: (teacherId: number): Promise<Classroom[]> =>
        api.get('/classrooms', { params: { teacherId } }),
    getById: (id: number): Promise<Classroom> => api.get(`/classrooms/${id}`),
    create: (data: Classroom): Promise<Classroom> => api.post('/classrooms', data),
    update: (id: number, data: Classroom): Promise<Classroom> => api.put(`/classrooms/${id}`, data),
    delete: (id: number): Promise<void> => api.delete(`/classrooms/${id}`),

    // Students
    listStudents: (classId: number): Promise<StudentClass[]> =>
        api.get(`/classrooms/${classId}/students`),
    addStudent: (classId: number, studentId: number, studentNo?: string): Promise<StudentClass> =>
        api.post(`/classrooms/${classId}/students`, { studentId, studentNo }),
    removeStudent: (classId: number, studentId: number): Promise<void> =>
        api.delete(`/classrooms/${classId}/students/${studentId}`),
}

export const parentApi = {
    // 家长端
    listChildren: (parentId: number): Promise<ParentStudent[]> =>
        api.get('/parent/children', { params: { parentId } }),
    requestBinding: (parentId: number, studentId: number, relationship: string): Promise<ParentStudent> =>
        api.post('/parent/bind', { parentId, studentId, relationship }),
    unbind: (parentId: number, studentId: number): Promise<void> =>
        api.delete('/parent/unbind', { params: { parentId, studentId } }),

    // 学生端
    getPendingBindings: (studentId: number): Promise<ParentStudent[]> =>
        api.get('/parent/pending', { params: { studentId } }),
    approveBinding: (id: number): Promise<void> => api.post(`/parent/approve/${id}`),
    rejectBinding: (id: number): Promise<void> => api.post(`/parent/reject/${id}`),
}
