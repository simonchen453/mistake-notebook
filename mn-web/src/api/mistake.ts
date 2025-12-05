import api from './index'

export interface Mistake {
    id?: number
    userId: number
    subjectId: number
    chapterId?: number
    knowledgePointId?: number
    questionContent: string
    questionImages?: string
    correctAnswer?: string
    myAnswer?: string
    errorReason?: string
    errorReasonDetail?: string
    difficulty?: number
    masteryStatus?: string
    masteryLevel?: number
    source?: string
    sourceName?: string
    createdAt?: string
    updatedAt?: string
}

export interface PageResult<T> {
    content: T[]
    totalElements: number
    totalPages: number
    number: number
    size: number
}

export const mistakeApi = {
    list: (userId: number, page = 0, size = 10, subjectId?: number): Promise<PageResult<Mistake>> => {
        const params: Record<string, any> = { userId, page, size }
        if (subjectId) params.subjectId = subjectId
        return api.get('/mistakes', { params })
    },

    getById: (id: number): Promise<Mistake> => {
        return api.get(`/mistakes/${id}`)
    },

    create: (data: Mistake): Promise<Mistake> => {
        return api.post('/mistakes', data)
    },

    update: (id: number, data: Mistake): Promise<Mistake> => {
        return api.put(`/mistakes/${id}`, data)
    },

    delete: (id: number): Promise<void> => {
        return api.delete(`/mistakes/${id}`)
    },

    updateMastery: (id: number, status: string, level: number): Promise<void> => {
        return api.patch(`/mistakes/${id}/mastery`, null, { params: { status, level } })
    },

    analyze: (id: number): Promise<{ analysis: string }> => {
        return api.get(`/mistakes/${id}/analyze`)
    },

    getSolution: (id: number): Promise<{ solution: string }> => {
        return api.get(`/mistakes/${id}/solution`)
    },

    getReviewList: (userId: number): Promise<Mistake[]> => {
        return api.get('/mistakes/review', { params: { userId } })
    },

    getStats: (userId: number): Promise<{
        bySubject: [number, number][]
        byErrorReason: [string, number][]
    }> => {
        return api.get('/mistakes/stats', { params: { userId } })
    }
}
