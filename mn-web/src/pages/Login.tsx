import { useState, useRef } from 'react';
import { Form, Input, Button, Card, message, theme, Typography } from 'antd';
import { UserOutlined, LockOutlined, SafetyOutlined } from '@ant-design/icons';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuthStore } from '../context/authStore';
import { login, type LoginRequest } from '../api/auth';
import Captcha, { type CaptchaRef } from '../components/Captcha';

const { Title, Text } = Typography;

export default function Login() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const { login: setAuth } = useAuthStore();
    const [loading, setLoading] = useState(false);
    const [captchaKey, setCaptchaKey] = useState<string>('');
    const captchaRef = useRef<CaptchaRef>(null);
    const { token } = theme.useToken();
    const [form] = Form.useForm();

    const onFinish = async (values: { username: string; password: string; captcha: string }) => {
        setLoading(true);
        try {
            const loginData: LoginRequest = {
                userId: values.username,
                password: values.password,
                domain: 'system',
                captcha: values.captcha,
                captchaKey: captchaKey,
            };
            
            const response = await login(loginData);
            
            const loginResponse = (response as any).data || response;
            
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
            
            const redirect = searchParams.get('redirect') || '/';
            navigate(redirect, { replace: true });
        } catch (error: any) {
            let errorMsg = '登录失败，请检查用户名、密码和验证码';
            
            if (error?.response?.data?.message) {
                errorMsg = error.response.data.message;
            } else if (error?.message) {
                errorMsg = error.message;
            }
            
            message.error(errorMsg);
            
            if (captchaRef.current) {
                captchaRef.current.refresh();
            }
            form.setFieldsValue({ captcha: '' });
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
                    form={form}
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

                    <Form.Item
                        name="captcha"
                        rules={[{ required: true, message: 'Please input the captcha!' }]}
                    >
                        <div style={{ display: 'flex', gap: 8 }}>
                            <Input
                                prefix={<SafetyOutlined style={{ color: token.colorTextQuaternary }} />}
                                placeholder="Captcha"
                                style={{ flex: 1, borderRadius: 8 }}
                            />
                            <div style={{ 
                                display: 'flex', 
                                alignItems: 'center',
                                justifyContent: 'center',
                                border: '1px solid #d9d9d9',
                                borderRadius: 8,
                                overflow: 'hidden',
                                backgroundColor: '#fff',
                                transition: 'border-color 0.3s ease',
                                flexShrink: 0
                            }}
                            onMouseEnter={(e) => {
                                e.currentTarget.style.borderColor = '#40a9ff';
                            }}
                            onMouseLeave={(e) => {
                                e.currentTarget.style.borderColor = '#d9d9d9';
                            }}
                            >
                                <Captcha
                                    ref={captchaRef}
                                    onCaptchaChange={setCaptchaKey}
                                />
                            </div>
                        </div>
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
