import { useState, useEffect } from 'react'
import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { Layout as AntLayout, Menu, Dropdown, Avatar, Space } from 'antd'
import {
    BookOutlined,
    RobotOutlined,
    SyncOutlined,
    BarChartOutlined,
    TeamOutlined,
    SettingOutlined,
    UserOutlined,
    LogoutOutlined,
    FileTextOutlined,
    PlusOutlined,
    ReloadOutlined,
    AppstoreOutlined,
    HomeOutlined
} from '@ant-design/icons'
import { useAuthStore } from '../context/authStore'
import { getMenuList, type BackendMenuItem } from '../api/menu'

const { Header, Sider, Content } = AntLayout

interface MenuItem {
    key: string;
    icon?: React.ReactNode;
    label: string;
    path?: string;
    children?: MenuItem[];
    type?: 'divider';
}

export default function Layout() {
    const [collapsed, setCollapsed] = useState(false)
    const [menuItems, setMenuItems] = useState<MenuItem[]>([])
    const [openKeys, setOpenKeys] = useState<string[]>([])
    const [selectedKeys, setSelectedKeys] = useState<string[]>([])
    const navigate = useNavigate()
    const location = useLocation()
    const { user, logout } = useAuthStore()

    const iconComponents: Record<string, React.ReactNode> = {
        'BookOutlined': <BookOutlined />,
        'RobotOutlined': <RobotOutlined />,
        'ReloadOutlined': <ReloadOutlined />,
        'BarChartOutlined': <BarChartOutlined />,
        'TeamOutlined': <TeamOutlined />,
        'SettingOutlined': <SettingOutlined />,
        'FileTextOutlined': <FileTextOutlined />,
        'PlusOutlined': <PlusOutlined />,
        'AppstoreOutlined': <AppstoreOutlined />,
        'HomeOutlined': <HomeOutlined />,
        'UserOutlined': <UserOutlined />,
    }

    const isImg = (icon: string): boolean => {
        return Boolean(icon && (icon.endsWith('.png') || icon.endsWith('.jpg') || icon.endsWith('.jpeg') || icon.endsWith('.gif')))
    }

    const getIconComponent = (iconName: string) => {
        if (isImg(iconName)) {
            return null
        }
        return iconComponents[iconName] || <BookOutlined />
    }

    const cleanUrl = (url: string): string | null => {
        if (!url || url === 'null' || url.startsWith('null?')) {
            return null
        }
        return url.split('?')[0]
    }

    const convertMenuItems = (backendMenus: BackendMenuItem[]): MenuItem[] => {
        return backendMenus.map(menu => {
            const cleanedUrl = cleanUrl(menu.url)
            const menuItem: MenuItem = {
                key: menu.index,
                label: menu.title,
                path: cleanedUrl || undefined,
            }

            if (menu.icon) {
                if (isImg(menu.icon)) {
                    menuItem.icon = <img src={menu.icon} alt={menu.title} style={{ width: 16, height: 16 }} />
                } else {
                    menuItem.icon = getIconComponent(menu.icon)
                }
            }

            if (menu.subs && menu.subs.length > 0) {
                menuItem.children = convertMenuItems(menu.subs)
            }

            return menuItem
        })
    }

    const findMenuItemByPath = (items: MenuItem[], path: string): MenuItem | null => {
        for (const item of items) {
            if (item.path === path) {
                return item
            }
            if (item.children) {
                const found = findMenuItemByPath(item.children, path)
                if (found) return found
            }
        }
        return null
    }

    const getParentKeys = (items: MenuItem[], targetKey: string, parentKeys: string[] = []): string[] | null => {
        for (const item of items) {
            if (item.key === targetKey) {
                return parentKeys
            }
            if (item.children) {
                const found = getParentKeys(item.children, targetKey, [...parentKeys, item.key])
                if (found !== null) {
                    return found
                }
            }
        }
        return null
    }

    useEffect(() => {
        const loadMenus = async () => {
            try {
                const backendMenus = await getMenuList()
                const convertedMenus = convertMenuItems(backendMenus)
                if (import.meta.env.DEV) {
                    console.log('原始菜单数据:', backendMenus)
                    console.log('转换后菜单数据:', convertedMenus)
                }
                setMenuItems(convertedMenus)
            } catch (error) {
                console.error('加载菜单失败:', error)
                setMenuItems([
                    { key: '/mistakes', icon: <BookOutlined />, label: 'Mistakes', path: '/mistakes' },
                    { key: '/ai', icon: <RobotOutlined />, label: 'AI Assistant', path: '/ai' },
                    { key: '/review', icon: <SyncOutlined />, label: 'Review Center', path: '/review' },
                    { key: '/statistics', icon: <BarChartOutlined />, label: 'Statistics', path: '/statistics' },
                    { type: 'divider' as const },
                    { key: '/teacher/classes', icon: <TeamOutlined />, label: 'Class Mgmt', path: '/teacher/classes' },
                    { key: '/admin/subjects', icon: <SettingOutlined />, label: 'Subjects', path: '/admin/subjects' },
                ])
            }
        }
        loadMenus()
    }, [])

    const userMenu = [
        {
            key: 'logout',
            icon: <LogoutOutlined />,
            label: 'Sign Out',
            onClick: () => {
                logout();
                navigate('/login');
            }
        }
    ];

    useEffect(() => {
        if (menuItems.length === 0) return

        const currentPath = location.pathname
        const matchedMenuItem = findMenuItemByPath(menuItems, currentPath)

        if (matchedMenuItem) {
            setSelectedKeys([matchedMenuItem.key])
            const parentKeys = getParentKeys(menuItems, matchedMenuItem.key)
            if (parentKeys && parentKeys.length > 0 && !collapsed) {
                setOpenKeys(parentKeys)
            }
        } else {
            setSelectedKeys([])
        }
    }, [location.pathname, menuItems, collapsed])

    useEffect(() => {
        if (collapsed) {
            setOpenKeys([])
        } else {
            if (menuItems.length > 0) {
                const currentPath = location.pathname
                const matchedMenuItem = findMenuItemByPath(menuItems, currentPath)
                if (matchedMenuItem) {
                    const parentKeys = getParentKeys(menuItems, matchedMenuItem.key)
                    if (parentKeys && parentKeys.length > 0) {
                        setOpenKeys(parentKeys)
                    }
                }
            }
        }
    }, [collapsed, menuItems, location.pathname])

    return (
        <AntLayout style={{ minHeight: '100vh', background: '#f0f2f5' }}>
            <Sider
                collapsible
                collapsed={collapsed}
                onCollapse={setCollapsed}
                width={260}
                style={{
                    background: '#fff',
                    boxShadow: '4px 0 16px rgba(0,0,0,0.02)',
                    zIndex: 10
                }}
            >
                <div style={{
                    height: 80,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                    marginBottom: 16
                }}>
                    <div style={{
                        color: 'white',
                        fontSize: collapsed ? 24 : 20,
                        fontWeight: 700,
                        display: 'flex',
                        alignItems: 'center',
                        gap: 8,
                        transition: 'all 0.3s'
                    }}>
                        <span style={{ fontSize: 24 }}>📝</span>
                        {!collapsed && 'MistakeNB'}
                    </div>
                </div>
                <Menu
                    mode="inline"
                    selectedKeys={selectedKeys}
                    openKeys={openKeys}
                    items={menuItems.map(item => ({
                        key: item.key,
                        icon: item.icon,
                        label: item.label,
                        type: item.type,
                        children: item.children?.map(child => ({
                            key: child.key,
                            icon: child.icon,
                            label: child.label,
                            children: child.children?.map(subChild => ({
                                key: subChild.key,
                                icon: subChild.icon,
                                label: subChild.label,
                            }))
                        }))
                    }))}
                    onClick={({ key }) => {
                        const findMenuItem = (items: MenuItem[], targetKey: string): MenuItem | null => {
                            for (const item of items) {
                                if (item.key === targetKey) {
                                    return item
                                }
                                if (item.children) {
                                    const found = findMenuItem(item.children, targetKey)
                                    if (found) return found
                                }
                            }
                            return null
                        }
                        const menuItem = findMenuItem(menuItems, key)
                        if (menuItem && menuItem.path && menuItem.path !== '/logout') {
                            navigate(menuItem.path)
                        } else if (menuItem && menuItem.path === '/logout') {
                            logout()
                            navigate('/login', { replace: true })
                        }
                    }}
                    onOpenChange={(keys) => setOpenKeys(keys)}
                    style={{
                        borderRight: 0,
                        padding: '0 8px'
                    }}
                    theme="light"
                />
            </Sider>
            <AntLayout style={{ background: '#f5f7fa' }}>
                <Header style={{
                    padding: '0 24px',
                    background: 'rgba(255, 255, 255, 0.8)',
                    backdropFilter: 'blur(8px)',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    boxShadow: '0 1px 4px rgba(0,0,0,0.03)',
                    height: 80,
                    position: 'sticky',
                    top: 0,
                    zIndex: 9
                }}>
                    <h2 style={{ margin: 0, fontSize: 20, fontWeight: 600, color: '#1f2937' }}>
                        Dashboard
                    </h2>

                    <Dropdown menu={{ items: userMenu }}>
                        <Space style={{ cursor: 'pointer', padding: '4px 12px', borderRadius: 24, background: '#f3f4f6' }}>
                            <Avatar
                                icon={<UserOutlined />}
                                style={{ backgroundColor: '#667eea' }}
                            />
                            <span style={{ fontWeight: 500, color: '#374151' }}>
                                {user?.username || 'Student'}
                            </span>
                        </Space>
                    </Dropdown>
                </Header>
                <Content style={{
                    margin: '24px 24px',
                    minHeight: 280,
                    borderRadius: 16,
                    // background: '#fff', // Individual pages should handle their own cards/containers usually, but we can set a helper class
                }}>
                    <div style={{
                        animation: 'fadeIn 0.5s ease-in-out'
                    }}>
                        <Outlet />
                    </div>
                </Content>
            </AntLayout>
            <style>{`
                @keyframes fadeIn {
                    from { opacity: 0; transform: translateY(10px); }
                    to { opacity: 1; transform: translateY(0); }
                }
                .ant-menu-item-selected {
                    background: linear-gradient(90deg, #e0e7ff 0%, #f5f3ff 100%) !important;
                    color: #667eea !important;
                    font-weight: 600;
                    border-radius: 8px;
                }
                .ant-menu-item {
                    border-radius: 8px;
                    margin-bottom: 4px;
                }
            `}</style>
        </AntLayout>
    )
}
