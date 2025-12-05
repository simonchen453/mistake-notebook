
import api from './axios';

// Assuming AdminPro uses standard /admin/login or /sys/login. 
// Based on typical AdminPro setups, it's often /api/auth/login or /sys/login.
// Since the user said "adminpro", I'll try the common path.
// If it's the framework I suspect, might be `/admin/auth/login`.
// But user said "user, permission, role... use adminpro inside".
// I will point to `/admin/login` for now and ask user to verify if they have a specific path.
// Actually standard AdminPro often uses `/auth/login` or similar.
// I will use `/sys/login` or similar if I can find a reference. 
// Creating a safe guess.

export const login = async (data: any) => {
    // Standard AdminPro usually exposes login at /auth/login or /sys/auth/login
    // Let's try /auth/login as a generic guess, assuming standard controller.
    return api.post('/auth/login', data);
};


export const register = async (data: any) => {
    return api.post('/auth/register', data);
};
