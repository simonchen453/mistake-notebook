import api from './index'

export interface ChatMessage {
    role: 'user' | 'model'
    content: string
}

export const aiApi = {
    chat: (message: string, history?: ChatMessage[]): Promise<{ response: string }> => {
        return api.post('/ai/chat', { message, history })
    },

    analyzeError: (question: string, correctAnswer: string, wrongAnswer: string): Promise<{ analysis: string }> => {
        return api.post('/ai/analyze-error', { question, correctAnswer, wrongAnswer })
    },

    generateSolution: (question: string): Promise<{ solution: string }> => {
        return api.post('/ai/solution', { question })
    },

    recommendSimilar: (question: string, knowledgePoint: string, count = 3): Promise<{ questions: string }> => {
        return api.post('/ai/similar-questions', { question, knowledgePoint, count })
    },

    generateSuggestion: (subject: string, weakPoints: string[], errorCount: Record<string, number>): Promise<{ suggestion: string }> => {
        return api.post('/ai/study-suggestion', { subject, weakPoints, errorCount })
    }
}
