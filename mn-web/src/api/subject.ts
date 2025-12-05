import api from './index'

export interface Subject {
    id?: number
    name: string
    icon?: string
    sortOrder?: number
}

export interface Chapter {
    id?: number
    subjectId: number
    parentId?: number
    name: string
    sortOrder?: number
}

export interface KnowledgePoint {
    id?: number
    chapterId: number
    name: string
    description?: string
}

export const subjectApi = {
    // Subject
    listSubjects: (): Promise<Subject[]> => api.get('/subjects'),
    getSubject: (id: number): Promise<Subject> => api.get(`/subjects/${id}`),
    createSubject: (data: Subject): Promise<Subject> => api.post('/subjects', data),
    updateSubject: (id: number, data: Subject): Promise<Subject> => api.put(`/subjects/${id}`, data),
    deleteSubject: (id: number): Promise<void> => api.delete(`/subjects/${id}`),

    // Chapter
    listChapters: (subjectId: number): Promise<Chapter[]> => api.get(`/subjects/${subjectId}/chapters`),
    createChapter: (data: Chapter): Promise<Chapter> => api.post('/subjects/chapters', data),
    updateChapter: (id: number, data: Chapter): Promise<Chapter> => api.put(`/subjects/chapters/${id}`, data),
    deleteChapter: (id: number): Promise<void> => api.delete(`/subjects/chapters/${id}`),

    // KnowledgePoint
    listKnowledgePoints: (chapterId: number): Promise<KnowledgePoint[]> =>
        api.get(`/subjects/chapters/${chapterId}/knowledge-points`),
    createKnowledgePoint: (data: KnowledgePoint): Promise<KnowledgePoint> =>
        api.post('/subjects/knowledge-points', data),
    updateKnowledgePoint: (id: number, data: KnowledgePoint): Promise<KnowledgePoint> =>
        api.put(`/subjects/knowledge-points/${id}`, data),
    deleteKnowledgePoint: (id: number): Promise<void> => api.delete(`/subjects/knowledge-points/${id}`),
}
