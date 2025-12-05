import { useState } from 'react';
import { Form, Input, Button, Card, message, theme, Typography } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../context/authStore';
import { login } from '../api/auth';

const { Title, Text } = Typography;

export default function Login() {
    const navigate = useNavigate();
    const { login: setAuth } = useAuthStore();
    const [loading, setLoading] = useState(false);
    const { token } = theme.useToken();

    const onFinish = async (values: any) => {
        setLoading(true);
        try {
            const { data } = await login(values);
            // Assuming data is { token: '...', user: { ... } } or just { token: '...' }
            // Adjust based on actual backend response structure
            setAuth(data.token, { username: values.username });
            message.success('Welcome back!');
            navigate('/');
        } catch (error: any) {
            const msg = error.response?.data?.error || 'Login failed. Please check your credentials.';
            message.error(msg);
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
