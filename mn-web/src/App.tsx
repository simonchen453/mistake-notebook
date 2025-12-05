import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import Layout from './pages/Layout'
import MistakeList from './pages/Mistake/MistakeList'
import MistakeForm from './pages/Mistake/MistakeForm'
import AIChat from './pages/AI/AIChat'
import Review from './pages/Review/Review'
import Statistics from './pages/Statistics/Statistics'
import SubjectManagement from './pages/Admin/SubjectManagement'
import ClassManagement from './pages/Teacher/ClassManagement'
import Login from './pages/Login'
import { AuthGuard } from './components/AuthGuard'

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/" element={
                    <AuthGuard>
                        <Layout />
                    </AuthGuard>
                }>
                    <Route index element={<Navigate to="/mistakes" replace />} />
                    <Route path="mistakes" element={<MistakeList />} />
                    <Route path="mistakes/new" element={<MistakeForm />} />
                    <Route path="mistakes/:id" element={<MistakeForm />} />
                    <Route path="ai" element={<AIChat />} />
                    <Route path="review" element={<Review />} />
                    <Route path="statistics" element={<Statistics />} />
                    <Route path="admin/subjects" element={<SubjectManagement />} />
                    <Route path="teacher/classes" element={<ClassManagement />} />
                </Route>
            </Routes>
        </BrowserRouter>
    )
}

export default App
