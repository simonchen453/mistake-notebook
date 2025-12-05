import { useState } from 'react';
import { Form, Input, Button, Card, message, theme, Typography } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuthStore } from '../context/authStore';
import { login, type LoginRequest } from '../api/auth';

const { Title, Text } = Typography;

export default function Login() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const { login: setAuth } = useAuthStore();
    const [loading, setLoading] = useState(false);
    const { token } = theme.useToken();

    const onFinish = async (values: { username: string; password: string }) => {
        setLoading(true);
        try {
            const loginData: LoginRequest = {
                userId: values.username,
                password: values.password,
                domain: 'system', // AdminPro 默认 domain
            };
            
            const response = await login(loginData);
            
            // axios 拦截器已经处理了响应
            // AdminPro 返回格式：{ restCode: '200', data: LoginResponse, success: true }
            // 拦截器会返回 data 字段，所以 response 可能是 { data: LoginResponse } 或直接是 LoginResponse
            const loginResponse = (response as any).data || response;
            
            // 保存 token 和用户信息
            if (loginResponse.token) {
                setAuth(loginResponse.token, {
                    id: loginResponse.id,
                    username: loginResponse.userId,
                    realName: loginResponse.realName,
                    avatarUrl: loginResponse.avatarUrl,
                    mobileNo: loginResponse.mobileNo,
                });
            } else {
                throw new Error('登录响应中缺少 token');
            }
            
            message.success('登录成功！');
            
            // 跳转到 redirect 参数指定的页面，或默认首页
            const redirect = searchParams.get('redirect') || '/';
            navigate(redirect, { replace: true });
        } catch (error: any) {
            // 错误处理：优先使用后端返回的 message
            let errorMsg = '登录失败，请检查用户名和密码';
            
            if (error?.response?.data?.message) {
                errorMsg = error.response.data.message;
            } else if (error?.message) {
                errorMsg = error.message;
            }
            
            message.error(errorMsg);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div style={{
            height: '100vh',
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            position: 'relative',
            overflow: 'hidden'
        }}>
            {/* Decorative circles */}
            <div style={{
                position: 'absolute',
                top: '-10%',
                left: '-10%',
                width: '50%',
                height: '50%',
                borderRadius: '50%',
                background: 'rgba(255, 255, 255, 0.1)',
                zIndex: 0
            }} />
            <div style={{
                position: 'absolute',
                bottom: '-10%',
                right: '-10%',
                width: '50%',
                height: '50%',
                borderRadius: '50%',
                background: 'rgba(255, 255, 255, 0.1)',
                zIndex: 0
            }} />

            <Card
                style={{
                    width: 400,
                    borderRadius: 16,
                    boxShadow: '0 8px 32px 0 rgba(31, 38, 135, 0.37)',
                    backdropFilter: 'blur(8px)',
                    background: 'rgba(255, 255, 255, 0.9)',
                    border: '1px solid rgba(255, 255, 255, 0.18)',
                    zIndex: 1
                }}
                bordered={false}
            >
                <div style={{ textAlign: 'center', marginBottom: 32 }}>
                    <div style={{
                        fontSize: 48,
                        marginBottom: 16,
                        background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                        WebkitBackgroundClip: 'text',
                        WebkitTextFillColor: 'transparent',
                    }}>
                        📝
                    </div>
                    <Title level={3} style={{ marginBottom: 4, color: '#333' }}>
                        Welcome Back
                    </Title>
                    <Text type="secondary">
                        Sign in to your Mistake Notebook
                    </Text>
                </div>

                <Form
                    name="login"
                    initialValues={{ remember: true }}
                    onFinish={onFinish}
                    size="large"
                >
                    <Form.Item
                        name="username"
                        rules={[{ required: true, message: 'Please input your username!' }]}
                    >
                        <Input
                            prefix={<UserOutlined style={{ color: token.colorTextQuaternary }} />}
                            placeholder="Username"
                            style={{ borderRadius: 8 }}
                        />
                    </Form.Item>

                    <Form.Item
                        name="password"
                        rules={[{ required: true, message: 'Please input your password!' }]}
                    >
                        <Input.Password
                            prefix={<LockOutlined style={{ color: token.colorTextQuaternary }} />}
                            placeholder="Password"
                            style={{ borderRadius: 8 }}
                        />
                    </Form.Item>

                    <Form.Item>
                        <Button
                            type="primary"
                            htmlType="submit"
                            style={{
                                width: '100%',
                                height: 48,
                                borderRadius: 8,
                                background: 'linear-gradient(90deg, #667eea 0%, #764ba2 100%)',
                                border: 'none',
                                fontWeight: 600
                            }}
                            loading={loading}
                        >
                            Sign In
                        </Button>
                    </Form.Item>
                </Form>
            </Card>
        </div>
    );
}
