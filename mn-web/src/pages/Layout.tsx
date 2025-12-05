import { useState } from 'react'
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
    LogoutOutlined
} from '@ant-design/icons'
import { useAuthStore } from '../context/authStore'

const { Header, Sider, Content } = AntLayout

const menuItems = [
    { key: '/mistakes', icon: <BookOutlined />, label: 'Mistakes' },
    { key: '/ai', icon: <RobotOutlined />, label: 'AI Assistant' },
    { key: '/review', icon: <SyncOutlined />, label: 'Review Center' },
    { key: '/statistics', icon: <BarChartOutlined />, label: 'Statistics' },
    { type: 'divider' as const },
    { key: '/teacher/classes', icon: <TeamOutlined />, label: 'Class Mgmt' },
    { key: '/admin/subjects', icon: <SettingOutlined />, label: 'Subjects' },
]

export default function Layout() {
    const [collapsed, setCollapsed] = useState(false)
    const navigate = useNavigate()
    const location = useLocation()
    const { user, logout } = useAuthStore()

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
                    selectedKeys={[location.pathname]}
                    items={menuItems}
                    onClick={({ key }) => navigate(key)}
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
