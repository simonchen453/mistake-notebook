import { useNavigate, useLocation } from 'react-router-dom';
import { useAuthStore } from '../context/authStore';
import { useEffect } from 'react';

export const AuthGuard = ({ children }: { children: JSX.Element }) => {
    const { isAuthenticated } = useAuthStore();
    const navigate = useNavigate();
    const location = useLocation();

    useEffect(() => {
        if (!isAuthenticated) {
            navigate('/login', { state: { from: location } });
        }
    }, [isAuthenticated, navigate, location]);

    if (!isAuthenticated) {
        return null;
    }

    return children;
};
