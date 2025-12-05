import { create } from 'zustand';
import { getToken, setToken, removeToken, getUser, setUser, removeUser } from '../utils/auth';

interface AuthState {
    token: string | null;
    user: any | null;
    isAuthenticated: boolean;
    login: (token: string, user: any) => void;
    logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
    token: getToken(),
    user: getUser(),
    isAuthenticated: !!getToken(),
    login: (token, user) => {
        setToken(token);
        setUser(user);
        set({ token, user, isAuthenticated: true });
    },
    logout: () => {
        removeToken();
        removeUser();
        set({ token: null, user: null, isAuthenticated: false });
    },
}));
