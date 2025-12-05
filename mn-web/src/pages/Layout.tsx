import { useState } from 'react'
import { Outlet, useNavigate, useLocation } from 'react-router-dom'
import { Layout as AntLayout, Menu, theme } from 'antd'
import {
    BookOutlined,
    RobotOutlined,
    SyncOutlined,
    BarChartOutlined,
    TeamOutlined,
    SettingOutlined
} from '@ant-design/icons'

const { Header, Sider, Content } = AntLayout

const menuItems = [
    { key: '/mistakes', icon: <BookOutlined />, label: '错题本' },
    { key: '/ai', icon: <RobotOutlined />, label: 'AI 助手' },
    { key: '/review', icon: <SyncOutlined />, label: '复习中心' },
    { key: '/statistics', icon: <BarChartOutlined />, label: '统计分析' },
    { type: 'divider' as const },
    { key: '/teacher/classes', icon: <TeamOutlined />, label: '班级管理' },
    { key: '/admin/subjects', icon: <SettingOutlined />, label: '科目管理' },
]

export default function Layout() {
    const [collapsed, setCollapsed] = useState(false)
    const navigate = useNavigate()
    const location = useLocation()
    const { token } = theme.useToken()

    return (
        <AntLayout style={{ minHeight: '100vh' }}>
            <Sider
                collapsible
                collapsed={collapsed}
                onCollapse={setCollapsed}
                theme="light"
                style={{ boxShadow: '2px 0 8px rgba(0,0,0,0.05)' }}
            >
                <div style={{
                    height: 64,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                    color: 'white',
                    fontSize: collapsed ? 20 : 18,
                    fontWeight: 600
                }}>
                    {collapsed ? '📝' : '📝 错题本'}
                </div>
                <Menu
                    mode="inline"
                    selectedKeys={[location.pathname]}
                    items={menuItems}
                    onClick={({ key }) => navigate(key)}
                    style={{ borderRight: 0 }}
                />
            </Sider>
            <AntLayout>
                <Header style={{
                    padding: '0 24px',
                    background: token.colorBgContainer,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'space-between',
                    boxShadow: '0 1px 4px rgba(0,0,0,0.05)'
                }}>
                    <h1 style={{ margin: 0, fontSize: 18, fontWeight: 500 }}>
                        智能错题本管理系统
                    </h1>
                    <span style={{ color: '#999' }}>欢迎，同学！</span>
                </Header>
                <Content style={{ margin: 24, minHeight: 280 }}>
                    <Outlet />
                </Content>
            </AntLayout>
        </AntLayout>
    )
}
