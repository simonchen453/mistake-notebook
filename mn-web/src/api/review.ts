import api from './index'

export interface ReviewRecord {
    id?: number
    mistakeId: number
    userId: number
    reviewResult?: string
    reviewTime?: string
    nextReviewTime?: string
    reviewCount?: number
    reviewStage?: number
}

export interface ReviewStats {
    totalMistakes: number
    totalReviews: number
    mastered: number
    dueToday: number
}

export const reviewApi = {
    getDueReviews: (userId: number): Promise<ReviewRecord[]> =>
        api.get('/review/due', { params: { userId } }),

    countDueReviews: (userId: number): Promise<{ count: number }> =>
        api.get('/review/due/count', { params: { userId } }),

    createPlan: (mistakeId: number, userId: number): Promise<ReviewRecord> =>
        api.post('/review/plan', { mistakeId, userId }),

    recordReview: (mistakeId: number, userId: number, correct: boolean): Promise<ReviewRecord> =>
        api.post('/review/record', { mistakeId, userId, correct }),

    getStats: (userId: number): Promise<ReviewStats> =>
        api.get('/review/stats', { params: { userId } }),

    getAllRecords: (userId: number): Promise<ReviewRecord[]> =>
        api.get('/review/records', { params: { userId } }),
}
