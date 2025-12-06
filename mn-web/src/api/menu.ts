import api from './axios';

export interface BackendMenuItem {
    index: string;
    title: string;
    icon: string;
    url: string;
    subs: BackendMenuItem[];
    data?: unknown;
}

export interface ApiResponse<T> {
    restCode?: string;
    message?: string;
    data?: T;
    success?: boolean;
}

export const getMenuList = async (): Promise<BackendMenuItem[]> => {
    try {
        const response = await api.get<ApiResponse<BackendMenuItem[]>>('/common/menus');
        return Array.isArray(response) ? response : (response.data || []);
    } catch (error) {
        console.error('获取菜单列表失败:', error);
        return [];
    }
};
